from django.urls import path, include
from .views import DataList

urlpatterns = [
    path("data_list/", DataList.as_view()),
]
