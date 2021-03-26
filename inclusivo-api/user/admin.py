from django.contrib import admin
from .models import *
# Register your models here.

admin.site.register(CustomUser)
admin.site.register(Country)
admin.site.register(State)
admin.site.register(City)

@admin.register(Candidate)
class CandidateAdmin(admin.ModelAdmin):
    list_display = ('user', 'is_active')

admin.site.register(Employer)
admin.site.register(EducationalInstitute)
admin.site.register(EmploymentHistory)
admin.site.register(Degree)
admin.site.register(EducationalDetail)