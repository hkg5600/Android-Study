from django.db import models

class Data(models.Model):
    dust = models.CharField(max_length=100)
    weather = models.CharField(max_length=200)
    meal = models.CharField(max_length=200)
