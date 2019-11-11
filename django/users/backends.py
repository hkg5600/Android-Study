import jwt
import time
from django.conf import settings
from rest_framework import authentication, exceptions
from .models import User

JWT_SECRET = settings.SECRET_KEY

class JWTUserAuthentication(authentication.BaseAuthentication):

    authentication_header_prefix ='Token'

    def authenticate(self, request):
        request.user = None

        auth_header = authentication.get_authorization_header(request).split()
        auth_header_prefix = self.authentication_header_prefix.lower()

        if not auth_header:
            return None

        if len(auth_header) == 1:
            return None

        elif len(auth_header) > 2:
            return None

        prefix = auth_header[0].decode('utf-8')
        token = auth_header[1].decode('utf-8')

        if prefix.lower() != auth_header_prefix:
            return None
        return self._authenticate_credentials(request, token)

    def _authenticate_credentials(self, request, token):
        payload = jwt.decode(token, JWT_SECRET, algorithms=['HS256'])
        expire = payload.get('expire')

        if int(time.time()) > expire:
            return None

        user_id = payload.get('user_id')
        try:
            user = User.objects.get(user_id=user_id)
        except User.DoesNotExist:
            raise exceptions.AuthenticationFailed('No such user1'+str(payload)+str(user_id))

        return (user, token)
