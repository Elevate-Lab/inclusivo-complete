package com.dsciiita.inclusivo.models;

public class Event {

    private int id;
    private String title, description, location, time, thumbnail_url;

    public Event(int id, String title, String description, String location, String time, String thumbnail_url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.time = time;
        this.thumbnail_url = thumbnail_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}
