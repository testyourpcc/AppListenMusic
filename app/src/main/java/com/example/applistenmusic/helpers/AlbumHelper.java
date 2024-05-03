package com.example.applistenmusic.helpers;

import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                if(a.getSongIdList() != null) {
                    aristIDList.add(a.getId());
                }
            }
        }
        return aristIDList;
    }

    public static Album getAlbumByID(int id) {
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
        List<Long> aristIDList = new ArrayList<>();
        for(Album a : allAlbum){
            if (id == a.getId()){
                return  a;
            }
        }
        return  new Album();
    }

    public static List<Album> getAlbumByArtist(List<Integer> artistID) {
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
        List<Album> result = new ArrayList<>();
        if(!artistID.isEmpty()){
            for (Album album : allAlbum){
                for(Integer i : artistID ){
                    if(album.getArtist() == i){
                        result.add(album);
                    }
                }
            }
        }
        return result;
    }

    public static List<Album> getAlbumBySong(List<Integer> songID) {
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
        List<Album> result = new ArrayList<>();
        if(!songID.isEmpty()){
            for (Album album : allAlbum){
                for (Long songid : album.getSongIdList()) {
                    for (Integer i : songID) {
                        if (songid != null){
                            if (songid.equals(i.longValue())) {
                                result.add(album);
                            }
                        }
                    }
                }
            }
        }
        return result;
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
