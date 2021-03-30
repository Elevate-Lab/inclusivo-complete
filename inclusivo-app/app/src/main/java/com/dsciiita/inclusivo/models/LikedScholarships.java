package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LikedScholarships implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("candidate")
    @Expose
    private UserCandidate candidate;

    @SerializedName("scholarship")
    @Expose
    private Scholarship scholarship;

    @SerializedName("liked_date")
    @Expose
    private String likedDate;

    @SerializedName("removed")
    @Expose
    private Boolean removed;

    public LikedScholarships() {
    }

    /**
     * @param candidate
     * @param scholarship
     * @param removed
     * @param id
     * @param likedDate
     */
    public LikedScholarships(Integer id, UserCandidate candidate, Scholarship scholarship, String likedDate, Boolean removed) {
        super();
        this.id = id;
        this.candidate = candidate;
        this.scholarship = scholarship;
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

    public Scholarship getScholarship() {
        return scholarship;
    }

    public void setScholarship(Scholarship scholarship) {
        this.scholarship = scholarship;
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
