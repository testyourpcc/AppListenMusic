package com.example.applistenmusic.models;

import com.google.gson.annotations.SerializedName;

public class YouTubeThumbnails {
    @SerializedName("default")
    private YouTubeThumbnailsDefault Default;

    public YouTubeThumbnailsDefault getDefault() {
        return Default;
    }
}
