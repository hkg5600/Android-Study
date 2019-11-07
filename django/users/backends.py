import jwt
import time
from django.conf import settings
from rest_framework import authentication, exceptions
from django.contrib.auth import get_user_model

JWT_SECRET = settings.SECRET_KEY

class MyBackend(object):

    def authenticate(self, username=None, password=None, **kwargs):
        if not username or not password:
            return None
        r = requests.get('https://auth.server.com/', auth=(username, password))
        if r.status_code != 200:
            return None

        try:
            user = User.objects.get(username=username)
        except User.DoesNotExist:
            return None
        return user

    def get_user(self, user_id):
        UserModel = get_user_model()
        try:
            return UserModel._default_manager.get(pk=user_id)
        except UserModel.DoesNotExist:
            return None
