from rest_framework.views import APIView
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
import json

from recognition.lib import example, signs
import os

class RecognitionView(APIView):
        

    def get(self, request, format=None):
        return HttpResponse(signs.get_signs())

    def post(self, request, format=None):
        body = json.loads(request.body)
        return HttpResponse(example.check(body["label"], body["image"]))
