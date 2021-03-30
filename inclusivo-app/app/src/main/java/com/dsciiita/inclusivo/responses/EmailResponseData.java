package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

public class EmailResponseData {

    @SerializedName("is_user")
    String isUser;

    public EmailResponseData(String isUser) {
        this.isUser = isUser;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }
}
