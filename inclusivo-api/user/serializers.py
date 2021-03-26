from rest_framework import serializers

from .models import *
from company.serializers import CompanySerializer


class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = CustomUser
        fields = '__all__'

class UserPublicSerializer(serializers.ModelSerializer):

    class Meta:
        model = CustomUser
        fields = ['first_name', 'last_name', 'photo_url', 'gender', 'dob', 'email']

class EmployerSerializer(serializers.ModelSerializer):
    user = UserPublicSerializer(read_only = True)
    company = CompanySerializer(read_only = True)

    class Meta:
        model = Employer
        fields = '__all__'

class TagSerializer(serializers.ModelSerializer):

    class Meta:
        model = Tag
        fields = '__all__'


class CountrySerializer(serializers.ModelSerializer):

    class Meta:
        model = Country
        fields = '__all__'

class StateSerializer(serializers.ModelSerializer):
    country = CountrySerializer(read_only = True)
    class Meta:
        model = State
        fields = '__all__'

class CitySerializer(serializers.ModelSerializer):
    state = StateSerializer(read_only = True)
    class Meta:
        model = City
        fields = '__all__'

class DegreeSerializer(serializers.ModelSerializer):

    class Meta:
        model = Degree
        fields = '__all__'

class CandidateSerializer(serializers.ModelSerializer):
    user = UserPublicSerializer(read_only = True)
    country = CountrySerializer(read_only = True)
    state = StateSerializer(read_only = True)
    city = CitySerializer(read_only = True)
    diversity_tags = TagSerializer(read_only=True, many=True)
    preferred_city = CitySerializer(read_only=True, many=True)

    class Meta:
        model = Candidate
        fields = '__all__'

class InstituteSerializer(serializers.ModelSerializer):
    city = CitySerializer(read_only = True)

    class Meta:
        model = EducationalInstitute
        fields = '__all__'

class EducationDetailsSerializer(serializers.ModelSerializer):
    candidate = CandidateSerializer(read_only = True)
    institute = InstituteSerializer(read_only = True)
    degree = DegreeSerializer(read_only = True)

    class Meta:
        model = EducationalDetail
        fields = '__all__'

class EmploymentHistorySerializer(serializers.ModelSerializer):
    candidate = CandidateSerializer(read_only = True)

    class Meta:
        model = EmploymentHistory
        fields = '__all__'
