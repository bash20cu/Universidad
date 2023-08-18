import sys, os
from PyQt6.QtWidgets import QApplication, QMainWindow, QTreeView, QInputDialog, QMessageBox, QLineEdit
from PyQt6.QtCore import QDir
from explorer_ui import Ui_MainWindow  
from PyQt6.QtGui import QFileSystemModel
from PyQt6.QtWidgets import QMainWindow
from modelo.security_manager import SecurityManager
from controlador.address_bar import AddressBar
from controlador.search import DocumentSearcher
from controlador.actions import Actions
from controlador.bloc_notas import MainBlocNotas


class Explorer(QMainWindow, Ui_MainWindow):
    def __init__(self):
        super().__init__()
        self.setupUi(self)
        
        # Crear el QFileSystemModel en la clase MainWindow
        self.file_system_model = QFileSystemModel()
        self.file_system_model.setRootPath(QDir.rootPath())
        
        # Crear la base de datos
        self.security_manager = SecurityManager()
        self.security_manager.create_table()                  
        
        # Crea una instancia de AddressBar para manejar la barra de direcciones
        self.address_bar_handler = AddressBar(self.address_bar, self.LstVista,self.BtUp,self.BtBack, self.file_system_model)
        
        # Crea una instancia vacia para el bloc de notas, Se me cerraba por el recolector de basura :( 
        self.bloc_notas_instance = None
        
        # Habilitar la funcionalidad de arrastrar y soltar en el QTreeView        
        self.LstVista.setDragEnabled(True)
        self.LstVista.setAcceptDrops(True)
        self.LstVista.setDropIndicatorShown(True)
        self.LstVista.viewport().setAcceptDrops(True)
        self.LstVista.setDragDropMode(QTreeView.DragDropMode.DragDrop) # Habilitar el arrastrar y soltar
        
        # Conectar la señal de clic en el QTreeView
        self.LstVista.clicked.connect(self.on_tree_view_clicked)
        self.LstVista.doubleClicked.connect(self.handle_tree_view_double_clicked)
        self.LstVista.expanded.connect(self.on_expander_clicked)
        self.LstVista.setItemsExpandable(False)
        self.LstVista.setExpandsOnDoubleClick(False)
        
        #Busqueda de documentos 
        # Crea una instancia de DocumentSearcher para manejar la búsqueda de documentos
        self.document_searcher = DocumentSearcher(self.LstVista, self.search_bar,self.BtBusqueda, self.file_system_model)       
        
        #Instanciar la clase Actions
        self.Actions = Actions(self.LstVista,self.address_bar_handler, self.file_system_model)        
        # Conectar las Actions con el menubar
        self.actionNuevo_archivo_de_texto.triggered.connect(self.Actions.create_text_file)  
        self.actionCarpeta_de_Archivos.triggered.connect(self.Actions.create_folder)  
        self.actionCopiar.triggered.connect(self.Actions.copiar_archivo)
        self.actionCortar.triggered.connect(self.Actions.cortar_archivo)
        self.actionEliminar.triggered.connect(self.Actions.deleteFile)
    
    def on_tree_view_clicked(self, index):
        if index.isValid():
            file_path = self.file_system_model.filePath(index)
            if QDir(file_path).exists():
                # Hacer algo con la carpeta seleccionada (por ejemplo, imprimir su ruta)
                print("Carpeta seleccionada:", file_path)
            elif file_path.lower().endswith('.txt'):
                # Abrir el archivo TXT con el programa predeterminado del sistema
                print("Archivo TXT seleccionado:", file_path)
        
    
    def handle_tree_view_double_clicked(self, index):
        # Obtener la ruta del elemento en el índice seleccionado
        path = self.file_system_model.filePath(index)
        
        # Si es un archivo TXT, abrir el bloc de notas personalizado
        if path.lower().endswith('.txt'):
            try:
                self.bloc_notas_instance = MainBlocNotas(file_path=path)
                self.bloc_notas_instance.show()  # Ejecutar el ciclo de eventos del bloc de notas
            except Exception as e:
                print(f"Error opening custom notepad: {e}")
        elif os.path.isdir(path):  # Si es un directorio
            if self.security_manager.is_protected_folder(path):
                password, ok = QInputDialog.getText(self, "Advertencia", "Introduzca la contraseña:", QLineEdit.EchoMode.Password)
                if ok and self.security_manager.verify_password(path, password):
                    self.address_bar_handler.update_tree_view(path)
                    self.address_bar_handler.update_address_bar(path)
                else:
                    QMessageBox.warning(self, "Advertencia", "Contraseña incorrecta. Acceso Denegado.")
                    return
            else:
                self.address_bar_handler.update_tree_view(path)
                self.address_bar_handler.update_address_bar(path)
        else:  # Si no es un archivo de texto ni un directorio
            self.address_bar_handler.open_with_default_os(path)
        
    def on_expander_clicked(self, index):
        print("Expanded")
        path = self.file_system_model.filePath(index)
        if self.security_manager.is_protected_folder(path):
            self.LstVista.setExpanded(index, False)
            password, ok = QInputDialog.getText(self, "Advertencia", "Introduzca la contraseña:", QLineEdit.EchoMode.Password)
            if ok and self.security_manager.verify_password(path, password):
                self.LstVista.setExpanded(index, True)
            else:
                QMessageBox.warning(self, "Advertencia", "Contraseña incorrecta. Acceso Denegado.")


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = Explorer()
    window.show()
    sys.exit(app.exec())
