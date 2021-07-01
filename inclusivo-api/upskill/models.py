from django.db import models

# Create your models here.

class LearningTag(models.Model):
    name = models.CharField(max_length=50)

class Blog(models.Model):
    name = models.CharField(max_length=255)
    author_credits = models.CharField(max_length=255)
    description = models.CharField(max_length=1000)
    photo_url = models.CharField(max_length=255, blank=True, null=True)
    removed = models.BooleanField(default=False)
    tags = models.ManyToManyField(LearningTag)

class Video(models.Model):
    name = models.CharField(max_length=255)
    author_credits = models.CharField(max_length=255)
    description = models.CharField(max_length=1000)
    removed = models.BooleanField(default=False)
    video_link = models.CharField(max_length=255)
    tags = models.ManyToManyField(LearningTag)
