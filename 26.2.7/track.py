import cv2
from ultralytics import YOLO

img = cv2.imread('1.png')
yolo = YOLO('yolov8n.pt')
yolo(source=img,save=True)
