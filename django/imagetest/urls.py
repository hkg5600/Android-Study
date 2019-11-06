from django.urls import path, include
from .views import (
    ImageList,
    ImageDetail,
    TextList,
    TextDetail,
)

urlpatterns = [
    path("image_list/", ImageList.as_view()),
    path("image_list/<int:pk>/", ImageDetail.as_view()),
    path("text_list/", TextList.as_view()),
    path("text_list/<int:pk>/", TextDetail.as_view()),
]
