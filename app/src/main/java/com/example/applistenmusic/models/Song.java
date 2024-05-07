package com.example.applistenmusic.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.File;

public class Song implements Parcelable {
    private int id;
    private String name;
    private String url;
    private String image;
    private String lyric;
    private int genres;
    private int artist;
    private int album;
    private int view;

    private static int idCounter = 0;

    public Song() {
    }

    public Song(int id, String name, String url, String image, String lyric, int genres, int artist, int album, int view) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image = image;
        this.lyric = lyric;
        this.genres = genres;
        this.artist = artist;
        this.album = album;
        this.view = view;

    }

    protected Song(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        image = in.readString();
        lyric = in.readString();
        genres = in.readInt();
        artist = in.readInt();
        album = in.readInt();
        view = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getArtist() {
        return artist;
    }

    public void setArtist(int artist) {
        this.artist = artist;
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

    public static Song fromFile(File file) {
        int id = idCounter++;
        String name = file.getName();
        String url = file.getAbsolutePath();

        // Create a new Song object with the extracted name, url and id
        // Other fields are set to default values
        Song song = new Song();
        song.setId(id);
        song.setName(name);
        song.setUrl(url);

        return song;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(image);
        dest.writeString(lyric);
        dest.writeInt(genres);
        dest.writeInt(artist);
        dest.writeInt(album);
        dest.writeInt(view);
    }
}
