# -*- coding: utf-8 -*-

################################################################################
## Form generated from reading UI file 'main_window.ui'
##
## Created by: Qt User Interface Compiler version 6.10.0
##
## WARNING! All changes made in this file will be lost when recompiling UI file!
################################################################################

from PySide6.QtCore import (QCoreApplication, QDate, QDateTime, QLocale,
    QMetaObject, QObject, QPoint, QRect,
    QSize, QTime, QUrl, Qt)
from PySide6.QtGui import (QBrush, QColor, QConicalGradient, QCursor,
    QFont, QFontDatabase, QGradient, QIcon,
    QImage, QKeySequence, QLinearGradient, QPainter,
    QPalette, QPixmap, QRadialGradient, QTransform)
from PySide6.QtWidgets import (QApplication, QHBoxLayout, QLabel, QLineEdit,
    QListWidget, QListWidgetItem, QMainWindow, QPlainTextEdit,
    QPushButton, QSizePolicy, QSpinBox, QVBoxLayout,
    QWidget)

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        if not MainWindow.objectName():
            MainWindow.setObjectName(u"MainWindow")
        self.centralwidget = QWidget(MainWindow)
        self.centralwidget.setObjectName(u"centralwidget")
        self.verticalLayoutRoot = QVBoxLayout(self.centralwidget)
        self.verticalLayoutRoot.setObjectName(u"verticalLayoutRoot")
        self.layoutConnection = QHBoxLayout()
        self.layoutConnection.setObjectName(u"layoutConnection")
        self.leHost = QLineEdit(self.centralwidget)
        self.leHost.setObjectName(u"leHost")

        self.layoutConnection.addWidget(self.leHost)

        self.sbPort = QSpinBox(self.centralwidget)
        self.sbPort.setObjectName(u"sbPort")
        self.sbPort.setMinimum(1)
        self.sbPort.setMaximum(65535)
        self.sbPort.setValue(5555)

        self.layoutConnection.addWidget(self.sbPort)

        self.leUser = QLineEdit(self.centralwidget)
        self.leUser.setObjectName(u"leUser")

        self.layoutConnection.addWidget(self.leUser)

        self.lePass = QLineEdit(self.centralwidget)
        self.lePass.setObjectName(u"lePass")
        self.lePass.setEchoMode(QLineEdit.Password)

        self.layoutConnection.addWidget(self.lePass)

        self.btnConnect = QPushButton(self.centralwidget)
        self.btnConnect.setObjectName(u"btnConnect")

        self.layoutConnection.addWidget(self.btnConnect)


        self.verticalLayoutRoot.addLayout(self.layoutConnection)

        self.layoutMain = QHBoxLayout()
        self.layoutMain.setObjectName(u"layoutMain")
        self.layoutChat = QVBoxLayout()
        self.layoutChat.setObjectName(u"layoutChat")
        self.lblStatus = QLabel(self.centralwidget)
        self.lblStatus.setObjectName(u"lblStatus")

        self.layoutChat.addWidget(self.lblStatus)

        self.lblMode = QLabel(self.centralwidget)
        self.lblMode.setObjectName(u"lblMode")

        self.layoutChat.addWidget(self.lblMode)

        self.teChat = QPlainTextEdit(self.centralwidget)
        self.teChat.setObjectName(u"teChat")
        self.teChat.setReadOnly(True)

        self.layoutChat.addWidget(self.teChat)

        self.layoutInput = QHBoxLayout()
        self.layoutInput.setObjectName(u"layoutInput")
        self.leMessage = QLineEdit(self.centralwidget)
        self.leMessage.setObjectName(u"leMessage")

        self.layoutInput.addWidget(self.leMessage)

        self.btnSend = QPushButton(self.centralwidget)
        self.btnSend.setObjectName(u"btnSend")

        self.layoutInput.addWidget(self.btnSend)

        self.btnPublic = QPushButton(self.centralwidget)
        self.btnPublic.setObjectName(u"btnPublic")

        self.layoutInput.addWidget(self.btnPublic)


        self.layoutChat.addLayout(self.layoutInput)


        self.layoutMain.addLayout(self.layoutChat)

        self.layoutUsers = QVBoxLayout()
        self.layoutUsers.setObjectName(u"layoutUsers")
        self.lblUsersTitle = QLabel(self.centralwidget)
        self.lblUsersTitle.setObjectName(u"lblUsersTitle")

        self.layoutUsers.addWidget(self.lblUsersTitle)

        self.lwUsers = QListWidget(self.centralwidget)
        self.lwUsers.setObjectName(u"lwUsers")

        self.layoutUsers.addWidget(self.lwUsers)


        self.layoutMain.addLayout(self.layoutUsers)


        self.verticalLayoutRoot.addLayout(self.layoutMain)

        MainWindow.setCentralWidget(self.centralwidget)

        self.retranslateUi(MainWindow)

        QMetaObject.connectSlotsByName(MainWindow)
    # setupUi

    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(QCoreApplication.translate("MainWindow", u"PSP Chat Client", None))
        self.leHost.setPlaceholderText(QCoreApplication.translate("MainWindow", u"Host", None))
        self.leUser.setPlaceholderText(QCoreApplication.translate("MainWindow", u"Usuario", None))
        self.lePass.setPlaceholderText(QCoreApplication.translate("MainWindow", u"Contrase\u00f1a", None))
        self.btnConnect.setText(QCoreApplication.translate("MainWindow", u"Conectar", None))
        self.lblStatus.setText(QCoreApplication.translate("MainWindow", u"Desconectado", None))
        self.lblMode.setText(QCoreApplication.translate("MainWindow", u"P\u00fablico", None))
        self.leMessage.setPlaceholderText(QCoreApplication.translate("MainWindow", u"Escribe un mensaje\u2026", None))
        self.btnSend.setText(QCoreApplication.translate("MainWindow", u"Enviar", None))
        self.btnPublic.setText(QCoreApplication.translate("MainWindow", u"Volver a p\u00fablico", None))
        self.lblUsersTitle.setText(QCoreApplication.translate("MainWindow", u"Usuarios", None))
    # retranslateUi

