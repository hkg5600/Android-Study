# -*- coding: utf-8 -*-
from rest_framework import exceptions
from django.contrib.auth import get_user_model
import jwt
import time
User = get_user_model()
JWT_SECRET = 'mysecretkey'

def get_authorization_header(request):
    auth = request.META.get('HTTP_AUTHORIZATION', b'')
    if isinstance(auth, type('')):
        auth = auth.encode(HTTP_HEADER_ENCODING)
    return auth

class MyTokenAuthentication():
    def authenticate(self, request):
        token = get_authorization_header(request)
        if not token:
            return None
        # token 디코딩

        payload = jwt.decode(token, JWT_SECRET, algorithms=['HS256'])
        # token 만료 확인
        expire = payload.get('expire')
        if int(time.time()) > expire:
            return None
        # user 객체
        username = payload.get('username')
        if not username:
            return None
        try:
            user = User.objects.get(username=username)
        except User.DoesNotExist:
            raise exceptions.AuthenticationFailed('No such user')
        return (user, token)
