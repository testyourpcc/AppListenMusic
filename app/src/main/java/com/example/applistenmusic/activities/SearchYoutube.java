package com.example.applistenmusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.VideoAdapter;
import com.example.applistenmusic.interfaces.YouTubeAPI;
import com.example.applistenmusic.models.VideoItem;
import com.example.applistenmusic.models.YouTubeSearchResponse;
import com.example.applistenmusic.models.YouTubeVideo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchYoutube extends AppCompatActivity {

    private TextView textViewTitle, textViewChannelName;
    private EditText edtSearch;
    private Button btnSearch;
    private WebView webView;
    private String videoId = "F0B7HDiY-10"; // Thay VIDEO_ID bằng id của video muốn phát
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<VideoItem> videoItems = new ArrayList<>();
    private VideoItem videoItem;
    private static final int RC_SIGN_IN = 1001;
    private LinearLayout playVideoLayout;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_youtube);
        recyclerView = findViewById(R.id.recycler_view);
        btnSearch = findViewById(R.id.searchButton);
        edtSearch = findViewById(R.id.searchEditText);
        playVideoLayout = findViewById(R.id.layoutPlayVideo);
        textViewChannelName =  findViewById(R.id.textViewChannel);
        textViewTitle =  findViewById(R.id.textViewTitle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(videoItems);
        recyclerView.setAdapter(adapter);

        fetchYouTubeVideos();
        webView = findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        webView.loadData(html, "text/html", "utf-8");
        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String VideoId) {
                playVideoLayout.setVisibility(View.VISIBLE);
                for(VideoItem v : videoItems){
                    if(v.getVideoId().equals(VideoId)){
                        videoItem = v;
                    }
                }
                textViewTitle.setText(videoItem.getTitle().toString());
                textViewChannelName.setText(videoItem.getVideoId().toString());
                String html = "<html><head><style>body, html { margin: 0; padding: 0; } iframe { width: 100%; height: 100%; }</style></head><body><iframe src=\"https://www.youtube.com/embed/" + videoItem.getVideoId() + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                webView.loadData(html, "text/html", "utf-8");

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchYouTubeVideos(edtSearch.getText().toString());
            }
        });


//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("37669953608-ts4oirtc74b16fpk1nc7uirvqsgqa74f.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(SearchYoutube.this, gso);
//
//        // Bắt đầu quy trình đăng nhập khi người dùng nhấn nút đăng nhập
//        signIn();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result from the sign-in intent
        if (requestCode == RC_SIGN_IN) {
            try {
                // Get the signed-in account information from the intent
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                if (account != null) {
                    // Handle the signed-in account, e.g., display the display name
                    String displayName = account.getDisplayName();
                    Toast.makeText(this, "Welcome, " + displayName, Toast.LENGTH_SHORT).show();
                    // Sign-in successful, you can proceed to perform other actions
                } else {
                    // Handle case when account is null
                    Toast.makeText(this, "Sign-in failed: Account is null", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                // Sign-in failed
                Toast.makeText(this, "Failed to sign in: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchYouTubeVideos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeAPI youTubeAPI = retrofit.create(YouTubeAPI.class);

        youTubeAPI.searchVideos("AIzaSyBzG3L2hjlJZ9iDS4DfFJwzKAimzE5FTVc", "ive", "snippet", 20, "video", "viewCount")
                .enqueue(new Callback<YouTubeSearchResponse>() {
                    @Override
                    public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<YouTubeVideo> videos = response.body().getVideos();
                            for (YouTubeVideo video : videos) {
                                String title = video.getSnippet().getTitle();
                                String thumbnailUrl = video.getSnippet().getThumbnails().getHigh().getUrl();
                                String videoId = video.getId().getVideoId();
                                VideoItem videoItem = new VideoItem(title, thumbnailUrl, videoId);
                                videoItems.add(videoItem);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SearchYoutube.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<YouTubeSearchResponse> call, Throwable t) {
                        Log.e("MainActivity", "Error fetching videos", t);
                        Toast.makeText(SearchYoutube.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchYouTubeVideos(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeAPI youTubeAPI = retrofit.create(YouTubeAPI.class);

        youTubeAPI.searchVideos("AIzaSyBzG3L2hjlJZ9iDS4DfFJwzKAimzE5FTVc", keyword, "snippet", 20, "video", "viewCount")
                .enqueue(new Callback<YouTubeSearchResponse>() {
                    @Override
                    public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                        videoItems = new ArrayList<>();
                        if (response.isSuccessful() && response.body() != null) {
                            List<YouTubeVideo> videos = response.body().getVideos();
                            for (YouTubeVideo video : videos) {
                                String title = video.getSnippet().getTitle();
                                String thumbnailUrl = video.getSnippet().getThumbnails().getHigh().getUrl();
                                String videoId = video.getId().getVideoId();
                                VideoItem videoItem = new VideoItem(title, thumbnailUrl, videoId);
                                videoItems.add(videoItem);
                            }
                            adapter.setmData(videoItems);
                        } else {
                            Toast.makeText(SearchYoutube.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<YouTubeSearchResponse> call, Throwable t) {
                        Log.e("MainActivity", "Error fetching videos", t);
                        Toast.makeText(SearchYoutube.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
