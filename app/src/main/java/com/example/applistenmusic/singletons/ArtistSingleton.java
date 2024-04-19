package com.example.applistenmusic.singletons;


import android.util.Log;

import com.example.applistenmusic.interfaces.ArtistLoadListener;
import com.example.applistenmusic.models.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ArtistSingleton {

    private static volatile ArtistSingleton instance;
    private List<Artist> allArtist;
    private boolean isArtistLoaded = false;
    private ArtistLoadListener ArtistLoadListener;

    private ArtistSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static ArtistSingleton getInstance() {
        if (instance == null) {
            synchronized (ArtistSingleton.class) {
                if (instance == null) {
                    instance = new ArtistSingleton();
                }
            }
        }
        return instance;
    }

    public boolean hasSong() {
        return allArtist != null;
    }

    public synchronized List<Artist> getallArtistIfExist(){
        return allArtist;
    }
    public synchronized void setAllArtist(List<Artist> artists){
        allArtist = artists;
    }

    public synchronized void getAllArtist(ArtistLoadListener listener) {
        if (isArtistLoaded) {
            // If data is already loaded, return it immediately
            listener.onArtistLoaded(allArtist);
        } else {
            // Data is not loaded, fetch it asynchronously
            this.ArtistLoadListener = listener;
            fetchDataFromFirebase();
        }
    }

    private void fetchDataFromFirebase() {
        if (allArtist == null) {
            allArtist = new ArrayList<>();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("artist");

        reference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        Artist artist = songSnapshot.getValue(Artist.class);
                        allArtist.add(artist);
                    }
                    isArtistLoaded = true;
                    ArtistLoadListener.onArtistLoaded(allArtist); // Notify listener when data is loaded
                } else {
                    Log.e("firebase", "No data found");
                }
            }
        });
    }
}
