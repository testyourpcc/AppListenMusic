package com.example.applistenmusic.singletons;

import android.media.MediaPlayer;

public class MediaPlayerSingleton {

    private static volatile MediaPlayerSingleton instance;
    private MediaPlayer mediaPlayer;

    private MediaPlayerSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static MediaPlayerSingleton getInstance() {
        if (instance == null) {
            synchronized (MediaPlayerSingleton.class) {
                if (instance == null) {
                    instance = new MediaPlayerSingleton();
                }
            }
        }
        return instance;
    }

    public synchronized MediaPlayer getMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }

    public synchronized void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }


    public boolean isNull(){
        if (instance != null){
            return false;
        }
       return true;
    }
}
