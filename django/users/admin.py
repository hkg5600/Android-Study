from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from django.contrib.auth.models import Group
from django.utils.translation import ugettext_lazy as _

from .models import User


class UserAdmin(BaseUserAdmin):
    # The forms to add and change user instances


    # The fields to be used in displaying the User model.
    # These override the definitions on the base UserAdmin
    # that reference specific fields on auth.User.
    list_display = ('user_id','name', 'created_at', 'is_staff', 'is_superuser')
    list_display_links = ('user_id',)
    list_filter = ('is_superuser', 'is_staff',)
    fieldsets = (
        (None, {'fields': ('user_id', 'password')}),
        (_('Personal info'), {'fields': ('name', )}),
        (_('Permissions'), {'fields': ('is_superuser',)}),
    )
    # add_fieldsets is not a standard ModelAdmin attribute. UserAdmin
    # overrides get_fieldsets to use this attribute when creating a user.
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('user_id', 'name', 'password1', 'password2')}
         ),
    )
    search_fields = ('user_id','name')
    ordering = ('-created_at',)
    filter_horizontal = ()


# Now register the new UserAdmin...
admin.site.register(User, UserAdmin)
