package com.dsciiita.inclusivo.responses;

import java.io.Serializable;
import java.util.List;

import com.dsciiita.inclusivo.models.LikedJobs;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedJobsResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<LikedJobs> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public LikedJobsResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public LikedJobsResponse(String status, String message, List<LikedJobs> data) {
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

    public List<LikedJobs> getData() {
        return data;
    }

    public void setData(List<LikedJobs> data) {
        this.data = data;
    }

}