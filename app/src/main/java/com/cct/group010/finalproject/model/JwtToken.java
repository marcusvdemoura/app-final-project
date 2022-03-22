package com.cct.group010.finalproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JwtToken {


    @SerializedName("Token")
    @Expose
    private String token;


    public JwtToken() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
