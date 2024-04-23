package com.example.applistenmusic.helpers;

import com.example.applistenmusic.interfaces.ArtistLoadListener;
import com.example.applistenmusic.models.Artist;
import com.example.applistenmusic.singletons.ArtistSingleton;


import java.util.ArrayList;
import java.util.List;

public class ArtistHelper {
    static List<Artist> allArtist;
    Artist artist;

    public static boolean containKeyWord(String keyWork) {
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
            if (a.getName().toLowerCase().contains(keyWork.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static List<Integer> getArtistIDByArtistName(String name) {
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
        List<Integer> aristIDList = new ArrayList<>();
        for(Artist a : allArtist){
            if (a.getName().toLowerCase().contains(name.toLowerCase())){
                aristIDList.add(a.getId());
            }
        }
        return aristIDList;
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
