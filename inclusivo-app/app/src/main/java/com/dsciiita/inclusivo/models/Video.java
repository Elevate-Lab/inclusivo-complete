package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tags")
    @Expose
    private List<Diversity> tags = null;
    @SerializedName("credits")
    @Expose
    private String credits;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("author_credits")
    @Expose
    private String authorCredits;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("removed")
    @Expose
    private Boolean removed;
    @SerializedName("video_link")
    @Expose
    private String videoLink;

    /**
     * No args constructor for use in serialization
     *
     */
    public Video() {
    }

    /**
     *
     * @param removed
     * @param credits
     * @param name
     * @param authorCredits
     * @param description
     * @param videoLink
     * @param id
     * @param tags
     */
    public Video(Integer id, List<Diversity> tags, String credits, String name, String authorCredits, String description, Boolean removed, String videoLink) {
        super();
        this.id = id;
        this.tags = tags;
        this.credits = credits;
        this.name = name;
        this.authorCredits = authorCredits;
        this.description = description;
        this.removed = removed;
        this.videoLink = videoLink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Diversity> getTags() {
        return tags;
    }

    public void setTags(List<Diversity> tags) {
        this.tags = tags;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorCredits() {
        return authorCredits;
    }

    public void setAuthorCredits(String authorCredits) {
        this.authorCredits = authorCredits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

}