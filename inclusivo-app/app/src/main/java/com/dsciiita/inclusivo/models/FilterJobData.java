package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterJobData {

    @SerializedName("jobs")
    private List<Job> jobs;

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("page_number")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;

    public FilterJobData(List<Job> jobs, int totalCount, int pageNo, int pageSize) {
        this.jobs = jobs;
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }


    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
