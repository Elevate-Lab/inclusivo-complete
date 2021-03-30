package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

public class GetCompanyDetails {
    @SerializedName("company")
    Company company;

    @SerializedName("is_following")
    boolean isFollowing;


    public GetCompanyDetails(Company company, boolean isFollowing) {
        this.company = company;
        this.isFollowing = isFollowing;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
