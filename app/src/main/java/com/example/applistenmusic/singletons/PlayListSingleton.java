package com.example.applistenmusic.singletons;


import android.util.Log;

import com.example.applistenmusic.interfaces.PlayListLoadListener;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PlayListSingleton {

    private static volatile PlayListSingleton instance;
    private List<PlayList> playList;
    private boolean isPlayListLoaded = false;
    private PlayListLoadListener PlayListLoadListener;

    private PlayListSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static PlayListSingleton getInstance() {
        if (instance == null) {
            synchronized (PlayListSingleton.class) {
                if (instance == null) {
                    instance = new PlayListSingleton();
                }
            }
        }
        return instance;
    }

    public boolean hasPlayList() {
        return playList != null;
    }

    public synchronized List<PlayList> getAllPlayListIfExist(){
        return playList;
    }
    public synchronized void setAllPlayList(List<PlayList> songs){
        playList = songs;
    }

    public synchronized void getAllPlayList(PlayListLoadListener listener) {
        if (isPlayListLoaded) {
            // If data is already loaded, return it immediately
            listener.onPlayListLoaded(playList);
        } else {
            // Data is not loaded, fetch it asynchronously
            this.PlayListLoadListener = listener;
            fetchDataFromFirebase();
        }
    }

    private void fetchDataFromFirebase() {
        if (playList== null) {
            playList = new ArrayList<>();
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (currentUser != null) {
            DatabaseReference reference = database.getReference().child("playList").child(currentUser.getUid());

            reference.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                            PlayList p = songSnapshot.getValue(PlayList.class);
                            playList.add(p);
                        }
                        isPlayListLoaded = true;
                        PlayListLoadListener.onPlayListLoaded(playList); // Notify listener when data is loaded
                    } else {
                        Log.e("firebase", "No data found");
                    }
                }
            });
        } else {
            PlayListLoadListener.onPlayListLoaded(new ArrayList<PlayList>());
        }
    }
}
