from django.db import models
from django.conf import settings
from users.models import User


class Image(models.Model):
    image = models.ImageField(default='image/default_image.jpeg')
    owner = models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)

class Text(models.Model):
    title = models.CharField(max_length=100)
    content = models.CharField(max_length=100)
    owner = models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)
