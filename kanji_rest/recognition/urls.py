from django.urls import path
from . import views

urlpatterns = [
	path('kanji', views.recognize, name='recognize')
]