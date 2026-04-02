"""Modelo base y utilidades de inferencia para el proyecto."""

from __future__ import annotations

import platform
from pathlib import Path

import numpy as np
from PIL import Image

from .constants import ID_TO_CLASS


def select_device(torch_module) -> tuple[object, str]:
    """Selecciona el backend disponible con prioridad CUDA, MPS, DirectML y CPU."""

    torch = torch_module
    if torch.cuda.is_available():
        return torch.device("cuda"), "cuda"
    mps_backend = getattr(torch.backends, "mps", None)
    if mps_backend is not None and mps_backend.is_available():
        return torch.device("mps"), "mps"
    try:
        import torch_directml

        return torch_directml.device(), "directml"
    except ImportError:
        return torch.device("cpu"), "cpu"


def explain_device_choice(torch_module) -> str:
    """Explica en texto humano por que se eligio un backend de computo concreto."""

    torch = torch_module
    system_name = platform.system().lower()

    if torch.cuda.is_available():
        return "GPU detectada por CUDA. Se usara aceleracion por NVIDIA."

    mps_backend = getattr(torch.backends, "mps", None)
    if mps_backend is not None and mps_backend.is_available():
        return "GPU detectada por MPS. Se usara aceleracion por Metal en macOS."

    if system_name == "darwin":
        return (
            "Usando CPU. En macOS/Hackintosh con GPU AMD normalmente PyTorch no "
            "dispone de un backend acelerado equivalente a CUDA o DirectML."
        )

    try:
        import torch_directml  # noqa: F401

        return "GPU detectada por DirectML. Se usara aceleracion compatible con Windows."
    except ImportError:
        return "Usando CPU. No se detecto un backend de GPU compatible en este entorno."


def build_simple_cnn(nn_module):
    """Construye una CNN pequena y suficiente para tres clases basicas."""

    nn = nn_module

    class SimpleCNN(nn.Module):
        def __init__(self, num_classes: int = 3):
            super().__init__()
            # Bloque extractor de caracteristicas.
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
            # Clasificador final sobre el embedding de 128 canales.
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
    """Reconstruye el modelo desde un checkpoint portable y lo mueve al dispositivo elegido."""

    import torch
    import torch.nn as nn

    device, device_name = select_device(torch)
    device_note = explain_device_choice(torch)
    SimpleCNN = build_simple_cnn(nn)
    model = SimpleCNN(num_classes=num_classes)
    try:
        checkpoint = torch.load(checkpoint_path, map_location="cpu", weights_only=False)
    except NotImplementedError as exc:
        message = str(exc)
        if "PrivateUse1" in message:
            raise RuntimeError(
                "No se pudo cargar el checkpoint porque fue guardado desde un backend "
                "no portable (por ejemplo DirectML/PrivateUse1). Este entorno macOS no "
                "puede reconstruir ese tensor. La solucion es regenerar el checkpoint en "
                "CPU/CUDA/MPS o guardar un checkpoint portable con pesos en CPU. "
                f"Backend seleccionado actualmente: {device_name}. {device_note}"
            ) from exc
        raise
    # Los checkpoints se guardan con tensores en CPU para poder abrirlos en otros entornos.
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
    """Ejecuta inferencia sobre una imagen y devuelve la clase ganadora con probabilidades."""

    import torch

    with Image.open(image_path) as img:
        img = img.convert("RGB").resize((image_size, image_size))
        arr = np.asarray(img, dtype=np.float32) / 255.0

    # El modelo espera tensores BCHW normalizados en el rango [0, 1].
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
