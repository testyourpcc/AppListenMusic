package com.example.applistenmusic.api;

import com.example.applistenmusic.interfaces.YouTubeAPI;
import com.example.applistenmusic.models.YouTubeSearchResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YouTubeApiClient {

    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static final String API_KEY = "AIzaSyBzG3L2hjlJZ9iDS4DfFJwzKAimzE5FTVc";

    private YouTubeAPI youTubeAPI;

    public YouTubeApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        youTubeAPI = retrofit.create(YouTubeAPI.class);
    }

    public void searchVideos(String query, int maxResults, Callback<YouTubeSearchResponse> callback) {
        youTubeAPI.searchVideos(API_KEY, query, "snippet", maxResults, "video", "viewCount").enqueue(callback);
    }
}
