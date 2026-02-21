"""Modelo y utilidades de inferencia/checkpoint."""

from __future__ import annotations

from pathlib import Path

import numpy as np
from PIL import Image

from .constants import ID_TO_CLASS


def select_device(torch_module) -> tuple[object, str]:
    torch = torch_module
    if torch.cuda.is_available():
        return torch.device("cuda"), "cuda"
    try:
        import torch_directml

        return torch_directml.device(), "directml"
    except ImportError:
        return torch.device("cpu"), "cpu"


def build_simple_cnn(nn_module):
    nn = nn_module

    class SimpleCNN(nn.Module):
        def __init__(self, num_classes: int = 3):
            super().__init__()
            self.features = nn.Sequential(
                nn.Conv2d(3, 32, kernel_size=3, padding=1),
                nn.ReLU(inplace=True),
                nn.MaxPool2d(2),
                nn.Conv2d(32, 64, kernel_size=3, padding=1),
                nn.ReLU(inplace=True),
                nn.MaxPool2d(2),
                nn.Conv2d(64, 128, kernel_size=3, padding=1),
                nn.ReLU(inplace=True),
                nn.AdaptiveAvgPool2d((1, 1)),
            )
            self.classifier = nn.Sequential(
                nn.Flatten(),
                nn.Dropout(p=0.2),
                nn.Linear(128, num_classes),
            )

        def forward(self, x):
            x = self.features(x)
            return self.classifier(x)

    return SimpleCNN


def load_model_from_checkpoint(
    checkpoint_path: Path,
    num_classes: int = 3,
):
    import torch
    import torch.nn as nn

    device, device_name = select_device(torch)
    SimpleCNN = build_simple_cnn(nn)
    model = SimpleCNN(num_classes=num_classes)
    checkpoint = torch.load(checkpoint_path, map_location="cpu", weights_only=False)
    model.load_state_dict(checkpoint["model_state_dict"])
    model = model.to(device)
    model.eval()
    return model, device, device_name, checkpoint


def predict_image(
    model,
    device,
    image_path: Path,
    image_size: int = 128,
) -> dict[str, object]:
    import torch

    with Image.open(image_path) as img:
        img = img.convert("RGB").resize((image_size, image_size))
        arr = np.asarray(img, dtype=np.float32) / 255.0

    x = torch.from_numpy(arr).permute(2, 0, 1).contiguous().unsqueeze(0).to(device)

    with torch.no_grad():
        logits = model(x)
        probs = torch.softmax(logits, dim=1)[0].detach().cpu().numpy()

    pred_id = int(np.argmax(probs))
    confidence = float(probs[pred_id])
    return {
        "pred_id": pred_id,
        "pred_label": ID_TO_CLASS.get(pred_id, f"class_{pred_id}"),
        "confidence": confidence,
        "probabilities": {ID_TO_CLASS.get(i, str(i)): float(p) for i, p in enumerate(probs)},
    }
