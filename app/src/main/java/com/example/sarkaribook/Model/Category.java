package com.example.sarkaribook.Model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("category_name")
    private String categoryName;

    private String title;

    @SerializedName("time")
    public String time;

    @SerializedName("id")
    public int id;

    public Category(String categoryName, String title) {
        this.categoryName = categoryName;
        this.title = title;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
