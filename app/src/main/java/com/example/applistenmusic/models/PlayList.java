package com.example.applistenmusic.models;

import java.util.List;

public class PlayList {
    private int Id;
    private String name;
    private List<Song> Songs;
    private String image;
    private String user;

    public PlayList() {
    }

    public PlayList(int id, String name, List<Song> songs, String image, String user) {
        Id = id;
        this.name = name;
        Songs = songs;
        this.image = image;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
