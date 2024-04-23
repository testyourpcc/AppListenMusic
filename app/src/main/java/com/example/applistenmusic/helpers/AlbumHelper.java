package com.example.applistenmusic.helpers;

import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.AlbumSingleton;

import java.util.ArrayList;
import java.util.List;

public class AlbumHelper {
    static List<Album> allAlbum;
    static Album album;

    public static boolean containKeyWord(String keyWord) {
        if (AlbumSingleton.getInstance().hasAlbum()) {
            allAlbum = AlbumSingleton.getInstance().getAllAlbumIfExist();
        } else {
            AlbumSingleton.getInstance().getAllAlbum(new AlbumLoadListener(){
                @Override
                public void onAlbumLoaded(List<Album> AlbumList) {
                    allAlbum = AlbumList;
                }
            });
        }

        for(Album a : allAlbum){
            if (a.getName().toLowerCase().contains(keyWord.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static List<Integer> getAlbumIDByAlbumName(String name) {
        if (AlbumSingleton.getInstance().hasAlbum()) {
            allAlbum = AlbumSingleton.getInstance().getAllAlbumIfExist();
        } else {
            AlbumSingleton.getInstance().getAllAlbum(new AlbumLoadListener(){
                @Override
                public void onAlbumLoaded(List<Album> AlbumList) {
                    allAlbum = AlbumList;
                }
            });
        }
        List<Integer> aristIDList = new ArrayList<>();
        for(Album a : allAlbum){
            if (a.getName().toLowerCase().contains(name.toLowerCase())){
                aristIDList.add(a.getId());
            }
        }
        return aristIDList;
    }
    
    public List<Album> getAlbumByArtis() {
        return new ArrayList<>();
    }
    public static String getAlbumNameByID(int id) {
        if (AlbumSingleton.getInstance().hasAlbum()) {
            allAlbum = AlbumSingleton.getInstance().getAllAlbumIfExist();
        } else {
            AlbumSingleton.getInstance().getAllAlbum(new AlbumLoadListener(){
                @Override
                public void onAlbumLoaded(List<Album> AlbumList) {
                    allAlbum = AlbumList;
                }
            });
        }

        for(Album a : allAlbum){
            if (a.getId() == id){
                return a.getName();
            }
        }
        return "";
    }
    public Album getAlbum() {
        return album;
    }
}
