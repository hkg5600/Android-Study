from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework import authentication, permissions, status, generics
from .backends import JWTUserAuthentication
from .serializers import (
    RegistrationSerializer,
    UserLoginSerializer,
    UserSerializer,
    CheckUserIDSerializer,
    UserFollowingSerializer,
    UserFollowerSerializer,
)
#from .models import User
from django.http import JsonResponse
from post.models import Post, Image
from .models import User, UserFollowing
from django.forms.models import model_to_dict
import json
import requests
class UserProfile(APIView):
    authentication_classes = [JWTUserAuthentication, ]
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
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    serializer_class = UserSerializer

    def get_object(self):
        return self.request.user

    def get(self, request, format=None):
        item = model_to_dict(self.get_object())
        qs = User.objects.get(user_id=list(item.values())[3])
        serializer = UserSerializer(qs, many=False)
        following_data = UserFollowingSerializer(qs.following.all(), many=True)
        followers_data = UserFollowerSerializer(qs.followers.all(), many=True)
        return JsonResponse({'status':status.HTTP_200_OK, 'data':{'user':serializer.data, 'following':following_data.data, 'followers':followers_data.data}, "message":"조회 성공"})


class Registration(APIView):
    permission_classes = (AllowAny,)
    serializer_class = RegistrationSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)

class UserLogin(APIView):
    permission_classes = (AllowAny,)
    serializer_class = UserLoginSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        return JsonResponse({'status':status.HTTP_200_OK, 'data':serializer.data, 'message':"로그인 성공"})

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
    permission_classes = [IsAuthenticated, ]
    def post(self, requset, format=None):
        user = User.objects.get(user_id=self.request.data.get('user_id'))
        follow = User.objects.get(user_id=self.request.data.get('follow'))
        print('user : ' + str(user) + ' follow : ' + str(follow))
        UserFollowing.objects.create(user_id=user, following_user_id=follow)
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"팔로우 "+str(follow.user_id)})

class UnFollow(APIView):
    permission_classes = [IsAuthenticated, ]
    def post(self, requset, format=None):
        user = User.objects.get(user_id=self.request.data.get('user_id'))
        unfollow = User.objects.get(user_id=self.request.data.get('unfollow'))
        test = UserFollowing.objects.filter(user_id=user, following_user_id=unfollow)
        test.delete()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"언팔로우 "+str(unfollow.user_id)})
