package com.dsciiita.inclusivo.responses;

import java.io.Serializable;
import java.util.List;

import com.dsciiita.inclusivo.models.JobStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationStatusResponseByID implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<JobStatus> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ApplicationStatusResponseByID() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public ApplicationStatusResponseByID(String status, String message, List<JobStatus> data) {
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

    public List<JobStatus> getData() {
        return data;
    }

    public void setData(List<JobStatus> data) {
        this.data = data;
    }

}