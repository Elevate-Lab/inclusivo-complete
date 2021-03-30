package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import java.util.List;

import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.LocationResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations
        .SerializedName;

public class Job implements Serializable
{
    @SerializedName("company_id")
    private Integer companyId;

    @SerializedName("id")
    private Integer jobId;

    @SerializedName("company")
    private Company company;

    @SerializedName("title")
    private String title;

    @SerializedName("short_code")
    private String shortCode;

    @SerializedName("locations")
    private List<City> cities = null;

    @SerializedName("accepted_locations")
    private List<CityResponse> locations = null;

    @SerializedName("tags")
    private List<Diversity> diversities = null;

    @SerializedName("job_role")
    private String jobRole;

    @SerializedName("description")
    private String description;

    @SerializedName("degrees")
    private List<Degree> degrees = null;

    @SerializedName("job_type")
    private String jobType;

    @SerializedName("is_apply_here")
    private Boolean isApplyHere;

    @SerializedName("last_date")
    private String lastDate;

    @SerializedName("min_exp")
    private Integer minExp;

    @SerializedName("max_exp")
    private Integer maxExp;

    @SerializedName("selection_process")
    private String selectionProcess;

    @SerializedName("min_sal")
    private String minSal;

    @SerializedName("max_sal")
    private String maxSal;

    @SerializedName("display_salary")
    private Boolean displaySalary;

    @SerializedName("status")
    private String status;

    @SerializedName("vacancies")
    private Integer vacancies;

    @SerializedName("apply_url")
    private String applyUrl;

    @SerializedName("published_date")
    private String publishDate;

    @SerializedName("posted_on")
    private String postedOn;

    @SerializedName("created_on")
    private String createdOn;

    @SerializedName("is_liked")
    private boolean isLiked;


    public Job(Integer companyId, String title, String shortCode, List<City> cities,
               List<Diversity> diversities, String jobRole, String description, List<Degree> degrees,
               String jobType, Boolean isApplyHere, String applyUrl, String lastDate, Integer minExp,
               Integer maxExp, String selectionProcess, String minSal, String maxSal,
               Boolean displaySalary, String status, Integer vacancies) {
        this.companyId = companyId;
        this.title = title;
        this.shortCode = shortCode;
        this.cities = cities;
        this.diversities = diversities;
        this.jobRole = jobRole;
        this.description = description;
        this.degrees = degrees;
        this.jobType = jobType;
        this.isApplyHere = isApplyHere;
        this.applyUrl = applyUrl;
        this.lastDate = lastDate;
        this.minExp = minExp;
        this.maxExp = maxExp;
        this.selectionProcess = selectionProcess;
        this.minSal = minSal;
        this.maxSal = maxSal;
        this.displaySalary = displaySalary;
        this.status = status;
        this.vacancies = vacancies;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Diversity> getDiversities() {
        return diversities;
    }

    public void setDiversities(List<Diversity> diversities) {
        this.diversities = diversities;
    }

    public Boolean getApplyHere() {
        return isApplyHere;
    }

    public void setApplyHere(Boolean applyHere) {
        isApplyHere = applyHere;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public List<CityResponse> getLocations() {
        return locations;
    }

    public void setLocations(List<CityResponse> locations) {
        this.locations = locations;
    }

    public List<Diversity> getTags() {
        return diversities;
    }

    public void setTags(List<Diversity> diversities) {
        this.diversities = diversities;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Boolean getIsApplyHere() {
        return isApplyHere;
    }

    public void setIsApplyHere(Boolean isApplyHere) {
        this.isApplyHere = isApplyHere;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public Integer getMinExp() {
        return minExp;
    }

    public void setMinExp(Integer minExp) {
        this.minExp = minExp;
    }

    public Integer getMaxExp() {
        return maxExp;
    }

    public void setMaxExp(Integer maxExp) {
        this.maxExp = maxExp;
    }

    public String getSelectionProcess() {
        return selectionProcess;
    }

    public void setSelectionProcess(String selectionProcess) {
        this.selectionProcess = selectionProcess;
    }

    public String getMinSal() {
        return minSal;
    }

    public void setMinSal(String minSal) {
        this.minSal = minSal;
    }

    public String getMaxSal() {
        return maxSal;
    }

    public void setMaxSal(String maxSal) {
        this.maxSal = maxSal;
    }

    public Boolean getDisplaySalary() {
        return displaySalary;
    }

    public void setDisplaySalary(Boolean displaySalary) {
        this.displaySalary = displaySalary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

}