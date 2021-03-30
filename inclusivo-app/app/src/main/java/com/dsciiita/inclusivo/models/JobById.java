package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

public class JobById {

    @SerializedName("job")
    private Job job;

    @SerializedName("is_applied")
    private boolean isApplied;

    @SerializedName("application_status")
    private String applicationStatus;

    public JobById(Job job, boolean isApplied) {
        this.job = job;
        this.isApplied = isApplied;
    }

    public JobById(Job job, boolean isApplied, String applicationStatus) {
        this.job = job;
        this.isApplied = isApplied;
        this.applicationStatus = applicationStatus;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
