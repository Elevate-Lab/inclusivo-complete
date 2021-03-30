package com.dsciiita.inclusivo.responses;

import java.io.Serializable;

import com.dsciiita.inclusivo.models.JobApplication;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationByIdResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private JobApplication data;

    /**
     * No args constructor for use in serialization
     *
     */
    public ApplicationByIdResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public ApplicationByIdResponse(String status, String message, JobApplication data) {
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

    public JobApplication getData() {
        return data;
    }

    public void setData(JobApplication data) {
        this.data = data;
    }

}