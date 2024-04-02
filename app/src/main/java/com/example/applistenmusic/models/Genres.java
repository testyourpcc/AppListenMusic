package com.example.applistenmusic.models;

public class Genres {
    private int Id;
    private String name;

    public Genres() {
    }

    public Genres(int id, String name) {
        Id = id;
        this.name = name;
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
