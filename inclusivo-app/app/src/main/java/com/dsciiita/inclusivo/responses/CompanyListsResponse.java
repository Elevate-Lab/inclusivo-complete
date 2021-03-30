package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

import com.dsciiita.inclusivo.models.Company;

public class CompanyListsResponse {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    Company[] data;


    public CompanyListsResponse(String status, String message, Company[] data) {
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

    public Company[] getData() {
        return data;
    }

    public void setData(Company[] data) {
        this.data = data;
    }
}
