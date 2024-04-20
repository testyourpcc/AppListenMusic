package com.example.applistenmusic.singletons;


import android.util.Log;

import com.example.applistenmusic.interfaces.DataLoadListener;

import com.example.applistenmusic.interfaces.SongDataCallback;
import com.example.applistenmusic.models.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SongListSingleton {

    private static volatile SongListSingleton instance;
    private List<Song> allSong;
    private boolean isDataLoaded = false;
    private DataLoadListener dataLoadListener;

    private int currentIndex = 0; // Vị trí bài hát hiện tại trong danh sách


    private SongListSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static SongListSingleton getInstance() {
        if (instance == null) {
            synchronized (SongListSingleton.class) {
                if (instance == null) {
                    instance = new SongListSingleton();
                }
            }
        }
        return instance;
    }

    public boolean hasSong() {
        return allSong != null;
    }

    public synchronized List<Song> getAllSongIfExist(){
        return allSong;
    }
    public synchronized void setAllSong(List<Song> songs){
        allSong = songs;
    }

    public synchronized void getAllSong(DataLoadListener listener) {
        if (isDataLoaded) {
            // If data is already loaded, return it immediately
            listener.onDataLoaded(allSong);
        } else {
            // Data is not loaded, fetch it asynchronously
            this.dataLoadListener = listener;
            fetchDataFromFirebase();
        }
    }

    private void fetchDataFromFirebase() {
        if (allSong == null) {
            allSong = new ArrayList<>();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("song");

        reference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        Song song = songSnapshot.getValue(Song.class);
                        allSong.add(song);
                    }
                    isDataLoaded = true;
                    dataLoadListener.onDataLoaded(allSong); // Notify listener when data is loaded
                } else {
                    Log.e("firebase", "No data found");
                }
            }
        });
    }

    // Trả về vị trí hiện tại của bài hát trong danh sách
    public int getCurrentIndex() {
        return currentIndex;
    }

    // Thiết lập vị trí hiện tại của bài hát trong danh sách
    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

}
