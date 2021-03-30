package com.dsciiita.inclusivo.models;

import com.dsciiita.inclusivo.responses.CityResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserCandidate extends User {

    @SerializedName("is_active")
    boolean isActive;
    @SerializedName("user")
    User user;
    @SerializedName("nationality")
    String nationality;
    @SerializedName("job_role")
    String jobRole;
    @SerializedName("profile_description")
    String profileDesc;
    @SerializedName("year")
    String year;
    @SerializedName("month")
    String month;
    @SerializedName("registered_via")
    String registeredVia;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("alternate_mobile")
    String alternateMobile;
    @SerializedName("resume_link")
    String resumeLink;

    @SerializedName("is_relocation")
    boolean isRelocation;

    @SerializedName("city") City city;
    @SerializedName("country") Country country;
    @SerializedName("state") State state;

    @SerializedName("diversity_tags")
    ArrayList<Diversity> diversity;

    @SerializedName("preferred_city")
    ArrayList<CityResponse> prefCities;

    @SerializedName("linkedIn")
    String linkedIn;

    @SerializedName("github")
    String github;

    @SerializedName("twitter")
    String twitter;


    public UserCandidate(){

    }

    public UserCandidate(User user, String nationality, String jobRole,
                         String profileDesc, String year, String month, String registeredVia,
                         City city, Country country, State state, boolean isRelocation, ArrayList<Diversity> diversity,
                         ArrayList<CityResponse> prefCities, String mobile, String alternateMobile, String resumeLink) {
        super(user.getProfileUrl(), user.getFirstName(), user.getLastName(), user.isEmployer(),
                user.getEmail(), user.getDob(), user.getGender());
        this.nationality = nationality;
        this.jobRole = jobRole;
        this.profileDesc = profileDesc;
        this.year = year;
        this.month = month;
        this.registeredVia = registeredVia;
        this.city = city;
        this.country = country;
        this.state = state;
        this.isRelocation = isRelocation;
        this.diversity = diversity;
        this.mobile = mobile;
        this.alternateMobile = alternateMobile;
        this.resumeLink = resumeLink;
        this.prefCities = prefCities;
    }

    public boolean isRelocation() {
        return isRelocation;
    }

    public void setRelocation(boolean relocation) {
        isRelocation = relocation;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public boolean getIsRelocation() {
        return isRelocation;
    }

    public void setIsRelocation(boolean isRelocation) {
        this.isRelocation = isRelocation;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<CityResponse> getPrefCities() {
        return prefCities;
    }

    public void setPrefCities(ArrayList<CityResponse> prefCities) {
        this.prefCities = prefCities;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public void setResumeLink(String resumeLink) {
        this.resumeLink = resumeLink;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ArrayList<Diversity> getDiversity() {
        return diversity;
    }

    public void setDiversity(ArrayList<Diversity> diversity) {
        this.diversity = diversity;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRegisteredVia() {
        return registeredVia;
    }

    public void setRegisteredVia(String registeredVia) {
        this.registeredVia = registeredVia;
    }
}
