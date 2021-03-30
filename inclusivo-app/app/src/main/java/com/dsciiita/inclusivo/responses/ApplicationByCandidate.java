package com.dsciiita.inclusivo.responses;

import java.io.Serializable;
import java.util.List;

import com.dsciiita.inclusivo.models.JobApplicationByCandidateData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationByCandidate implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<JobApplicationByCandidateData> data = null;
    private final static long serialVersionUID = 4587593836363842366L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ApplicationByCandidate() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public ApplicationByCandidate(String status, String message, List<JobApplicationByCandidateData> data) {
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

    public List<JobApplicationByCandidateData> getData() {
        return data;
    }

    public void setData(List<JobApplicationByCandidateData> data) {
        this.data = data;
    }

}