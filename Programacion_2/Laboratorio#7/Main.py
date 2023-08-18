import os
import sys
import traceback
import asyncio

from PyQt6 import QtCore, QtGui, QtWidgets, uic

class FrmUsuario(QtWidgets.QDialog):
    def __init__(self):                
        super(FrmUsuario,self).__init__()                        
        
        self.ui_path = os.path.dirname(os.path.abspath(__file__))               
        uic.load_ui.loadUi(os.path.join(self.ui_path, "form.ui"),self)        
        
        self.textEscribir = self.findChild(QtWidgets.QPlainTextEdit,"TxtEscribir")
        self.btnEscribir = self.findChild(QtWidgets.QPushButton,"BtnCrear")
        
        self.textLeer = self.findChild(QtWidgets.QPlainTextEdit,"TxtLeer")
        self.btnLeer = self.findChild(QtWidgets.QPushButton,"BtnLeer")
        self.btnLeerXLinea = self.findChild(QtWidgets.QPushButton,"BtnLeerXLinea")
        
        self.btnEscribir.clicked.connect(self.mensajeEmergenteEscritura)        
        self.btnLeer.clicked.connect(self.LecturaArchivo)          
        self.btnLeerXLinea.clicked.connect(self.mensajeEmergenteLectura)
        
        self.VentanaEmergente = None      
        
    def mensajeEmergenteEscritura(self):        
        msg = QtWidgets.QMessageBox(self)  
        msg.setIcon(QtWidgets.QMessageBox.Icon.Warning)      
        msg.setText("Desea escrbir en el archivo?")        
        msg.setWindowTitle("Validacion de escritura")        
        msg.setStandardButtons(QtWidgets.QMessageBox.StandardButton.Yes | QtWidgets.QMessageBox.StandardButton.No)        
        a = msg.exec()  
        if (a == QtWidgets.QMessageBox.StandardButton.Yes):
            self.EscribirFile()            
        else:
            pass 
    def mensajeEmergenteLectura(self):        
        msg = QtWidgets.QMessageBox(self)  
        msg.setIcon(QtWidgets.QMessageBox.Icon.Warning)      
        msg.setText("Desea leer el archivo linea por linea?")        
        msg.setWindowTitle("Validacion de lectura")        
        msg.setStandardButtons(QtWidgets.QMessageBox.StandardButton.Yes | QtWidgets.QMessageBox.StandardButton.No)        
        a = msg.exec()  
        if (a == QtWidgets.QMessageBox.StandardButton.Yes):
            self.LecturaArchivoXLinea()           
        else:
            pass 
        
    def EscribirFile(self):
        try:            
            archivo = open(os.path.join(self.ui_path,"ClienteTs.txt"),'a') #append       
            archivo.write(self.textEscribir.toPlainText()+"\r")
            archivo.close()
            self.textEscribir.clear()
            
        except OSError as oE:
            archivo.close()
            print(oE.strerror)
        except BaseException:
            print(traceback.format_exc())
            archivo.close()
    
    def LecturaArchivo(self):
        try:
            archivo = open(os.path.join(self.ui_path,"ClienteTs.txt"),'r')        
            texto = archivo.read()                        
            self.textLeer.setPlainText(texto)
            archivo.close()
            #print(archivo.read()) 
        except OSError as oE:
            archivo.close()
            print(oE.strerror)
        except BaseException:
            archivo.close()  
            
    #Metodo para leer el archivo línea por línea y muestra cada línea en el widget de texto        
    def LecturaArchivoXLinea(self):
        try:
            archivo = open(os.path.join(self.ui_path,"ClienteTs.txt"),'r') 
            self.textLeer.clear()    
            # Lee el archivo línea por línea y muestra cada línea en el widget de texto
            linea = archivo.readline()
            while linea: #Hasta EOF
                self.textLeer.appendPlainText(linea.rstrip('\n'))  # Usamos rstrip para eliminar el salto de línea
                print(f'Linea leida: {linea}')             
                linea = archivo.readline()
            archivo.close()
        except OSError as oE:
            archivo.close()
            print(oE.strerror)
        except BaseException:
            archivo.close()          
    
    
app = QtWidgets.QApplication(sys.argv)       
form = FrmUsuario()
form.show()

sys.exit(app.exec())
    