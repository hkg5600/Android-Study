from django.urls import path
from knox import views as knox_views
from .views import (RegistrationAPI, LoginAPI, UserAPI, UserListView)
urlpatterns = [
    path('register/', RegistrationAPI.as_view()),
    path('login/', LoginAPI.as_view()),
    path('user/', UserListView.as_view()),
]
