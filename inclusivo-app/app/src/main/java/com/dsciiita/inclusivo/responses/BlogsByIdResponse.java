package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Blog;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BlogsByIdResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Blog data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public BlogsByIdResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public BlogsByIdResponse(String status, String message, Blog data) {
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

    public Blog getData() {
        return data;
    }

    public void setData(Blog data) {
        this.data = data;
    }

}