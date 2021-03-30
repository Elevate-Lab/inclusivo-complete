package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.FollowedCompanyData;
import com.dsciiita.inclusivo.models.FollowedCompanyData;
import com.dsciiita.inclusivo.models.Job;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FollowedCompaniesResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<FollowedCompanyData> data;

    /**
     * No args constructor for use in serialization
     *
     */
    public FollowedCompaniesResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public FollowedCompaniesResponse(String status, String message, List<FollowedCompanyData> data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FollowedCompanyData> getData() {
        return data;
    }

    public void setData(List<FollowedCompanyData> data) {
        this.data = data;
    }

}