# Generated by Django 2.2.2 on 2019-11-06 08:12

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('post', '0005_post_profile_image'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='post',
            name='profile_image',
        ),
    ]
