from django.urls import path, include
from .views import TestList

urlpatterns = [
    path("api/", TestList.as_view()),
]
