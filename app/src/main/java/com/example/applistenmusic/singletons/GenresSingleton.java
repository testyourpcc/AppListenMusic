package com.example.applistenmusic.singletons;


import android.util.Log;

import com.example.applistenmusic.interfaces.GenresLoadListener;
import com.example.applistenmusic.models.Genres;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GenresSingleton {

    private static volatile GenresSingleton instance;
    private List<Genres> allGenres;
    private boolean isGenresLoaded = false;
    private GenresLoadListener GenresLoadListener;

    private GenresSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static GenresSingleton getInstance() {
        if (instance == null) {
            synchronized (GenresSingleton.class) {
                if (instance == null) {
                    instance = new GenresSingleton();
                }
            }
        }
        return instance;
    }

    public boolean hasGenres() {
        return allGenres != null;
    }

    public synchronized List<Genres> getAllGenresIfExist(){
        return allGenres;
    }
    public synchronized void setAllGenres(List<Genres> Genress){
        allGenres = Genress;
    }

    public synchronized void getAllGenres(GenresLoadListener listener) {
        if (isGenresLoaded) {
            // If data is already loaded, return it immediately
            listener.onGenresLoaded(allGenres);
        } else {
            // Data is not loaded, fetch it asynchronously
            this.GenresLoadListener = listener;
            fetchDataFromFirebase();
        }
    }

    private void fetchDataFromFirebase() {
        if (allGenres == null) {
            allGenres = new ArrayList<>();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Genres");

        reference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        Genres genres = songSnapshot.getValue(Genres.class);
                        allGenres.add(genres);
                    }
                    isGenresLoaded = true;
                    GenresLoadListener.onGenresLoaded(allGenres); // Notify listener when data is loaded
                } else {
                    Log.e("firebase", "No data found");
                }
            }
        });
    }


}
