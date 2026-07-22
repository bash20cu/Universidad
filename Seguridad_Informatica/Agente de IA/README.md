# Agente de IA de laboratorio (Kali Linux)

Agente educativo y defensivo para el curso de Seguridad Informatica.
Solo ejecuta comandos informativos de una lista blanca, siempre con
confirmacion humana y registro de auditoria. Uso EXCLUSIVO en maquina
virtual aislada.

## Instalacion rapida
```bash
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
cp .env.example .env   # editar segun el backend elegido
python app.py
```

Consulte el manual completo para el procedimiento detallado, los controles
de seguridad y la practica de laboratorio.
