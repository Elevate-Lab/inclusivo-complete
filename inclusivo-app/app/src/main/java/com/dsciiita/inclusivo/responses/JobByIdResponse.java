package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.JobById;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobByIdResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private JobById data;

    /**
     * No args constructor for use in serialization
     *
     */
    public JobByIdResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public JobByIdResponse(String status, String message, JobById data) {
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

    public JobById getData() {
        return data;
    }

    public void setData(JobById data) {
        this.data = data;
    }

}