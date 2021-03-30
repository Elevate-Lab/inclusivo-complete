package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Scholarship;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScholarshipByIDResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Scholarship data;

    /**
     * No args constructor for use in serialization
     */
    public ScholarshipByIDResponse() {
    }

    /**
     * @param data
     * @param message
     * @param status
     */

    public ScholarshipByIDResponse(String status, String message, Scholarship data) {
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

    public Scholarship getData() {
        return data;
    }

    public void setData(Scholarship data) {
        this.data = data;
    }
}
