from django.urls import path, include
from .views import(
    PostView,
    PostDetail,
    AddPost,
    AddComment,
    CommentDetail,
    LikeToPost,
    UnlikeToPost,
)

urlpatterns = [
    path("post/", PostView.as_view()),
    path("post/<int:pk>/", PostDetail.as_view()),
    path("add_post/", AddPost.as_view()),
    path("add_comment/", AddComment.as_view()),
    path("comment/<int:pk>/", CommentDetail.as_view()),
    path("like/", LikeToPost.as_view()),
    path("unlike/", UnlikeToPost.as_view()),
]
