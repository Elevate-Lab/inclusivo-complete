package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationResponse {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    private List<CityResponse> cities = null;


    public LocationResponse(String status, String message, List<CityResponse> cities) {
        this.status = status;
        this.message = message;
        this.cities = cities;
    }

    public List<CityResponse> getCities() {
        return cities;
    }

    public void setCities(List<CityResponse> cities) {
        this.cities = cities;
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
}
