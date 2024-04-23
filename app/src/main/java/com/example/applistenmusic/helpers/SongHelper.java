package com.example.applistenmusic.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.interfaces.SongDataCallback;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static Song getRandomSong(List<Song> songList){
        Random random = new Random();
        int id =  random.nextInt(songList.size()) + 1;

        for(Song song : songList){
            if(song.getId() == id){
                return song;
            }
        }
        return new Song();
    }

    public static Song getSongById(int id){
        if (SongListSingleton.getInstance().hasSong()) {
            songList = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> List) {
                    songList = List;
                }
            });
        }

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

    public static List<Song> getSongByGenres(List<Integer> genres){
        if (SongListSingleton.getInstance().hasSong()) {
            songList = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> List) {
                    songList = List;
                }
            });
        }
        List<Song> result = new ArrayList<>();
        if(genres.size()>0){
            for (Song song : songList){
                for(Integer i : genres){
                    if(song.getGenres() == i){
                        result.add(song);
                    }
                }
            }
        }
        return result;
    }

    public static List<Song> getSongByArtist(List<Integer> artist){
        if (SongListSingleton.getInstance().hasSong()) {
            songList = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> List) {
                    songList = List;
                }
            });
        }
        List<Song> result = new ArrayList<>();
        if(artist.size()>0){
            for (Song song : songList){
                for(Integer i : artist){
                    if(song.getArtis() == i){
                        result.add(song);
                    }
                }
            }
        }
        return result;
    }

    public static List<Song> getSongByAlbum(List<Integer> album){
        if (SongListSingleton.getInstance().hasSong()) {
            songList = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> List) {
                    songList = List;
                }
            });
        }
        List<Song> result = new ArrayList<>();
        if(album.size()>0){
            for (Song song : songList){
                for(Integer i : album){
                    if(song.getAlbum() == i){
                        result.add(song);
                    }
                }
            }
        }
        return result;
    }



}
