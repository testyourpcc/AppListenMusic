package com.example.applistenmusic.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.applistenmusic.interfaces.SongDataCallback;
import com.example.applistenmusic.models.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SongHelper {
    static List<Song> songList = new ArrayList<>();
    public SongHelper() {
    }

    public static Song getSongById(List<Song> songList, int id){
        for(Song song : songList){
            if(song.getId() == id){
                return song;
            }
        }
        return new Song();
    }

    public static Song getSongById(int id){
        getALLSong(new SongDataCallback() {
            @Override
            public void onSongDataReceived(List<Song> List) {
                songList = List;
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi ở đây
            }
        });

        for(Song song : songList){
            if(song.getId() ==  id){
                return  song;
            }
        }
        return null;
    }

    public static void getALLSong(SongDataCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        List<Song> songList = new ArrayList<>();
        reference.child("song").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    callback.onError("Error getting data");
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                            Song song = songSnapshot.getValue(Song.class);
                            songList.add(song);
                        }
                        callback.onSongDataReceived(songList);
                    } else {
                        // Không có dữ liệu trong nhánh "song"
                        callback.onError("No data available");
                    }
                }
            }
        });
    }

    public static List<Song> getTrendingSong(){
        Song song = new Song();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        return songList;
    }

    public static List<Song> getSongByGenres(int genres){
        List<Song> songList = new ArrayList<>();
        for (Song song : songList){
            if(song.getGenres() == genres){
                songList.add(song);
            }
        }
        return songList;
    }

    public static List<Song> getSongByArtist(int artist){
        List<Song> songList = new ArrayList<>();
        for (Song song : songList){
            if(song.getGenres() == artist){
                songList.add(song);
            }
        }
        return songList;
    }


}
