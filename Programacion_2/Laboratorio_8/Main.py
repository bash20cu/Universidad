import sys
from UserModule_ui import *
from EntidadesModulo import Persona

class FrmUsuario(QtWidgets.QDialog):
    def __init__(self):        
        #super(FrmUsuario,self).__init__()
        super().__init__()
        self.ui = Ui_FrmUsr()
        self.objPersona = None
                
        self.ui.setupUi(self)
        self.inicializarControles()
        self.ui.BtnRegistrar.clicked.connect(self.registrarPersona)
        self.ui.TblBitacora.clicked.connect(self.seleccionarDatos)
        self.ui.TblBitacora.clicked.connect(self.cargarDatos)
        self.ui.BtnEliminar.clicked.connect(self.eliminarRegistro)
        self.ui.BtnModificar.clicked.connect(self.modificarDatos)
        
    def registrarPersona(self): 
        
        self.fila = self.ui.TblBitacora.rowCount()
        cedula = self.ui.TxtCedula.text()
        nombre = self.ui.TxtNombre.text()
        genero = self.ui.cmbGenero.currentText()
        pais = self.ui.CmbPais.currentText()
        registro = self.ui.DtRegistro.text()
        self.objPersona = Persona(cedula,nombre,genero,pais,registro)        
        self.ui.TblBitacora.insertRow(self.fila)
        celdaCedula = QtWidgets.QTableWidgetItem(self.objPersona.cedula)
        celdaNombre = QtWidgets.QTableWidgetItem(self.objPersona.nombre)
        celdaGenero = QtWidgets.QTableWidgetItem(self.objPersona.genero)
        celdaPais = QtWidgets.QTableWidgetItem(self.objPersona.pais)
        celdaRegistro = QtWidgets.QTableWidgetItem(self.objPersona.registro)        
        celdaCategoria = QtWidgets.QTableWidgetItem(self.objPersona.categoria)   
    
        
        
        self.ui.TblBitacora.setItem(self.fila,0,celdaCedula)
        self.ui.TblBitacora.setItem(self.fila,1,celdaNombre)
        self.ui.TblBitacora.setItem(self.fila,2,celdaGenero)
        self.ui.TblBitacora.setItem(self.fila,3,celdaPais)
        self.ui.TblBitacora.setItem(self.fila,4,celdaRegistro)
        self.ui.TblBitacora.setItem(self.fila,5,celdaCategoria)

        ##self.fila += 1
        self.msgProceso()
        self.inicializarControles()
        
        
        
        
    def inicializarControles(self):
        self.ui.TxtCedula.clear()
        self.ui.TxtNombre.clear()
        self.ui.cmbGenero.setCurrentIndex(0)
        self.ui.CmbPais.setCurrentIndex(0)
        self.ui.DtRegistro.setDate(QtCore.QDate.currentDate())
      
    def cargarDatos(self):
        posicion = self.ui.TblBitacora.selectedItems ()
        fila = posicion[0].row()
        
        cedula = self.ui.TblBitacora.item(fila,0)
        self.ui.TxtCedula.setText(cedula.text())
        
        nombre = self.ui.TblBitacora.item(fila,1)
        self.ui.TxtNombre.setText(nombre.text())       
    
    
    def seleccionarDatos(self):
        posicion = self.ui.TblBitacora.selectedItems ()
        return posicion     


            


    def eliminarRegistro(self):
        filaSelect = self.seleccionarDatos()
        fila = filaSelect[0].row()
        
        if filaSelect:        
            self.ui.TblBitacora.removeRow(fila)
            self.fila -=1
            self.msgProceso()
        else:
            msg = QtWidgets.QMessageBox(self)
            msg.setIcon(QtWidgets.QMessageBox.Icon.Critical)
            msg.setWindowTitle("Eliminar Fila")
            msg.setText("Seleccione una fila. ")
            msg.setStandardButtons(QtWidgets.QMessageBox.StandardButton.Ok)
            msg.exec()
        self.inicializarControles()
      
    def modificarDatos(self): 
        filaSelect = self.seleccionarDatos()
        fila = filaSelect[0].row()
        
        cedula = self.ui.TxtCedula.text()
        nombre = self.ui.TxtNombre.text()
        genero = self.ui.cmbGenero.currentText()
        pais = self.ui.CmbPais.currentText()
        registro = self.ui.DtRegistro.text()
        self.objPersona = Persona(cedula,nombre,genero,pais,registro)        
        celdaCedula = QtWidgets.QTableWidgetItem(self.objPersona.cedula)
        celdaNombre = QtWidgets.QTableWidgetItem(self.objPersona.nombre)
        celdaGenero = QtWidgets.QTableWidgetItem(self.objPersona.genero)
        celdaPais = QtWidgets.QTableWidgetItem(self.objPersona.pais)
        celdaRegistro = QtWidgets.QTableWidgetItem(self.objPersona.registro)
        celdaCategoria = QtWidgets.QTableWidgetItem(self.objPersona.categoria)
        self.ui.TblBitacora.setItem(fila,0,celdaCedula)
        self.ui.TblBitacora.setItem(fila,1,celdaNombre)
        self.ui.TblBitacora.setItem(fila,2,celdaGenero)
        self.ui.TblBitacora.setItem(fila,3,celdaPais)
        self.ui.TblBitacora.setItem(fila,4,celdaRegistro)
        self.ui.TblBitacora.setItem(fila,5,celdaCategoria)
        self.msgProceso()
        self.inicializarControles()
    
    
    
    #Mensaje de decisión al escribir
    def msgProceso(self):        
        msg = QtWidgets.QMessageBox(self)
        msg.setIcon(QtWidgets.QMessageBox.Icon.Information)
        msg.setWindowTitle("Ejecución de proceso")        
        msg.setText("  Proceso Existoso ")
        msg.setStandardButtons(QtWidgets.QMessageBox.StandardButton.Ok)
        msg.exec() 
 
        
        
if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)    
    myapp = FrmUsuario()        
    myapp.show()
    sys.exit(app.exec())
    
  