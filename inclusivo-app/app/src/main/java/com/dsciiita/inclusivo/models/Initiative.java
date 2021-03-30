package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Initiative implements Serializable
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
    private Integer companyID;

    /**
     * No args constructor for use in serialization
     *
     */
    public Initiative() {
    }

    /**
     *
     * @param name
     * @param description
     */
    public Initiative(String description, String name) {
        super();
        this.description = description;
        this.name = name;
    }

    public Initiative(Integer id, String description, String name, Boolean removed, Integer companyID) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.removed = removed;
        this.companyID = companyID;
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

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
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