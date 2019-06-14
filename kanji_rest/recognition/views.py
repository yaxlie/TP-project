from rest_framework.views import APIView
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
import json

from tensorflow.keras.models import load_model

from recognition.lib import example, signs


class RecognitionView(APIView):

    def __init__(self, **kwargs):
        super(RecognitionView).__init__(kwargs)
        self.model = load_model("model/model_kanji.ckpt")

    def get(self, request, format=None):
        return HttpResponse(signs.get_signs())

    def post(self, request, format=None):

        body = json.loads(request.body)
        return HttpResponse(example.check(body["label"], body["image"], self.model))
