package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

public class EmailResponse {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    EmailResponseData data;


    public EmailResponse(String status, String message, EmailResponseData data) {
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

    public EmailResponseData getData() {
        return data;
    }

    public void setData(EmailResponseData data) {
        this.data = data;
    }
}
