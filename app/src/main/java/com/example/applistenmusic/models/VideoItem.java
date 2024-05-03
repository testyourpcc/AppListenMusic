package com.example.applistenmusic.models;

public class VideoItem {
    private String title;
    private String thumbnailUrl;
    private String videoId;

    public VideoItem(String title, String thumbnailUrl, String videoId) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoId() {
        return videoId;
    }
}
