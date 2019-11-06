from .models import Post, Image
from rest_framework import serializers

class FileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Image
        fields = '__all__'

class PostSerializer(serializers.ModelSerializer):
    images = FileSerializer(source='image_set', many=True, read_only=True)

    class Meta:
        model = Post
        fields = ('id', 'text', 'owner', 'created_at', 'images')

    def create(self, validated_data):
        print(self)
        images_data = self.context.get('request').FILES
        post = Post.objects.create(text=validated_data.get('text'),owner=validated_data.get('owner'))
        for image_data in images_data.values():
            Image.objects.create(post=post, image=image_data)
        return post
