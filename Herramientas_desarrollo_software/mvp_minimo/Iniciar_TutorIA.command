#!/bin/zsh

SCRIPT_DIR="${0:A:h}"
cd "$SCRIPT_DIR"

./run.sh
STATUS=$?

echo
if [[ $STATUS -eq 0 || $STATUS -eq 130 ]]; then
  echo "TutorIA se detuvo correctamente."
else
  echo "TutorIA no pudo iniciar. Revisa el mensaje anterior."
fi
echo "Presiona Enter para cerrar esta ventana."
read -r

exit $STATUS
