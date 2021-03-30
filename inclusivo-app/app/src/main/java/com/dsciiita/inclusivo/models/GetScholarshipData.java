package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetScholarshipData implements Serializable
{

    @SerializedName("scholarships")
    @Expose
    private List<Scholarship> scholarships = null;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("page_number")
    @Expose
    private Integer pageNumber;
    @SerializedName("page_size")
    @Expose
    private Integer pageSize;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetScholarshipData() {
    }

    /**
     *
     * @param pageNumber
     * @param scholarships
     * @param pageSize
     * @param totalCount
     */
    public GetScholarshipData(List<Scholarship> scholarships, Integer totalCount, Integer pageNumber, Integer pageSize) {
        super();
        this.scholarships = scholarships;
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<Scholarship> getScholarships() {
        return scholarships;
    }

    public void setScholarships(List<Scholarship> scholarships) {
        this.scholarships = scholarships;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
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