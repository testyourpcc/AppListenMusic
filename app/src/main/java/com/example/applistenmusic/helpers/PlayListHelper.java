package com.example.applistenmusic.helpers;

import android.util.Log;

import com.example.applistenmusic.interfaces.PlayListFetchListener;
import com.example.applistenmusic.interfaces.PlayListLoadListener;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.singletons.PlayListSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    // tìm kiếm playlist theo tên
    public static void getPlayListByUserId(String userId, PlayListFetchListener playListFetchListener) {
        List<PlayList> userPlayLists = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("playList").child(userId);

        reference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot playListSnapshot : dataSnapshot.getChildren()) {
                        PlayList playList = playListSnapshot.getValue(PlayList.class);
                        userPlayLists.add(playList);
                        // Fetch songIdList from the first child
                        if (playListSnapshot.hasChild("songIdList")) {
                            List<Integer> songIdList = new ArrayList<>();
                            for (DataSnapshot songIdSnapshot : playListSnapshot.child("songIdList").getChildren()) {
                                songIdList.add(songIdSnapshot.getValue(Integer.class));
                                Log.d("firebase", "songIdList: " + songIdList);
                            }
                            playList.setSongIdList(songIdList);
                        }
                    }
                } else {
                    Log.e("firebase", "No data found");
                }
                playListFetchListener.onPlayListFetched(userPlayLists);
            }
        });
    }


    public PlayList getPlayList() {
        return playList;
    }
}