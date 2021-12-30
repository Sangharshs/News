package com.example.sarkaribook.Model;

import com.google.gson.annotations.SerializedName;

public class Subcategory {

    @SerializedName("sub_category_name")
    private String subcategoryName;
    private String bookDownloadUrl;

    @SerializedName("id")
    public int id;


    public Subcategory(String subcategoryName, String bookDownloadUrl) {
        this.subcategoryName = subcategoryName;
        this.bookDownloadUrl = bookDownloadUrl;
    }

    public String getsubcategoryName() {
        return subcategoryName;
    }

    public void setsubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getBookDownloadUrl() {
        return bookDownloadUrl;
    }

    public void setBookDownloadUrl(String bookDownloadUrl) {
        this.bookDownloadUrl = bookDownloadUrl;
    }
}
