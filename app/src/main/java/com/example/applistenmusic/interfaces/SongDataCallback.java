package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.Song;

import java.util.List;

public interface SongDataCallback {
    void onSongDataReceived(List<Song> songList);
    void onError(String errorMessage);

}
