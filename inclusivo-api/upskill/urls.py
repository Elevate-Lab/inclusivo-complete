from django.conf.urls import url, include
from .views import *    

urlpatterns = [    
    url(r'^blog/add/$', add_blog),
    url(r'^blog/get/$', get_blog),
    url(r'^blog/get/(?P<blog_id>\d+)/individual/$', get_blog_individual),
    url(r'^blog/delete/(?P<blog_id>\d+)/$', delete_blog),
    url(r'^video/add/$', add_video),
    url(r'^video/get/$', get_video),
    url(r'^video/get/(?P<video_id>\d+)/individual/$', get_video_individual),
    url(r'^video/delete/(?P<video_id>\d+)/$', delete_video),
]