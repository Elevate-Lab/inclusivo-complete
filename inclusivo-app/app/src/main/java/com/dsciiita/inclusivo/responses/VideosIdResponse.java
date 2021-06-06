package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Video;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VideosIdResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Video data;
    private final static long serialVersionUID = -8815769333164360961L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VideosIdResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public VideosIdResponse(String status, String message, Video data) {
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

    public Video getData() {
        return data;
    }

    public void setData(Video data) {
        this.data = data;
    }

}