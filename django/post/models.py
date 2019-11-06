from django.db import models
from django.conf import settings
User = settings.AUTH_USER_MODEL

class Post(models.Model):
    text = models.CharField(max_length=5000)
    owner = models.ForeignKey(User, on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now_add=True)

class Image(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE)
    image = models.FileField(blank=True)
