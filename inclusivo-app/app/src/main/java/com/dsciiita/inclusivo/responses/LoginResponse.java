package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("key")
    String key;

    public LoginResponse(String id) {
        this.key = id;
    }

    public String getId() {
        return key;
    }

    public void setId(String id) {
        this.key = id;
    }

}
