package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowedCompanyData {

    @SerializedName("subscribed_company")
    private SubscribedCompanyDetails company;

    @SerializedName("jobs")
    private List<Job> jobs;

    public FollowedCompanyData(SubscribedCompanyDetails company, List<Job> jobs) {
        this.company = company;
        this.jobs = jobs;
    }

    public SubscribedCompanyDetails getCompanyModel() {
        return company;
    }

    public void setCompany(SubscribedCompanyDetails company) {
        this.company = company;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
