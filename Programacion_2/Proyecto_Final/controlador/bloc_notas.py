from PyQt6.QtWidgets import QMainWindow, QApplication, QFileDialog, QMessageBox
from controlador.bloc_notas_ui import Ui_MainWindowBlocNotas
import sys, os


class MainBlocNotas(QMainWindow, Ui_MainWindowBlocNotas):
    def __init__(self,file_path=None):
        super().__init__()
        self.setupUi(self)        
        # Obtener el directorio actual de trabajo
        self.current_dir = os.getcwd()
        self.current_path = None 
        self.current_fontsize = 8
        self.setWindowTitle("Bloc de Notas")      
    
        
        self.actionNew.triggered.connect(self.newFile)
        self.actionSave.triggered.connect(self.saveFile)
        self.actionSave_As.triggered.connect(self.saveFileAs)
        #self.actionOpen.triggered.connect(self.openFile)
        self.actionUndo.triggered.connect(self.undo)
        self.actionRedo.triggered.connect(self.redo)
        self.actionCut.triggered.connect(self.cut)
        self.actionCopy.triggered.connect(self.copy)
        self.actionPaste.triggered.connect(self.paste)
        self.actionModo_Oscuro.triggered.connect(self.setDarkMode)
        self.actionModo_Claro.triggered.connect(self.setLightMode)
        self.actionAgrandar_Fuente.triggered.connect(self.incFontSize)
        self.actionDisminuir_Fuente.triggered.connect(self.decFontSize)



    def newFile(self):
        self.textEdit.clear()
        self.setWindowTitle("Untitled")
        self.current_path = None

    def saveFile(self):       
        if self.current_path is not None:
            # Guardar sin abrir el dialogo
            filetext = self.textEdit.toPlainText()
            with open(self.current_path, 'w') as f:
                f.write(filetext)
        else:
            # Dialogo para que el usuario escoga el directorio donde desea guardar el archivo
            pathname, _ = QFileDialog.getSaveFileName(self, 'Save file', self.current_dir, 'Text files(*.txt)')
            if pathname:
                filetext = self.textEdit.toPlainText()
                with open(pathname, 'w') as f:
                    f.write(filetext)
                self.current_path = pathname
                self.setWindowTitle(pathname)

    def saveFileAs(self):       
        # Mostrar el diálogo de guardar archivo con el directorio actual de trabajo como directorio inicial
        pathname = QFileDialog.getSaveFileName(self, 'Save file', self.current_dir, 'Text files(*.txt)')
        filetext = self.textEdit.toPlainText()
        with open(pathname[0], 'w') as f:
            f.write(filetext)
        self.current_path = pathname[0]
        self.setWindowTitle(pathname[0])

    '''def openFile(self, file_path=None):
        
        if file_path == None :
            fname = QFileDialog.getOpenFileName(self, 'Open file', self.current_dir, 'Text files (*.txt)')
            if not fname[0]:
                return
            file_path = fname[0]
        else:            
            try:
                with open(file_path, 'r') as f:                    
                    filetext = f.read()
                    self.textEdit.setText(filetext)
                self.setWindowTitle(file_path)
                self.current_path = file_path
            except Exception as e:
                print(f"Error opening file: {e}") '''
            
    def undo(self):
        self.textEdit.undo()

    def redo(self):
        self.textEdit.redo()

    def copy(self):
        self.textEdit.copy()

    def cut(self):
        self.textEdit.cut()

    def paste(self):
        self.textEdit.paste()

    def setDarkMode(self):
        self.setStyleSheet('''QWidget{
            background-color: rgb(33,33,33);
            color: #FFFFFF;
            }
            QTextEdit{
            background-color: rgb(46,46,46);
            }
            QMenuBar::item:selected{
            color: #000000
            } ''')

    def setLightMode(self):
        self.setStyleSheet("")   

    def incFontSize(self):
        self.current_fontsize +=1
        self.textEdit.setFontPointSize(self.current_fontsize)
        
    def decFontSize(self):
        self.current_fontsize -=1
        self.textEdit.setFontPointSize(self.current_fontsize)        
    
    def closeEvent(self, event):
        if self.textEdit.document().isModified():
            reply = QMessageBox.question(
                self, "Guardar Cambios",
                "Desea guardar los cambios?",
                QMessageBox.StandardButton.Save | QMessageBox.StandardButton.Discard | QMessageBox.StandardButton.Cancel
            )

            if reply == QMessageBox.StandardButton.Save:
                self.saveFile()
            elif reply == QMessageBox.StandardButton.Cancel:
                event.ignore()  # Ignorar y seguir con la ventana abierta
        
if __name__ == '__main__':
    app = QApplication(sys.argv)
    ui = MainBlocNotas()
    ui.show()
    sys.exit(app.exec())