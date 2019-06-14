from django.db import models
from tensorflow.keras.models import load_model
import tensorflow as tf

# Create your models here.

model = load_model("model/model_kanji.ckpt")

global graph
graph = tf.get_default_graph() 