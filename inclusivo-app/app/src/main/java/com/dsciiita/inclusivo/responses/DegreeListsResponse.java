package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Degree;
import com.google.gson.annotations.SerializedName;

public class DegreeListsResponse {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    Degree[] data;


    public DegreeListsResponse(String status, String message, Degree[] data) {
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

    public Degree[] getData() {
        return data;
    }

    public void setData(Degree[] data) {
        this.data = data;
    }
}
