package com.eldhopj.fcmnotificationsample;

public class UserDetialsModel {
    private String email;
    private String token;

    public UserDetialsModel(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
