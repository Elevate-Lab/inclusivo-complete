package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Scholarship implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("company")
    @Expose
    private Company company;

    @SerializedName("company_id")
    @Expose
    private int companyId;

    @SerializedName("short_code")
    @Expose
    private String shortCode;

    @SerializedName("tags")
    @Expose
    private List<Diversity> tags = null;

    @SerializedName("posted_on")
    @Expose
    private String postedOn;

    @SerializedName("is_liked")
    @Expose
    private boolean isLiked;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("last_date")
    @Expose
    private String lastDate;

    @SerializedName("is_apply_here")
    @Expose
    private Boolean isApplyHere;

    @SerializedName("selection_process")
    @Expose
    private String selectionProcess;

    @SerializedName("vacancies")
    @Expose
    private Integer vacancies;

    @SerializedName("apply_url")
    @Expose
    private String applyUrl;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("accepted_degrees")
    @Expose
    private List<Degree> acceptedDegrees = null;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    /**
     * No args constructor for use in serialization
     */
    public Scholarship() {
    }

    /**
     * @param description
     * @param isApplyHere
     * @param title
     * @param shortCode
     * @param tags
     * @param lastDate
     */


    public Scholarship(String title, int companyId, String shortCode, List<Diversity> tags,
                       String description, String lastDate, Boolean isApplyHere,
                       String selectionProcess, Integer vacancies, String applyUrl,
                       List<Degree> acceptedDegrees, String phoneNumber, String status) {
        this.title = title;
        this.companyId = companyId;
        this.shortCode = shortCode;
        this.tags = tags;
        this.description = description;
        this.lastDate = lastDate;
        this.isApplyHere = isApplyHere;
        this.selectionProcess = selectionProcess;
        this.vacancies = vacancies;
        this.applyUrl = applyUrl;
        this.status = status;
        this.acceptedDegrees = acceptedDegrees;
        this.phoneNumber = phoneNumber;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getSelectionProcess() {
        return selectionProcess;
    }

    public void setSelectionProcess(String selectionProcess) {
        this.selectionProcess = selectionProcess;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Degree> getAcceptedDegrees() {
        return acceptedDegrees;
    }

    public void setAcceptedDegrees(List<Degree> acceptedDegrees) {
        this.acceptedDegrees = acceptedDegrees;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Boolean getApplyHere() {
        return isApplyHere;
    }

    public void setApplyHere(Boolean applyHere) {
        isApplyHere = applyHere;
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

    public List<Diversity> getTags() {
        return tags;
    }

    public void setTags(List<Diversity> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public Boolean getIsApplyHere() {
        return isApplyHere;
    }

    public void setIsApplyHere(Boolean isApplyHere) {
        this.isApplyHere = isApplyHere;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}