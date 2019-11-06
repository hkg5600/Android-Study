from django.db import models
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager
import datetime
import jsonfield

class UserManager(BaseUserManager):

    use_in_migrations = True

    def create_user(
        self, user_id, name, school_name, password,
        school_subject, school_grade, school_class ,school_number
    ):
        user = self.model(
            school_name=school_name,
            user_id=user_id,
            name=name,
            password=password,
            school_subject=school_subject,
            school_grade=school_grade,
            school_class=school_class,
            school_number=school_number
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_staffuser(self, user_id, name, school_name, password):
        user = self.create_user(
            school_name=school_name,
            user_id=user_id,
            name=name,
        )
        user.is_staff = True
        user.save(using=self._db)
        return user

    def create_superuser(self, user_id, name, school_name, password):
        user = self.create_user(
            school_name=school_name,
            user_id=user_id,
            name=name,
            password=password,
            school_subject=None,
            school_grade=None,
            school_class=None,
            school_number=None
        )
        user.is_staff = True
        user.is_admin = True
        user.is_superuser = True
        user.save(using=self._db)
        return user


class User(AbstractBaseUser):

    #공통필드
    username = None
    user_id = models.CharField(unique=True, max_length=10)
    name = models.CharField(max_length=100)
    school_name = models.CharField(max_length=100)
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    is_teacher = models.BooleanField(default=False)

    #선생님 필드
    #school_subject = jsonfield.JSONField(blank=True, null=True)
    school_subject = models.CharField(max_length=10, blank=True, null=True)
    #학생 필드
    school_grade = models.CharField(max_length=10, blank=True, null=True)
    school_class = models.CharField(max_length=10, blank=True, null=True)
    school_number = models.CharField(max_length=10, blank=True, null=True)

    USERNAME_FIELD = 'user_id'
    REQUIRED_FIELDS = ['name', 'school_name']

    def __str__(self):
        return self.user_id
    def has_perm(self, perm, obj=None):
        return True

    def has_module_perms(self, app_label):
        return True

    objects = UserManager()
