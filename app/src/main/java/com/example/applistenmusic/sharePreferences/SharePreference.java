package com.example.applistenmusic.sharePreferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.example.applistenmusic.models.Song;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharePreference extends Application {

    private static final String PREF_NAME = "MyAppPrefs";
    private static SharedPreferences sharedPreferences;
    private static Gson gson = new Gson();
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

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
