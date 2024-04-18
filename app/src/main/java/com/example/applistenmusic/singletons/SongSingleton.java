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
    private List<Song> allSong;

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

    public synchronized List<Song> getAllSong() {
        if (allSong == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference().child("song");

            reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        List<Song> songList = new ArrayList<>();
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()) {
                            songList = new ArrayList<>();
                            for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                                Song song = songSnapshot.getValue(Song.class);
                                songList.add(song);

                            }

                        } else {

                        }
                    }
                }
            });
        }
        return songList;
    }

    public synchronized void setsongList(List<Song> songList) {
        this.allSong = songList;
    }
    public synchronized void setAllSongList(List<Song> songList) {
        this.allSong = songList;
    }


}
