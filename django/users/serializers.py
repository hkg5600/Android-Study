from rest_framework import serializers
from .models import (
    User, Post,
)
from django.contrib.auth import authenticate

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('user_id', 'name', 'followers', 'following', 'profile_image')

class RegistrationSerializer(serializers.ModelSerializer):
    password = serializers.CharField(max_length=128, write_only=True)
    token = serializers.CharField(max_length=255, read_only=True)

    class Meta:
        model = User
        fields = '__all__'

    def create(self, validated_data):
        return User.objects.create_user(**validated_data)

class UserLoginSerializer(serializers.Serializer):
    user_id = serializers.CharField(max_length=100)
    name =serializers.CharField(max_length=100, read_only=True)
    password = serializers.CharField(max_length=255, write_only=True)
    token = serializers.CharField(max_length=255, read_only=True)


    def validate(self, data):
        user_id = data.get('user_id', None)
        password = data.get('password', None)

        user = authenticate(username=user_id, password=password)

        if user is None:
            raise serializers.ValidationError(
                'user with this user_id and password is not found.'
            )

        try:
            userObj = User.objects.get(user_id=user.user_id)
        except User.DoesNotExist:
            raise serializers.ValidationError(
                'user with this user_id and password does not exist.'
            )

        if not user.is_active:
            raise serializers.ValidationError(
                'This user has been deactivated.'
            )

        return {
            'user_id' : user.user_id,
            'name' : user.name,
            'token' : user.token
        }

class CheckUserIDSerializer(serializers.Serializer):
    class Meta:
        model = User
        fields = ('user_id',)
