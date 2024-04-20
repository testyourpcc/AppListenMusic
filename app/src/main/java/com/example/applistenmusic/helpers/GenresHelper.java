package com.example.applistenmusic.helpers;

import com.example.applistenmusic.interfaces.GenresLoadListener;
import com.example.applistenmusic.models.Genres;
import com.example.applistenmusic.singletons.GenresSingleton;

import java.util.ArrayList;
import java.util.List;

public class GenresHelper {
    static List<Genres> allGenres;
    static Genres genres;

    public static boolean containKeyWord(String keyWord) {
        if (GenresSingleton.getInstance().hasGenres()) {
            allGenres = GenresSingleton.getInstance().getAllGenresIfExist();
        } else {
            GenresSingleton.getInstance().getAllGenres(new GenresLoadListener(){
                @Override
                public void onGenresLoaded(List<Genres> GenresList) {
                    allGenres = GenresList;
                }
            });
        }

        for(Genres a : allGenres){
            if (a.getName().toLowerCase().contains(keyWord.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public List<Genres> getGenresByArtis() {
        return new ArrayList<>();
    }
    public static String getGenresNameByID(int id) {
        if (GenresSingleton.getInstance().hasGenres()) {
            allGenres = GenresSingleton.getInstance().getAllGenresIfExist();
        } else {
            GenresSingleton.getInstance().getAllGenres(new GenresLoadListener(){
                @Override
                public void onGenresLoaded(List<Genres> GenresList) {
                    allGenres = GenresList;
                }
            });
        }

        for(Genres a : allGenres){
            if (a.getId() == id){
                return a.getName();
            }
        }
        return "";
    }
    public Genres getGenres() {
        return genres;
    }
}
