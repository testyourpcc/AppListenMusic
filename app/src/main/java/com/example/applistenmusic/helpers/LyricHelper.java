package com.example.applistenmusic.helpers;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.applistenmusic.activities.LyricView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LyricHelper {
    static String Lyric = "";

    String[] LyricLRC;
    DatabaseReference reference;
    private Handler handler,handlerSync;
    public LyricHelper() {
    }

    public String getLyric(){
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("song").child("2").child("lyric").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Lyric = "";
                }
                else {
                    Lyric = String.valueOf(task.getResult().getValue());
                }
            }
        });
        return Lyric;
    }

    public String[] getLyricLRC( String Lyric){
        LyricLRC = Lyric.split("/r/n");
        return LyricLRC;
    }

    public int getLyricHighlightIndex(long currentTime) {
        int currentIndex = 0;
        for (int i = LyricLRC.length - 2 ; i >= 0; i = i - 2) {
            String startLyricTime = LyricLRC[i];
            long startLyricTimeFormat = convertTimeToMilliseconds(startLyricTime);
            if(currentTime > startLyricTimeFormat){
                currentIndex = i + 1 ;
                break;
            }
        }
        return currentIndex;
    }

    public static long convertTimeToMilliseconds(String timeString) {
        String[] parts = timeString.split(":");
        int minutes = Integer.parseInt(parts[0].trim());
        int seconds = Integer.parseInt(parts[1].trim());
        return (minutes * 60 + seconds) * 1000;
    }
}
