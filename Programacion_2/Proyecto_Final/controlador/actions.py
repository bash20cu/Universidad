import os
import shutil
from PyQt6.QtWidgets import QTreeView, QFileDialog, QMessageBox, QApplication, QMenu, QInputDialog, QLineEdit
from PyQt6.QtGui import QFileSystemModel, QAction
from PyQt6.QtCore import Qt
from controlador.address_bar import AddressBar
from modelo.security_manager import SecurityManager

# Clase Actions con la lógica de la aplicación
class Actions():
    def __init__(self, tree_view: QTreeView, address_bar: AddressBar, file_system_model: QFileSystemModel):
        self.tree_view = tree_view
        self.address_bar = address_bar
        self.security_manager = SecurityManager()
        self.file_system_model = file_system_model
               
        
        self.clipboard = QApplication.clipboard()  # Instancia del portapapeles  
        
        # Crear las acciones 
        self.actionNuevoDocumentoTXT = QAction("Nuevo Documento Texto", self.tree_view)
        self.actionNuevaCarpeta = QAction("Nueva Carpeta", self.tree_view)
        self.actionCortar = QAction("Cortar", self.tree_view)
        self.actionCopiar = QAction("Copiar", self.tree_view)
        self.actionPegar = QAction("Pegar", self.tree_view)
        self.actionEliminar = QAction("Eliminar", self.tree_view)
        self.actionTreeReport = QAction("Tree_Report", self.tree_view)
        self.actionProtect = QAction("Proteger", self.tree_view)

        # Conectar las acciones a sus funciones correspondientes
        self.actionNuevoDocumentoTXT.triggered.connect(self.create_text_file)
        self.actionNuevaCarpeta.triggered.connect(self.create_folder)
        self.actionCortar.triggered.connect(self.cortar_archivo)
        self.actionCopiar.triggered.connect(self.copiar_archivo)
        self.actionPegar.triggered.connect(self.pegar_archivo)
        self.actionEliminar.triggered.connect(self.deleteFile)
        self.actionTreeReport.triggered.connect(self.export_report_to_txt)
        self.actionProtect.triggered.connect(self.protect_directory)

        # Crear el menú contextual y agregar las acciones
        self.context_menu = QMenu(self.tree_view)
        self.context_menu.addAction(self.actionNuevoDocumentoTXT)
        self.context_menu.addAction(self.actionNuevaCarpeta)
        self.context_menu.addSeparator()
        self.context_menu.addAction(self.actionCortar)
        self.context_menu.addAction(self.actionCopiar)
        self.context_menu.addAction(self.actionPegar)
        self.context_menu.addSeparator()
        self.context_menu.addAction(self.actionEliminar)
        self.context_menu.addSeparator()
        self.context_menu.addAction(self.actionTreeReport)
        self.context_menu.addSeparator()
        self.context_menu.addAction(self.actionProtect)
        

        # Agregar las acciones al menú contextual del árbol
        self.tree_view.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu)
        self.tree_view.customContextMenuRequested.connect(self.show_context_menu)

    # Método para mostrar el menú contextual
    def show_context_menu(self, pos):
        global_pos = self.tree_view.viewport().mapToGlobal(pos)
        self.context_menu.exec(global_pos)
    
    def copiar_archivo(self):
        # Obtener la ruta del archivo seleccionado
        selected_index = self.tree_view.currentIndex()
        file_path = self.address_bar.get_file_path(selected_index)
        
        # Copiar el archivo y almacenar su ruta en el portapapeles
        self.clipboard.setText(file_path)
        print(f"Archivo copiado: {file_path}")
    
    # Metodo para cortar archivo
    def cortar_archivo(self):
        # Obtener la ruta del archivo seleccionado
        selected_index = self.tree_view.currentIndex()
        file_path = self.address_bar.get_file_path(selected_index)

        # Verificar si se seleccionó un archivo o carpeta
        if not file_path:
            return

        # Cortar el archivo y almacenar su ruta en el portapapeles
        self.clipboard.setText(file_path)

        # Eliminar el archivo o carpeta del sistema de archivos
        try:
            if os.path.isfile(file_path):
                os.remove(file_path)
            elif os.path.isdir(file_path):
                shutil.rmtree(file_path)
            
            # Actualizar el QTreeView para reflejar los cambios
            parent_path = os.path.dirname(file_path)
            self.address_bar.update_tree_view(parent_path)
        except Exception as e:
            # Manejar errores al cortar el archivo o carpeta
            print(f"Error al cortar el archivo o carpeta: {e}")
    # Metodo para pegar archivos
    def pegar_archivo(self):
        # Obtener la ruta de la carpeta actual
        current_folder_path = self.address_bar.get_current_folder_path()
        #current_folder_path = os.path.abspath(os.getcwd())

        # Obtener la ruta del archivo o carpeta almacenado en el portapapeles
        file_path = self.clipboard.text()

        if not current_folder_path or not file_path:
            return

        # Obtener el nombre del archivo o carpeta para pegarlo en la carpeta actual
        item_name = os.path.basename(file_path)
        destination_path = os.path.join(current_folder_path, item_name)

        # Verificar si el archivo o carpeta a pegar se encuentra en el mismo directorio de destino
        if os.path.abspath(file_path) == os.path.abspath(destination_path):
            reply = QMessageBox.question(self.tree_view, "Sobrescribir", "El archivo o carpeta a pegar está en el mismo directorio de destino. ¿Desea sobrescribirlo?", QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No)
            if reply != QMessageBox.StandardButton.Yes:
                return

        try:
            if os.path.isfile(file_path):
                shutil.copy(file_path, destination_path)
            elif os.path.isdir(file_path):
                shutil.copytree(file_path, destination_path)            
            # Actualizar el QTreeView para reflejar los cambios
            self.address_bar.update_tree_view(current_folder_path)
        except Exception as e:
            # Manejar errores al pegar el archivo o carpeta
            print(f"Error al pegar el archivo o carpeta: {e}")
        

        # TO-DO: Implementar la lógica para pegar el archivo o carpeta en la carpeta actual
        # (por ejemplo, usando shutil.move para cortar o shutil.copy para copiar)

        print(f"Archivo o carpeta pegado en: {current_folder_path}")
    # Metodo para crear un archivo de texto
    def create_text_file(self):
        # Obtener la carpeta actual del QTreeView utilizando el método get_current_folder_path de AddressBar
        current_folder_path = self.address_bar.get_current_folder_path()            
        if current_folder_path:
            file_name, ok = QInputDialog.getText(self.tree_view, "Crear archivo de texto", "Nombre del archivo:")
            if ok and file_name:
                file_path = os.path.join(current_folder_path, f"{file_name}.txt")
                with open(file_path, "w") as file:
                    file.write("")
                self.address_bar.update_tree_view(current_folder_path)
    
    # Método para crear una nueva carpeta en la ubicación actual
    def create_folder(self):
        current_folder_path = self.address_bar.get_current_folder_path()
        print(current_folder_path)

        # Solicitar el nombre de la nueva carpeta al usuario
        new_folder_name, ok = QInputDialog.getText(self.tree_view, "Nueva Carpeta", "Nombre de la carpeta:")
        if ok and new_folder_name:
            new_folder_path = os.path.join(current_folder_path, new_folder_name)

            try:
                os.makedirs(new_folder_path)
                # Actualizar el QTreeView para reflejar los cambios
                self.address_bar.update_tree_view(current_folder_path)
            except Exception as e:
                # Manejar errores al crear la carpeta
                print(f"Error al crear la carpeta: {e}")
    
    def deleteFile(self):
        # Obtener el índice del archivo seleccionado en el QTreeView
        selected_index = self.tree_view.currentIndex()
        
        if not selected_index.isValid():
            return

        # Obtener la ruta completa del archivo/directorio seleccionado utilizando el método get_file_path del AddressBar
        file_path = self.address_bar.get_file_path(selected_index)
        
        if self.address_bar.get_file_path(selected_index) is None:
            return
     
        if not self.address_bar.get_file_path(selected_index):
            return
                
        # Mostrar un cuadro de diálogo de confirmación
        confirm_dialog = QMessageBox.question(self.tree_view, "Eliminar elemento",
                                            f"¿Estás seguro que deseas eliminar el archivo/directorio '{os.path.basename(file_path)}'?",
                                            QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No)
        if confirm_dialog == QMessageBox.StandardButton.Yes:
            try:
                # Verificar si es un directorio antes de eliminar
                if os.path.isdir(file_path):                    
                    # Eliminar el directorio y su contenido utilizando shutil.rmtree
                    shutil.rmtree(file_path)
                elif os.path.isfile(file_path):
                    # Si es un archivo, eliminarlo directamente con os.remove
                    os.remove(file_path)

                # Actualizar la vista después de eliminar el archivo/directorio
                self.address_bar.update_tree_view(self.address_bar.get_current_folder_path())
            except Exception as e:
                # Manejar errores al eliminar el archivo/directorio
                print(f"Error al eliminar el archivo/directorio: {e}")
                QMessageBox.warning(self.tree_view, "Error", "No se pudo eliminar el archivo/directorio.")
                    
    # Método para exportar un informe en formato TXT con el inventario de documentos y rutas
    def export_report_to_txt(self):
        # Mostrar la ventana emergente para seleccionar la ubicación de guardado del archivo TXT
        file_dialog = QFileDialog()
        file_path, _ = file_dialog.getSaveFileName(self.tree_view, "Guardar informe como archivo de texto", "", "Archivos de texto (*.txt)")

        if not file_path:
            # Si el usuario cancela la selección, no hacemos nada
            return

        try:
            # Obtener la ruta de la carpeta actual
            current_folder_path = self.address_bar.get_current_folder_path()

            # Obtener el contenido del QTreeView
            model = self.tree_view.model()

            # Generar el contenido del informe en formato de árbol
            report_content = "Árbol de Directorios:\n\n"
            report_content = self.generate_tree_structure(current_folder_path, model)

            # Guardar el informe en el archivo de texto seleccionado
            with open(file_path, "w", encoding="utf-8") as file:
                file.write(report_content)

            # Mostrar el informe en la consola
            print(report_content)

            QMessageBox.information(self.tree_view, "Informe exportado", "El informe ha sido exportado correctamente.")
        except Exception as e:
            # Manejar errores al guardar el informe
            print(f"Error al guardar el informe: {e}")
            QMessageBox.warning(self.tree_view, "Error", "No se pudo guardar el informe.")

    # Método para generar la estructura de directorios en formato de árbol de manera no recursiva
    def generate_tree_structure(self, current_folder_path, model):
        stack = [(current_folder_path, 0)]
        lines = []  # Lista temporal para almacenar las líneas del informe

        while stack:
            current_folder_path, level = stack.pop()
            line = f"{'    ' * level}|- {os.path.basename(current_folder_path)}\n"
            lines.append(line)

            # Obtener los elementos dentro del directorio actual utilizando os
            try:
                entries = os.listdir(current_folder_path)
            except OSError:
                continue

            for entry in entries:
                file_path = os.path.join(current_folder_path, entry)
                print(file_path)

                if os.path.isdir(file_path):
                    stack.append((file_path, level + 1))
                else:
                    line = f"{'    ' * (level + 1)}|- {file_path}\n"
                    lines.append(line)

        # Unir las líneas para formar el contenido final del informe
        report_content = "".join(lines)
        return report_content
    
    def protect_directory(self):
        selected_indexes = self.tree_view.selectedIndexes()

        if not selected_indexes:
            return

        selected_index = selected_indexes[0]  # Solo considerando la posicion [0] , primera posicion
        selected_path = self.file_system_model.filePath(selected_index)

        if not self.file_system_model.isDir(selected_index):
            QMessageBox.warning(self.tree_view, "Error", "Solo se protegen directorios")
            return

        # Para actualizar la contraseña
        if self.security_manager.is_protected_folder(selected_path):
            reply = QMessageBox.question(self.tree_view, "Advertencia", "¿Directorio protegido, desea actualizar la contraseña?",QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No)
            if reply == QMessageBox.StandardButton.Yes:
                password, ok = QInputDialog.getText(self.tree_view, "Informacion", "Introduzca la nueva contraseña:", QLineEdit.EchoMode.Password)
                if self.security_manager.check_password(selected_path, password):
                    self.security_manager.add_folder(selected_path, password)
                else:
                    QMessageBox.warning(self.tree_view, "Advertencia", "Contraseña incorrecta. Acceso Denegado.")
                    return                
            else:
                return          
        password, ok = QInputDialog.getText(self.tree_view, "Informacion", "Introduzca contraseña:", QLineEdit.EchoMode.Password)
        if ok:
            self.security_manager.add_folder(selected_path, password)
            QMessageBox.information(self.tree_view, "Informacion", "Directorio protegido con contraseña.")    
