package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterCompanyData {

    @SerializedName("companies")
    private List<Company> companies;

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("page_number")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;

    public FilterCompanyData(List<Company> companies, int totalCount, int pageNo, int pageSize) {
        this.companies = companies;
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }


    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
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
