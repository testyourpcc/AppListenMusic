package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Artist;

import java.util.List;

public interface ArtistLoadListener {

    void onArtistLoaded(List<Artist> playList);
}
