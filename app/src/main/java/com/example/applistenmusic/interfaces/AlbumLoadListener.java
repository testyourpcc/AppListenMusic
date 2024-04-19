package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.PlayList;

import java.util.List;

public interface AlbumLoadListener {

    void onAlbumLoaded(List<Album> playList);
}
