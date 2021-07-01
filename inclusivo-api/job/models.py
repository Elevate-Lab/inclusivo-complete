from django.db import models
from company.models import Company

JOB_STATUS = (
        ("Draft", "Draft"),
        ("Expired", "Expired"),
        ("Disabled", "Disabled"),
        ("Published", "Published"),
        ("Hired", "Hired"),
    )

SCHOLARSHIP_STATUS = (
        ("Draft", "Draft"),
        ("Expired", "Expired"),
        ("Disabled", "Disabled"),
        ("Published", "Published"),
        ("Selected", "Selected"),
    )

APPLICATION_STATUS = (
    ("Draft", "Draft"),
    ("Pending", "Pending"),
    ("Shortlisted", "Shortlisted"),
    ("Selected", "Selected"),
    ("Rejected", "Rejected"),
    ("Process", "Process"),
)

JOB_TYPES = (
    ("full-time", "Full Time"),
    ("internship", "Internship"),
    ("student", "Student"),
    ("contract", "Contract"),
)

class Tag(models.Model):
    name = models.CharField(max_length=50)

class JobPost(models.Model):    
    company = models.ForeignKey(Company, null=True, on_delete=models.CASCADE)
    short_code = models.CharField(max_length=50, null=True)
    title = models.CharField(max_length=100)
    locations = models.ManyToManyField('user.City', related_name="locations")
    job_role = models.CharField(max_length=50, default="")
    vacancies = models.IntegerField()
    tags = models.ManyToManyField(Tag)
    description = models.TextField()
    min_exp = models.IntegerField(default=0)
    max_exp = models.IntegerField(default=0)
    accepted_degrees = models.ManyToManyField('user.Degree')
    selection_process = models.TextField(default="")
    last_date = models.DateField(null=True)
    published_date = models.DateTimeField(null=True, blank=True)
    posted_on = models.DateTimeField(auto_now=True)
    created_on = models.DateField(auto_now_add=True)
    status = models.CharField(choices=JOB_STATUS, max_length=50)
    previous_status = models.CharField(choices=JOB_STATUS, max_length=50, default="Draft")
    job_type = models.CharField(choices=JOB_TYPES, max_length=50)
    is_apply_here = models.BooleanField(default=True)
    apply_url = models.CharField(max_length=150, null=True)
    removed = models.BooleanField(default=False)
    min_sal=models.PositiveIntegerField(null=True, blank=True)
    max_sal=models.PositiveIntegerField(null=True, blank=True)
    display_salary=models.BooleanField(default=False)

class StatusUpdate(models.Model):
    job_application = models.ForeignKey('job.JobApplication', on_delete=models.CASCADE)
    status = models.CharField(choices=JOB_STATUS, max_length=50)
    message = models.CharField(max_length=500, null=True)
    recruiter_notes = models.CharField(max_length=500, null=True)
    removed = models.BooleanField(default=False)

class JobDTO(models.Model):
    id = models.IntegerField(primary_key=True)

    class Meta:
        managed = False

class CountDTO(models.Model):
    count = models.IntegerField(primary_key=True)

    class Meta:
        managed = False

class ScholarshipPost(models.Model):    
    company = models.ForeignKey(Company, null=True, on_delete=models.CASCADE)
    short_code = models.CharField(max_length=50, null=True)
    title = models.CharField(max_length=100)
    vacancies = models.IntegerField()
    tags = models.ManyToManyField(Tag)
    description = models.TextField()
    accepted_degrees = models.ManyToManyField('user.Degree')
    selection_process = models.TextField(default="")
    last_date = models.DateField(null=True)
    published_date = models.DateTimeField(null=True, blank=True)
    posted_on = models.DateTimeField(auto_now=True)
    created_on = models.DateField(auto_now_add=True)
    status = models.CharField(choices=SCHOLARSHIP_STATUS, max_length=50)
    previous_status = models.CharField(choices=SCHOLARSHIP_STATUS, max_length=50, default="Draft")
    is_apply_here = models.BooleanField(default=False)
    apply_url = models.CharField(max_length=150, null=True)
    removed = models.BooleanField(default=False)

class JobApplication(models.Model):    
    job = models.ForeignKey(JobPost, on_delete=models.CASCADE)
    candidate = models.ForeignKey('user.Candidate', null=True, blank=True, on_delete=models.CASCADE)
    status = models.CharField(choices=APPLICATION_STATUS, max_length=50)
    application_date = models.DateTimeField(auto_now_add=True)
    message = models.CharField(max_length=300, null=True)
    removed = models.BooleanField(default=False)

class ScholarshipApplication(models.Model):    
    scholarship = models.ForeignKey(ScholarshipPost, on_delete=models.CASCADE)
    candidate = models.ForeignKey('user.Candidate', null=True, blank=True, on_delete=models.CASCADE)
    status = models.CharField(choices=APPLICATION_STATUS, max_length=50)
    application_date = models.DateTimeField(auto_now_add=True)
    removed = models.BooleanField(default=False)

class LikedJobs(models.Model):
    liked_date = models.DateTimeField(auto_now=True)
    job_post = models.ForeignKey(JobPost, on_delete=models.CASCADE)
    candidate = models.ForeignKey('user.Candidate', on_delete=models.CASCADE)
    removed = models.BooleanField(default=False)

class LikedScholarships(models.Model):
    liked_date = models.DateTimeField(auto_now=True)
    scholarship = models.ForeignKey(ScholarshipPost, on_delete=models.CASCADE)
    candidate = models.ForeignKey('user.Candidate', on_delete=models.CASCADE)
    removed = models.BooleanField(default=False)