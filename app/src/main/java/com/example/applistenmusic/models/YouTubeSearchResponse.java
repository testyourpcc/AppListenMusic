package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YouTubeSearchResponse {
    @SerializedName("items")
    private List<YouTubeVideo> videos;

    public List<YouTubeVideo> getVideos() {
        return videos;
    }
}

