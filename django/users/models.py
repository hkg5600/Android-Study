from django.db import models
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin, BaseUserManager
from django.conf import settings
from datetime import datetime
from datetime import timedelta
import time
import jwt
from post.models import Post

class UserManager(BaseUserManager):

    def get_by_natural_key(self, user_id):
        return self.get(user_id=user_id)

    def create_user(self, user_id, name, password):

        if user_id is None:
            raise TypeError('Users must have an user_id.')

        user = User(
            name=name, user_id=user_id, password=password,
        )
        user.set_password(password)
        user.save()
        return user

    def create_superuser(self, user_id, password, name, school_name):

        superuser = self.create_user(
            name=name,user_id=user_id, password=password
        )

        superuser.is_superuser = True
        superuser.is_admin = True
        superuser.is_active = True
        superuser.is_staff = True
        superuser.set_password(password)
        superuser.save()
        return superuser

class User(AbstractBaseUser, PermissionsMixin):
    user_id = models.CharField(max_length=100, unique=True, primary_key=True)
    name = models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)
    is_staff = models.BooleanField(default=False)
    followers = models.ManyToManyField('self', blank=True)
    following = models.ManyToManyField('self', blank=True)

    USERNAME_FIELD = 'user_id'
    REQUIRED_FIELDS = ['name']

    objects = UserManager()

    @property
    def token(self):
        dt = datetime.now() + timedelta(days=20)
        token = jwt.encode({
            'user_id' : self.user_id,
            'expire' : int(time.mktime(dt.timetuple()))
        }, settings.SECRET_KEY, algorithm='HS256')
        return token.decode('utf-8')

    def __str__(self):
        return self.user_id
