from rest_framework import serializers

from .models import  *
from company.serializers import CompanySerializer
from user.serializers import CitySerializer, DegreeSerializer, TagSerializer, CandidateSerializer

class JobSerializer(serializers.ModelSerializer):
    company = CompanySerializer(read_only = True)
    accepted_locations = CitySerializer(read_only = True, many=True, source='locations')
    degrees = DegreeSerializer(read_only= True, many=True, source='accepted_degrees')
    tags = TagSerializer(read_only=True,many=True)
    is_liked = serializers.SerializerMethodField()

    def get_is_liked(self, obj): 
        user = None
        request = self.context.get("request")
        if request and hasattr(request, "user"):
            user = request.user
        return LikedJobs.objects.filter(job_post__id=obj.id, candidate__user=user).exists()

    class Meta:
        model = JobPost
        exclude = ('locations', 'accepted_degrees')
        #fields = '__all__'

class JobApplicationSerializer(serializers.ModelSerializer):
    job = JobSerializer(read_only = True)
    candidate = CandidateSerializer(read_only = True)

    class Meta:
        model = JobApplication
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # We pass the "upper serializer" context to the "nested one"
        self.fields['job'].context.update(self.context)

class ScholarshipSerializer(serializers.ModelSerializer):
    company = CompanySerializer(read_only = True)
    accepted_degrees = DegreeSerializer(read_only= True, many=True)
    tags = TagSerializer(read_only=True,many=True)
    is_liked = serializers.SerializerMethodField()

    def get_is_liked(self, obj): 
        user = None
        request = self.context.get("request")
        if request and hasattr(request, "user"):
            user = request.user
        return LikedScholarships.objects.filter(scholarship_id=obj.id, candidate__user=user).exists()


    class Meta:
        model = ScholarshipPost
        fields = '__all__'

class LikedJobsSerializer(serializers.ModelSerializer):
    candidate = CandidateSerializer(read_only = True)
    job_post = JobSerializer(read_only = True)

    class Meta:
        model = LikedJobs
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # We pass the "upper serializer" context to the "nested one"
        self.fields['job_post'].context.update(self.context)

class LikedScholarshipsSerializer(serializers.ModelSerializer):
    candidate = CandidateSerializer(read_only = True)
    scholarship = ScholarshipSerializer(read_only = True)

    class Meta:
        model = LikedScholarships
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # We pass the "upper serializer" context to the "nested one"
        self.fields['scholarship'].context.update(self.context)