package com.example.applistenmusic.models;

public class MenuItem {

    int id;
    String Name;
    String Image;

    public MenuItem() {

    }

    public MenuItem(int id, String name, String image) {
        this.id = id;
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
