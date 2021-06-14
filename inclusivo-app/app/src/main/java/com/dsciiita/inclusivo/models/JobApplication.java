package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobApplication implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("job")
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

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("removed")
    @Expose
    private Boolean removed;



    /**
     * No args constructor for use in serialization
     *
     */
    public JobApplication() {
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
    public JobApplication(Integer id, UserCandidate candidate, String status, String applicationDate, Boolean removed, Job job) {
        super();
        this.id = id;
        this.candidate = candidate;
        this.status = status;
        this.applicationDate = applicationDate;
        this.removed = removed;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}