package com.example.sarkaribook.Retrofit;

import com.example.sarkaribook.Model.StorePlan;
import com.example.sarkaribook.Model.UserLogin;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Res {
    @SerializedName("ResponseCode")
    private String ResponseCode;

    @SerializedName("Result")
    private String Result;

    @SerializedName("ResultMsg")
    private String ResponseMsg;

     @SerializedName("UserLogin")
     UserLogin userLogin;

    @SerializedName("message")
    String message;

    public Res(String responseCode, String result, String responseMsg, UserLogin userLogin, String message) {
        ResponseCode = responseCode;
        Result = result;
        ResponseMsg = responseMsg;
        this.userLogin = userLogin;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Res(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public Res(String responseCode, String responseMessage, String result, String ResponseMsg) {
        this.ResponseCode = responseCode;
        this.Result = result;
        this.ResponseMsg = ResponseMsg;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        ResponseMsg = responseMsg;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}
