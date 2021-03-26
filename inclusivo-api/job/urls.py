from django.conf.urls import url, include
from .views import *

urlpatterns = [
    url(r'^add/$', add_job),
    url(r'^scholarship/add/$', add_scholarship),
    url(r'^get/$', list_jobs),
    url(r'^scholarship/get/$', list_scholarships),
    url(r'^get/(?P<job_id>\d+)/$', get_job),
    url(r'^like/(?P<job_id>\d+)/$', like_job),
    url(r'^unlike/(?P<job_id>\d+)/$', unlike_job),
    url(r'^delete/(?P<job_id>\d+)/$', delete_job),
    url(r'^status/update/(?P<job_id>\d+)/$', update_job_status),
    url(r'^scholarship/get/(?P<scholarship_id>\d+)/$', get_scholarship),
    url(r'^scholarship/like/(?P<scholarship_id>\d+)/$', like_scholarship),
    url(r'^scholarship/unlike/(?P<scholarship_id>\d+)/$', unlike_scholarship),
    url(r'^company/(?P<company_id>\d+)/$', get_company_jobs), 
    url(r'^scholarship/company/(?P<company_id>\d+)/$', get_company_scholarships), 
    url(r'^candidate/$', get_candidate_job_applications), 
    url(r'^applications/(?P<job_id>\d+)/list/$', list_job_applications),
    url(r'^application/create/(?P<job_id>\d+)/$', job_application_creation),
    url(r'^application/evaluate/(?P<job_id>\d+)/$', evaluate_profile),
    url(r'^application/get/(?P<job_application_id>\d+)/$', get_individual_job_application),
    url(r'^application/delete/(?P<job_application_id>\d+)/$', delete_job_application),
    url(r'^application/status/update/(?P<job_application_id>\d+)/$', update_job_application_status),
    url(r'^get/by_diversity/', get_job_by_diversity),
]