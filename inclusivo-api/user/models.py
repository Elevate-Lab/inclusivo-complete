from django.db import models
from django.contrib.auth.models import AbstractUser
from .managers import CustomUserManager
from django.utils.translation import ugettext_lazy as _
from company.models import Company
from job.models import Tag

DEGREE_TYPES = (
    ("Permanent", "Permanent"),
    ("PartTime", "PartTime"),
)

REGISTERED_VIA = (
    ("Email", "Email"),
    ("Social", "Social"),
    ("Careers", "Careers"),
    ("Friend", "Friend"),
)

class CustomUser(AbstractUser):
    username = None
    first_name = models.CharField(max_length=255, blank=True, null=True)
    last_name = models.CharField(max_length=255, blank=True, null=True)
    enc_pass = models.CharField(max_length=40, blank=True, null=True)
    email = models.EmailField(_('email address'), unique=True)
    dob = models.DateField(blank=True, null=True)
    photo_url = models.CharField(max_length=255, blank=True, null=True)
    gender = models.CharField(max_length=30, blank=True, null=True)
    removed = models.BooleanField(default=False)
    inserted_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    is_admin = models.IntegerField(default=0)
    is_employer = models.BooleanField(default=False)
    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []
    objects = CustomUserManager()
    def __str__(self):
        return self.email

class Country(models.Model):
    name = models.CharField(max_length=500)
    def __str__(self):
        return self.name

class State(models.Model):
    country = models.ForeignKey(Country, on_delete=models.CASCADE)
    name = models.CharField(max_length=500)
    def __str__(self):
        return self.name

class City(models.Model):
    name = models.CharField(max_length=500)
    state = models.ForeignKey(State, null=True, on_delete=models.CASCADE)
    def __str__(self):
        return self.name

class Candidate(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    is_active = models.BooleanField(default=False)
    nationality = models.TextField(max_length=50, blank=True, null=True)
    mobile = models.CharField(max_length=20, blank=True, null=True)
    alternate_mobile = models.BigIntegerField(blank=True, null=True)
    city = models.ForeignKey(City, null=True, blank=True, on_delete=models.CASCADE)
    state = models.ForeignKey(State, null=True, blank=True, on_delete=models.CASCADE)
    country = models.ForeignKey(Country, null=True, blank=True, on_delete=models.CASCADE)
    preferred_city = models.ManyToManyField(City, related_name="preferred_city")
    job_role = models.CharField(max_length=500, default="")
    #preferred_industry = models.CharField(choices=COMPANY_TYPES, max_length=30, default="")
    profile_description = models.CharField(max_length=2000, default="")
    resume_link = models.CharField(max_length=255, blank=True, null=True)
    is_relocation = models.BooleanField(default=False)
    year = models.CharField(max_length=50, blank=True, null=True)
    month = models.CharField(max_length=50, default="")
    registered_via = models.CharField(choices=REGISTERED_VIA, max_length=15, default="")
    diversity_tags = models.ManyToManyField(Tag)
    linkedin = models.CharField(max_length=150, null=True)
    github = models.CharField(max_length=150, null=True)
    twitter = models.CharField(max_length=150, null=True)

    def __str__(self):
        return self.user.email

class Employer(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    company = models.ForeignKey(Company, on_delete=models.CASCADE)
    is_active = models.BooleanField(default=False)
    mobile = models.CharField(max_length=20, blank=True, null=True)
    alternate_mobile = models.BigIntegerField(blank=True, null=True)
    registered_via = models.CharField(choices=REGISTERED_VIA, max_length=15, default="")
    def __str__(self):
        return self.user.email

class EducationalInstitute(models.Model):
    name = models.CharField(max_length=500)
    city = models.ForeignKey(City, on_delete=models.CASCADE)
    def __str__(self):
        return self.name

class EmploymentHistory(models.Model):
    candidate = models.ForeignKey(Candidate, on_delete=models.CASCADE)
    company = models.CharField(max_length=500)
    from_date = models.DateField(null=True)
    to_date = models.DateField(null=True, blank=True)
    designation = models.CharField(max_length=500)
    current_job = models.BooleanField(default=False)
    job_profile = models.TextField()
    removed = models.BooleanField(default=False)

class Degree(models.Model):
    degree_name = models.CharField(max_length=500)
    degree_type = models.CharField(choices=DEGREE_TYPES, max_length=50)
    specialization = models.CharField(max_length=500, null=True, blank=True)
    removed = models.BooleanField(default=False)
    def __str__(self):
        return self.degree_name

class EducationalDetail(models.Model):
    candidate = models.ForeignKey(Candidate, on_delete=models.CASCADE)
    institute = models.ForeignKey(EducationalInstitute, on_delete=models.CASCADE)
    from_date = models.DateField()
    to_date = models.DateField(null=True, blank=True)
    degree = models.ForeignKey(Degree, on_delete=models.CASCADE)
    score = models.CharField(max_length=50)
    is_currently_enrolled = models.BooleanField(default=False)
    removed = models.BooleanField(default=False)
    def __str__(self):
        return self.degree.degree_name