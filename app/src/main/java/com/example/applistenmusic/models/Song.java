package com.example.applistenmusic.models;

public class Song {
    private int id;
    private String name;
    private String Url;
    private String image;
    private String lyric;
    private int genres;
    private int artis;
    private int album;
    private int view;

    public Song() {
    }

    public Song(int id, String name, String url, String image, String lyric, int genres, int artis, int album, int view) {
        this.id = id;
        this.name = name;
        this.Url = url;
        this.image = image;
        this.lyric = lyric;
        this.genres = genres;
        this.artis = artis;
        this.album = album;
        this.view = view;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public int getGenres() {
        return genres;
    }

    public void setGenres(int genres) {
        this.genres = genres;
    }

    public int getArtis() {
        return artis;
    }

    public void setArtis(int artis) {
        this.artis = artis;
    }

    public int getAlbum() {
        return album;
    }

    public void setAlbum(int album) {
        this.album = album;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void addView() {
        this.view +=1;
    }
}
