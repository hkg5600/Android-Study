from django.urls import path, include
from .views import(
    PostView,
    PostDetail,
    AddPost,
)

urlpatterns = [
    path("post/", PostView.as_view()),
    path("post/<int:pk>/", PostDetail.as_view()),
    path("add_post/", AddPost.as_view()),
]
