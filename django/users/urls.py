from django.urls import path, include
from .views import (
    Registration,
    UserLogin,
    UserAPI,
    CheckUserID,
    AddFollower,
    UnFollow,
    UserProfile,
)
urlpatterns = [
    path("join/", Registration.as_view()),
    path("login/", UserLogin.as_view()),
    path("user/", UserAPI.as_view()),
    path("follow/", AddFollower.as_view()),
    path("unfollow/", UnFollow.as_view()),
    path("user_id/check/", CheckUserID.as_view()),
    path("profile/", UserProfile.as_view()),
]
