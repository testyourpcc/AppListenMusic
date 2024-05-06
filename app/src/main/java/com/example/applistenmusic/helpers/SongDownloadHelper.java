package com.example.applistenmusic.helpers;

import android.os.Bundle;

import com.example.applistenmusic.models.SongDownload;

import java.util.List;

public class SongDownloadHelper {
    public static SongDownload getSongByFilePath(List<SongDownload> songList, String filePath) {
        for (SongDownload song : songList) {
            if (song.getFilePath().equals(filePath)) {
                return song;
            }
        }
        return null;
    }

    public static Bundle songToBundle(SongDownload song) {
        Bundle bundle = new Bundle();
        bundle.putString("name", song.getName());
        bundle.putString("artist", song.getArtist());
        bundle.putString("album", song.getAlbum());
        bundle.putString("genres", song.getGenres());
        bundle.putString("filePath", song.getFilePath());
        return bundle;
    }
}

