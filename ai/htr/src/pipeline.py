import cv2
import keras_ocr
import numpy as np
import matplotlib.pyplot as plt
import statistics
from recognizer import Recognizer
import tempfile
import os
import sys
import shutil

class Pipeline:

    def __init__(self, tf_model_dir: str) -> None:
        self.recognizer = Recognizer(tf_model_dir)

    def __detect(self, img_data):
        """
        Detects all text words in PNG file using keras-ocr CRAFT text detection
        Makes a box (4 points) per word 
        Sorts boxes in reading order (left to right, top to bottom)
        """
        keras_detector = keras_ocr.detection.Detector()
        boxes = keras_detector.detect(img_data)[0]
        return Tools(boxes).sort_boxes()

    
    def recognize(self, img_path, debug=False, verbose=1) -> str:
        """
        Crops all words from img_path using the detector boudary boxes
        Feeds croped words to the recognizer model that translates the text 
        """
        # discard stdout if not verbose
        if verbose == 0:
            sys.stdout = open(os.devnull, 'w')
        
        img_data = [cv2.imread(img_path)]
        boxes = self.__detect(img_data)
        store_images_path = img_path.replace(".png", "_out/")
        
        if not boxes:
            return
        
        if debug == True:
            if os.path.exists(store_images_path):
                shutil.rmtree(store_images_path)
            os.makedirs(store_images_path)
        
        arr = []
        count = 1
        for box in boxes:
            ext_img = Tools.extract_img(box, img_data[0])
            with tempfile.NamedTemporaryFile(prefix='frag-', suffix='.png', delete=False) as temp_file:
                cv2.imwrite(temp_file.name, np.array(ext_img))
                temp_file_name = temp_file.name
                temp_file.close()
                trad = self.recognizer.recognize_from_path(temp_file_name)
                arr.append(trad)
            os.remove(temp_file_name)
            
            if debug == True: 
                plt.imshow(ext_img)
                plt.show()
                plt.savefig(store_images_path + str(count) + ".png")
            
            count += 1

        # reset sdtout
        if verbose == 0:
            sys.stdout = sys.__stdout__

        return ' '.join(arr)           


class Tools:

    def __init__(self, boxes) -> None:
        self.boxes = boxes
        self.avg = self.__get_avg_box_width(boxes)

    def extract_img(box, img_data):
        """
        Given a boundary box and a image path, crops and extracts image data from 
        the location defined by the boundary box in the image
        """
        boundary_box = np.array(box).astype(int)
        x, y = boundary_box[:, 0], boundary_box[:, 1]
        xmin, xmax = np.min(x), np.max(x)
        ymin, ymax = np.min(y), np.max(y)
        img = np.array(img_data)
        return img[ymin:ymax, xmin:xmax]
    
    def __get_avg_box_width(self, boxes):
        """
        Get average box length, that is, the average word size
        """
        arr = []
        for box in boxes: 
            boxes_array = np.array(box)
            y_coords = boxes_array[:, 1]
            lenght = ((y_coords[2] - y_coords[0]) + (y_coords[3] - y_coords[1])) / 2
            arr.append(int(lenght))
        return int(statistics.mean(arr)/2)

    def sort_boxes(self):
        """
        Sort boundary boxes to english reading order (left to right, top to bottom)
        Uses quicksort
        """
        return self.__quicksort(self.boxes)

    def __quicksort(self, arr):
        """
        Implementation of quicksort for a array of boundary boxes
        """
        if len(arr) <= 1:
            return arr
        else:
            pivot = arr[0]
            left = [box for box in arr[1:] if self.__left_comp(box, pivot)]
            right = [box for box in arr[1:] if self.__right_comp(box, pivot)]
            return self.__quicksort(left) + [pivot] + self.__quicksort(right)
    
    def __left_comp(self, box, pivot):
        """
        Compares left box and pivot.
        Comparison is made using the first box point (a box is made of 4 points)
        If first points's y is in range of the pivot's y order by x
        Else order by y
        """
        if int(box[0][1]) in range(int(pivot[0][1]) - self.avg, int(pivot[0][1]) + self.avg):
            return box[0][0] < pivot[0][0]
        else: return box[0][1] < pivot[0][1]

    def __right_comp(self, box, pivot):
        """
        Compares right box and pivot.
        Comparison is made using the first box point (a box is made of 4 points)
        If first points's y is in range of the pivot's y order by x
        Else order by y
        """
        if int(box[0][1]) in range(int(pivot[0][1]) - self.avg, int(pivot[0][1]) + self.avg):
            return box[0][0] >= pivot[0][0]
        else: return box[0][1] >= pivot[0][1]