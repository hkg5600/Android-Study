from rest_framework import viewsets, permissions, generics
from rest_framework.response import Response
from .serializers import (
    CreateUserSerializer,
    UserSerializer,
    LoginUserSerializer
)
from knox.models import AuthToken
from . import models

class RegistrationAPI(generics.GenericAPIView):
    serializer_class = CreateUserSerializer

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.save()
        return Response(
            {
                "user":UserSerializer(
                    user, context=self.get_serializer_context()
                ).data,
                "token":AuthToken.objects.create(user)[1],
            }
        )

class LoginAPI(generics.GenericAPIView):
    serializer_class = LoginUserSerializer

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data
        return Response(
            {
                "user":UserSerializer(
                    user, context=self.get_serializer_context()
                    ).data,
                    "token":AuthToken.objects.create(user)[1],
            }
        )

class UserAPI(generics.RetrieveAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = UserSerializer

    def get_object(self):
        return self.request.user

class UserListView(generics.ListCreateAPIView):
    queryset = models.User.objects.all()
    serializer_class = UserSerializer