package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.FilterScholarshipData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FilterScholarshipsResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private FilterScholarshipData data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public FilterScholarshipsResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public FilterScholarshipsResponse(String status, String message, FilterScholarshipData data) {
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

    public FilterScholarshipData getData() {
        return data;
    }

    public void setData(FilterScholarshipData data) {
        this.data = data;
    }

}