package com.example.sarkaribook.Model;

import com.google.gson.annotations.SerializedName;

public class StorePlan {

//    "id": "1",
//            "user_id": "23",
//            "book_id": "0",
//            "price": "199",
//            "method": "upi",
//            "created_at": "2021-12-24 06:10:22",
//            "duration": "20",
//            "expiry_date": "2022-01-24"

    @SerializedName("duration")
    int duration;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("price")
    String price;

    @SerializedName("expiry_date")
    String expired_date;



    public StorePlan(int duration, int user_id, String price, String expired_date) {
        this.duration = duration;
        this.user_id = user_id;
        this.price = price;
        this.expired_date = expired_date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }
}
