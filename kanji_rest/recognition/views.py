from django.http import HttpResponse, HttpRequest
from django.views.decorators.csrf import csrf_exempt
import json

from recognition.lib import example
from recognition.lib import signs

@csrf_exempt
def check(request: HttpRequest):
	if request.method == 'POST':
		body = json.loads(request.body)
		return HttpResponse(example.check(body["label"], body["image"]))
	else:
		return HttpResponse("{error}")

@csrf_exempt
def get_signs(request: HttpRequest):
	if request.method == 'GET':
		return HttpResponse(signs.get_signs())
	else:
		return HttpResponse("{error}")
