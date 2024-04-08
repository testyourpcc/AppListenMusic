package com.example.applistenmusic.singletons;

import android.media.MediaPlayer;

public class MediaPlayerSingleton {
    private static  MediaPlayer instance;

    private MediaPlayerSingleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }

}
