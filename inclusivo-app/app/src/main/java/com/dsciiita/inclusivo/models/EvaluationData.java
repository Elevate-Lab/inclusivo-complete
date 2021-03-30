package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EvaluationData implements Serializable
{

    @SerializedName("matching_skills")
    @Expose
    private List<String> matchingSkills = null;
    @SerializedName("matching_rate")
    @Expose
    private Double matchingRate;

    /**
     * No args constructor for use in serialization
     *
     */
    public EvaluationData() {
    }

    /**
     *
     * @param matchingRate
     * @param matchingSkills
     */
    public EvaluationData(List<String> matchingSkills, Double matchingRate) {
        super();
        this.matchingSkills = matchingSkills;
        this.matchingRate = matchingRate;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public Double getMatchingRate() {
        return matchingRate;
    }

    public void setMatchingRate(Double matchingRate) {
        this.matchingRate = matchingRate;
    }

}