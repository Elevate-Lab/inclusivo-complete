package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscribedCompanyDetails {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("company")
    @Expose
    private Company company;

    @SerializedName("subscribed_date")
    @Expose
    private String subscribedDate;

    @SerializedName("removed")
    @Expose
    private Boolean removed;

    @SerializedName("candidate")
    @Expose
    private Integer candidate;

    /**
     * No args constructor for use in serialization
     *
     */
    public SubscribedCompanyDetails() {
    }

    /**
     *
     * @param subscribedDate
     * @param candidate
     * @param removed
     * @param company
     * @param id
     */
    public SubscribedCompanyDetails(Integer id, Company company, String subscribedDate, Boolean removed, Integer candidate) {
        super();
        this.id = id;
        this.company = company;
        this.subscribedDate = subscribedDate;
        this.removed = removed;
        this.candidate = candidate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(String subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Integer getCandidate() {
        return candidate;
    }

    public void setCandidate(Integer candidate) {
        this.candidate = candidate;
    }
}
