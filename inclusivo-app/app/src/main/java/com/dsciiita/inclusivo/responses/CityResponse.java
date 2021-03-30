package com.dsciiita.inclusivo.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityResponse implements Serializable {


    @SerializedName("id")
    int id;

    @SerializedName("state_name")
    String stateName;

    @SerializedName("name")
    String name;

    @SerializedName("country_name")
    String countryName;

    @SerializedName("state_id")
    int stateId;

    @SerializedName("country_id")
    int countryId;

    @SerializedName("state")
    StateResponse state;

    public CityResponse(int id, StateResponse state, String name) {
        this.id = id;
        this.state = state;
        this.name = name;
    }


    public CityResponse(int id, String stateName, String name, String countryName, int stateId, int countryId) {
        this.id = id;
        this.stateName = stateName;
        this.name = name;
        this.countryName = countryName;
        this.stateId = stateId;
        this.countryId = countryId;
    }

    public StateResponse getState() {
        return state;
    }

    public void setState(StateResponse state) {
        this.state = state;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
