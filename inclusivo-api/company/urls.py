from django.conf.urls import url, include
from .views import *

urlpatterns = [
    url(r'^company_details/(?P<company_id>\d+)/$' , get_company_details),
    url(r'^add/$', add_company),
    url(r'^get/$', list_companies),
    url(r'^dropdown/$', get_company_dropdown),
    url(r'^follow/(?P<company_id>\d+)/$', subscribe_company),
    url(r'^unfollow/(?P<company_id>\d+)/$', unsubscribe_company),
    url(r'^delete/(?P<company_id>\d+)/$', delete_company),
    url(r'^story/add/$', add_story),
    url(r'^story/get/$', get_story),
    url(r'^story/get/(?P<story_id>\d+)/individual/$', get_story_individual),
    url(r'^story/delete/(?P<story_id>\d+)/$', delete_story),
    url(r'^initiatives/add/$', add_initiatives),
    url(r'^initiatives/delete/(?P<initiative_id>\d+)/$', delete_initiative),
    url(r'^initiatives/get/(?P<initiative_id>\d+)/$' , get_initiative_individual),
    url(r'^initiatives/get_by_company/(?P<company_id>\d+)/$' , get_initiative_by_company),
]