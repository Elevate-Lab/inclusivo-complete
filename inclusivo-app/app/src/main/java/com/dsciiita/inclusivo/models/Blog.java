package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blog implements Serializable
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("credits")
    @Expose
    private String credits;
    @SerializedName("author_credits")
    @Expose
    private String authorCredits;
    @SerializedName("tags")
    @Expose
    private List<Diversity> tags = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Blog() {
    }

    /**
     *
     * @param photoUrl
     * @param credits
     * @param name
     * @param description
     * @param tags
     */
    public Blog(String description, String photoUrl, String name, String credits, List<Diversity> tags) {
        super();
        this.description = description;
        this.photoUrl = photoUrl;
        this.name = name;
        this.credits = credits;
        this.tags = tags;
    }

    public String getAuthorCredits() {
        return authorCredits;
    }

    public void setAuthorCredits(String authorCredits) {
        this.authorCredits = authorCredits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public List<Diversity> getTags() {
        return tags;
    }

    public void setTags(List<Diversity> tags) {
        this.tags = tags;
    }

}