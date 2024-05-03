package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class YouTubeSnippet {
    @SerializedName("title")
    private String title;

    @SerializedName("thumbnails")
    private YouTubeThumbnails thumbnails;

    public String getTitle() {
        return title;
    }

    public YouTubeThumbnails getThumbnails() {
        return thumbnails;
    }
// Getter...
}
