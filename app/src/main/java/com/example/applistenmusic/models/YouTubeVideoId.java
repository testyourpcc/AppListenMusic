package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

public class YouTubeVideoId {
    @SerializedName("videoId")
    private String videoId;

    // Getter...

    public String getVideoId() {
        return videoId;
    }
}
