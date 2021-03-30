package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

public class Company {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("title")
    String title;
    @SerializedName("short_code")
    String shortCode;
    @SerializedName("phone_number")
    String phoneNumber;
    @SerializedName("size")
    String size;
    @SerializedName("profile")
    String profile;
    @SerializedName("email")
    String email;
    @SerializedName("description")
    String description;
    @SerializedName("address")
    String address;
    @SerializedName("website")
    String website;
    @SerializedName("twitter")
    String twitter;
    @SerializedName("facebook")
    String facebook;
    @SerializedName("linkedin")
    String linkedin;
    @SerializedName("instagram")
    String instagram;
    @SerializedName("logo_url")
    String logoUrl;

    @SerializedName("jobs_count")
    int jobCount;

    public Company(int id, String name, String title, String shortCode,
                   String phoneNumber, String size, String profile,
                   String email, String description, String address,
                   String website, String twitter, String facebook,
                   String linkedin, String instagram, String logoUrl) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.shortCode = shortCode;
        this.phoneNumber = phoneNumber;
        this.size = size;
        this.profile = profile;
        this.email = email;
        this.description = description;
        this.address = address;
        this.website = website;
        this.twitter = twitter;
        this.facebook = facebook;
        this.linkedin = linkedin;
        this.instagram = instagram;
        this.logoUrl = logoUrl;
    }


    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public int getJobCount() {
        return jobCount;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }
}
