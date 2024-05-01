package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.PlayList;
import java.util.List;

public interface PlayListFetchListener {
    void onPlayListFetched(List<PlayList> playLists);
}