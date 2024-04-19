package com.example.applistenmusic;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.applistenmusic.activities.Home;
import com.example.applistenmusic.activities.HomeAdmin;
import com.example.applistenmusic.activities.LoginAndRegister;
import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.ArtistLoadListener;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.interfaces.PlayListLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Artist;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.ArtistSingleton;
import com.example.applistenmusic.singletons.PlayListSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SplashView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    List<Song> songs;
    List<Artist> allArtist;
    List<Album> allAlbum;
    List<PlayList> allUserPlayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_view);
        SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
            @Override
            public void onDataLoaded(List<Song> songList) {
                songs = songList;
            }
        });

        ArtistSingleton.getInstance().getAllArtist(new ArtistLoadListener(){
            @Override
            public void onArtistLoaded(List<Artist> artists) {
                allArtist = artists;}
        });

        PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener(){
            @Override
            public void onPlayListLoaded(List<PlayList> playLists) {
                allUserPlayList = playLists;}
        });

        AlbumSingleton.getInstance().getAllAlbum(new AlbumLoadListener(){
            @Override
            public void onAlbumLoaded(List<Album> albums) {
                allAlbum = albums;}
        });


        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child(mAuth.getUid());
                    mDatabase.child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                String userRole =  String.valueOf(task.getResult().getValue());
                                if (userRole.equals("ADMIN")){
                                    Intent intent = new Intent(SplashView.this, HomeAdmin.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(SplashView.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });
                }else{
                startActivity(new Intent(SplashView.this, LoginAndRegister.class));
                finish();
                }
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SongListSingleton.getInstance().setAllSong(songs);
        ArtistSingleton.getInstance().setAllArtist(allArtist);
        AlbumSingleton.getInstance().setAllAlbum(allAlbum);
        PlayListSingleton.getInstance().setAllPlayList(allUserPlayList);
    }
}