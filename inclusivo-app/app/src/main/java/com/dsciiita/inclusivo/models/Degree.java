package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Degree implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("degree_name")
    @Expose
    private String name;
    @SerializedName("degree_type")
    @Expose
    private String type;
    @SerializedName("specialization")
    @Expose
    private String specialization;

    @SerializedName("removed")
    @Expose
    private boolean isRemoved;

    public Degree(Integer id, String name, String type, String specialization, boolean isRemoved) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.specialization = specialization;
        this.isRemoved = isRemoved;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}