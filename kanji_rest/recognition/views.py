from django.http import HttpResponse, HttpRequest
from django.views.decorators.csrf import csrf_exempt
import json
import time

@csrf_exempt
def recognize(request: HttpRequest):
	if request.method == 'POST':
		body = json.loads(request.body)
		time.sleep(5)
		return HttpResponse("Test: {}\n".format(body["image"]))
	else:
		return HttpResponse("{error}")
