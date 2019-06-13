import time
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import base64
import cv2
import io
import json

def check(label, image):
	img = base64.b64decode(image)
	img = io.BytesIO(img)
	img = mpimg.imread(img, format='PNG')

	img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

	img = crop_image(img)

	img = resize_image(img, 32, 32)


	plt.imshow(img, interpolation='nearest')
	plt.show()
	plt.close()

	isCorrect = correct_classification(image, label)

	response = {}
	response['correct'] = isCorrect
	json_response = json.dumps(response)

	return json_response 

def correct_classification(image, label):
	isCorrect = True

	# TODO: Use model to check if image-label pair is correct

	return isCorrect



def crop_image(img):
	pixels = np.argwhere(img==1)

	st_y = min(pixels, key=lambda x: x[0])[0]
	end_y = max(pixels, key=lambda x: x[0])[0]

	st_x = min(pixels, key=lambda x: x[1])[1]
	end_x = max(pixels, key=lambda x: x[1])[1]

	img = img[st_y:end_y, st_x:end_x]

	return img

def resize_image(img, x, y):
	return cv2.resize(img, (x, y))