package com.dsciiita.inclusivo.models;

import com.google.gson.annotations.SerializedName;

public class JobFilterBody {
    @SerializedName("filters")
    JobFilterObject jobFilterObject;

    public JobFilterBody(JobFilterObject jobFilterObject) {
        this.jobFilterObject = jobFilterObject;
    }

    public JobFilterObject getJobFilterObject() {
        return jobFilterObject;
    }

    public void setJobFilterObject(JobFilterObject jobFilterObject) {
        this.jobFilterObject = jobFilterObject;
    }
}
