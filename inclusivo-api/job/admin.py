from django.contrib import admin
from .models import *

# Register your models here.
admin.site.register(Tag)
admin.site.register(JobPost)
admin.site.register(JobDTO)
admin.site.register(CountDTO)
admin.site.register(ScholarshipPost)
admin.site.register(JobApplication)
admin.site.register(ScholarshipApplication)
admin.site.register(LikedJobs)
admin.site.register(LikedScholarships)
