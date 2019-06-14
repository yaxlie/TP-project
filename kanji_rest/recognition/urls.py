from django.urls import path
from . import views

urlpatterns = [
	path('', views.RecognitionView.as_view(), name='recognition'),
	#path('signs', views.get_signs, name='signs')
]