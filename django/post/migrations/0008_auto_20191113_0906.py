# Generated by Django 2.2.2 on 2019-11-13 00:06

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('post', '0007_reply'),
    ]

    operations = [
        migrations.RenameField(
            model_name='reply',
            old_name='post',
            new_name='comment',
        ),
    ]
