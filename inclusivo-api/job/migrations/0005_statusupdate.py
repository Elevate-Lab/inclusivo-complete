# Generated by Django 3.1.5 on 2021-06-14 06:56

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('job', '0004_jobapplication_message'),
    ]

    operations = [
        migrations.CreateModel(
            name='StatusUpdate',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('status', models.CharField(choices=[('Draft', 'Draft'), ('Expired', 'Expired'), ('Disabled', 'Disabled'), ('Published', 'Published'), ('Hired', 'Hired')], max_length=50)),
                ('message', models.CharField(max_length=500, null=True)),
                ('recruiter_notes', models.CharField(max_length=500, null=True)),
                ('removed', models.BooleanField(default=False)),
                ('job_application', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='job.jobapplication')),
            ],
        ),
    ]
