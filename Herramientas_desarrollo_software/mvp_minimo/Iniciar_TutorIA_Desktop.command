#!/bin/zsh

SCRIPT_DIR="${0:A:h}"
cd "$SCRIPT_DIR"
exec .venv/bin/python desktop_launcher.py
