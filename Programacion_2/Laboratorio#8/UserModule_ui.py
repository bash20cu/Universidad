# Form implementation generated from reading ui file 'c:\Users\Pc Personal\Documents\GitHub\EntregaLaboratoriosProgaII\Laboratorio#8\MiguelFernandezArteaga\UserModule.ui'
#
# Created by: PyQt6 UI code generator 6.4.2
#
# WARNING: Any manual changes made to this file will be lost when pyuic6 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt6 import QtCore, QtGui, QtWidgets


class Ui_FrmUsr(object):
    def setupUi(self, FrmUsr):
        FrmUsr.setObjectName("FrmUsr")
        FrmUsr.setEnabled(True)
        FrmUsr.resize(641, 441)
        self.TblBitacora = QtWidgets.QTableWidget(parent=FrmUsr)
        self.TblBitacora.setGeometry(QtCore.QRect(10, 230, 611, 131))
        self.TblBitacora.setEditTriggers(QtWidgets.QAbstractItemView.EditTrigger.AnyKeyPressed|QtWidgets.QAbstractItemView.EditTrigger.EditKeyPressed)
        self.TblBitacora.setSelectionMode(QtWidgets.QAbstractItemView.SelectionMode.SingleSelection)
        self.TblBitacora.setSelectionBehavior(QtWidgets.QAbstractItemView.SelectionBehavior.SelectRows)
        self.TblBitacora.setObjectName("TblBitacora")
        self.TblBitacora.setColumnCount(6)
        self.TblBitacora.setRowCount(0)
        item = QtWidgets.QTableWidgetItem()
        self.TblBitacora.setHorizontalHeaderItem(0, item)
        item = QtWidgets.QTableWidgetItem()
        self.TblBitacora.setHorizontalHeaderItem(1, item)
        item = QtWidgets.QTableWidgetItem()
        self.TblBitacora.setHorizontalHeaderItem(2, item)
        item = QtWidgets.QTableWidgetItem()
        self.TblBitacora.setHorizontalHeaderItem(3, item)
        item = QtWidgets.QTableWidgetItem()
        self.TblBitacora.setHorizontalHeaderItem(4, item)
        item = QtWidgets.QTableWidgetItem()
        self.TblBitacora.setHorizontalHeaderItem(5, item)
        self.TblBitacora.horizontalHeader().setVisible(True)
        self.TblBitacora.verticalHeader().setVisible(True)
        self.formLayoutWidget = QtWidgets.QWidget(parent=FrmUsr)
        self.formLayoutWidget.setGeometry(QtCore.QRect(10, 10, 341, 178))
        self.formLayoutWidget.setObjectName("formLayoutWidget")
        self.formLayout = QtWidgets.QFormLayout(self.formLayoutWidget)
        self.formLayout.setContentsMargins(0, 0, 0, 0)
        self.formLayout.setHorizontalSpacing(8)
        self.formLayout.setVerticalSpacing(10)
        self.formLayout.setObjectName("formLayout")
        self.label = QtWidgets.QLabel(parent=self.formLayoutWidget)
        self.label.setObjectName("label")
        self.formLayout.setWidget(0, QtWidgets.QFormLayout.ItemRole.LabelRole, self.label)
        self.TxtCedula = QtWidgets.QLineEdit(parent=self.formLayoutWidget)
        self.TxtCedula.setObjectName("TxtCedula")
        self.formLayout.setWidget(0, QtWidgets.QFormLayout.ItemRole.FieldRole, self.TxtCedula)
        self.label_2 = QtWidgets.QLabel(parent=self.formLayoutWidget)
        self.label_2.setObjectName("label_2")
        self.formLayout.setWidget(1, QtWidgets.QFormLayout.ItemRole.LabelRole, self.label_2)
        self.TxtNombre = QtWidgets.QLineEdit(parent=self.formLayoutWidget)
        self.TxtNombre.setObjectName("TxtNombre")
        self.formLayout.setWidget(1, QtWidgets.QFormLayout.ItemRole.FieldRole, self.TxtNombre)
        self.label_5 = QtWidgets.QLabel(parent=self.formLayoutWidget)
        self.label_5.setObjectName("label_5")
        self.formLayout.setWidget(2, QtWidgets.QFormLayout.ItemRole.LabelRole, self.label_5)
        self.cmbGenero = QtWidgets.QComboBox(parent=self.formLayoutWidget)
        self.cmbGenero.setObjectName("cmbGenero")
        self.cmbGenero.addItem("")
        self.cmbGenero.addItem("")
        self.cmbGenero.addItem("")
        self.formLayout.setWidget(2, QtWidgets.QFormLayout.ItemRole.FieldRole, self.cmbGenero)
        self.label_3 = QtWidgets.QLabel(parent=self.formLayoutWidget)
        self.label_3.setObjectName("label_3")
        self.formLayout.setWidget(3, QtWidgets.QFormLayout.ItemRole.LabelRole, self.label_3)
        self.CmbPais = QtWidgets.QComboBox(parent=self.formLayoutWidget)
        self.CmbPais.setObjectName("CmbPais")
        self.CmbPais.addItem("")
        self.CmbPais.addItem("")
        self.CmbPais.addItem("")
        self.formLayout.setWidget(3, QtWidgets.QFormLayout.ItemRole.FieldRole, self.CmbPais)
        self.label_4 = QtWidgets.QLabel(parent=self.formLayoutWidget)
        self.label_4.setObjectName("label_4")
        self.formLayout.setWidget(4, QtWidgets.QFormLayout.ItemRole.LabelRole, self.label_4)
        self.DtRegistro = QtWidgets.QDateEdit(parent=self.formLayoutWidget)
        self.DtRegistro.setObjectName("DtRegistro")
        self.formLayout.setWidget(4, QtWidgets.QFormLayout.ItemRole.FieldRole, self.DtRegistro)
        self.horizontalLayoutWidget = QtWidgets.QWidget(parent=FrmUsr)
        self.horizontalLayoutWidget.setGeometry(QtCore.QRect(10, 190, 611, 31))
        self.horizontalLayoutWidget.setObjectName("horizontalLayoutWidget")
        self.horizontalLayout = QtWidgets.QHBoxLayout(self.horizontalLayoutWidget)
        self.horizontalLayout.setContentsMargins(0, 0, 0, 0)
        self.horizontalLayout.setSpacing(3)
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.BtnRegistrar = QtWidgets.QPushButton(parent=self.horizontalLayoutWidget)
        self.BtnRegistrar.setObjectName("BtnRegistrar")
        self.horizontalLayout.addWidget(self.BtnRegistrar)
        self.BtnModificar = QtWidgets.QPushButton(parent=self.horizontalLayoutWidget)
        self.BtnModificar.setObjectName("BtnModificar")
        self.horizontalLayout.addWidget(self.BtnModificar)
        self.BtnEliminar = QtWidgets.QPushButton(parent=self.horizontalLayoutWidget)
        self.BtnEliminar.setObjectName("BtnEliminar")
        self.horizontalLayout.addWidget(self.BtnEliminar)
        self.label_6 = QtWidgets.QLabel(parent=FrmUsr)
        self.label_6.setGeometry(QtCore.QRect(370, 10, 131, 41))
        self.label_6.setObjectName("label_6")

        self.retranslateUi(FrmUsr)
        QtCore.QMetaObject.connectSlotsByName(FrmUsr)

    def retranslateUi(self, FrmUsr):
        _translate = QtCore.QCoreApplication.translate
        FrmUsr.setWindowTitle(_translate("FrmUsr", "Sistema de usuarios Goku"))
        item = self.TblBitacora.horizontalHeaderItem(0)
        item.setText(_translate("FrmUsr", "Id"))
        item = self.TblBitacora.horizontalHeaderItem(1)
        item.setText(_translate("FrmUsr", "Nombre"))
        item = self.TblBitacora.horizontalHeaderItem(2)
        item.setText(_translate("FrmUsr", "Género"))
        item = self.TblBitacora.horizontalHeaderItem(3)
        item.setText(_translate("FrmUsr", "Pais"))
        item = self.TblBitacora.horizontalHeaderItem(4)
        item.setText(_translate("FrmUsr", "Registro"))
        item = self.TblBitacora.horizontalHeaderItem(5)
        item.setText(_translate("FrmUsr", "Categoria"))
        self.label.setText(_translate("FrmUsr", "Cedula"))
        self.label_2.setText(_translate("FrmUsr", "Nombre"))
        self.label_5.setText(_translate("FrmUsr", "Género"))
        self.cmbGenero.setCurrentText(_translate("FrmUsr", "Masculino"))
        self.cmbGenero.setItemText(0, _translate("FrmUsr", "Masculino"))
        self.cmbGenero.setItemText(1, _translate("FrmUsr", "Femenino"))
        self.cmbGenero.setItemText(2, _translate("FrmUsr", "Otro"))
        self.label_3.setText(_translate("FrmUsr", "Pais"))
        self.CmbPais.setItemText(0, _translate("FrmUsr", "Costa Rica"))
        self.CmbPais.setItemText(1, _translate("FrmUsr", "Mexico"))
        self.CmbPais.setItemText(2, _translate("FrmUsr", "Panama"))
        self.label_4.setText(_translate("FrmUsr", "Registro"))
        self.BtnRegistrar.setText(_translate("FrmUsr", "Registrar"))
        self.BtnModificar.setText(_translate("FrmUsr", "Modificar"))
        self.BtnEliminar.setText(_translate("FrmUsr", "Eliminar"))
        self.label_6.setText(_translate("FrmUsr", "Miguel Fernandez"))
