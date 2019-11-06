from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from .serializers import TestSerializer
from .models import Test
from django.http import JsonResponse

class SongList(APIView):

    def get(self, request, format=None):
        queryset = Test.objects.all()
        serializer = TestSerializer(queryset, many=True)
        return JsonResponse({'status':status.HTTP_200_OK, 'data':serializer.data})

    def post(self, request, format=None):
        serializer = TestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        return JsonResponse({'status':status.HTTP_200_OK, 'data':serializer.data})

class SongDetail(APIView):
    def get_object(self, pk):
        try:
            return Test.objects.get(pk=pk)
        except Test.DoesNotExist:
            raise Http404

    def delete(self, request, pk, format=None):
        image = self.get_object(pk)
        image.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
