from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework import authentication, permissions, status, generics
from users.backends import JWTUserAuthentication
from .serializers import (
    PostSerializer, FileSerializer, CommentSerializer,PostDetailSerializer, UserProfile, ReplySerializer
)
from users.serializers import UserFollowingSerializer
from django.http import JsonResponse
from .models import  Post, Image, Comment, Reply
from django.forms.models import model_to_dict
import json
from django.conf import settings
from django.contrib.auth import get_user_model
User = get_user_model()

class UserProfileList(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def post(self, *args, **kwargs):
        if (self.request.data.get('post') is not None):
            try:
                post = Post.objects.filter(id=self.request.data.get('post')).values_list('like').distinct()
            except:
                return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 게시물이 존재하지 않습니다"})
            user = User.objects.filter(user_id__in=post)
            serializer = UserProfile(user, many=True)
        elif (self.request.data.get('user_id')):
            try:
                user = User.objects.get(user_id=self.request.data.get('user_id'))
            except:
                return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"존재하지 않는 사용자입니다"})
            serializer = UserFollowingSerializer(user.following.all(), many=True)

        return JsonResponse({'status':status.HTTP_200_OK, 'data':{'user_list':serializer.data}, 'message':"조회 성공"})

class LikeToReply(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def get(self, request, pk,format=None):
        try:
            reply = Reply.objects.get(id=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 댓글이 존재하지 않습니다"})
        user = self.get_object()
        reply.like.add(user)
        reply.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요"})

class UnlikeToReply(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def get(self, request, pk,format=None):
        try:
            reply = Reply.objects.get(id=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 댓글이 존재하지 않습니다"})
        user = self.get_object()
        reply.like.remove(user)
        reply.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요 취소"})

class LikeToComment(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def get(self, request, pk,format=None):
        try:
            comment = Comment.objects.get(id=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 댓글이 존재하지 않습니다"})
        user = self.get_object()
        comment.like.add(user)
        comment.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요"})

class UnlikeToComment(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def get(self, request, pk,format=None):
        try:
            comment = Comment.objects.get(id=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 댓글이 존재하지 않습니다"})
        user = self.get_object()
        comment.like.remove(user)
        comment.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요 취소"})

class LikeToPost(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def get(self, request, pk, page,format=None):
        try:
            post = Post.objects.get(id=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 게시물이 존재하지 않습니다"})
        user = self.get_object()
        post.like.add(user)
        post.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요"})

class UnlikeToPost(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def get(self, request, pk, page,format=None):
        try:
            post = Post.objects.get(id=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 게시물이 존재하지 않습니다"})
        user = self.get_object()
        post.like.remove(user)
        post.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':"", 'message':"좋아요 취소"})

class PostView(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get_object(self):
        return self.request.user

    def post(self, request, format=None):
        last_page = False
        page = self.request.data.get('page')
        print(page)
        user_list = self.request.data.get('user_id')
        user_list.append(self.get_object())
        users = User.objects.filter(user_id__in=user_list).values_list('user_id').distinct()
        queryset = Post.objects.filter(owner__in=users).order_by('-created_at')[7*page:(page+1)*7] #35
        serializer = PostSerializer(queryset, many=True)
        if (Post.objects.filter(owner__in=users).order_by('-created_at').count() <= (page+1)*7):
            last_page = True

        page += 1
        return JsonResponse({'status':status.HTTP_200_OK, "message":"게시물 불러오기 성공",'data':{'last_page':last_page,'nextPage':page,'post':serializer.data}})

class PostDetail(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def get_object(self, pk):
        try:
            return Post.objects.get(pk=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"해당 게시물이 존재하지 않습니다"})

    def get(self, request, pk, page,format=None):
        post = self.get_object(pk)
        last_page = False
        cur_page = page
        serializer = PostSerializer(post, many=False)
        if (type(post) is JsonResponse):
            return post
        comment = Comment.objects.filter(post=post).order_by('created_at')[15*page:(page+1)*15]
        comment_serializer = CommentSerializer(comment, many=True)
        if (Comment.objects.filter(post=post).order_by('created_at').count() <= (page+1)*15):
            last_page = True
        cur_page += 1
        return JsonResponse({'status':status.HTTP_200_OK, "message":"게시물 조회 성공",'data':{'last_page':last_page,'nextPage':cur_page,'post':serializer.data,'comment':comment_serializer.data}})

    def delete(self, request, pk, page,format=None):
        post = self.get_object(pk)
        if (type(post) is JsonResponse):
            return post
        post.delete()
        return JsonResponse({'status':status.HTTP_200_OK, "message":"게시물 삭제 성공",'data':""})

class AddPost(APIView):
    serializer_class = PostSerializer
    permission_classes = [IsAuthenticated, ]
    def post(self, request, format=None):
        serializer = PostSerializer(data=request.data, context={'request':request})

        serializer.is_valid(raise_exception=True)
        serializer.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'message':"게시물 저장 성공", 'data':""})

class CommentView(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def post(self, request, format=None):
        serializer = CommentSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'message':"댓글 작성 성공", 'data':""})

class CommentList(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def post(self, request, format=None):
        comments = Comment.objects.filter(post=self.request.data.get('post'))
        serializer = CommentSerializer(comments, many=True)
        return JsonResponse({'status':status.HTTP_200_OK, "message":"댓글 불러오기 성공",'data':{'comment':serializer.data}})

class CommentDetail(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def get_object(self, pk):
        try:
            return Comment.objects.get(pk=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"댓글이 없습니다"})

    def delete(self, request, pk, format=None):
        comment = self.get_object(pk)
        if (type(comment) is JsonResponse):
            return comment
        comment.delete()
        return JsonResponse({'status':status.HTTP_200_OK, "message":"댓글 삭제 성공",'data':""})

class ReplyView(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def post(self, request, format=None):
        serializer = ReplySerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return JsonResponse({'status':status.HTTP_200_OK, 'message':"답글 작성 성공", 'data':""})

class ReplyDetail(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]
    def get_object(self, pk):
        try:
            return Reply.objects.get(pk=pk)
        except:
            return JsonResponse({'status':status.HTTP_400_BAD_REQUEST, 'data':"", 'message':"답글이 없습니다"})

    def delete(self, request, pk, format=None):
        reply = self.get_object(pk)
        if (type(reply) is JsonResponse):
            return reply
        reply.delete()
        return JsonResponse({'status':status.HTTP_200_OK, "message":"답글 삭제 성공",'data':""})

class ReplyList(APIView):
    authentication_classes = [JWTUserAuthentication, ]
    permission_classes = [IsAuthenticated, ]

    def get(self, request, pk, page,format=None):
        next_count = False
        cur_page = page
        reply = Reply.objects.filter(comment=pk).order_by('-created_at')[2*page:(page+1)*2]
        serializer = ReplySerializer(reply, many=True)
        next_count = Reply.objects.filter(comment=pk).order_by('-created_at').count() - 2 * (cur_page + 1)
        if (next_count < 0):
            next_count = 0
        cur_page += 1
        return JsonResponse({'status':status.HTTP_200_OK, "message":"게시물 조회 성공",'data':{'next_count':next_count,'nextPage':cur_page,'reply':serializer.data}})
