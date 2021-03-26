from rest_framework import serializers
from job.models import JobPost
from .models import *


class CompanySerializer(serializers.ModelSerializer):
    is_following = serializers.SerializerMethodField()
    jobs_count = serializers.SerializerMethodField()

    def get_jobs_count(self, obj):
        return JobPost.objects.filter(company = obj, status__in = ['Expired', 'Published']).count()

    def get_is_following(self, obj): 
        user = None
        request = self.context.get("request")
        if request and hasattr(request, "user"):
            user = request.user
        return SubscribedCompanies.objects.filter(company=obj, candidate__user=user).exists()

    class Meta:
        model = Company
        fields = '__all__'

class SubscribedCompaniesSerializer(serializers.ModelSerializer):
    company = CompanySerializer(read_only = True)

    class Meta:
        model = SubscribedCompanies
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # We pass the "upper serializer" context to the "nested one"
        self.fields['company'].context.update(self.context)

class StoriesSerializer(serializers.ModelSerializer):
    company = CompanySerializer(read_only = True)

    class Meta:
        model = Stories
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # We pass the "upper serializer" context to the "nested one"
        self.fields['company'].context.update(self.context)

class InitiativeSerializer(serializers.ModelSerializer):

    class Meta:
        model = Initiative
        fields = '__all__'