package com.example.applistenmusic.helpers;

import com.example.applistenmusic.interfaces.ArtistLoadListener;
import com.example.applistenmusic.models.Artist;
import com.example.applistenmusic.singletons.ArtistSingleton;


import java.util.ArrayList;
import java.util.List;

public class ArtistHelper {
    static List<Artist> allArtist;
    Artist artist;

    public static boolean containKeyWord() {
        return true;
    }

    public List<Artist> getArtistByArtis() {
        return new ArrayList<>();
    }
    public static String getArtistNameByID(int id) {
        if (ArtistSingleton.getInstance().hasArtist()) {
            allArtist = ArtistSingleton.getInstance().getAllArtistIfExist();
        } else {
            ArtistSingleton.getInstance().getAllArtist(new ArtistLoadListener(){
                @Override
                public void onArtistLoaded(List<Artist> ArtistList) {
                    allArtist = ArtistList;
                }
            });
        }

        for(Artist a : allArtist){
            if (a.getId() == id){
                return a.getName();
            }
        }
        return "";
    }
    public Artist getArtist() {
        return artist;
    }
}
