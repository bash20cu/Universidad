cd Inteligencia_Artificial

python -m venv .venv 

.\.venv\Scripts\activate

pip install -r requirements.txt

.\.venv\Scripts\python.exe -m pip install -r requirements.txt

UI/UX

python .\app.py 


# Fase 1
.\.venv\Scripts\python.exe -m core.phase1 --dataset-root gatos_perros_pandas
.\.venv\Scripts\python.exe -m core.phase1 --dataset-root gatos_perros_pandas --gpu-test --matmul-size 512

# Fase 2
.\.venv\Scripts\python.exe -m core.phase2 --dataset-root gatos_perros_pandas --output-dir phase2_artifacts --dedupe-mode sha1 --verify-dataloaders --batch-size 16 --debug

# Fase 3 entrenamiento
.\.venv\Scripts\python.exe -m core.phase3_train --manifest-csv phase2_artifacts/dataset_split.csv --output-dir phase3_artifacts --epochs 5 --batch-size 32 --debug

# Fase 3 inferencia
.\.venv\Scripts\python.exe -m core.phase3_infer --image-path gatos_perros_pandas\animals\animals\cats\cats_00001.jpg --checkpoint-path phase3_artifacts\best_checkpoint.pt --image-size 128

# Fase 4 evaluacion
.\.venv\Scripts\python.exe -m core.phase4_evaluate --manifest-csv phase2_artifacts/dataset_split.csv --checkpoint-path phase3_artifacts/best_checkpoint.pt --output-dir phase4_artifacts --split test --image-size 128

# Ver checkpoint rapido
.\.venv\Scripts\python.exe -c "import torch; c=torch.load('phase3_artifacts/best_checkpoint.pt', map_location='cpu'); print(c.keys(), c['epoch'], c['val_acc'])"

# Flask dashboard + inferencia web
.\.venv\Scripts\python.exe app.py



