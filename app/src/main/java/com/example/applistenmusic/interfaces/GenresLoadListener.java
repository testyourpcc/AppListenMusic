package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.Genres;

import java.util.List;

public interface GenresLoadListener {

    void onGenresLoaded(List<Genres> playList);
}
