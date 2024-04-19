package com.example.applistenmusic.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {

    String role;
    String phone;
    String address;
    boolean premium;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserInfo(String role, String phone, String address, boolean premium) {
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.premium = premium;
    }

    public UserInfo() {

    }
}
