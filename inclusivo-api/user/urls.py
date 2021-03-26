from django.conf.urls import url, include
from .views import *

urlpatterns = [
    url(r'^update/$', update_profile),
    url(r'^check/user/$', check_if_user),
    url(r'^delete/$', delete_user),
    url(r'^complete_profile/$', complete_profile_details),
    url(r'^educational_detail/add' , educational_details),
    url(r'^educational_detail/delete/(?P<detail_id>\d+)/$' , delete_educational_details),
    url(r'^employment_history/add/$', employment_history_add),
    url(r'^employment_history/delete/(?P<employment_id>\d+)/$', delete_employment_history),
    url(r'^get/(?P<user_id>\d+)/$', get_user),
    url(r'^degree/dropdown/$', get_degree_dropdown),
    url(r'^location/dropdown/$', get_locations_dropdown),
    url(r'^location/dropdown/india$', get_locations_dropdown_india),
]
