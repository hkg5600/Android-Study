from .models import Image, Text
from rest_framework import serializers
from django.conf import settings
from users.models import User


class ImageSerializer(serializers.ModelSerializer):
    image = serializers.ImageField(use_url=True)

    class Meta:
        model = Image
        fields = ('id','owner', 'image')

class TextSerializer(serializers.ModelSerializer):

    class Meta:
        model = Text
        fields = ('id', 'owner', 'title' ,'text')
