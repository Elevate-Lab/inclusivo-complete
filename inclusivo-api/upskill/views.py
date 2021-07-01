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


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_blog(request):
    try:
        user = request.user
        body = request.data
        description = body['description'] if 'description' in body else None
        photo_url = body['photo_url'] if 'photo_url' in body else None
        name = body['name'] if 'name' in body else None
        author_credits = body['credits'] if 'credits' in body else None
        
        blog = Blog()
        blog.name = name
        blog.save()
        
        if description:
            blog.description=description
        if author_credits:
            blog.author_credits=author_credits
        if photo_url:
            blog.photo_url=photo_url
        tags = body['tags'] if 'tags' in body else []
        for tag in tags:
            if tag['id'] == 0:
                tag, created = LearningTag.objects.get_or_create(name=tag['name'])
            else:
                tag = LearningTag.objects.get(id=tag['id'])
            blog.tags.add(tag.id)

        blog.save()

        return response_201("Blog saved", BlogSerializer(blog, context={'request': request}).data)
    except Exception as e:
        return response_500("internal error",e)   

@api_view(['GET'])
def get_blog(request):
    try:
        tags = request.query_params.getlist('tag', None)
        blogs = Blog.objects.filter(removed = False)

        if tags:
            tagsObjects = LearningTag.objects.filter(name__in= tags )
            blogs= blogs.filter(removed=False, tags__in = tagsObjects)
              
        return response_200("Blogs fetched successfully", BlogSerializer(blogs, context={'request': request}, many=True).data)

    except Exception as e:
        return response_500("internal error",e)   
        

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_blog(request, blog_id):
    try:
        blog = Blog.objects.filter(id=blog_id).first()
        blog.removed = True
        blog.save()  
        return response_204("Blog deleted successfully")  
            
    except Exception as e:
        return response_500("internal error",e)    

@api_view(['GET'])
def get_blog_individual(request, blog_id):
    try:
        if Blog.objects.filter(removed = False , id = blog_id).exists():
            blog = Blog.objects.filter(removed = False , id = blog_id).first()
            return response_200("Blog fetched successfully", BlogSerializer(blog, context={'request': request}).data)
        else:
            return response_400("Blog does not exists", "Blog does not exists", None)

    except Exception as e:
        return response_500("internal error",e)


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def add_video(request):
    try:
        user = request.user
        body = request.data
        description = body['description'] if 'description' in body else None
        video_link = body['video_link'] if 'video_link' in body else None
        name = body['name'] if 'name' in body else None
        author_credits = body['credits'] if 'credits' in body else None
        
        video = Video()
        video.name = name
        video.save()
        
        if description:
            video.description=description
        if author_credits:
            video.author_credits=author_credits
        if video_link:
            video.video_link=video_link
        tags = body['tags'] if 'tags' in body else []
        for tag in tags:
            if tag['id'] == 0:
                tag, created = LearningTag.objects.get_or_create(name=tag['name'])
            else:
                tag = LearningTag.objects.get(id=tag['id'])
            video.tags.add(tag.id)

        video.save()

        return response_201("Video saved", VideoSerializer(video, context={'request': request}).data)
    except Exception as e:
        return response_500("internal error",e)   

@api_view(['GET'])
def get_video(request):
    try:
        tags = request.query_params.getlist('tag', None)
        videos = Video.objects.filter(removed = False)
        if tags:
            tagsObjects = LearningTag.objects.filter(name__in= tags )
            videos = videos.filter(removed=False, tags__in = tagsObjects)
      
        return response_200("Video fetched successfully", VideoSerializer(videos, context={'request': request}, many=True).data)

    except Exception as e:
        return response_500("internal error",e)   
        

@api_view(['DELETE'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def delete_video(request, video_id):
    try:
        video = Video.objects.filter(id=video_id).first()
        video.removed = True
        video.save()  
        return response_204("Video deleted successfully")  
            
    except Exception as e:
        return response_500("internal error",e)    

@api_view(['GET'])
def get_video_individual(request, video_id):
    try:
        if Video.objects.filter(removed = False , id = video_id).exists():
            video = Video.objects.filter(removed = False , id = video_id).first()
            return response_200("Video fetched successfully", VideoSerializer(video, context={'request': request}).data)
        else:
            return response_400("Video does not exists", "Video does not exists", None)

    except Exception as e:
        return response_500("internal error",e)