package com.example.applistenmusic.activities;

public class LoginData {
    private int thanhCong;
    private int tongDangNhap;
    private int dayOfWeek;

    public LoginData() {
        // Default constructor required for Firebase
    }

    public LoginData(int thanhCong, int tongDangNhap, int dayOfWeek) {
        this.thanhCong = thanhCong;
        this.tongDangNhap = tongDangNhap;
        this.dayOfWeek = dayOfWeek;
    }

    public int getThanhCong() {
        return thanhCong;
    }

    public void setThanhCong(int thanhCong) {
        this.thanhCong = thanhCong;
    }

    public int getTongDangNhap() {
        return tongDangNhap;
    }

    public void setTongDangNhap(int tongDangNhap) {
        this.tongDangNhap = tongDangNhap;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
