package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobApplicationByCandidateData implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job")
    @Expose
    private Job job;
    @SerializedName("candidate")
    @Expose
    private UserCandidate candidate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("application_date")
    @Expose
    private String applicationDate;
    @SerializedName("removed")
    @Expose
    private Boolean removed;
    private final static long serialVersionUID = -4028630403637733484L;

    /**
     * No args constructor for use in serialization
     *
     */
    public JobApplicationByCandidateData() {
    }

    /**
     *
     * @param candidate
     * @param removed
     * @param id
     * @param job
     * @param status
     * @param applicationDate
     */
    public JobApplicationByCandidateData(Integer id, Job job, UserCandidate candidate, String status, String applicationDate, Boolean removed) {
        super();
        this.id = id;
        this.job = job;
        this.candidate = candidate;
        this.status = status;
        this.applicationDate = applicationDate;
        this.removed = removed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public UserCandidate getCandidate() {
        return candidate;
    }

    public void setCandidate(UserCandidate candidate) {
        this.candidate = candidate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

}