package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class YouTubeSnippet {
    @SerializedName("title")
    private String title;

    @SerializedName("thumbnails")
    private YouTubeThumbnails thumbnails;
    @SerializedName("channelTitle")
    private String channelTitle;

    @SerializedName("publishedAt")
    private String publishAt;


    public String getTitle() {
        return title;
    }

    public YouTubeThumbnails getThumbnails() {
        return thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getPublishAt() {
        return publishAt;
    }
// Getter...
}
