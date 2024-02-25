package com.example.applistenmusic;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {
    String name, email, password;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public UserInfo(){

    }
}
