package com.dsciiita.inclusivo.responses;

import java.io.Serializable;

import com.dsciiita.inclusivo.models.FilterJobData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class    FilterJobResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private FilterJobData data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public FilterJobResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public FilterJobResponse(String status, String message, FilterJobData data) {
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

    public FilterJobData getData() {
        return data;
    }

    public void setData(FilterJobData data) {
        this.data = data;
    }

}