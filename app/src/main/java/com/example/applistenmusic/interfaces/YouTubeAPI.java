package com.example.applistenmusic.interfaces;

import com.example.applistenmusic.models.YouTubeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeAPI {
    @GET("search")
    Call<YouTubeSearchResponse> searchVideos(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("part") String part,
            @Query("maxResults") int maxResults,
            @Query("type") String type,
             @Query("order") String order
    );
}
