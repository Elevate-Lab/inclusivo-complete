package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobFilterObject implements Serializable
{

    @SerializedName("sortField")
    @Expose
    private String sortField;

    @SerializedName("sortOrder")
    @Expose
    private String sortOrder;

    @SerializedName("search")
    @Expose
    private List<JobFilterSearch> jobFilterSearches = null;

    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;

    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    /**
     * No args constructor for use in serialization
     *
     */
    public JobFilterObject() {
    }

    /**
     *
     * @param jobFilterSearches
     * @param pageNumber
     * @param sortOrder
     * @param sortField
     * @param pageSize
     */
    public JobFilterObject(String sortField, String sortOrder, List<JobFilterSearch> jobFilterSearches, Integer pageNumber, Integer pageSize) {
        super();
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.jobFilterSearches = jobFilterSearches;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<JobFilterSearch> getJobFilterSearches() {
        return jobFilterSearches;
    }

    public void setJobFilterSearches(List<JobFilterSearch> jobFilterSearches) {
        this.jobFilterSearches = jobFilterSearches;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}