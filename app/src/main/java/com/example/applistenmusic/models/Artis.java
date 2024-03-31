package com.example.applistenmusic.models;

import java.util.List;

public class Artis {
    private int Id;
    private String name;

    public Artis() {
    }

    public Artis(int id, String name) {
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
