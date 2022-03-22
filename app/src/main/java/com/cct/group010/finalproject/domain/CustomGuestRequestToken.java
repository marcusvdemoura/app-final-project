package com.cct.group010.finalproject.domain;

public class CustomGuestRequestToken {

    private String username, password;

    public CustomGuestRequestToken(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public CustomGuestRequestToken() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
