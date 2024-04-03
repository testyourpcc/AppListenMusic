package com.example.applistenmusic.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {

    String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserInfo(String role) {
        this.role = role;
    }
    public UserInfo() {

    }
}
