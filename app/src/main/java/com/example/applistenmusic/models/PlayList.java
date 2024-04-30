package com.example.applistenmusic.models;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
    private int Id;
    private String name;
    private List<Song> songList;
    private List<Integer> songIdList;
    private String image;
    private String user;

    public PlayList() {
    }

    public PlayList(int id, String name, List<Song> songList, List<Integer> songIdList, String image, String user) {
        this.Id = id;
        this.name = name;
        this.songList = songList;
        this.songIdList = songIdList;
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

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Integer> getSongIdList() {
        return songIdList;
    }

    public void setSongIdList(List<Integer> songIdList) {
        this.songIdList = songIdList;
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
