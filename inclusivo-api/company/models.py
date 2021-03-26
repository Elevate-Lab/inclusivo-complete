from django.db import models

COMPANY_SIZE = (
    ("1-10", "1-10"),
    ("11-20", "11-20"),
    ("21-50", "21-50"),
    ("50-200", "50-200"),
    ("200+", "200+"),
)

# Create your models here.
class Company(models.Model):
    name = models.CharField(max_length=5000)
    website = models.CharField(max_length=5000, null=True, blank=True)
    address = models.TextField()
    logo_url = models.CharField(max_length=255, blank=True, null=True)
    size = models.CharField(choices=COMPANY_SIZE, max_length=10, default="")
    #company_type = models.CharField(choices=COMPANY_TYPES, max_length=50, default="")
    profile = models.TextField()
    phone_number = models.CharField(max_length=15)
    registered_date = models.DateField(auto_now_add=True)
    email = models.EmailField(max_length=255, null=True)
    short_code = models.CharField(max_length=50, null=True)
    is_active = models.BooleanField(default=False)
    title = models.TextField(default="")
    description = models.TextField(default="")
    removed = models.BooleanField(default=False)
    facebook = models.CharField(max_length=150, null=True)
    twitter = models.CharField(max_length=150, null=True)
    linkedin = models.CharField(max_length=150, null=True)
    instagram = models.CharField(max_length=150, null=True)

class SubscribedCompanies(models.Model):
    subscribed_date = models.DateTimeField(auto_now=True)
    company = models.ForeignKey(Company, on_delete=models.CASCADE)
    candidate = models.ForeignKey('user.Candidate', on_delete=models.CASCADE)
    removed = models.BooleanField(default=False)

class Initiative(models.Model):
    company = models.ForeignKey(Company, on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    description = models.CharField(max_length=1000)
    removed = models.BooleanField(default=False)

class Stories(models.Model):
    company = models.ForeignKey(Company, on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    description = models.CharField(max_length=1000)
    photo_url = models.CharField(max_length=255, blank=True, null=True)
    removed = models.BooleanField(default=False)

# COMPANY_TYPES =
'''AGRICULTURE AND ALLIED INDUSTRIES
AUTOMOBILES
AUTO COMPONENTS
AVIATION
BANKING
BIOTECHNOLOGY
CEMENT
CHEMICALS
CONSUMER DURABLES
DEFENCE MANUFACTURING
E-COMMERCE
EDUCATION AND TRAINING
ELECTRONICS SYSTEM DESIGN & MANUFACTURING
ENGINEERING AND CAPITAL GOODS
FINANCIAL SERVICES
FMCG
GEMS AND JEWELLERY
HEALTHCARE
INFRASTRUCTURE
INSURANCE
IT & BPM
MANUFACTURING
MEDIA AND ENTERTAINMENT
MEDICAL DEVICES
METALS AND MINING
MSME
OIL AND GAS
PHARMACEUTICALS
PORTS
POWER
RAILWAYS
REAL ESTATE
RENEWABLE ENERGY
RETAIL
ROADS
SCIENCE AND TECHNOLOGY
SERVICES
STEEL
TELECOMMUNICATIONS
TEXTILES
TOURISM AND HOSPITALITY'''

