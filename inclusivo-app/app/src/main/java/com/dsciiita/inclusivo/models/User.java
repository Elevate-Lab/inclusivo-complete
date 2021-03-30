package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("is_employer")
    private boolean isEmployer;

    @SerializedName("email")
    private String email;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    @SerializedName("photo_url")
    private String profileUrl;

    private String key;
    private boolean isCandidate;

    public User(){

    }

    public User(String profileUrl, String firstName, String lastName, boolean isEmployer, String email, String dob, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEmployer = isEmployer;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.profileUrl = profileUrl;
    }


    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }


    public boolean isCandidate() {
        return isCandidate;
    }

    public void setCandidate(boolean candidate) {
        isCandidate = candidate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEmployer() {
        return isEmployer;
    }

    public void setEmployer(boolean employer) {
        isEmployer = employer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
