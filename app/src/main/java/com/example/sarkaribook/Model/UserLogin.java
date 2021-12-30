package com.example.sarkaribook.Model;

import com.google.gson.annotations.SerializedName;

public class UserLogin {

//                "id": "1",
//                "user_name": "user",
//                "password": "password",
//                "email": "new@gmail.com",
//                "contact_number": "1234567890",
//                "status": "1"

    @SerializedName("id")
    public int id;
@SerializedName("user_name")
String user_name;
@SerializedName("email")
String email;
@SerializedName("contact_number")
String contact_number;
@SerializedName("status")
String status;

    public UserLogin( String user_name, String email, String contact_number, String status) {

        this.user_name = user_name;
        this.email = email;
        this.contact_number = contact_number;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
