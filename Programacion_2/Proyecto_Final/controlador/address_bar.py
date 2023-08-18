import os
from controlador.custom_item_delegate import CustomItemDelegate


from PyQt6.QtWidgets import QLineEdit, QTreeView, QHeaderView,QPushButton, QApplication
from PyQt6.QtCore import QDir, QSize
from PyQt6.QtGui import QFileSystemModel
from PyQt6.QtWidgets import QStyledItemDelegate

# Clase especializada en la barra de direcciones de la interfaz gráfica
class AddressBar:
    def __init__(self, address_bar_widget: QLineEdit, tree_view: QTreeView,BtUp: QPushButton, BtBack: QPushButton, file_system_model: QFileSystemModel):
        self.address_bar_widget = address_bar_widget
        self.tree_view = tree_view
        self.file_system_model = file_system_model
        btUp = BtUp
        btBack = BtBack
        self.path = os.path.abspath(os.getcwd())  # Variable para almacenar la dirección actual
        # Historial para almacenar las direcciones anteriores
        self.history = [self.path]
        
        # Conecta la función go_up_directory al botón "BtUp"
        btUp.clicked.connect(self.go_up_directory)       
        
        # Conecta la función restore_directory al botón "BtBack"
        btBack.clicked.connect(self.go_back)
        
        # Configurar el QTreeView
        self.tree_view.setHeaderHidden(False)

        # Configurar el QTreeView para que utilice el QFileSystemModel y CustomItemDelegate
        self.tree_view.setModel(self.file_system_model)
        delegate = CustomItemDelegate()
        self.tree_view.setItemDelegate(delegate)

        # Ajustar el tamaño de los íconos
        icon_size = 48  # Tamaño deseado en píxeles
        self.tree_view.setIconSize(QSize(icon_size, icon_size))
        # Ajustar el tamaño de las columnas para que se expandan automáticamente
        self.tree_view.header().setSectionResizeMode(0, QHeaderView.ResizeMode.Stretch)  # Columna de íconos
        self.tree_view.header().setSectionResizeMode(1, QHeaderView.ResizeMode.Stretch)  # Columna de nombres

        # Conectar la señal de clic en el QTreeView a la función handle_tree_view_clicked
        #self.tree_view.doubleClicked.connect(self.handle_tree_view_double_clicked)

        # Actualizar el QTreeView después de restaurar el directorio
        self.update_tree_view(self.path)
        # Actualizar la barra de direcciones con la dirección actual del directorio de trabajo
        self.update_address_bar(self.path)

    # Método para actualizar la barra de direcciones con la dirección proporcionada
    def update_address_bar(self, path):
        # Agregar una comprobación para actualizar solo si es un directorio
        if QDir(path).exists():
            self.tree_view.setCurrentIndex(self.file_system_model.index(path))
            self.address_bar_widget.setText(QDir.toNativeSeparators(path))
            self.path = path  # Actualiza la variable path
        
   
    # Sube un directorio en el sistema de archivos y actualiza la barra de direcciones y el QTreeView
    def go_up_directory(self):
        # Guarda la dirección actual antes de subir un directorio
        self.save_current_directory()

        parent_directory = os.path.dirname(self.address_bar_widget.text())

        # Siempre que sea posible, sube un directorio
        if os.path.exists(parent_directory):
            os.chdir(parent_directory)  # Utiliza os.chdir() para cambiar el directorio de trabajo
            # Actualiza la variable path con la nueva dirección
            self.path = os.getcwd()
            # Actualiza la barra de direcciones después de subir el directorio
            self.update_address_bar(self.path)
            # Actualizar el QTreeView después de subir el directorio
            self.update_tree_view(self.path)
            # Agregar la dirección actual al historial
            self.history.append(self.path)

    # Guarda la dirección actual en la variable path
    def save_current_directory(self):
        self.path = self.address_bar_widget.text()
    
    # Método para volver atrás en el historial
    def go_back(self):
        if len(self.history) > 1:
            # Eliminar la dirección actual del historial
            self.history.pop()
            # Obtener la dirección previa del historial
            previous_path = self.history[-1]

            # Cambiar al directorio previo
            if QDir(previous_path).exists():
                os.chdir(previous_path)

                # Actualizar la barra de direcciones y el QTreeView
                self.update_address_bar(previous_path)
                self.update_tree_view(previous_path)

    # Método para actualizar el contenido del QTreeView para una ruta dada
    def update_tree_view(self, path):
        if self.file_system_model.setRootPath(path):
            self.tree_view.setRootIndex(self.file_system_model.index(path))
        else:
            print("No se pudo establecer el directorio raíz para el QFileSystemModel.")

    
 # Funcionalidad movida hacia la clase explorer para tener mejor control en la clase principal 
 # Método para manejar el evento de doble clic en el QTreeView 
    def handle_tree_view_double_clicked(self, index):
        # Obtener la ruta del elemento en el índice seleccionado
        path = self.file_system_model.filePath(index)
        

        # Si es un directorio, actualizar el contenido del QTreeView con el nuevo directorio
        if QDir(path).exists():
            self.update_tree_view(path)

        # Actualizar la barra de direcciones con la dirección del elemento seleccionado en el QTreeView
        self.update_address_bar(path)

        # Agregar la dirección actual al historial
        self.history.append(path)

    def open_with_default_os(self, file_path):
        try:
            os.startfile(file_path)
            if os.name == 'nt':  # Windows
                os.startfile(file_path)
            else:  # Unix/Linux
                os.system('xdg-open "{}"'.format(file_path))
        except OSError as e:
                print(f"Error opening file: {e}")    
    # Devuelve el directorio actual almacenado en la variable path
    def get_current_folder_path(self):
        return self.path

    # Devuelve la dirección del archivo o directorio seleccionado
    def get_file_path(self, index):
        if index.isValid():
            file_info = self.file_system_model.fileInfo(index)
            if file_info.isDir():
                return file_info.filePath()  # Es un directorio, devuelve la ruta del directorio
            else:
                return file_info.filePath()  # Es un archivo, devuelve la ruta del archivo
        return None
            
