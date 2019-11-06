from rest_framework import serializers
from django.conf import settings
from django.contrib.auth import authenticate

class CreateUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = settings.AUTH_USER_MODEL
        fields = ('id', 'user_id', 'name', 'school_name', 'password',
        'school_subject', 'school_grade','school_class', 'school_number')
        extra_kwargs = {"password":{"write_only":True}}

    def create(self, validated_data):
        user = settings.AUTH_USER_MODEL.objects.create_user(
            validated_data['user_id'], validated_data['name'],
            validated_data['school_name'], validated_data['password'],
            validated_data['school_subject'], validated_data['school_grade'],
            validated_data['school_class'], validated_data['school_number']
        )
        return user

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = settings.AUTH_USER_MODEL
        fields = ("id", "user_id")

class LoginUserSerializer(serializers.Serializer):
    user_id = serializers.CharField()
    password = serializers.CharField()

    def validate(self, data):
        user = authenticate(**data)
        if user and user.is_active:
            return user
        raise serializers.ValidationError("Unable to log in with provided credentials.")
