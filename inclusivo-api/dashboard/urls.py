from django.conf.urls import url, include
from .views import *

urlpatterns = [
    url(r'^get/subscribed_companies/$', get_subscribed_company_jobs),
    url(r'^get/liked_jobs/$', get_liked_jobs),
    url(r'^get/liked_scholarships/$', get_liked_scholarships),
    url(r'^get/jobs_location/$', get_jobs_in_preferred_cities),
]