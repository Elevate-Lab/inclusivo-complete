package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Story;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IDStoryResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Story data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public IDStoryResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public IDStoryResponse(String status, String message, Story data) {
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

    public Story getData() {
        return data;
    }

    public void setData(Story data) {
        this.data = data;
    }

}