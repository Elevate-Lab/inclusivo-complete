
package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobStatus implements Serializable
{

    @SerializedName("id")
    private int id;

    @SerializedName("job_application")
    private int application_id;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("recruiter_notes")
    @Expose
    private String recruiterNotes;

    /**
     * No args constructor for use in serialization
     *
     */
    public JobStatus() {
    }

    /**
     *
     * @param recruiterNotes
     * @param message
     * @param status
     */
    public JobStatus(String status, String message, String recruiterNotes) {
        super();
        this.status = status;
        this.message = message;
        this.recruiterNotes = recruiterNotes;
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

    public String getRecruiterNotes() {
        return recruiterNotes;
    }

    public void setRecruiterNotes(String recruiterNotes) {
        this.recruiterNotes = recruiterNotes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
    }
}