package com.example.applistenmusic.models;

public class Artist {
    private int Id;
    private String name;
    private String image;
    public Artist() {
    }

    public Artist(int id, String name, String image) {
        Id = id;
        this.name = name;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
