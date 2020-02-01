# coding : utf-8
import sys,os
from keras.models import Sequential
from keras.layers import Convolution2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense
from keras.utils import np_utils
from keras.models import load_model
from PIL import Image
import numpy as np

# 테스트 이미지 목록
image_files=["C:/Users/LG/Desktop/deep/food/Chicken/abc.jpg",
             "C:/Users/LG/Desktop/deep/food/Chicken/chicken_02.jpg",
             "C:/Users/LG/Desktop/deep/food/Kimchi/kimchi15.jpg",
             "C:/Users/LG/Desktop/deep/food/Kimchi/kimchi07.jpg",
             "C:/Users/LG/Desktop/deep/food/Samgyeobsal/Samgyeobsal04.jpg",]
image_size=64
nb_classes=len(image_files)
categories=["Chicken","Dolsotbab","Jeyugbokk-eum","Kimchi","Samgyeobsal",
            "SoybeanPasteStew"]

X=[]
files=[]
#이미지 불러오기
for fname in image_files:
    img=Image.open(fname)
    img=img.convert("RGB")
    img=img.resize((image_size,image_size))
    in_data=np.asarray(img)
    in_data=in_data.astype("float")/256
    X.append(in_data)
    files.append(fname)

X=np.array(X)

#모델 파일 읽어오기
model=load_model('C:/Users/LG/Desktop/deep/food/kfood_model.h5')

#예측 실행
pre=model.predict(X)

#예측 결과 출력
for i,p in enumerate(pre):
    y=p.argmax()
    print("입력:",files[i])
    print("예측:","[",y,"]", categories[y],"/ Socre",p[y])
