from django.shortcuts import render
from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated 
from rest_framework.authentication import TokenAuthentication
from .serializers import *
from user.views import response_200, response_400, response_500, response_201, response_204
from .models import *
from company.models import SubscribedCompanies
from dashboard import commonservice, constants
from user.models import Tag, City, Degree, Employer, Candidate
from datetime import datetime
from django.db.models import Q
from django.core.mail import EmailMultiAlternatives
from django.template.loader import render_to_string
import logging
import requests
logger = logging.getLogger(__name__)

# Create your views here.
def log_error(message,endpoint):
    #Logger
    #logger = logging.getLogger('applyft_rest_api__error')
    error_logger = logging.getLogger('error_logger')
    error_logger.error('{} in {} at: {}'.format(message, endpoint, str(datetime.now())))
    error_logger.error('----------------------------------------------')

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_job(request):
    try:
        user = request.user
        body = request.data 

        if not user.is_employer:
            return response_400("Only employers can add job profile", "Only employers can add job profile", None)
            
        else:
            if 'company_id' not in body:
                return response_400("Company id required", "Company id required", None)
            if Employer.objects.filter(user=user).first().company_id!=body['company_id']:
                return response_400("Only employers of this company can add job profile", "Only employers of this company can add job profile", None)
            company_id = body['company_id'] 
            title = body['title'] if 'title' in body else None
            status = body['status'] if 'status' in body else "Draft"
            previous_status = body['previous_status'] if 'previous_status' in body else "Draft"
            job_type = body['job_type'] if 'job_type' in body else None
            is_apply_here = body['is_apply_here'] if 'is_apply_here' in body else True
            last_date = body['last_date'] if 'last_date' in body else None
            vacancies = int(body['vacancies']) if 'vacancies' in body else 1
            min_exp = int(body['min_exp']) if 'min_exp' in body else 0
            max_exp = int(body['max_exp']) if 'max_exp' in body else 0
            selection_process = body['selection_process'] if 'selection_process' in body else ""
            short_code = body['short_code'] if 'short_code' in body else None
            job_role = body['job_role'] if 'job_role' in body else None
            description = body['description'] if 'description' in body else None
            min_sal = body['min_sal'] if 'min_sal' in body else None
            max_sal = body['max_sal'] if 'max_sal' in body else None
            display_salary = body['display_salary'] if 'display_salary' in body else False

            job = JobPost()
            job.title = title
            job.company = Company.objects.get(id=company_id)
            job.description = description
            job.short_code = short_code
            job.status = status
            job.previous_status = previous_status
            job.last_date = datetime.strptime(last_date, "%Y-%m-%d").date()
            job.vacancies = vacancies
            job.job_type = job_type
            job.job_role = job_role
            job.selection_process = selection_process
            job.min_exp = min_exp
            job.max_exp = max_exp
            job.is_apply_here = is_apply_here
            job.min_sal = min_sal
            job.max_sal = max_sal
            job.display_salary = display_salary
            if not is_apply_here:
                job.apply_url = body['apply_url'] 
            job.save()

            tags = body['tags'] if 'tags' in body else []
            for tag in tags:
                if tag['id'] == 0:
                    tag, created = Tag.objects.get_or_create(name=tag['name'])
                else:
                    tag = Tag.objects.get(id=tag['id'])
                job.tags.add(tag.id)

            locations = body['locations'] if 'locations' in body else []
            for location in locations:
                if location['id'] == 0:
                    location, created = City.objects.get_or_create(name=location['name'])
                else:
                    location = City.objects.get(id=location['id'])
                job.locations.add(location.id)

            degrees = body['degrees'] if 'degrees' in body else []
            for degree in degrees:
                if degree['id'] == 0:
                    degree, created  = Degree.objects.get_or_create(degree_name=degree['name'], degree_type = degree['type'], specialization = degree['specialization'])
                else:
                    degree= Degree.objects.get(id=degree['id'])
                job.accepted_degrees.add(degree.id)

            job.save()
            email_status = {'sent':[], 'invalid':[]}
            try:
                subject, from_email, to = 'New Job Posted!', 'inclusivoofficial@gmail.com', '{0}'.format(user.email)
                text_content = 'New Job Posted!'
                message_obj = {
                            'company_name': job.company.title,
                            'job_title': job.title,
                            'employer_first_name': user.first_name,
                            'employer_last_name': user.last_name
                            }
                html_content = render_to_string('job_creation.html', message_obj)
                msg = EmailMultiAlternatives(subject, text_content, from_email, [to])
                msg.attach_alternative(html_content, "text/html")
                msg.send()
                email_status['sent'].append(user.email)
            except Exception as ex:
                template = "{0}"
                message = template.format(ex.args)
                log_error(message,
                            'welcome -- Sending email from inclusivoofficial@gmail.com to {0}'.format(
                                user.email))
                email_status['invalid'].append(user.email)  
            try:
                to = [a.candidate.email for a in SubscribedCompanies.objects.filter(company=job.company)]
                subject, from_email = '{0} Posted a New Job!'.format(job_application.job.company.title), 'inclusivoofficial@gmail.com'
                text_content = '{0} Posted a New Job!'.format(job_application.job.company.title)
                message_obj = {
                            'company_name': job.company.title,
                            'job_title': job.title
                            }
                html_content = render_to_string('job_creation_subscribers.html', message_obj)
                msg = EmailMultiAlternatives(subject, text_content, from_email, bcc=to)
                msg.attach_alternative(html_content, "text/html")
                msg.send()
                email_status['sent'].extend(to)
            except Exception as ex:
                template = "{0}"
                message = template.format(ex.args)
                log_error(message,
                            'welcome -- Sending email from inclusivoofficial@gmail.com to {0}'.format(
                                user.email))
                email_status['invalid'].extend(to)    
            return response_201("Job details added successfully. Email status: " + str(email_status['sent']), JobSerializer(job, context={'request': request}).data)
    
    #send mail to all employers of the company that job has been created
    #send mail to candidates subscribed to that company about new job
    except Exception as e:
        return response_500("internal error",e)  

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_job(request , job_id):
    try:
        user = request.user
        if user.is_employer and JobPost.objects.filter(id=job_id).exists():
            employer = Employer.objects.filter(user_id = user.id).first()
            job_post = JobPost.objects.filter(id=job_id).first()
            if employer.company_id == job_post.company_id:
                job_post.removed = True
                job_post.save()
                return response_204("Job Post deleted successfully!")
            else:
                return response_400("Your are not the employer of this job's company" , "COMPANY_EMPLOYER_ID_MISMATCH", None)
        else: 
            return response_400("Job or employer not found!" , "NO_EMPLOYER_ID_MISMATCH", None)
    except Exception as e:
        return response_500("Internal Error" , e)

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def job_application_creation(request, job_id):
    try:
        user = request.user
        body = request.data 
        # TODO Candidate remove check
        if not(user.is_employer is False and Candidate.objects.filter(user=user).exists()):
            return response_400("Only candidate can add job application", "Only candidate can add job application", None)
            
        candidate = Candidate.objects.filter(user=user).first()
        
        if  not JobPost.objects.filter(~Q(status__in=['Draft','Expired','Hired','Disabled']), removed=False, id=job_id).exists():
            return response_400("Job Post not accessibe", "Job Post not accessible", None)
        
        job_post = JobPost.objects.filter(id=job_id).first()
        job_application = JobApplication(candidate=candidate)
        job_application.message = body['message']
        job_application.job = job_post
        
        job_application.status = body['status'] if 'status' in body else None
        
        job_application.save()
        email_status = {'sent':[], 'invalid':[]}
        employer_user = Employer.objects.filter(company=job_post.company).first().user
        try:
            subject, from_email, to = 'Job Application submitted!', 'inclusivoofficial@gmail.com', '{0}'.format(employer_user.email)
            text_content = 'Job Application submitted!'
            message_obj = {
                        'company_name': job_post.company.title,
                        'job_title': job_post.title,
                        'user_first_name': user.first_name,
                        'user_last_name': user.last_name,
                        'employer_first_name': employer_user.first_name,
                        'employer_last_name': employer_user.last_name,
                        'message_from_candidate':job_application.message
                        }
            html_content = render_to_string('job_application_creation_employer.html', message_obj)
            msg = EmailMultiAlternatives(subject, text_content, from_email, [to])
            msg.attach_alternative(html_content, "text/html")
            msg.send()
            email_status['sent'].append(employer_user.email)
        except Exception as ex:
            template = "{0}"
            message = template.format(ex.args)
            log_error(message,
                        'welcome -- Sending email from inclusivoofficial@gmail.com to {0}'.format(
                            user.email))
            email_status['invalid'].append(user.email)
        try:
            subject, from_email, to = 'Job Application created!', 'inclusivoofficial@gmail.com', '{0}'.format(user.email)
            text_content = 'Job Application created!'
            message_obj = {
                        'company_name': job_post.company.title,
                        'job_title': job_post.title,
                        'user_first_name': user.first_name,
                        'user_last_name': user.last_name
                        }
            html_content = render_to_string('job_application_creation.html', message_obj)
            msg = EmailMultiAlternatives(subject, text_content, from_email, [to])
            msg.attach_alternative(html_content, "text/html")
            msg.send()
            email_status['sent'].append(user.email)
        except Exception as ex:
            template = "{0}"
            message = template.format(ex.args)
            log_error(message,
                        'welcome -- Sending email from inclusivoofficial@gmail.com to {0}'.format(
                            user.email))
            email_status['invalid'].append(user.email)
        return response_201("Job Application created successfully. Email status: " + str(email_status['sent']), JobApplicationSerializer(job_application, context={'request': request}).data)
    
    except Exception as e:
        return response_500("internal error",e)  

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def evaluate_profile(request, job_id):
    try:
        user = request.user
        body = request.data 
        # TODO Candidate remove check
        if not(user.is_employer is False and Candidate.objects.filter(user=user).exists()):
            return response_400("Only candidate can add job application", "Only candidate can add job application", None)
            
        candidate = Candidate.objects.filter(user=user).first()
        
        if  not JobPost.objects.filter(~Q(status__in=['Draft','Expired','Hired','Disabled']), removed=False, id=job_id).exists():
            return response_400("Job Post not accessibe", "Job Post not accessible", None)
        
        job_post = JobPost.objects.filter(id=job_id).first()
        employer_user = Employer.objects.filter(company=job_post.company).first().user

        url = "https://auth.emsicloud.com/connect/token"

        CLIENT_SECRET =  "EUbAIeKO"
        CLIENT_ID = "6tokckyk2e7cgilf"
        payload = "client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&grant_type=client_credentials&scope=emsi_open"
        headers = {'Content-Type': 'application/x-www-form-urlencoded'}

        response = requests.request("POST", url, data=payload, headers=headers)

        ACCESS_TOKEN = response.json()["access_token"]

        url = "https://emsiservices.com/skills/versions/latest/extract"

                
        payload_response = job_post.description
        headers = {
            'Authorization': "Bearer "+ ACCESS_TOKEN,
            'Content-Type': "text/plain"
            }

        job_response = requests.request("POST", url, data=payload_response, headers=headers).json()

        payload_url = candidate.resume_link
        payload_response = requests.get(payload_url)
        with open('/tmp/metadata.pdf', 'wb+') as f:
            f.write(payload_response.content)
        headers = {
            'Authorization': "Bearer "+ ACCESS_TOKEN,
            'Content-Type': "application/pdf"
            }

        candidate_response = requests.request("POST", url, data=open('/tmp/metadata.pdf','rb'), headers=headers).json()

        response = {}
        m = 0
        matching_skill = []
        for skill in candidate_response['data']:
            if skill in job_response['data']:
                matching_skill.append(skill['skill']['name'])
                m = m+1
        
        
        response['matching_skills'] = matching_skill
        response['matching_rate'] = m/len(job_response['data']) if len(job_response['data'])!= 0 else 0
        response['job_skills'] = job_response
        response['candidate_skills'] = candidate_response
        return response_200("Evaluated profile successfully.",response)

    except Exception as e:
        return response_500("internal error",e) 


@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_job_application(request , job_application_id):
    try:
        user = request.user
        if JobApplication.objects.filter(id=job_application_id).exists():
            if Candidate.objects.filter(user_id = user.id).exists():
                candidate = Candidate.objects.filter(user_id = user.id).first()
                job_application = JobApplication.objects.filter(id=job_application_id).first()
                if candidate.id == job_application.candidate_id:
                    job_application.removed = True
                    job_application.save()
                    return response_204("Job Application successfully")
                else:
                    return response_400("You've not applied for this job." , "CANDIDATE_APPLICATION_ID_MISMATCH", None)
        else: 
            return response_400("Job Application not found!" , "NO_ID_MATCH", None)
    except Exception as e:
        return response_500("Internal Error" , e)

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_scholarship(request):
    try:
        user = request.user
        body = request.data 

        company_id = body['company_id'] if 'company_id' in body else None
        title = body['title'] if 'title' in body else None
        status = body['status'] if 'status' in body else "Draft"
        previous_status = body['previous_status'] if 'previous_status' in body else "Draft"
        is_apply_here = body['is_apply_here'] if 'is_apply_here' in body else False
        last_date = body['last_date'] if 'last_date' in body else None
        vacancies = int(body['vacancies']) if 'vacancies' in body else 1
        selection_process = body['selection_process'] if 'selection_process' in body else ""
        short_code = body['short_code'] if 'short_code' in body else None
        description = body['description'] if 'description' in body else None

        scholarship = ScholarshipPost()
        scholarship.title = title
        scholarship.company = Company.objects.get(id=company_id) if company_id else None
        scholarship.description = description
        scholarship.short_code = short_code
        scholarship.status = status
        scholarship.previous_status = previous_status
        scholarship.last_date = datetime.strptime(last_date, "%Y-%m-%d").date() if last_date else None
        scholarship.vacancies = vacancies
        scholarship.selection_process = selection_process
        scholarship.is_apply_here = is_apply_here
        if not is_apply_here:
                scholarship.apply_url = body['apply_url'] 
        scholarship.save()

        tags = body['tags'] if 'tags' in body else []
        for tag in tags:
            if tag['id'] == 0:
                tag, created = Tag.objects.get_or_create(name=tag['name'])
            else:
                tag = Tag.objects.get(id=tag['id'])
            scholarship.tags.add(tag.id)

        degrees = body['degrees'] if 'degrees' in body else []
        for degree in degrees:
            if degree['id'] == 0:
                degree, created  = Degree.objects.get_or_create(degree_name=degree['name'], degree_type = degree['type'], specialization = degree['specialization'])
            else:
                degree= Degree.objects.get(id=degree['id'])
            scholarship.accepted_degrees.add(degree.id)

        scholarship.save()

        return response_201("Scholarship details added successfully", ScholarshipSerializer(scholarship, context={'request': request}).data)

    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
def get_job(request, job_id):
    try:
        if not JobPost.objects.filter(~Q(status='Draft'), id = job_id, removed=False).exists():
            return response_400("Job not found", "Job not found", None)
        job = JobPost.objects.get(id = job_id)
        user = request.user
        is_applied = False
        is_liked = False
        data = {}
        if JobApplication.objects.filter(job=job, candidate__user=user):
            is_applied = True
            data['application_status'] = JobApplication.objects.filter(job=job, candidate__user=user).first().status
        if LikedJobs.objects.filter(job_post=job, candidate__user=user):
            is_liked = True

        data['job'] = JobSerializer(job, context={'request': request}).data
        data['is_applied'] = is_applied
        return response_200("Job fetched successfully", data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
def get_scholarship(request, scholarship_id):
    try:
        if not ScholarshipPost.objects.filter(~Q(status='Draft'),id = scholarship_id, removed=False).exists():
            return response_400("Scholarship not found", "Scholarship not found", None)
        sc = ScholarshipPost.objects.get(id = scholarship_id)
        return response_200("Scholarship fetched successfully", ScholarshipSerializer(sc, context={'request': request}).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def like_job(request, job_id):
    try:
        user = request.user

        if not JobPost.objects.filter(id=job_id).exists():
            return response_400("Job  not found","Job not found", None)  

        if JobPost.objects.get(id=job_id).status not in ['Expired','Published']:
            return response_400("Job state should be Published or Expired","Job state should be Published or Expired", None)  
        
        if user.is_employer:
            return response_400("Only Candidates can access this page.", "Only Candidates can access this page", None)
        
        if not Candidate.objects.filter(user=user).exists():
            return response_400("Candidate not found.", "Candidate not found.", None)
        
        candidate = Candidate.objects.filter(user=user).first()

        if LikedJobs.objects.filter(candidate = candidate, job_post__id = job_id).exists():
            return response_400("Job already liked", "Job already liked", None)

        like_job = LikedJobs()
        like_job.job_post = JobPost.objects.filter(id=job_id).first()
        like_job.candidate = candidate
        like_job.save()
        return response_201("Job liked",LikedJobsSerializer(like_job, context={'request': request}).data)

    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def unlike_job(request, job_id):
    try:
        user = request.user

        if not JobPost.objects.filter(id=job_id).exists():
            return response_400("Job  not found","Job not found", None)  

        if JobPost.objects.get(id=job_id).status not in ['Expired','Published']:
            return response_400("Job state should be Published or Expired","Job state should be Published or Expired", None)  
        
        if user.is_employer:
            return response_400("Only Candidates can access this page.", "Only Candidates can access this page", None)
        
        if not Candidate.objects.filter(user=user).exists():
            return response_400("Candidate not found.", "Candidate not found.", None)
        
        candidate = Candidate.objects.filter(user=user).first()

        if not LikedJobs.objects.filter(candidate = candidate, job_post__id = job_id).exists():
            return response_400("Job not already liked", "Job not already liked", None)

        like_job = LikedJobs.objects.filter(candidate = candidate, job_post__id = job_id).first()
        like_job.delete()
        return response_204("Job unliked")

    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def like_scholarship(request, scholarship_id):
    try:
        user = request.user

        if not ScholarshipPost.objects.filter(id=scholarship_id).exists():
            return response_400("Scholarship  not found","Scholarship not found", None)  
        
        if user.is_employer:
            return response_400("Only Candidates can access this page.", "Only Candidates can access this page", None)
        
        if not Candidate.objects.filter(user=user).exists():
            return response_400("Candidate not found.", "Candidate not found.", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        
        if LikedScholarships.objects.filter(candidate = candidate, scholarship_id = scholarship_id).exists():
            return response_400("Scholarship already liked", "Scholarship already liked", None)

        like_scholarship = LikedScholarships()
        like_scholarship.scholarship = ScholarshipPost.objects.filter(id=scholarship_id).first()
        like_scholarship.candidate = candidate
        like_scholarship.save()
        return response_201("Scholarship liked",LikedScholarshipsSerializer(like_scholarship, context={'request': request}).data)

    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def unlike_scholarship(request, scholarship_id):
    try:
        user = request.user

        if not ScholarshipPost.objects.filter(id=scholarship_id).exists():
            return response_400("Scholarship  not found","Scholarship not found", None)  
        
        if user.is_employer:
            return response_400("Only Candidates can access this page.", "Only Candidates can access this page", None)
        
        if not Candidate.objects.filter(user=user).exists():
            return response_400("Candidate not found.", "Candidate not found.", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        
        if not LikedScholarships.objects.filter(candidate = candidate, scholarship_id = scholarship_id).exists():
            return response_400("Scholarship not already liked", "Scholarship not already liked", None)

        like_scholarship = LikedScholarships.objects.filter(candidate = candidate, scholarship_id = scholarship_id).first()
        like_scholarship.delete()
        return response_204("Scholarship unliked")

    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_company_jobs(request, company_id):
    try:
        user = request.user
        status = request.query_params.get('status')
        if not user.is_employer:
            jobs = JobPost.objects.filter(removed=False, company__id = company_id, status__in=['Published','Expired'])
            return response_200("Jobs for Company fetched successfully", JobSerializer(jobs, context={'request': request}, many=True).data)

        #if not Employer.objects.filter(user=user,company__id = company_id).exists():
        #    return response_400("Only employers of this company can access this page.", "Only employers of this company can access this page", None)
        
        if status:
            jobs = JobPost.objects.filter(removed=False, company__id = company_id, status=status)
        else:
            jobs = JobPost.objects.filter(removed=False, company__id = company_id)
        return response_200("Jobs for Company fetched successfully", JobSerializer(jobs, context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_company_scholarships(request, company_id):
    try:
        user = request.user
        status = request.query_params.get('status')
        if not user.is_employer:
            scholarships = ScholarshipPost.objects.filter(removed=False, company__id = company_id, status__in=['Published','Expired'])
            return response_200("Scholarships for Company fetched successfully", ScholarshipSerializer(scholarships, context={'request': request}, many=True).data)

        #if not Employer.objects.filter(user=user,company__id = company_id).exists():
        #    return response_400("Only employers of this company can access this page.", "Only employers of this company can access this page", None)
        
        if status:
            scholarships = ScholarshipPost.objects.filter(removed=False, company__id = company_id, status=status)
        else:
            scholarships = ScholarshipPost.objects.filter(removed=False, company__id = company_id)
        return response_200("Scholarship for Company fetched successfully", ScholarshipSerializer(scholarships, context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_status_update_information(request, job_application_id):
    try:
        user = request.user
        if user.is_employer:
            status_updates = StatusUpdate.objects.filter(removed=False, job_application_id = job_application_id)
            return response_200("Status Updates for employers fetched successfully", StatusUpdateEmployerSerializer(status_updates, many=True).data)
        status_updates = StatusUpdate.objects.filter(removed=False, job_application_id = job_application_id)
        return response_200("Status Updates for candidates fetched successfully", StatusUpdateCandidateSerializer(status_updates, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def update_job_status(request, job_id):
    try:
        user = request.user
        body = request.data
        if not JobPost.objects.filter(id = job_id).exists():
            return response_400("Job not found.", "Job not found.", None)

        job = JobPost.objects.filter(id = job_id).first()
        status = job.status
        if not body['status']:
            return response_400("Status is a mandatory param here","Status is a mandatory param here", None)
        status_update = body['status']

        if not Employer.objects.filter(user=user,company__id = job.company.id).exists():
            return response_400("Only employers of this company can access this page.", "Only employers of this company can access this page", None)
        
        if status_update not in ['Published','Disabled','Hired','Expired']:
            return response_400("Status update not permitted","Status update not permitted", None)
        
        if status=="Draft" and status_update!="Published":
            return response_400("Status update not permitted","Status update not permitted", None)
        
        if status=="Disabled" and status_update !="Published":
            return response_400("Status update not permitted","Status update not permitted", None)

        if status in  ["Hired","Expired"]:
            return response_400("Status update not permitted","Status update not permitted", None)

        # send mail to recruiter all employees of the company about status update
        job.status = status_update
        job.previous_status = status
        job.save()
        result = {}
        result['status'] = job.status
        result['previous_state'] = job.previous_status
        return response_200("Jobs status Updated successfully", result)

    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def update_job_application_status(request, job_application_id):
    try:
        user = request.user
        body = request.data
        if not JobApplication.objects.filter(id = job_application_id).exists():
            return response_400("Job Application not found.", "Job Application not found.", None)

        job_application = JobApplication.objects.filter(~Q(status="Draft"),id = job_application_id).first()
        status = job_application.status
        if not body['status']:
            return response_400("Status is a mandatory param here","Status is a mandatory param here", None)
        status_update = body['status']

        if not Employer.objects.filter(user=user,company__id = job_application.job.company.id).exists():
            return response_400("Only employers of this company can access this page.", "Only employers of this company can access this page", None)
        
        if status_update not in ['Selected','Rejected','Shortlisted','Process']:
            return response_400("Status update not permitted","Status update not permitted", None)
        
        if status=="Pending" and status_update not in ["Rejected","Process"]:
            return response_400("Status update not permitted","Status update not permitted", None)
        
        if status=="Shortlisted" and status_update not in ["Selected","Rejected"]:
            return response_400("Status update not permitted","Status update not permitted", None)

        if status in  ["Rejected","Selected"]:
            return response_400("Status update not permitted","Status update not permitted", None)

        recruiter_notes = body['recruiter_notes'] if 'recruiter_notes' in body else None
        message = body['message'] if 'message' in body else None
        job_application.status = status_update
        status_update_obj = StatusUpdate()
        status_update_obj.job_application = job_application
        status_update_obj.status = status_update
        status_update_obj.message = message
        status_update_obj.recruiter_notes = recruiter_notes
        status_update_obj.save()
        job_application.save()
        result = {}
        result['status'] = job_application.status
        result['previous_state'] = status
        email_status = {'sent':[], 'invalid':[]}
        candidate_user = job_application.candidate.user
        try:
            subject, from_email, to = 'Update received on Job Application!', 'inclusivoofficial@gmail.com', '{0}'.format(candidate_user.email)
            text_content = 'Update received on Job Application!'
            message_obj = {
                        'company_name': job_application.job.company.title,
                        'job_title': job_application.job.title,
                        'user_first_name': candidate_user.first_name,
                        'user_last_name': candidate_user.last_name,
                        'status':job_application.status,
                        'previous_status': status
                        }
            html_content = render_to_string('job_application_update.html', message_obj)
            msg = EmailMultiAlternatives(subject, text_content, from_email, [to])
            msg.attach_alternative(html_content, "text/html")
            msg.send()
            email_status['sent'].append(candidate_user.email)
        except Exception as ex:
            template = "{0}"
            message = template.format(ex.args)
            log_error(message,
                        'welcome -- Sending email from inclusivoofficial@gmail.com to {0}'.format(
                            user.email))
            email_status['invalid'].append(user.email)
        return response_200("Job Application status Updated successfully. Email status: " + str(email_status['sent']), result)
        
    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
def list_jobs(request):
    try:
        user = request.user
        body = request.data
        filters = body['filters'] if 'filters' in body else {}
        sortField = getDBField(filters['sortField']) if 'sortField' in filters else "jb.id"
        sortOrder = filters['sortOrder'] if 'sortOrder' in filters else "desc"
        pageNumber = filters['pageNumber'] if 'pageNumber' in filters else constants.DEFAULT_PAGE_NUMBER
        pageSize = filters['pageSize'] if 'pageSize' in filters else constants.DEFAULT_PAGE_SIZE
        search =  filters['search'] if 'search' in filters else []
        filter_query = " where jb.removed=FALSE and jb.status in ('Expired','Published')"


        job_list = []

        totalCountQuery = """SELECT count(DISTINCT jb.id) as count   
        FROM job_jobpost jb
        inner join company_company c on c.id = jb.company_id
        left join job_jobpost_tags jbt on jbt.jobpost_id = jb.id
        left join job_tag t on jbt.tag_id = t.id
        left join job_jobpost_locations jbl on jbl.jobpost_id = jb.id 
        left join user_city ct on jbl.city_id = ct.id
        """
        query = """SELECT distinct(jb.id) as id, jb.title as title, jb.posted_on as posted_on, jb.vacancies as vacancies, c.id as company_id, c.name as company_name, jb.short_code as short_code, 
        jb.job_type as job_type, jb.job_role as job_role, jb.status as status, jb.previous_status as previous_status FROM job_jobpost jb 
        inner join company_company c on c.id = jb.company_id
        left join job_jobpost_tags jbt on jbt.jobpost_id = jb.id
        left join job_tag t on jbt.tag_id = t.id
        left join job_jobpost_locations jbl on jbl.jobpost_id = jb.id 
        left join user_city ct on jbl.city_id = ct.id
        """

        query += filter_query
        totalCountQuery += filter_query

        searchQuery = ""
        if len(search)>0:
            for s in search:
                searchType = commonservice.getSearchType(s['searchType'] if 'searchType' in s else None)
                dbField = getDBField(s['searchField'] if 'searchField' in s else None)
                fieldDatatype = getFieldDataType(s['searchField'] if 'searchField' in s else None)

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
            job = {}
            job['id'] = result.id
            job['company'] = CompanySerializer(Company.objects.get(id=result.company_id), context={'request': request}).data
            job['title'] = result.title
            job['job_role'] = result.job_role
            job['job_type'] = result.job_type
            job['short_code'] = result.short_code
            job['title'] = result.title
            job['posted_on'] = result.posted_on
            job['status'] = result.status
            job['previous_status'] = result.previous_status
            job['vacancies'] = result.vacancies
            job['accepted_locations'] = CitySerializer(JobPost.objects.get(id=result.id).locations, many=True).data
            job['tags'] = TagSerializer(JobPost.objects.get(id=result.id).tags, many=True).data
            job['is_liked'] = LikedJobs.objects.filter(job_post__id=result.id, candidate__user=user).exists()
            job_list.append(job)

        total_count_result = CountDTO.objects.raw(totalCountQuery)[0].count

        results['jobs'] = job_list
        results['total_count'] = total_count_result
        results['page_number'] = pageNumber
        results['page_size'] = pageSize

        return response_200("Fetched successfully",results)

    except Exception as e:
        return response_500("internal error",e)



def getDBField(UIField):
    switcher = {
        "id": "jb."+UIField,
        "title": "jb."+UIField,
        "company_name": "c.name",
        "job_type": "jb." + UIField,
        "locations":"ct.name",
        "tags":"t.name",
        "posted_on": "jb."+UIField,
        "salary":"jb.min_sal",
        "experience":"jb.min_exp"
    }
    return switcher.get(UIField, "")

def getFieldDataType(UIField):
    switcher = {
        "id": "int",
        "title": "string",
        "company_name": "string",
        "job_type": "string",
        "locations":"string",
        "tags":"string",
        "posted_on": "string",
        "salary":"int",
        "experience":"int"
    }
    return switcher.get(UIField, "")


@api_view(['POST'])
def list_scholarships(request):
    try:
        body = request.data
        user = request.user
        filters = body['filters'] if 'filters' in body else {}
        sortField = getDBFieldScholarship(filters['sortField']) if 'sortField' in filters else "s.id"
        sortOrder = filters['sortOrder'] if 'sortOrder' in filters else "desc"
        pageNumber = filters['pageNumber'] if 'pageNumber' in filters else constants.DEFAULT_PAGE_NUMBER
        pageSize = filters['pageSize'] if 'pageSize' in filters else constants.DEFAULT_PAGE_SIZE
        search =  filters['search'] if 'search' in filters else []
        filter_query = " where s.removed=FALSE and s.status in ('Expired','Published')"


        job_list = []

        totalCountQuery = """SELECT count(DISTINCT s.id) as count   
        FROM job_scholarshippost s
        left join company_company c on c.id = s.company_id
        left join job_scholarshippost_tags st on st.scholarshippost_id = s.id
        left join job_tag t on st.tag_id = t.id
        """
        query = """SELECT distinct(s.id) as id, s.title as title, s.description as description, s.posted_on as posted_on, c.id as company_id, c.name as company_name, s.short_code as short_code 
        FROM job_scholarshippost s 
        left join company_company c on c.id = s.company_id
        left join job_scholarshippost_tags st on st.scholarshippost_id = s.id
        left join job_tag t on st.tag_id = t.id
        """

        query += filter_query
        totalCountQuery += filter_query

        searchQuery = ""
        if len(search)>0:
            for s in search:
                searchType = commonservice.getSearchType(s['searchType'] if 'searchType' in s else None)
                dbField = getDBFieldScholarship(s['searchField'] if 'searchField' in s else None)
                fieldDatatype = getFieldDataTypeScholarship(s['searchField'] if 'searchField' in s else None)

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
            job = {}
            job['id'] = result.id
            if result.company_id:
                job['company'] = CompanySerializer(Company.objects.get(id=result.company_id), context={'request': request}).data
            job['title'] = result.title
            job['short_code'] = result.short_code
            job['posted_on'] = result.posted_on
            job['description'] = result.description
            job['tags'] = TagSerializer(ScholarshipPost.objects.get(id=result.id).tags, many=True).data
            job['is_liked'] = LikedScholarships.objects.filter(scholarship_id=result.id, candidate__user=user).exists()
            job_list.append(job)

        total_count_result = CountDTO.objects.raw(totalCountQuery)[0].count

        results['scholarships'] = job_list
        results['total_count'] = total_count_result
        results['page_number'] = pageNumber
        results['page_size'] = pageSize

        return response_200("Fetched successfully",results)

    except Exception as e:
        return response_500("internal error",e)



def getDBFieldScholarship(UIField):
    switcher = {
        "id": "s."+UIField,
        "title": "s."+UIField,
        "description": "s."+UIField,
        "company_name": "c.name",
        "tags":"t.name",
        "posted_on": "s."+UIField
    }
    return switcher.get(UIField, "")

def getFieldDataTypeScholarship(UIField):
    switcher = {
        "id": "int",
        "title": "string",
        "description": "string",
        "company_name": "string",
        "tags":"string",
        "posted_on": "string"

    }
    return switcher.get(UIField, "")

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_candidate_job_applications(request):
    try:
        user = request.user
        status = request.query_params.get('status')
        if user.is_employer:
            return response_400("Candidate does not exists", "Candidate does not exists", None)
        
        candidate = Candidate.objects.filter(user=user).first()
        
        if status:
            job_applications = JobApplication.objects.filter(removed=False, candidate = candidate, status=status)
        else:
            job_applications = JobApplication.objects.filter(removed=False, candidate = candidate)
        return response_200("Job Applications fetched successfully", JobApplicationSerializer(job_applications, context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)


@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def get_individual_job_application(request , job_application_id):
    try:
        user = request.user
        if JobApplication.objects.filter(id=job_application_id).exists():
            if Candidate.objects.filter(user_id = user.id).exists() or Employer.objects.filter(user_id = user.id).exists():
                candidate = Candidate.objects.filter(user_id = user.id).first()
                employer = Employer.objects.filter(user_id = user.id).first()
                job_application = JobApplication.objects.filter(id=job_application_id).first()

                if candidate and job_application.candidate_id == candidate.id or employer and job_application.job.company_id == employer.company_id:
                    return response_200("Job Application fetched successfully", JobApplicationSerializer(job_application, context={'request': request}).data)
                else:
                    return response_400("Access denied." , "CANDIDATE_APPLICATION_ID_MISMATCH", None)
            else:
                return response_400("You've not applied for this job." , "CANDIDATE_APPLICATION_ID_MISMATCH", None)
        else: 
            return response_400("Job Application not found!" , "NO_ID_MATCH", None)
    except Exception as e:
        return response_500("Internal Error" , e)

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def list_job_applications(request, job_id):
    try:
        user = request.user
        if not user.is_employer:
            return response_400("Page accessible to employers only", "Page accessible to employers only", None)
        
        if not Employer.objects.filter(user=user).exists():
            return response_400("Employer doesn't exist", "Employer doesn't exist", None)
        
        employer = Employer.objects.filter(user=user).first()
        
        if not JobPost.objects.filter(id=job_id, removed=False, company=employer.company).exists():
            return response_400("Job doesn't exist or not accessible", "Job doesn't exist or not accessible", None)
    
        job = JobPost.objects.filter(id=job_id, removed=False, company=employer.company).first()
        
        job_applications = JobApplication.objects.filter(removed=False, job=job)
        return response_200("Job Applications fetched successfully", JobApplicationSerializer(job_applications, context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)

@api_view(['GET'])
def get_job_by_diversity(request):
    try:
        tags = request.query_params.getlist('tag', None)

        jobs = JobPost.objects.filter(status__in = ['Expired','Published'])

        if tags:
            tagsObjects = Tag.objects.filter(name__in= tags )
            jobs = jobs.filter(removed=False, tags__in = tagsObjects, status__in = ['Expired','Published'])

        return response_200("Jobs Post fetched successfully", JobSerializer(jobs, context={'request': request}, many=True).data)
    
    except Exception as e:
        return response_500("internal error",e)        