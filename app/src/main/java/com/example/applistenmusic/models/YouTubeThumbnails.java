package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

public class YouTubeThumbnails {
    @SerializedName("default")
    private YouTubeThumbnailsDefault Default;
    @SerializedName("high")
    private YouTubeThumbnailsHigh high;

    public YouTubeThumbnailsDefault getDefault() {
        return Default;
    }

    public YouTubeThumbnailsHigh getHigh() {
        return high;
    }
}
