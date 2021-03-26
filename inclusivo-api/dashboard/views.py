from django.shortcuts import render
from user.views import response_200, response_400, response_500, response_201, response_204
from user.models import Candidate, City
from company.models import SubscribedCompanies
from company.serializers import SubscribedCompaniesSerializer
from job.models import JobPost, LikedJobs, LikedScholarships
from job.serializers import JobSerializer, LikedJobsSerializer, LikedScholarshipsSerializer
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated 
from rest_framework.authentication import TokenAuthentication
#from django.db.models import Q


# Create your views here.
@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_subscribed_company_jobs(request):
    try:
        user = request.user
        if not Candidate.objects.filter(user=user).exists():
            return response_400("No candidate associated with this user","No candidate associated with this user", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        result = []
        if SubscribedCompanies.objects.filter(candidate=candidate).exists():
            sub_companies = SubscribedCompanies.objects.filter(candidate=candidate)
            for company in sub_companies:
                obj = {}
                obj['subscribed_company'] = SubscribedCompaniesSerializer(company, context={'request': request}).data
                jobs = JobPost.objects.filter(company = company.company, status__in = ['Expired','Published'])
                obj['jobs'] = JobSerializer(jobs, context={'request': request}, many=True).data
                result.append(obj)
        return response_200("Subscribed Companies and jobs fetched", result)
    
    except Exception as e:
        return response_500("internal error",e)   


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_liked_jobs(request):
    try:
        user = request.user
        if not Candidate.objects.filter(user=user).exists():
            return response_400("No candidate associated with this user","No candidate associated with this user", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        liked_jobs = LikedJobs.objects.filter(candidate=candidate, job_post__status__in = ['Expired','Published'])
        return response_200("Liked jobs fetched", LikedJobsSerializer(liked_jobs,context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)   


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_liked_scholarships(request):
    try:
        user = request.user
        if not Candidate.objects.filter(user=user).exists():
            return response_400("No candidate associated with this user","No candidate associated with this user", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        liked_scholarships = LikedScholarships.objects.filter(candidate=candidate)
        return response_200("Liked scholarships fetched", LikedScholarshipsSerializer(liked_scholarships,context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_jobs_in_preferred_cities(request):
    try:
        user = request.user
        if not Candidate.objects.filter(user=user).exists():
            return response_400("No candidate associated with this user","No candidate associated with this user", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        preferred_cities = candidate.preferred_city.all()
        preferred_cities_name = [p.name for p in preferred_cities]
        
        locations = request.query_params.getlist('location') if request.query_params.getlist('location') else preferred_cities_name 

        jobs = JobPost.objects.filter(status__in = ['Expired','Published'])

        if locations:
            locationObjects = City.objects.filter(name__in= locations )
            jobs = jobs.filter(removed=False, locations__in = locationObjects, status__in = ['Expired','Published'])

        return response_200("Jobs in preferred cities fetched", JobSerializer(jobs, context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)
