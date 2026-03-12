# _*_ coding: utf-8 _*_
# autuor: yiXiao
# @project: 26.2.7
# title: export
# @time: 2026/3/4 14:53
from ultralytics import YOLO


if __name__ == '__main__':

    model = YOLO("yolov8n.pt")
    model.export(format="onnx")