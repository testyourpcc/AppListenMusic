package com.example.applistenmusic.models;

import java.util.List;

public class Album {
    private int Id;
    private String name;
    private List<Song> songList;
    private List<Long> songIdList;

    private String image;
    private int artist;

    public Album() {
    }

    public Album(int id, String name, List<Song> songList, List<Long> songIdList, String image, int artist) {
        Id = id;
        this.name = name;
        this.songList = songList;
        this.songIdList = songIdList;
        this.image = image;
        this.artist = artist;
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

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Long> getSongIdList() {
        return songIdList;
    }

    public void setSongIdList(List<Long> songIdList) {
        this.songIdList = songIdList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getArtist() {
        return artist;
    }

    public void setArtist(int artist) {
        this.artist = artist;
    }
}
