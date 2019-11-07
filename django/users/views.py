from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework import authentication, permissions, status, generics
#from .backends import JWTUserAuthentication
from .serializers import (
    RegistrationSerializer,
    UserLoginSerializer,
    UserSerializer,
    CheckUserIDSerializer,
)
from django.contrib.auth import login, logout, authenticate
from django.http import JsonResponse
from post.models import Post, Image
from .models import User
from django.forms.models import model_to_dict
import json
import time
import jwt
from django.conf import settings

class UserProfile(APIView):
    #authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def post(self, request, format=None):
        user = User.objects.get(user_id=self.get_object())
        if(self.request.data.get('image') is not None):
            user.profile_image = self.request.data.get('image')
            user.save()
            return JsonResponse({'status':status.HTTP_200_OK, 'data':"", "message":"프로필 저장 성공"})
        else:
            return JsonResponse({'status':status.HTTP_204_NO_CONTENT, 'data':"", "message":"error"})

class UserAPI(generics.RetrieveAPIView):
    #authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    serializer_class = UserSerializer

    def get_object(self):
        return self.request.user

    def get(self, request, format=None):
        item = model_to_dict(self.get_object())
        qs = User.objects.get(user_id=list(item.values())[3])
        serializer = UserSerializer(qs, many=False)

        return JsonResponse({'status':status.HTTP_200_OK, 'data':serializer.data, "message":"조회 성공"})


class Registration(APIView):
    permission_classes = (AllowAny,)
    serializer_class = RegistrationSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)

class UserLogin(APIView):
    authentication_classes = ()
    permission_classes = (AllowAny, )

    def post(self, request):
        username = request.data.get('user_id')
        password = request.data.get('password')
        user = authenticate(username=username, password=password)
        if user is None:
            return Response(serializer.data, status=status.HTTP_400_BAD_REQUEST)
        expire_ts = int(time.time()) + 3600
        payload = {'username': username, 'expire':expire_ts}
        token = jwt.encode(payload, settings.SECRET_KEY, algorithm='HS256')
        if getattr(settings, 'REST_SESSION_LOGIN', True):
            login(self.request, user)
        return Response(token)
        # serializer = UserLoginSerializer(data=request.data)
        # serializer.is_valid(raise_exception=True)
        # user = serializer.validated_data['user']
        # login(request, user)
        # return JsonResponse({'status':status.HTTP_200_OK, 'data':UserLoginSerializer(user).data, 'message':"로그인 성공"})

class Logout(APIView):
    def get(self, request):
        #user = User.objects.get(user_id=self.request.data.get('user_id'))
        logout(request)
        return JsonResponse({'status':status.HTTP_200_OK, 'data':'', 'message':"로그아웃 성공"})

class CheckUserID(APIView):
    permission_classes = (AllowAny,)
    serializer_class = CheckUserIDSerializer

    def get(self, request, format=None):
        user_id = request.data.get('user_id')

        if User.objects.filter(user_id=user_id).exists():
            return Response(data={"data":"true"})
        else:
            return Response(data={"data":"false "+str(user_id)})

class AddFollower(APIView):
    #permission_classes = [IsAuthenticated, ]
    def post(self, requset, format=None):
        user = User.objects.get(user_id=self.request.data.get('user_id'))
        user_to_follow = User.objects.get(user_id=self.request.data.get('user_to_follow'))
        user.followers.add(user_to_follow)
        user.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"팔로우 "+str(user_to_follow.user_id)})

class UnFollow(APIView):
    #permission_classes = [IsAuthenticated, ]
    def post(self, requset, format=None):
        user = User.objects.get(user_id=self.request.data.get('user_id'))
        unfollow = User.objects.get(user_id=self.request.data.get('unfollow'))
        user.followers.remove(unfollow)
        user.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"언팔로우 "+str(unfollow.user_id)})
