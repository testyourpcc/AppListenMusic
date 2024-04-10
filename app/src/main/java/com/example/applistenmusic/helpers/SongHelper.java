package com.example.applistenmusic.helpers;

import com.example.applistenmusic.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongHelper {

    public SongHelper() {
    }

    public static Song getSong(){
        Song song = new Song();
        return song;
    }

    public static List<Song> getALLSong(){
        Song song = new Song();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        return songList;
    }

    public static List<Song> getTrendingSong(){
        Song song = new Song();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        return songList;
    }

    public static List<Song> getSongByGenres(){
        Song song = new Song();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        return songList;
    }


}
