package com.example.applistenmusic.models;

import java.util.List;

public class Album {
    private int Id;
    private String name;
    private List<Song> Songs;
    private String image;
    private int artis;

    public Album() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return Songs;
    }

    public void setSongs(List<Song> songs) {
        Songs = songs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getArtis() {
        return artis;
    }

    public void setArtis(int artis) {
        this.artis = artis;
    }
}
