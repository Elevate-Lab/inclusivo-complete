package com.dsciiita.inclusivo.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobFilterSearch implements Serializable
{

    @SerializedName("searchText")
    @Expose
    private String searchText;

    @SerializedName("searchField")
    @Expose
    private String searchField;

    @SerializedName("searchType")
    @Expose
    private String searchType;


    /**
     * No args constructor for use in serialization
     *
     */
    public JobFilterSearch() {
    }

    /**
     *
     * @param searchText
     * @param searchField
     * @param searchType
     */
    public JobFilterSearch(String searchText, String searchField, String searchType) {
        super();
        this.searchText = searchText;
        this.searchField = searchField;
        this.searchType = searchType;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

}