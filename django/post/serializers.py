from .models import Post, Image, Comment, Reply
from rest_framework import serializers
from django.contrib.auth import get_user_model
User = get_user_model()

class UserProfile(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('user_id','name','profile_image',)

class UserInfo(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('profile_image',)

class FileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Image
        fields = '__all__'

class ReplySerializer(serializers.ModelSerializer):
    profile_image = UserInfo(source='owner', many=False, read_only=True)

    class Meta:
        model = Reply
        fields = '__all__'

class CommentSerializer(serializers.ModelSerializer):
    profile_image = UserInfo(source='owner', many=False, read_only=True)
    reply_count = serializers.IntegerField(source='reply_set.count', read_only=True)

    class Meta:
        model = Comment
        fields = '__all__'


class PostSerializer(serializers.ModelSerializer):
    images = FileSerializer(source='image_set', many=True, read_only=True)
    profile_image = UserInfo(source='owner', many=False, read_only=True)

    class Meta:
        model = Post
        fields = ('id', 'text', 'like', 'owner','created_at', 'images', 'profile_image')

    def create(self, validated_data):
        images_data = self.context.get('request').FILES
        post = Post.objects.create(text=validated_data.get('text'),owner=validated_data.get('owner'))
        for image_data in images_data.values():
            Image.objects.create(post=post, image=image_data)
        return post

class PostDetailSerializer(serializers.ModelSerializer):
    profile_image = UserInfo(source='owner', many=False, read_only=True)
    comments = CommentSerializer(source='comment_set' ,many=True, read_only=True)

    class Meta:
        model = Post
        fields = ('id', 'text', 'like', 'owner','created_at','profile_image', 'comments')
