from django.shortcuts import render
from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.authentication import TokenAuthentication
from rest_framework.status import (
    HTTP_400_BAD_REQUEST,
    HTTP_404_NOT_FOUND,
    HTTP_500_INTERNAL_SERVER_ERROR,
    HTTP_200_OK,
    HTTP_204_NO_CONTENT,
    HTTP_201_CREATED
)
from rest_framework.response import Response
import logging
logger = logging.getLogger(__name__)
from rest_framework import status
from .serializers import *
from .models import *
from job.models import Tag, JobDTO
from datetime import datetime

# Create your views here.

def response_400(message, log_msg, e):
    if e is not None:
        logger.debug(log_msg + str(e))
    return Response({'status': 'error','message': message}, status= status.HTTP_400_BAD_REQUEST)

def response_500(log_msg, e):
    logger.debug(log_msg + str(e))
    return Response({'status': 'error','message': 'Something went wrong.' + str(e)}, status= status.HTTP_500_INTERNAL_SERVER_ERROR)

def response_200(message,data):
    return Response({'status': 'OK','message': message,'data':data}, status= status.HTTP_200_OK)

def response_201(message,data):
    return Response({'status': 'Inserted','message': message,'data':data}, status= status.HTTP_201_CREATED)

def response_204(message):
    return Response({'status': 'OK','message': message}, status= status.HTTP_204_NO_CONTENT)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def update_profile(request):
    try:
        user = request.user
        body = request.data
        first_name = body['first_name']
        last_name = body['last_name']
        email = body['email'] if 'email' in body else None
        dob = body['dob'] if 'dob' in body else None
        gender = body['gender'] if 'gender' in body else None
        is_employer = body['is_employer'] if 'is_employer' in body else False
        photo_url = body['photo_url'] if 'photo_url' in body else None

        user.first_name = first_name
        user.last_name = last_name
        if email:
            user.email = email 
        user.dob = datetime.strptime(dob, "%Y-%m-%d").date() if dob else None
        if gender:
            user.gender = gender
        user.is_employer = is_employer
        if photo_url:
            user.photo_url = photo_url
        user.save()

        return response_200("User Profile updated successfully",UserSerializer(user).data)
    
    except Exception as e:
        return response_500("internal error",e)

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_user(request):
    try:
        user = request.user
        user.removed = True
        user.save()
        return response_204("User Profile deleted successfully")
    except Exception as e:
        return response_500("internal error",e)
        
@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def complete_profile_details(request):
    try:
        user = request.user
        body = request.data

        if user.is_employer:
            company_id = body['company_id'] if 'company_id' in body else None
            mobile = body['mobile'] if 'mobile' in body else None
            alternate_mobile = int(body['alternate_mobile']) if 'alternate_mobile' in body else None
            registered_via = body['registered_via'] if 'registered_via' in body else None

            employer = Employer(user=user)
            employer.company = Company.objects.get(id=company_id)
            employer.mobile = mobile
            employer.alternate_mobile = alternate_mobile
            employer.registered_via = registered_via
            employer.save()
            return response_200("Profile details added successfully", EmployerSerializer(employer).data)

        else:
            nationality = body['nationality'] if 'nationality' in body else None
            mobile = body['mobile'] if 'mobile' in body else None
            alternate_mobile = int( body['alternate_mobile'] ) if 'alternate_mobile' in body else None
            job_role = body['job_role'] if 'job_role' in body else None
            profile_description = body['profile_description'] if 'profile_description' in body else None
            resume_link = body['resume_link'] if 'resume_link' in body else None
            year = body['year'] if 'year' in body else None
            month = body['month'] if 'month' in body else None
            registered_via = body['registered_via'] if 'registered_via' in body else None
            linkedin = body['linkedin'] if 'linkedin' in body else None
            github = body['github'] if 'github' in body else None
            twitter = body['twitter'] if 'twitter' in body else None

            candidate = Candidate(user=user)
            
            candidate.nationality = nationality
            candidate.mobile = mobile
            candidate.alternate_mobile = alternate_mobile
            candidate.job_role = job_role
            candidate.profile_description = profile_description
            candidate.resume_link = resume_link
            candidate.year = year
            candidate.month = month
            candidate.registered_via = registered_via
            candidate.linkedin = linkedin
            candidate.github = github
            candidate.twitter = twitter

            candidate.save()

            diversity_tags = body['diversity_tags'] if 'diversity_tags' in body else None
            if diversity_tags:
                for diversity_tag in diversity_tags:
                    if diversity_tag['id'] == 0:
                        tag, created = Tag.objects.get_or_create(name=diversity_tag['name'])
                    else:
                        tag = Tag.objects.get(id=diversity_tag['id'])
                    candidate.diversity_tags.add(tag.id)

            country = body['country'] if 'country' in body else None
            if country:
                if country['id'] == 0:
                    country, created = Country.objects.get_or_create(name=country['name'])
                else:
                    country = Country.objects.get(id=country['id'])
                candidate.country = country

            state = body['state'] if 'state' in body else None
            if state:
                if state['id'] == 0:
                    state, created = State.objects.get_or_create(name=state['name'], country=country)
                else:
                    state = State.objects.get(id=state['id'])
                candidate.state = state

            city = body['city'] if 'city' in body else None
            if city:
                if city['id'] == 0:
                    city, created = City.objects.get_or_create(name=city['name'], state=state)
                else:
                    city = City.objects.get(id=city['id'])
                candidate.city = city

            preferred_cities = body['preferred_city'] if 'preferred_city' in body else None
            if preferred_cities:
                for preferred_city in preferred_cities:
                    if preferred_city['id'] == 0:
                        city, created = City.objects.get_or_create(name=preferred_city['name'])
                    else:
                        city = City.objects.get(id=preferred_city['id'])
                    candidate.preferred_city.add(city.id)

            candidate.save()
            return response_200("Profile details added successfully", CandidateSerializer(candidate).data)
            
    except Exception as e:
       return response_500("internal error",e)   

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def educational_details(request):
    try:
        body = request.data

        educationDetails = EducationalDetail()
        candidate_id = body['candidate_id'] if 'candidate_id' in body else None
        if not Candidate.objects.filter(id=candidate_id).exists():
            return response_400('First register yourself on Inclusivo' , 'CANDIDATE_NOT_REGISTERED' , None)

        candidate = Candidate.objects.get(id=candidate_id)
        educationDetails.candidate = candidate

        institute = body['institute'] if 'institute' in body else None
        if institute:
            if institute['id'] == 0:
                city, created = City.objects.get_or_create(name=institute['city'])
                institute, created = EducationalInstitute.objects.get_or_create(name=institute['name'], city=city)
            else:
                institute = EducationalInstitute.objects.get(id=institute['id'])
            educationDetails.institute = institute
        
        degree = body['degree'] if 'degree' in body else None
        if degree:
            if degree['id'] == 0:
                degree_specialization = degree['specialization'] if 'specialization' in degree else None
                degree, created = Degree.objects.get_or_create(degree_name = degree['degree_name'] , degree_type = degree['degree_type'] , specialization = degree_specialization)
            else:
                degree = Degree.objects.get(id=degree['id'])
            educationDetails.degree = degree
        
        from_date = body['from_date'] if 'from_date' in body else None
        educationDetails.from_date = datetime.strptime(from_date, "%Y-%m-%d").date()
        to_date = body['to_date'] if 'to_date' in body else None
        educationDetails.to_date = datetime.strptime(to_date, "%Y-%m-%d").date() if to_date else None
        educationDetails.score = body['score'] if 'score' in body else None
        educationDetails.is_currently_enrolled = body['is_currently_enrolled'] if 'is_currently_enrolled' in body else None

        educationDetails.save()
        return response_201("Educational details added successfully.", EducationDetailsSerializer(educationDetails).data)
    except Exception as e:
        return response_500("internal error",e)
                

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def employment_history_add(request):
    try:
        user = request.user
        body = request.data
        if not(user.is_employer is False and Candidate.objects.filter(user=user).exists()):
            return response_400("User is not a candidate or user details not complete", "User is not a candidate or user details not complete",None)
        candidate = Candidate.objects.filter(user=user).first()

        employment_history = EmploymentHistory(candidate=candidate)

        company = body['company'] if 'company' in body else None
        employment_history.company = company

        from_date = body['from_date'] if 'from_date' in body else None
        if from_date:
            employment_history.from_date = datetime.strptime(from_date, "%Y-%m-%d").date()

        to_date = body['to_date'] if 'to_date' in body else None
        if to_date:
            employment_history.to_date = datetime.strptime(to_date, "%Y-%m-%d").date()

        designation = body['designation'] if 'designation' in body else None
        employment_history.designation = designation

        job_profile = body['job_profile'] if 'job_profile' in body else None
        employment_history.job_profile = job_profile
        
        employment_history.save()

        return response_201("Employment History added successfully",EmploymentHistorySerializer(employment_history).data)

    except Exception as e:
        return response_500("internal error",e)          

@api_view(['GET'])
def get_user(request, user_id):
    try:
        user_data = {}
        user_logged = request.user
        #return Response(user_id=="0")
        if user_id=="0" and user_logged:
            user_data["is_employer"] = user_logged.is_employer
            user_data["is_update_profile"] = len(user_logged.first_name)>0 if user_logged.first_name else False
            if not user_logged.is_employer:
                candidate = Candidate.objects.filter(user=user_logged).first()
                user_data["candidate"] = CandidateSerializer(candidate).data
            else:
                employer = Employer.objects.filter(user=user_logged).first()
                user_data["employer"] = EmployerSerializer(employer).data

        elif user_id!="0" and CustomUser.objects.filter(pk=user_id).exists():
            user = CustomUser.objects.filter(pk=user_id).first()
            user_data["is_update_profile"] = len(user.first_name)>0 if user.first_name else False
            user_data["is_employer"] = user.is_employer
            if not user.is_employer:
                candidate = Candidate.objects.filter(user=user).first()
                user_data["candidate"] = CandidateSerializer(candidate).data
            else:
                employer = Employer.objects.filter(user=user).first()
                user_data["employer"] = EmployerSerializer(employer).data

        else:
            return response_400("User not found", "User not found", None)
        
        return response_200("User fetched successfully", user_data)
        
    except Exception as e:
        return response_500("internal error",e)

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_employment_history(request , employment_id):
    try:
        user = request.user
        if EmploymentHistory.objects.filter(id=employment_id).exists():
            emp_history = EmploymentHistory.objects.filter(id=employment_id).first()
            candidate = Candidate.objects.filter(user=user).first()
            if candidate.id == emp_history.candidate_id:
                emp_history.removed = True
                emp_history.save()
                return response_204("History deleted successfully")
            else:
                return response_400("You're not authorized to delete this" , "DELETE_AUTHORIZATION_ERROR", None)
        else:
            return response_400("History doesn't exist" , "DOES_NOT_EXIST", None)
    except Exception as e:
        return response_500("Internal Error")

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_educational_details(request , detail_id):
    try:
        user = request.user
        if EducationalDetail.objects.filter(id=detail_id).exists() and Candidate.objects.filter(user=user).exists():
            educational_details = EducationalDetail.objects.filter(id=detail_id).first()
            candidate = Candidate.objects.filter(user=user).first()
            if candidate.id == educational_details.candidate_id:
                educational_details.removed = True
                educational_details.save()
                return response_200("Educational details deleted successfully.")
            else:
                return response_400("You're not authorized to delete this" , "DELETE_AUTHORIZATION_ERROR", None)
        else:
            return response_400("You've not added any details" , "DOES_NOT_EXIST", None)
    except Exception as e:
        return response_500("Internal Error", e)

@api_view(['POST'])
@permission_classes([AllowAny])
def check_if_user(request):
    try:
        body = request.data
        if 'email' not in body:
            return response_400("Email mandatory param", "Email mandatory param", None)

        email = body['email']
        if CustomUser.objects.filter(email=email, removed=False).exists():
            return response_200("fetched",{"is_user":True})
        else:
            return response_200("fetched",{"is_user":False})
    except Exception as e:
        return response_500("Internal Error", e)


@api_view(['GET'])
@permission_classes([AllowAny])
def get_degree_dropdown(request):
    try: 
        degrees = Degree.objects.filter(removed = False)
        return response_200("Degree fetched successfully" , DegreeSerializer(degrees, many=True).data) 
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@permission_classes([AllowAny])
def get_locations_dropdown_india(request):
    try: 
        query = """SELECT distinct(ct.id) as id, ct.name as name, st.id as state_id, st.name as state_name, co.id as country_id, co.name as country_name FROM user_city ct 
        inner join user_state st on st.id = ct.state_id
        inner join user_country co on co.id = st.country_id
        where co.name='India'
        """

        query_result = JobDTO.objects.raw(query)
        Locations = []
        for result in query_result:
            job = {}
            job['id'] = result.id
            job['name'] = result.name
            job['state_name'] = result.state_name
            job['country_name'] = result.country_name
            job['state_id'] = result.state_id
            job['country_id'] = result.country_id
            Locations.append(job)
        return response_200("Locations fetched successfully" , Locations)
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@permission_classes([AllowAny])
def get_locations_dropdown(request):
    try: 
        country = request.query_params.get('country')
        if country:
            countries = Country.objects.filter(name=country).first()
            states = State.objects.filter(country=countries)
            cities = City.objects.filter(state__in=states)
        else:   
            countries = Country.objects.all()
            states = State.objects.all()
            cities = City.objects.all()
        
        result = {}
        result['cities'] = CitySerializer(cities, many=True).data
        result['states'] = StateSerializer(states, many=True).data
        if country:
            result['countries'] = CountrySerializer(countries).data
        else:
            result['countries'] = CountrySerializer(countries, many=True).data
        return response_200("Locations fetched successfully" , result) 
    except Exception as e:
        return response_500("internal error",e)
