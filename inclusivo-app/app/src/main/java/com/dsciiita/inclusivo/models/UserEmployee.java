package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

public class UserEmployee extends User {

    @SerializedName("company_id")
    int companyID;

    @SerializedName("user")
    User user;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("company")
    Company company;

    @SerializedName("registered_via")
    String regVia;

    @SerializedName("alternate_mobile")
    String alternateMobile;


    public UserEmployee(User user, int companyID, String mobile, String regVia, String alternateMobile) {
        super(user.getProfileUrl(), user.getFirstName(), user.getLastName(), user.isEmployer(),
                user.getEmail(), user.getDob(), user.getGender());
        this.companyID = companyID;
        this.mobile = mobile;
        this.regVia = regVia;
        this.alternateMobile = alternateMobile;
    }


    public UserEmployee(String profileUrl, String firstName, String lastName, boolean isEmployer, String email, String dob, String gender, int companyID, String mobile, Company company, String regVia, String alternateMobile) {
        super(profileUrl, firstName, lastName, isEmployer, email, dob, gender);
        this.companyID = companyID;
        this.mobile = mobile;
        this.company = company;
        this.regVia = regVia;
        this.alternateMobile = alternateMobile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegVia() {
        return regVia;
    }

    public void setRegVia(String regVia) {
        this.regVia = regVia;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }
}
