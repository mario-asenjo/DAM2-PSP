"""
Envolvemos el archivo UI generado para que sea limpio, extensible y usable sin
tocar el fichero generado por pyside6-uic.
"""


from __future__ import annotations

from dataclasses import dataclass
from typing import Optional

from PySide6.QtCore import Slot
from PySide6.QtWidgets import QMainWindow

from app.ui_generada.ui_main_window import Ui_MainWindow


@dataclass(frozen=True)
class ConnectionParams:
    host: str
    port: int
    username: str
    password: str


class MainWindow(QMainWindow):
    """
    Capa limpia y extensible sobre Ui_Mainwindow (autogenerado).
    - No contiene red.
    - No contiene lógica de protocolo.
    - Solo UI + eventos + métodos helper para pintar.
    """
    def __init__(self, parent=None) -> None:
        super().__init__(parent)
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)

        self._connected: bool = False
        self._wire_signals()
        self._apply_initial_state()

    # --- API PUBLICA ---

    def get_connection_params(self) -> ConnectionParams:
        return ConnectionParams(
            host=self.ui.leHost.text().strip(),
            port=int(self.ui.sbPort.value()),
            username=self.ui.leUser.text().strip(),
            password=self.ui.lePass.text()
        )

    def set_connected(self, connected: bool, status_text: str = "") -> None:
        phrase: str = "Desconectar" if connected else "Conectar"
        self._connected = connected
        self.ui.btnSend.setEnabled(connected)
        self.ui.btnConnect.setText(phrase)
        self.ui.lblStatus.setText(status_text or phrase)

        # --- Bloqueamos la edición de credenciales en conectado
        self.ui.leHost.setEnabled(not connected)
        self.ui.sbPort.setEnabled(not connected)
        self.ui.leUser.setEnabled(not connected)
        self.ui.lePass.setEnabled(not connected)

    def append_chat_line(self, text: str) -> None:
        self.ui.teChat.appendPlainText(text)

    def clear_message_box(self) -> None:
        self.ui.leMessage.clear()

    def get_message_text(self) -> str:
        return self.ui.leMessage.text()

    # --- Eventos y Wiring ---

    def _apply_initial_state(self) -> None:
        self.ui.btnSend.setEnabled(False)
        self.ui.lblStatus.setText("Desconectado")

        # defaults úitles
        if not self.ui.leHost.text().strip():
            self.ui.leHost.setText("127.0.0.1")

    def _wire_signals(self) -> None:
        self.ui.btnConnect.clicked.connect(self.on_connect_clicked)
        self.ui.btnSend.clicked.connect(self.on_send_clicked)
        self.ui.btnPublic.clicked.connect(self.on_public_clicked)
        self.ui.leMessage.returnPressed.connect(self.on_send_clicked)  # Por que enter envía

    # Estas slots son manejadas o sobreescritas por el controlador.
    @Slot()
    def on_connect_clicked(self) -> None:
        self.append_chat_line("[ui] connect clicked")

    @Slot()
    def on_send_clicked(self) -> None:
        self.append_chat_line("[ui] send clicked")

    @Slot()
    def on_public_clicked(self) -> None:
        self.append_chat_line("[ui] public clicked")

    # --- Funiones helper para usuarios
    def selected_user(self) -> Optional[str]:
        """Return the currently selected username, if any."""
        item = self.ui.lwUsers.currentItem()
        return item.text() if item else None

    def set_users(self, users: list[str]) -> None:
        """Replace the user list contents."""
        self.ui.lwUsers.clear()
        self.ui.lwUsers.addItems(users)
