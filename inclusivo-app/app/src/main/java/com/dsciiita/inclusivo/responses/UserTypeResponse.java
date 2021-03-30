package com.dsciiita.inclusivo.responses;

import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.google.gson.annotations.SerializedName;

public class UserTypeResponse {

    @SerializedName("is_employer")
    boolean isEmployer;
    @SerializedName("employer")
    UserEmployee employee;
    @SerializedName("candidate")
    UserCandidate candidate;

    @SerializedName("is_update_profile")
    boolean isUpdated;

    public UserTypeResponse(UserEmployee employee) {
        this.employee = employee;
    }

    public boolean isEmployer() {
        return isEmployer;
    }

    public void setEmployer(boolean employer) {
        isEmployer = employer;
    }

    public UserEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(UserEmployee employee) {
        this.employee = employee;
    }

    public UserCandidate getCandidate() {
        return candidate;
    }

    public void setCandidate(UserCandidate candidate) {
        this.candidate = candidate;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}

