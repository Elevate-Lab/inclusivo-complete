package com.dsciiita.inclusivo.models;

import java.io.Serializable;

import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedJobs implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("candidate")
    @Expose
    private UserCandidate candidate;

    @SerializedName("job_post")
    @Expose
    private Job jobPost;

    @SerializedName("liked_date")
    @Expose
    private String likedDate;

    @SerializedName("removed")
    @Expose
    private Boolean removed;

    public LikedJobs() {
    }

    /**
     * @param candidate
     * @param jobPost
     * @param removed
     * @param id
     * @param likedDate
     */
    public LikedJobs(Integer id, UserCandidate candidate, Job jobPost, String likedDate, Boolean removed) {
        super();
        this.id = id;
        this.candidate = candidate;
        this.jobPost = jobPost;
        this.likedDate = likedDate;
        this.removed = removed;
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

    public Job getJobPost() {
        return jobPost;
    }

    public void setJobPost(Job jobPost) {
        this.jobPost = jobPost;
    }

    public String getLikedDate() {
        return likedDate;
    }

    public void setLikedDate(String likedDate) {
        this.likedDate = likedDate;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }
}
