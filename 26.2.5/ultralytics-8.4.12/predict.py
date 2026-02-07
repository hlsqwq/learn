import pyautogui

from ultralytics import YOLO


class Pos:
    def __init__(self, source: str):
        yolo = YOLO(model='demo.pt', task='detect')
        self.res = yolo(source=source)[0]

    def getPos(self, name: str):
        index = None
        for k, v in self.res.names.items():
            if v == name:
                index = k
                break
        if index is None:
            return None

        for k, v in enumerate(self.res.boxes.cls):
            if v == index:
                index = k
                break
        index_ = self.res.boxes.xywh[index]
        return index_[0], index_[1]


if __name__ == '__main__':
    pyautogui.screenshot('shot.png')
    pos = Pos('shot.png')
    get_pos = pos.getPos('trash')
    pyautogui.doubleClick(get_pos)
