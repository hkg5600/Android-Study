# Generated by Django 2.2.3 on 2019-08-01 17:24

from django.db import migrations
import userlist.models


class Migration(migrations.Migration):

    dependencies = [
        ('userlist', '0002_auto_20190802_0210'),
    ]

    operations = [
        migrations.AlterModelManagers(
            name='user',
            managers=[
                ('objects', userlist.models.UserManager()),
            ],
        ),
    ]