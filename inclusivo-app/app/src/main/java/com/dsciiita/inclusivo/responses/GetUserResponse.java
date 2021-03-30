package com.dsciiita.inclusivo.responses;

import java.io.Serializable;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.dsciiita.inclusivo.responses.UserTypeResponse;

public class GetUserResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private UserTypeResponse data;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetUserResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public GetUserResponse(String status, String message, UserTypeResponse data) {
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

    public UserTypeResponse getData() {
        return data;
    }

    public void setData(UserTypeResponse data) {
        this.data = data;
    }

}