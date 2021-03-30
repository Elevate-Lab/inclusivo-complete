package com.dsciiita.inclusivo.responses;

import java.io.Serializable;

import com.dsciiita.inclusivo.models.GetScholarshipData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetScholarshipResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private GetScholarshipData data;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetScholarshipResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public GetScholarshipResponse(String status, String message, GetScholarshipData data) {
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

    public GetScholarshipData getData() {
        return data;
    }

    public void setData(GetScholarshipData data) {
        this.data = data;
    }

}