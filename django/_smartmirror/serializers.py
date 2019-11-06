from .models import Data
from rest_framework import serializers

class DataSerializer(serializers.ModelSerializer):
    class Meta:
        model = Data
        fields = ('id', 'dust', 'weather', 'meal')
