package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Scholarship;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CompanyScholarshipResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Scholarship> data = null;

    /**
     * No args constructor for use in serialization
     */
    public CompanyScholarshipResponse() {
    }

    /**
     * @param data
     * @param message
     * @param status
     */
    public CompanyScholarshipResponse(String status, String message, List<Scholarship> data) {
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

    public List<Scholarship> getData() {
        return data;
    }

    public void setData(List<Scholarship> data) {
        this.data = data;
    }
}
