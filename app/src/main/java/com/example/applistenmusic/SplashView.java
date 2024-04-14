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
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.sharePreferences.SharePreference;
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
    private static SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "APP-DATA";

    private static Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_view);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        reference.child("song").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    List<Song> songList = new ArrayList<>();
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        songList = new ArrayList<>();
                        for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                            Song song = songSnapshot.getValue(Song.class);
                            songList.add(song);

                        }
                        SplashView.saveSongListData("allSong", songList);
                    } else {
                        SplashView.saveSongListData("allSong", new ArrayList<Song>());
                    }
                }
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

    public static void saveData(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void saveIntData(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntData(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void saveBooleanData(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanData(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void saveSongData(String key, Song value) {
        String json = gson.toJson(value);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static Song getSongData(String key, Song defaultValue) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            return gson.fromJson(json, Song.class);
        } else {
            return defaultValue;
        }
    }

    public static void saveSongListData(String key, List<Song> list) {
        String json = gson.toJson(list);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public static List<Song> getSongListData(String key) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            Type type = new TypeToken<List<Song>>(){}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>(); // Hoặc trả về giá trị mặc định khác tùy thuộc vào trường hợp của bạn
        }
    }

}