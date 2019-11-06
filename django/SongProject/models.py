from django.db import models

class Test(models.Model):
    title = models.CharField(max_length=100)
    code = models.CharField(max_length=100)
    url = models.CharField(max_length=400)
