package com.example.applistenmusic.models;

import java.security.Timestamp;

public class LyricForSync {
    public String time;
    public String data;

    public LyricForSync() {
    }

    public LyricForSync(String time, String data) {
        this.time = time;
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
