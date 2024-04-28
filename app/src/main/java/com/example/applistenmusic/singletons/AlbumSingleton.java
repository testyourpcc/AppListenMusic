package com.example.applistenmusic.singletons;


import android.util.Log;

import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlbumSingleton {

    private static volatile AlbumSingleton instance;
    private List<Album> allAlbum;
    private Album album;
    private boolean isAlbumLoaded = false;
    private AlbumLoadListener AlbumLoadListener;

    private AlbumSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static AlbumSingleton getInstance() {
        if (instance == null) {
            synchronized (AlbumSingleton.class) {
                if (instance == null) {
                    instance = new AlbumSingleton();
                }
            }
        }
        return instance;
    }

    public boolean hasAlbum() {
        return allAlbum != null;
    }

    public synchronized List<Album> getAllAlbumIfExist(){
        return allAlbum;
    }
    public synchronized void setAllAlbum(List<Album> albums){
        allAlbum = albums;
    }

    public synchronized void getAllAlbum(AlbumLoadListener listener) {
        if (isAlbumLoaded) {
            // If data is already loaded, return it immediately
            listener.onAlbumLoaded(allAlbum);
        } else {
            // Data is not loaded, fetch it asynchronously
            this.AlbumLoadListener = listener;
            fetchDataFromFirebase();
        }
    }

    private void fetchDataFromFirebase() {
        if (allAlbum == null) {
            allAlbum = new ArrayList<>();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("album");

        reference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        Album album = songSnapshot.getValue(Album.class);
                        allAlbum.add(album);
                    }
                    isAlbumLoaded = true;
                    AlbumLoadListener.onAlbumLoaded(allAlbum); // Notify listener when data is loaded
                } else {
                    Log.e("firebase", "No data found");
                }
            }
        });
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        if (album == null) {
            album = new Album();
        }
        return album;
    }
}
