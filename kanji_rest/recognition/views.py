from django.http import HttpResponse, HttpRequest
from django.views.decorators.csrf import csrf_exempt
import json

from recognition.lib import example

@csrf_exempt
def recognize(request: HttpRequest):
	if request.method == 'POST':
		body = json.loads(request.body)
		return HttpResponse(example.check(body["name"], body["image"]))
	else:
		return HttpResponse("{error}")
