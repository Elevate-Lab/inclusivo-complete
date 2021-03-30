package com.dsciiita.inclusivo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.models.Story;

import java.util.List;

public class CompanyViewModel extends ViewModel {

    private final MutableLiveData<Company> text = new MutableLiveData<>();
    private final MutableLiveData<List<Job>> jobList = new MutableLiveData<>();
    private final MutableLiveData<List<Initiative>> initiatives = new MutableLiveData<>();
    private final MutableLiveData<List<Story>> stories = new MutableLiveData<>();
    private final MutableLiveData<List<Scholarship>> scholarships = new MutableLiveData<>();


    public void setText(Company company) {
        text.setValue(company);
    }

    public MutableLiveData<Company> getText() {
        return text;
    }


    public MutableLiveData<List<Job>> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList.setValue(jobList);
    }

    public MutableLiveData<List<Initiative>> getInitiatives() {
        return initiatives;
    }

    public void setInitiatives(List<Initiative> initiatives) {
        this.initiatives.setValue(initiatives);
    }

    public MutableLiveData<List<Story>> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories.setValue(stories);
    }

    public MutableLiveData<List<Scholarship>> getScholarships() {
        return scholarships;
    }

    public void setScholarships(List<Scholarship> scholarships) {
        this.scholarships.setValue(scholarships);
    }
}