package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.Song;

import java.util.List;

public interface DataLoadListener {
    void onDataLoaded(List<Song> songList);
}
