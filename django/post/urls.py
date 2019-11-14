from django.urls import path, include
from .views import(
    PostView,
    PostDetail,
    AddPost,
    CommentView,
    CommentDetail,
    LikeToPost,
    UnlikeToPost,
    CommentList,
    UserProfileList,
    ReplyList,
    ReplyView,
    LikeToComment,
    UnlikeToComment,
)

urlpatterns = [
    path("post/", PostView.as_view()),
    path("post/<int:pk>/<int:page>/", PostDetail.as_view()),
    path("add_post/", AddPost.as_view()),
    path("comment/", CommentView.as_view()),
    path("comment_list/", CommentList.as_view()),
    path("comment/<int:pk>/", CommentDetail.as_view()),
    path("like_post/", LikeToPost.as_view()),
    path("unlike_post/", UnlikeToPost.as_view()),
    path("user_profile_data/", UserProfileList.as_view()),
    path("reply/", ReplyView.as_view()),
    path("reply_list/", ReplyList.as_view()),
    path("like_comment/", LikeToComment.as_view()),
    path("unlike_comment/", UnlikeToComment.as_view()),
]
