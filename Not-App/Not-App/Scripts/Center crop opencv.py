import numpy as np
import cv2
import glob
import os
os.chdir('/home/eon/Desktop/Sorted/Malignant melanoma')
images = [cv2.imread(file) for file in glob.glob("ToBeCCroped/*.jpeg")]
i=0
for img in images:
    cv2.imwrite("wart"+str(i)+".jpeg",img[15:114,6:106,:])
    i=i+1