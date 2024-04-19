package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;

import java.util.List;

public interface PlayListLoadListener {

    void onPlayListLoaded(List<PlayList> playList);
}
