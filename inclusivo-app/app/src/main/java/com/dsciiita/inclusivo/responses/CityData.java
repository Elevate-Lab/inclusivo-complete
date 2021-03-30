package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.City;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityData {

    @SerializedName("cities")
    private List<CityResponse> cities = null;


    public List<CityResponse> getCities() {
        return cities;
    }

    public void setCities(List<CityResponse> cities) {
        this.cities = cities;
    }

}
