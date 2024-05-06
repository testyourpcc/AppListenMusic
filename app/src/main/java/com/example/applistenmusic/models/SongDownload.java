package com.example.applistenmusic.models;

import java.io.File;

public class SongDownload  {
    private String name;
    private String artist;
    private String album;
    private String genres;
    private String filePath;

    public SongDownload(String name, String artist, String album, String genres, String filePath) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.genres = genres;
        this.filePath = filePath;
    }
    public static SongDownload fromFile(File file) {
        String name = file.getName();
        String artist = ""; // You need to extract this information from the file or its metadata
        String album = ""; // You need to extract this information from the file or its metadata
        String genres = ""; // You need to extract this information from the file or its metadata
        String filePath = file.getAbsolutePath();

        return new SongDownload(name, artist, album, genres, filePath);
    }
    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenres() {
        return genres;
    }

    public String getFilePath() {
        return filePath;
    }
}