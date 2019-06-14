import time
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import base64
import cv2
import io
import json
import pandas as pd
from PIL import Image
import imageio

from tensorflow.keras.preprocessing.image import ImageDataGenerator

from django.db import models
from tensorflow.keras.models import load_model
import tensorflow as tf

# from django.conf import settings

sess = tf.InteractiveSession()
sess.run(tf.global_variables_initializer())

model = load_model("model/model_kanji.ckpt")
# model.summary()

graph = tf.compat.v1.get_default_graph() 
# sess = tf.Sessi

def check(label, image):
    print("New Request for {}!".format(label))
    global graph
    global sess
    with graph.as_default():
        with sess.as_default():
            img = base64.b64decode(image)
            img = io.BytesIO(img)
            img = mpimg.imread(img, format='PNG')

            img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

            img = crop_image(255 - img)

            img = resize_image(img, 28, 28)

            kernel = np.ones((3,3),np.uint8)
            img = cv2.dilate(img, kernel)
            # img = cv2.filter2D(img,-1,np.ones((1,1),np.uint8))

            # plt.imshow(img, interpolation='nearest')
            # plt.show()
            # plt.close()

            classes = pd.read_csv("k49_classmap.csv")

            correct = correct_classification(image, label)
            img = img.reshape(1, 28, 28)

            # def test_gen(X_test):
            #     datagen = ImageDataGenerator(featurewise_center=True,
            #        featurewise_std_normalization=True)
            #     Y_trash = np.ones(X_test.shape[0])
            #     flow = datagen.flow(X_test, Y_trash)
            #     for X,Y in flow:
            #         yield X #ignore Y
            # distr = model.predict_generator(test_gen(img), steps = 1)
            # datagen_test = ImageDataGenerator(featurewise_center=True,
            #   featurewise_std_normalization=True)

            # datagen_test.flow(img)

            distr = model.predict(img)
            predicted_label = classes['rom'][np.argmax(distr)]
            print(predicted_label)
            
            # model.summary()
            # print(classes['rom'][classes['rom'] == label].index[0])
            response = {}
            response['correct'] = predicted_label == label
            response['prob_correct'] = float(distr[0][classes['rom'][classes['rom'] == label].index[0]])
            json_response = json.dumps(response)

            imageio.imwrite('images/{}_{}_{}_{}.png'.format(label, predicted_label, response['correct'], response['prob_correct']), img[0, :, :])

            return json_response


def correct_classification(image, label):
    isCorrect = True
    probCorrect = 0.9

    # TODO: Use model to check if image-label pair is correct

    return (isCorrect, probCorrect)


def crop_image(img):
    pixels = np.argwhere(img == 1)

    st_y = min(pixels, key=lambda x: x[0])[0]
    end_y = max(pixels, key=lambda x: x[0])[0]

    st_x = min(pixels, key=lambda x: x[1])[1]
    end_x = max(pixels, key=lambda x: x[1])[1]


    img = img[st_y:end_y, st_x:end_x]

    # centrowanie
    height = end_x - st_x
    width = end_y - st_y

    a = max(width, height)

    layout = np.zeros((a, a))

    layout[int((a-width)/2):width+int((a-width)/2), int((a-height)/2):height+int((a-height)/2)] = img[::,::]

    return layout


def resize_image(img, x, y):
    return cv2.resize(img, (x, y))
