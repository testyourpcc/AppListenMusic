package com.example.applistenmusic.models;

public class MenuItem {
    String Name;
    String Image;

    public MenuItem() {

    }

    public MenuItem(String name, String image) {
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
}
