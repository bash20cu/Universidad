"""Panel de control PySide6 para la ejecución local de TutorIA en macOS."""

from __future__ import annotations

import sys
import re
import webbrowser
from pathlib import Path

from runtime_manager import RuntimeManager

try:
    from PySide6.QtCore import QTimer, Qt, Signal
    from PySide6.QtGui import QFont
    from PySide6.QtWidgets import (
        QApplication,
        QGridLayout,
        QHBoxLayout,
        QLabel,
        QMainWindow,
        QMessageBox,
        QPushButton,
        QTextEdit,
        QVBoxLayout,
        QWidget,
    )
except ImportError as error:  # pragma: no cover - se ejecuta solo si falta la dependencia gráfica.
    raise SystemExit("PySide6 no está instalado. Ejecuta: python -m pip install -r requirements.txt") from error


class LogBridge(QWidget):
    """Puente de señales para llevar logs de hilos de procesos al hilo gráfico."""

    message = Signal(str)


class TutorIALauncher(QMainWindow):
    """Ventana principal para operar los servicios locales de TutorIA."""

    def __init__(self, project_dir: Path) -> None:
        super().__init__()
        self.setWindowTitle("TutorIA · Centro de control")
        self.setMinimumSize(760, 560)
        self.bridge = LogBridge()
        self.bridge.message.connect(self.append_log)
        self.manager = RuntimeManager(project_dir, on_log=self.bridge.message.emit)
        self._build_ui()
        self.timer = QTimer(self)
        self.timer.timeout.connect(self.refresh_status)
        self.timer.start(1500)
        self.refresh_status()

    def _build_ui(self) -> None:
        """Construye una interfaz compacta y legible para una demo académica."""

        root = QWidget()
        layout = QVBoxLayout(root)
        layout.setContentsMargins(28, 24, 28, 24)
        title = QLabel("TutorIA · Centro de control")
        title.setFont(QFont("Arial", 22, QFont.Weight.Bold))
        subtitle = QLabel("Enciende el aula, revisa la salud de la IA y apaga todo desde un solo lugar.")
        subtitle.setObjectName("subtitle")
        layout.addWidget(title)
        layout.addWidget(subtitle)

        self.cards = []
        cards = QGridLayout()
        for index, label in enumerate(("Foundation Models", "TutorIA Flask")):
            card = QLabel()
            card.setMinimumHeight(100)
            card.setAlignment(Qt.AlignmentFlag.AlignCenter)
            card.setObjectName("serviceCard")
            self.cards.append(card)
            cards.addWidget(card, 0, index)
        layout.addLayout(cards)

        buttons = QHBoxLayout()
        self.start_button = QPushButton("▶  Encender aula")
        self.stop_button = QPushButton("■  Apagar aula")
        self.open_button = QPushButton("↗  Abrir TutorIA")
        self.start_button.clicked.connect(self.start_services)
        self.stop_button.clicked.connect(self.stop_services)
        self.open_button.clicked.connect(lambda: webbrowser.open(self.manager.app_url))
        buttons.addWidget(self.start_button)
        buttons.addWidget(self.stop_button)
        buttons.addWidget(self.open_button)
        layout.addLayout(buttons)

        self.info = QLabel()
        self.info.setObjectName("info")
        layout.addWidget(self.info)
        self.two_factor_card = QLabel("🔐  Código 2FA de desarrollo\nAún no se ha generado un código en esta sesión.")
        self.two_factor_card.setObjectName("twoFactorCard")
        self.two_factor_card.setWordWrap(True)
        layout.addWidget(self.two_factor_card)
        layout.addWidget(QLabel("Actividad de la sesión"))
        self.logs = QTextEdit()
        self.logs.setReadOnly(True)
        self.logs.setObjectName("logs")
        layout.addWidget(self.logs)
        self.setCentralWidget(root)
        self.setStyleSheet(
            """
            QWidget { background: #f7f9fc; color: #243047; font-family: Arial; }
            #subtitle, #info { color: #667085; }
            #serviceCard { background: white; border: 1px solid #e4e7ec; border-radius: 14px; padding: 14px; font-size: 16px; }
            #twoFactorCard { background: #fff8e6; color: #8a5a00; border: 1px solid #f3d58a; border-radius: 10px; padding: 10px; font-weight: 600; }
            QPushButton { background: #6b5ce7; color: white; border: 0; border-radius: 9px; padding: 11px 16px; font-weight: 600; }
            QPushButton:hover { background: #5747cf; }
            QTextEdit { background: #172033; color: #d0d5dd; border-radius: 10px; padding: 10px; font-family: Menlo; }
            """
        )

    def append_log(self, message: str) -> None:
        """Añade mensajes de procesos sin interrumpir la operación del usuario."""

        self.logs.append(message)
        # En desarrollo, el servicio de correo escribe el código en el log.
        # Mostrarlo aquí evita cambiar el flujo de la demo ni exponer códigos cuando se usa Resend.
        match = re.search(r"Código 2FA de desarrollo para ([^:]+):\s*(\d{6})", message)
        if match:
            recipient, code = match.groups()
            self.two_factor_card.setText(
                f"🔐  Código 2FA de desarrollo\n{code} · enviado a {recipient}\n\nÚsalo en la pantalla de verificación."
            )

    def refresh_status(self) -> None:
        """Actualiza indicadores, PID y estado del proveedor cada pocos segundos."""

        fm, app = self.manager.statuses()
        for card, status in zip(self.cards, (fm, app)):
            icon = "●" if status.running else "○"
            owner = "controlado por esta ventana" if status.owned else "externo o apagado"
            card.setText(f"{icon}  {status.name}\n{status.detail}\n{owner}")
            card.setStyleSheet(f"color: {'#067647' if status.running else '#b42318'};")
        self.info.setText(f"FM: {self.manager.fm_host}:{self.manager.fm_port}   ·   Web: {self.manager.app_url}")

    def start_services(self) -> None:
        """Inicia FM y Flask en orden, mostrando cualquier error de arranque."""

        try:
            self.manager.start_all()
            self.append_log("Aula lista para la demo.")
        except (OSError, TimeoutError) as error:
            QMessageBox.critical(self, "No se pudo encender TutorIA", str(error))
        finally:
            self.refresh_status()

    def stop_services(self) -> None:
        """Apaga los procesos iniciados por el panel."""

        self.manager.stop_all()
        self.append_log("Sesión detenida. El panel también cierra un fm serve externo si pudo verificar su PID.")
        self.refresh_status()

    def closeEvent(self, event) -> None:  # noqa: N802 - nombre exigido por Qt.
        """Garantiza la limpieza al cerrar la ventana con la X o Cmd+Q."""

        self.manager.stop_all()
        event.accept()


def main() -> int:
    """Inicia la aplicación de escritorio desde el directorio del MVP."""

    application = QApplication(sys.argv)
    window = TutorIALauncher(Path(__file__).resolve().parent)
    window.show()
    return application.exec()


if __name__ == "__main__":
    raise SystemExit(main())
