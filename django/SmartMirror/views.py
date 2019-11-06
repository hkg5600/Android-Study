from rest_framework.views import APIView
from rest_framework import status
from rest_framework.response import Response
from .serializers import DataSerializer
from .models import Data

class DataList(APIView):
    def get(self, request, format=None):
        data = Data.objects.last()
        serializer = DataSerializer(data, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def post(self, request, format=None):
        serializer = DataSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(data=serializer.data, status=status.HTTP_200_OK)
