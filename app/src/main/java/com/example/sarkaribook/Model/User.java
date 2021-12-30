package com.example.sarkaribook.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_name")
    public String user_name;

    public User(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
