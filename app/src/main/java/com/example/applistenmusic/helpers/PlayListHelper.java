package com.example.applistenmusic.helpers;

import com.example.applistenmusic.interfaces.PlayListLoadListener;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.singletons.PlayListSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayListHelper {
    static List<PlayList> allPlayList;
    static PlayList playList;

    public static boolean containKeyWord(String keyWord) {
        if (PlayListSingleton.getInstance().hasPlayList()) {
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener(){
                @Override
                public void onPlayListLoaded(List<PlayList> PlayListList) {
                    allPlayList = PlayListList;
                }
            });
        }

        for(PlayList p : allPlayList){
            if (p.getName().toLowerCase().contains(keyWord.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static List<Long> getPlayListIDByPlayListName(String name) {
        if (PlayListSingleton.getInstance().hasPlayList()) {
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener(){
                @Override
                public void onPlayListLoaded(List<PlayList> PlayListList) {
                    allPlayList = PlayListList;
                }
            });
        }
        List<Long> playListIDList = new ArrayList<>();
        for(PlayList p : allPlayList){
            if (p.getName().toLowerCase().contains(name.toLowerCase())){
                if(p.getSongIdList() != null) {
                    List<Long> result = p.getSongIdList().stream()
                            .filter(Objects::nonNull)
                            .map(Integer::longValue)
                            .collect(Collectors.toList());
                    playListIDList.addAll(result);
                }
            }
        }
        return playListIDList;
    }

    public static PlayList getPlayListByID(int id) {
        if (PlayListSingleton.getInstance().hasPlayList()) {
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener(){
                @Override
                public void onPlayListLoaded(List<PlayList> PlayListList) {
                    allPlayList = PlayListList;
                }
            });
        }
        for(PlayList p : allPlayList){
            if (id == p.getId()){
                return  p;
            }
        }
        return  new PlayList();
    }

    public static String getPlayListNameByID(int id) {
        if (PlayListSingleton.getInstance().hasPlayList()) {
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener(){
                @Override
                public void onPlayListLoaded(List<PlayList> PlayListList) {
                    allPlayList = PlayListList;
                }
            });
        }

        for(PlayList p : allPlayList){
            if (p.getId() == id){
                return p.getName();
            }
        }
        return "";
    }
    public PlayList getPlayList() {
        return playList;
    }
}