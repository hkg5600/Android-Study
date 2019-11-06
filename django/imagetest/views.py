from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework import authentication, permissions, status, generics, exceptions
from .serializers import ImageSerializer, TextSerializer
from .models import Image, Text
from users.models import User
from multiprocessing.pool import ThreadPool
import threading

class ImageList(APIView):

    def get(self, request, format=None):
        image = Image.objects.all()
        serializer = ImageSerializer(image, many=True)
        #a = self.get_user_info(request.data.get('owner'))
        #a = self.get_user_info(request)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def post(self, request, format=None):
        serializer = ImageSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    def get_user_info(self, request):
        user = ''
        try:
            user = User.objects.get(user_id=request.data.get('owner'))
        except User.DoesNotExist:
            return  exceptions.AuthenticationFailed('No such user'+str(user))
        return user.subjects

class ImageDetail(APIView):

    def delete(self, request, pk, format=None):
        image = self.get_object(pk)
        image.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class TextList(APIView):

    def get(self, request, format=None):
        text = Text.objects.get.all()
        serializer = TextSerializer(text, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def post(self, request, format=None):
        if request.data.get("is_started") == "true" :
            pool = ThreadPool(processes=1)
            async_result = pool.apply_async(G.get_voice)
            return_val = async_result.get()


class TextDetail(APIView):

    def delete(self, request, pk, format=None):
        text = self.get_object(pk)
        image.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
