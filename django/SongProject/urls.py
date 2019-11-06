from django.urls import path, include
from .views import SongList, SongDetail

urlpatterns = [
    path("api/", SongList.as_view()),
    path("api/<int:pk>/", SongDetail.as_view()),
]
