"""

"""


import sys
from PySide6.QtWidgets import QApplication

from app.core.controller import ChatController
from app.gui.main_window import MainWindow


def main() -> int:
    app = QApplication(sys.argv)
    w = MainWindow()
    ctrl = ChatController(w)
    app.aboutToQuit.connect(ctrl.shutdown)
    w.show()
    return app.exec()


if __name__ == "__main__":
    raise SystemExit(main())
