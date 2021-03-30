package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class DefaultResponse {

    @SerializedName("error")
    private boolean err;

    @SerializedName("message")
    private String msg;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private JSONObject data;

    public DefaultResponse(boolean err, String msg, JSONObject data) {
        this.err = err;
        this.msg = msg;
        this.data = data;
    }

    public DefaultResponse(boolean err, String msg, String status, JSONObject data) {
        this.err = err;
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public boolean isErr() {
        return err;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getData() {
        return data;
    }
}
