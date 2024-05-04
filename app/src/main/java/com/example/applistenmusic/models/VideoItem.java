package com.example.applistenmusic.models;

public class VideoItem {
    private String title;
    private String thumbnailUrl;
    private String videoId;
    private String channelTitle;
    private String publishTime;

    public VideoItem(String title, String thumbnailUrl, String videoId) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoId = videoId;
    }

    public VideoItem(String title, String thumbnailUrl, String videoId, String channelTitle, String publishTime) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoId = videoId;
        this.channelTitle = channelTitle;
        this.publishTime = publishTime;
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

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getPublishTime() {
        return publishTime;
    }
}
