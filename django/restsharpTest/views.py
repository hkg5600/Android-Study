from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from .serializers import TestSerializer
from .models import Test

class TestList(APIView):

    def get(self, request, format=None):
        queryset = Test.objects.all()
        serializer = TestSerializer(queryset, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def post(self, request, format=None):
        serializer = TestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return Response(serializer.data, status=status.HTTP_200_OK)
