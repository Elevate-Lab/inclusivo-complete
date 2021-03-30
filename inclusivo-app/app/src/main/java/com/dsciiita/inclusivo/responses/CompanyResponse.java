package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.GetCompanyDetails;
import com.google.gson.annotations.SerializedName;

public class CompanyResponse {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    GetCompanyDetails data;


    public CompanyResponse(String status, String message, GetCompanyDetails data) {
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

    public GetCompanyDetails getData() {
        return data;
    }

    public void setData(GetCompanyDetails data) {
        this.data = data;
    }
}
