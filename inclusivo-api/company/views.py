from django.shortcuts import render
from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated, AllowAny 
from rest_framework.authentication import TokenAuthentication
from .serializers import *
from user.views import response_200, response_400, response_500, response_201, response_204
from .models import *
from user.models import Candidate, Employer
from job.models import JobDTO, CountDTO
from dashboard import commonservice, constants

@api_view(['GET'])
def get_company_details(request , company_id):
    try:
        user = request.user
        if not Company.objects.filter(id = company_id).exists():
            return response_400('Requested company is not registered with Inclusivo' , 'COMPANY_NOT_REGISTERED', None)
        else: 
            company_details = Company.objects.filter(id = company_id).first()
            is_following = False
            if SubscribedCompanies.objects.filter(company=company_details, candidate__user=user):
                is_following = True
            data = {}
            data['company'] = CompanySerializer(company_details, context={'request': request}).data
            data['is_following'] = is_following
            return response_200("Companies fetched successfully" , data) 
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@permission_classes([AllowAny])
def get_company_dropdown(request):
    try: 
        companies = Company.objects.filter(removed = False)
        return response_200("Company fetched successfully" , CompanySerializer(companies, context={'request': request}, many=True).data) 
    except Exception as e:
        return response_500("internal error",e)
        
# Create your views here.
@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_company(request):
    try:
        user = request.user
        body = request.data 

        if not user.is_employer:
            return response_400("Only employers can add company profile", "Only employers can add company profile", None)
            
        else:
            name = body['name'] if 'name' in body else None
            title = body['title'] if 'title' in body else None
            short_code = body['short_code'] if 'short_code' in body else None
            phone_number = body['phone_number'] if 'phone_number' in body else None
            size = body['size'] if 'size' in body else None
            level = body['level'] if 'level' in body else None
            website = body['website'] if 'website' in body else None
            logo_url = body['logo_url'] if 'logo_url' in body else None
            profile = body['profile'] if 'profile' in body else None
            description = body['description'] if 'description' in body else None
            email = body['email'] if 'email' in body else None
            is_active = body['is_active'] if 'is_active' in body else False
            facebook = body['facebook'] if 'facebook' in body else None
            twitter = body['twitter'] if 'twitter' in body else None
            linkedin = body['linkedin'] if 'linkedin' in body else None
            instagram = body['instagram'] if 'instagram' in body else None
            address = body['address'] if 'address' in body else None

            company = Company()
            company.name = name
            company.title = title
            company.description = description
            company.short_code = short_code
            company.size = size
            company.level = level
            company.website = website
            company.logo_url = logo_url
            company.profile = profile
            company.email = email
            company.phone_number = phone_number
            company.is_active = is_active
            company.facebook = facebook
            company.twitter = twitter
            company.linkedin = linkedin
            company.instagram = instagram
            company.address = address
            company.save()

            return response_200("Company details added successfully", CompanySerializer(company, context={'request': request}).data)

    except Exception as e:
        return response_500("internal error",e)   


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def subscribe_company(request, company_id):
    try:
        user = request.user

        if not Company.objects.filter(id=company_id,removed=False).exists():
            return response_400("Company  not found","Company not found", None)  
        
        if user.is_employer:
            return response_400("Only Candidates can access this page.", "Only Candidates can access this page", None)
        
        if not Candidate.objects.filter(user=user).exists():
            return response_400("Candidate not found.", "Candidate not found.", None)
        
        candidate = Candidate.objects.filter(user=user).first()

        if SubscribedCompanies.objects.filter(candidate = candidate, company__id = company_id).exists():
            return response_400("Company already subscribed", "Company already subscribed", None)

        subscribe_company = SubscribedCompanies()
        subscribe_company.company = Company.objects.filter(id=company_id,removed=False).first()
        subscribe_company.candidate = candidate
        subscribe_company.save()
        return response_201("Company followed", SubscribedCompaniesSerializer(subscribe_company, context={'request': request}).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def unsubscribe_company(request, company_id):
    try:
        user = request.user

        if not Company.objects.filter(id=company_id,removed=False).exists():
            return response_400("Company  not found","Company not found", None)  
        
        if user.is_employer:
            return response_400("Only Candidates can access this page.", "Only Candidates can access this page", None)
        
        if not Candidate.objects.filter(user=user).exists():
            return response_400("Candidate not found.", "Candidate not found.", None)
        
        candidate = Candidate.objects.filter(user=user).first()

        if not SubscribedCompanies.objects.filter(candidate = candidate, company__id = company_id).exists():
            return response_400("Company not already subscribed", "Company  not already subscribed", None)

        subscribe_company = SubscribedCompanies.objects.filter(candidate = candidate, company__id = company_id).first()
        subscribe_company.delete()
        return response_204("Company unfollowed")
    
    except Exception as e:
        return response_500("internal error",e)   


@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_company(request, company_id):
    try:
        user=request.user
        if user.is_employer:
            if Company.objects.filter(id=company_id).exists():
                company=Company.objects.filter(id=company_id).first()
                employer=Employer.objects.filter(user_id = user.id).first()
                if company.id == employer.company_id:
                    company.removed=True
                    company.save()
                    return response_204("Company deleted successfully!")
                else:
                    return response_400("Your are not the employer of this Company" , "COMPANY_EMPLOYER_ID_MISMATCH" , None)
            else:
                return response_400("Company does not exist" , "COMPANY_DOES_NOT_EXIST" , None)
        else:
            return response_400("Only employer can delete a company" , "NOT_EMPLOYER" , None)
    except Exception as e: 
        return response_500("Internal server error: ", e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_story(request):
    try:
        user = request.user
        body = request.data
        if user.is_employer:
            description = body['description'] if 'description' in body else None
            photo_url = body['photo_url'] if 'photo_url' in body else None
            name = body['name'] if 'name' in body else None
            
            company = user.employer_set.first().company
            story = Stories(company=company)
            story.name = name
            
            if description:
                story.description=description
            if photo_url:
                story.photo_url=photo_url

            story.save()

            return response_201("Story saved", StoriesSerializer(story, context={'request': request}).data)
        
        else:
            return response_400("User is not employer", "User is not employer", None)
    except Exception as e:
        return response_500("internal error",e)   

@api_view(['GET'])
def get_story(request):
    try:
        user = request.user
        company_id = request.query_params.get('company_id')

        if Company.objects.filter(id=company_id).exists():
            company=Company.objects.filter(id=company_id).first()
            stories = Stories.objects.filter(company=company, removed=False)
        else:
            stories = Stories.objects.filter(removed = False)
        
        return response_200("Stories fetched successfully", StoriesSerializer(stories, context={'request': request}, many=True).data)

    except Exception as e:
        return response_500("internal error",e)   
        

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_story(request, story_id):
    try:
        user = request.user
        body = request.data
        if not user.is_employer:
            return response_400("User is not employer", "User is not employer", None)
        if not Employer.objects.filter(user=user).exists():
            return response_400("Employer not found", "Employer not found", None)
        
        employer = Employer.objects.filter(user=user).first()
        if not Stories.objects.filter(id=story_id):
            return response_400("Story not found", "Story not found", None)

        story = Stories.objects.filter(id=story_id).first()
        if story.company_id!=employer.company_id:
            return response_400("Company does not match", "Company does not match", None)
        story.removed = True
        story.save()  
        return response_204("Story deleted successfully")  
            
    except Exception as e:
        return response_500("internal error",e)    

@api_view(['GET'])
def get_story_individual(request, story_id):
    try:
        user = request.user

        if Stories.objects.filter(removed = False , id = story_id).exists():
            story = Stories.objects.filter(removed = False , id = story_id).first()
            return response_200("Stories fetched successfully", StoriesSerializer(story, context={'request': request}).data)
        else:
            return response_400("Story does not exists", "Story does not exists", None)

    except Exception as e:
        return response_500("internal error",e)

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_initiatives(request):
    try:
        user = request.user
        body = request.data
        if user.is_employer:
            name = body['name'] if 'name' in body else None
            description = body['description'] if 'description' in body else None
            
            company = user.employer_set.first().company
            initiative = Initiative(company=company)
            initiative.name = name
            initiative.description = description
            
            initiative.save()

            return response_201("Initiative saved", InitiativeSerializer(initiative).data)
        
        else:
            return response_400("User is not employer", "User is not employer", None)
    except Exception as e:
        return response_500("internal error",e)   

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_initiative(request, initiative_id):
    try:
        user=request.user
        if user.is_employer:
            if Initiative.objects.filter(id = initiative_id).exists():
                initiative=Initiative.objects.filter(id=initiative_id).first()
                employer = Employer.objects.filter(user_id = user.id).first()
                if initiative.company.id == employer.company_id:
                    initiative.removed = True
                    initiative.save()
                    return response_204("Initiative deleted successfully!")
                else:
                    return response_400("Your are not the employer of this Company" , "COMPANY_EMPLOYER_ID_MISMATCH" , None)
            else:
                return response_400("Initiative does not exist" , "INITIATIVE_DOES_NOT_EXIST" , None)
        else:
            return response_400("Only employer can delete a Initiative" , "NOT_EMPLOYER" , None)
    except Exception as e: 
        return response_500("Internal server error: ", e)

@api_view(['GET'])
def get_initiative_individual(request , initiative_id):
    try:
        if not Initiative.objects.filter(id = initiative_id , removed = False).exists():
            return response_400('Requested Initiative does not exists' , 'INITIATIVE DOES NOT EXISTS', None)
        else: 
            initiative_details = Initiative.objects.filter(id = initiative_id , removed = False).first()
            return response_200("Initiative fetched successfully" , InitiativeSerializer(initiative_details).data) 
    except Exception as e:
        return response_500("internal error",e)

@api_view(['GET'])
def get_initiative_by_company(request , company_id):
    try:
        #if not Initiative.objects.filter(company__id = company_id , removed = False).exists():
        #    return response_400('Requested Initiative does not exists' , 'INITIATIVE DOES NOT EXISTS', None)
        #else: 
        initiative_details = Initiative.objects.filter(company__id = company_id , removed = False)
        return response_200("Initiative fetched successfully" , InitiativeSerializer(initiative_details , many=True).data) 
    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
def list_companies(request):
    try:
        user = request.user
        body = request.data
        filters = body['filters'] if 'filters' in body else {}
        sortField = getDBFieldCompany(filters['sortField']) if 'sortField' in filters else "c.id"
        sortOrder = filters['sortOrder'] if 'sortOrder' in filters else "desc"
        pageNumber = filters['pageNumber'] if 'pageNumber' in filters else constants.DEFAULT_PAGE_NUMBER
        pageSize = filters['pageSize'] if 'pageSize' in filters else constants.DEFAULT_PAGE_SIZE
        search =  filters['search'] if 'search' in filters else []
        filter_query = " where c.removed=FALSE"


        job_list = []

        totalCountQuery = """SELECT count(DISTINCT c.id) as count   
        FROM company_company c
        """
        query = """SELECT distinct(c.id) as id
        FROM company_company c 
        """

        query += filter_query
        totalCountQuery += filter_query

        searchQuery = ""
        if len(search)>0:
            for s in search:
                searchType = commonservice.getSearchType(s['searchType'] if 'searchType' in s else None)
                dbField = getDBFieldCompany(s['searchField'] if 'searchField' in s else None)
                fieldDatatype = getFieldDataTypeCompany(s['searchField'] if 'searchField' in s else None)

                searchQuery += " and " + commonservice.getSearchQueryField(searchType, fieldDatatype, dbField, s['searchText'] if 'searchText' in s else "")

        query += searchQuery
        totalCountQuery += searchQuery
        query += " order by " + sortField
        query += " " + sortOrder
        if pageNumber != -1:
            query += " limit " + str(pageSize)  + " offset " + (str)(pageNumber * pageSize)

        results = {}
        query_result = JobDTO.objects.raw(query)
        

        for result in query_result:
            job = CompanySerializer(Company.objects.get(id=result.id), context={'request': request}).data
            #job['is_following'] = SubscribedCompanies.objects.filter(company_id=result.id, candidate__user=user).exists()
            job_list.append(job)

        total_count_result = CountDTO.objects.raw(totalCountQuery)[0].count

        results['companies'] = job_list
        results['total_count'] = total_count_result
        results['page_number'] = pageNumber
        results['page_size'] = pageSize

        return response_200("Fetched successfully",results)

    except Exception as e:
        return response_500("internal error",e)



def getDBFieldCompany(UIField):
    switcher = {
        "id": "c."+UIField,
        "title": "c."+UIField,
        "name": "c."+UIField,
        "description": "c."+UIField
    }
    return switcher.get(UIField, "")

def getFieldDataTypeCompany(UIField):
    switcher = {
        "id": "int",
        "title": "string",
        "name": "string",
        "description": "string"

    }
    return switcher.get(UIField, "")