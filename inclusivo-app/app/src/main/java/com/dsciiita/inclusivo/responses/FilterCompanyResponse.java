package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.FilterCompanyData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FilterCompanyResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private FilterCompanyData data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public FilterCompanyResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public FilterCompanyResponse(String status, String message, FilterCompanyData data) {
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

    public FilterCompanyData getData() {
        return data;
    }

    public void setData(FilterCompanyData data) {
        this.data = data;
    }

}