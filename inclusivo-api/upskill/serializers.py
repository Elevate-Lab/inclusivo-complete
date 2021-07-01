from rest_framework import serializers

from .models import *

class LearningTagSerializer(serializers.ModelSerializer):

    class Meta:
        model = LearningTag
        fields = '__all__'

class BlogSerializer(serializers.ModelSerializer):
    tags = LearningTagSerializer(read_only=True,many=True)
    credits = serializers.CharField(source='author_credits')

    class Meta:
        model = Blog
        fields = '__all__'

class VideoSerializer(serializers.ModelSerializer):
    tags = LearningTagSerializer(read_only=True,many=True)
    credits = serializers.CharField(source='author_credits')

    class Meta:
        model = Video
        fields = '__all__'