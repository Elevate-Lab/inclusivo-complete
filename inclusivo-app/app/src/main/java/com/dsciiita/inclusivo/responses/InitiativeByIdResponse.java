package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Initiative;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InitiativeByIdResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Initiative data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public InitiativeByIdResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public InitiativeByIdResponse(String status, String message, Initiative data) {
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

    public Initiative getData() {
        return data;
    }

    public void setData(Initiative data) {
        this.data = data;
    }

}