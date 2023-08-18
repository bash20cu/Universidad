import os
from PyQt6.QtWidgets import QTreeView, QLineEdit,QPushButton
from PyQt6.QtGui import QFileSystemModel

class DocumentSearcher:
    def __init__(self, tree_view: QTreeView, search_bar: QLineEdit, BtBusqueda: QPushButton, file_system_model: QFileSystemModel):
        self.tree_view = tree_view
        self.search_bar = search_bar
        self.btBusqueda = BtBusqueda
        self.file_system_model = file_system_model        
        
        self.search_bar.setPlaceholderText("Busqueda...")
        self.btBusqueda.clicked.connect(self.on_search_button_clicked) 

    def filter_documents_by_name(self, name):
        root_path = os.getcwd()

        if name.strip():
            self.file_system_model.setNameFilters([f"*{name.lower()}*"])
            self.tree_view.setRootIndex(self.file_system_model.index(root_path))
            self.tree_view.setRootIsDecorated(False)
            self.tree_view.setSortingEnabled(False)
            self.tree_view.setExpanded(self.tree_view.rootIndex(), True)
        else:
            self.file_system_model.setNameFilters([])
            self.tree_view.setRootIndex(self.file_system_model.index(root_path))
            self.tree_view.setRootIsDecorated(True)
            self.tree_view.setSortingEnabled(True)


    def search_documents_by_name(self, path, name):
        filtered_files = []
        for root, _, files in os.walk(path):
            for file in files:
                if name in file.lower():
                    file_path = os.path.join(root, file)
                    filtered_files.append(file_path)
        return filtered_files

    def on_search_button_clicked(self):
        search_term = self.search_bar.text()
        self.filter_documents_by_name(search_term)
