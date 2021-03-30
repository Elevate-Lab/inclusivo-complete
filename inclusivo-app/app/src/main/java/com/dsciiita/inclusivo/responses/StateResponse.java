package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.Country;
import com.google.gson.annotations.SerializedName;

public class StateResponse {

    @SerializedName("id")
    int id;

    @SerializedName("country")
    Country county;

    @SerializedName("name")
    String name;

    public StateResponse(int id, Country county, String name) {
        this.id = id;
        this.county = county;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country getCounty() {
        return county;
    }

    public void setCounty(Country county) {
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
