package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Story implements Serializable
{
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("removed")
    @Expose
    private Boolean removed;

    @SerializedName("company")
    @Expose
    private Company company;

    @SerializedName("photo_url")
    @Expose
    private String photoUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public Story() {
    }

    public Story(String description, String name, String photoUrl) {
        this.description = description;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    /**
     *
     * @param name
     * @param description
     */


    public Story(Integer id, String description, String name, Boolean removed, Company companyID) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.removed = removed;
        this.company = companyID;
    }

    public Story(Integer id, String description, String name, Boolean removed, Company companyID, String photoUrl) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.removed = removed;
        this.company = companyID;
        this.photoUrl = photoUrl;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Company getCompanyID() {
        return company;
    }

    public void setCompanyID(Company companyID) {
        this.company = companyID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}