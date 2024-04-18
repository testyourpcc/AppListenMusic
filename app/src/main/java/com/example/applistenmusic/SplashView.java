package com.example.applistenmusic;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.applistenmusic.activities.Home;
import com.example.applistenmusic.activities.HomeAdmin;
import com.example.applistenmusic.activities.LoginAndRegister;
import com.example.applistenmusic.activities.LoginView;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.sharePreferences.SharePreference;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    List<Song> songs;
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
    }
}