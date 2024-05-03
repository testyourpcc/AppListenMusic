package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

public class YouTubeVideo {
    @SerializedName("id")
    private YouTubeVideoId id;

    @SerializedName("snippet")
    private YouTubeSnippet snippet;

    public YouTubeVideoId getId() {
        return id;
    }

    public YouTubeSnippet getSnippet() {
        return snippet;
    }

    // Getters...
}
