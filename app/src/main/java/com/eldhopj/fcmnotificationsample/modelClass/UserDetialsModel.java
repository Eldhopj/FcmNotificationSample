package com.eldhopj.fcmnotificationsample.modelClass;

public class UserDetialsModel {
    private String email;
    private String token;

    public UserDetialsModel() {
    }

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
