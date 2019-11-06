from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework import authentication, permissions, status, generics
from users.backends import JWTUserAuthentication
from .serializers import (
    PostSerializer, FileSerializer, CommentSerializer,
)

from django.http import JsonResponse
from .models import  Post, Image, Comment
from django.forms.models import model_to_dict
import json
from django.conf import settings
from django.contrib.auth import get_user_model
User = get_user_model()

class LikeToPost(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def post(self, *args, **kwargs):
        post = Post.objects.get(id=self.request.data.get('post'))
        user = User.objects.get(user_id=self.request.data.get('user_id'))
        post.like.add(user)
        post.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요"})

class UnlikeToPost(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def post(self, *args, **kwargs):
        post = Post.objects.get(id=self.request.data.get('post'))
        user = User.objects.get(user_id=self.request.data.get('user_id'))
        post.like.remove(user)
        post.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요 취소"})

class PostView(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def post(self, request, format=None):
        user_list = self.request.data.get('user_id')
        user_list.append(self.get_object())
        print(user_list)
        users = User.objects.filter(user_id__in=user_list).values_list('user_id').distinct()
        queryset = Post.objects.filter(owner__in=users).order_by('-created_at')
        serializer = PostSerializer(queryset, many=True)
        return JsonResponse({'status':status.HTTP_200_OK, "message":"게시물 불러오기 성공",'data':{'post':serializer.data}})

class PostDetail(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def get_object(self, pk):
        try:
            return Post.objects.get(pk=pk)
        except Post.DoesNotExist:
            raise Http404

    def delete(self, request, pk, format=None):
        post = self.get_object(pk)
        post.delete()
        return JsonResponse({'status':status.HTTP_200_OK, "message":"게시물 삭제 성공",'data':""})

class AddPost(APIView):
    serializer_class = PostSerializer
    permission_classes = [IsAuthenticated, ]
    def post(self, request, format=None):
        print(request.data)
        serializer = PostSerializer(data=request.data, context={'request':request})

        serializer.is_valid(raise_exception=True)
        serializer.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'message':"게시물 저장 성공", 'data':""})

class CommentView(APIView):
    permission_classes = [IsAuthenticated, ]
    def post(self, request, format=None):
        print(request.data)
        serializer = CommentSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'message':"댓글 작성 성공", 'data':""})

    def get(self, request, format=None):
        comments = Comment.objects.filter(post=self.request.data.get('post'))
        print(comments)
        serializer = CommentSerializer(comments, many=True)
        return JsonResponse({'status':status.HTTP_200_OK, "message":"댓글 불러오기 성공",'data':{'comment':serializer.data}})

class CommentDetail(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def get_object(self, pk):
        try:
            return Comment.objects.get(pk=pk)
        except Comment.DoesNotExist:
            raise Http404

    def delete(self, request, pk, format=None):
        comment = self.get_object(pk)
        comment.delete()
        return JsonResponse({'status':status.HTTP_200_OK, "message":"댓글 삭제 성공",'data':""})
