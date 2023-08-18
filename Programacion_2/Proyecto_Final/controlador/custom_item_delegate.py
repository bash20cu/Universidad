from PyQt6.QtWidgets import QStyledItemDelegate
from PyQt6.QtGui import QIcon
from PyQt6.QtCore import Qt
from PyQt6.QtGui import QFileSystemModel
from modelo.security_manager import SecurityManager

# Clase para personalizar la apariencia de los elementos en el QTreeView
class CustomItemDelegate(QStyledItemDelegate):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.file_system_model = QFileSystemModel()

    def initStyleOption(self, option, index):
        super().initStyleOption(option, index)
        fileInfo = index.model().fileInfo(index)

        # Utilizamos el QFileSystemModel para obtener el ícono del sistema
        icon = self.file_system_model.fileIcon(index)
        option.icon = icon

        option.displayAlignment = Qt.AlignmentFlag.AlignLeft | Qt.AlignmentFlag.AlignVCenter


class NoExpandItemDelegate(QStyledItemDelegate):
    def __init__(self, parent=None):
        super().__init__(parent)

    def expandable(self, index):
        explorer_instance = self.parent().parent()  # Accede a la instancia de Explorer
        return not explorer_instance.security_manager.is_protected_folder(explorer_instance.file_system_model.filePath(index))

    def flags(self, index):
        flags = super().flags(index)
        if self.expandable(index):
            flags &= ~QtCore.Qt.ItemIsExpandable
        return flags