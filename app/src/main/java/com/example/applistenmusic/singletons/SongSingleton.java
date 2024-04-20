package com.example.applistenmusic.singletons;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.applistenmusic.SplashView;
import com.example.applistenmusic.models.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SongSingleton {

    private static volatile SongSingleton instance;
    private Song song;

    private SongSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static SongSingleton getInstance() {
        if (instance == null) {
            synchronized (SongSingleton.class) {
                if (instance == null) {
                    instance = new SongSingleton();
                }
            }
        }
        return instance;
    }

    public synchronized Song getSong() {
       return song;
    }

    public synchronized void setSong(Song song) {
        this.song = song;
    }
    public synchronized void clearSong() {
        this.song = null;
    }

    public boolean hasSong() {
        return song != null;
    }
}
