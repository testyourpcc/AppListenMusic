package com.example.applistenmusic.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchYoutube extends AppCompatActivity {

    private TextView textViewTitle, textViewChannelName, textViewPublishTime,textViewTitleZoomIn;
    private EditText edtSearch;
    private NestedScrollView scrollView;
    private ImageView btnSearch, imgChannel, logoImageView, imageViewBack, btnPlayZoomIn, image_view_thumbnail_zoom_in;
    private WebView webView;
    private String videoId;
    //private final String APIkey = "AIzaSyBzG3L2hjlJZ9iDS4DfFJwzKAimzE5FTVc";
    private final String APIkey = "AIzaSyBYTlcmOLL5LBNysaT-jAP09WEupEZEYHY";
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<VideoItem> videoItems = new ArrayList<>();
    private VideoItem videoItem;
    private static final int RC_SIGN_IN = 1001;
    private LinearLayout playVideoLayout, layoutPlayVideoZoomIn;
    private GoogleSignInClient mGoogleSignInClient;
    private List<VideoItem> videoItemsHome;

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
        imgChannel =  findViewById(R.id.imageViewChannel);
        logoImageView =  findViewById(R.id.logoImageView);
        imageViewBack = findViewById(R.id.imageViewBack);
        textViewPublishTime = findViewById(R.id.textViewPublishTime);
        layoutPlayVideoZoomIn = findViewById(R.id.layoutPlayVideoZoomIn);
        textViewTitleZoomIn = findViewById(R.id.textViewTitleZoomIn);
        btnPlayZoomIn = findViewById(R.id.btnPlayZoomIn);
        image_view_thumbnail_zoom_in = findViewById(R.id.image_view_thumbnail_zoom_in);
        scrollView = findViewById(R.id.scrollView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(videoItems);
        recyclerView.setAdapter(adapter);

        fetchYouTubeVideos();
        webView = findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String VideoId) {
                playVideoLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                for(VideoItem v : videoItems){
                    if(v.getVideoId().equals(VideoId)){
                        videoItem = v;
                    }
                }
                Glide.with(SearchYoutube.this)
                        .load(R.drawable.noimage) // videoItem.getThumbnailUrl() là URL của hình ảnh
                        .apply(RequestOptions.circleCropTransform())
                        .override(40, 40)
                        .into(imgChannel);
                textViewPublishTime.setText(getTimeDifference(videoItem.getPublishTime()));
                textViewTitle.setText(videoItem.getTitle().replace("&#39;","'"));
                textViewChannelName.setText(videoItem.getChannelTitle());
                String html = "<html><head><style>body, html { margin: 0; padding: 0; } iframe { width: 100%; height: 100%; }</style></head><body><iframe src=\"https://www.youtube.com/embed/" + videoItem.getVideoId() + "\" frameborder=\"0\" allow=\"autoplay\" allowfullscreen></iframe></body></html>";
                webView.loadData(html, "text/html", "utf-8");
                if (layoutPlayVideoZoomIn.getVisibility() == View.VISIBLE) {
                    layoutPlayVideoZoomIn.setVisibility(View.GONE);
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                playVideoLayout.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                adapter.setmData(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtSearch.getText().toString().equals("") && edtSearch.getText() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
                    }
                    searchYouTubeVideos(edtSearch.getText().toString());

                    if(playVideoLayout.getVisibility() == View.VISIBLE){
                        playVideoLayout.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);
                        textViewTitleZoomIn.setText(videoItem.getTitle());
                        Glide.with(SearchYoutube.this)
                                .load(videoItem.getThumbnailUrl()) // videoItem.getThumbnailUrl() là URL của hình ảnh
                                .into(image_view_thumbnail_zoom_in);
                        layoutPlayVideoZoomIn.setVisibility(View.VISIBLE);
                    }
                    if(playVideoLayout.getVisibility() == View.GONE){
                        textViewTitleZoomIn.setText(videoItem.getTitle());
                        Glide.with(SearchYoutube.this)
                                .load(videoItem.getThumbnailUrl()) // videoItem.getThumbnailUrl() là URL của hình ảnh
                                .into(image_view_thumbnail_zoom_in);
                        layoutPlayVideoZoomIn.setVisibility(View.VISIBLE);
                    }
                }else {
                    edtSearch.setVisibility(View.VISIBLE);
                    // Focus vào EditText
                    edtSearch.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }

                }
            }
        });
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setmData(videoItemsHome);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
                }
                if(playVideoLayout.getVisibility() == View.VISIBLE){
                    playVideoLayout.setVisibility(View.GONE);
                    webView.setVisibility(View.GONE);
                    textViewTitleZoomIn.setText(videoItem.getTitle());
                    Glide.with(SearchYoutube.this)
                            .load(videoItem.getThumbnailUrl()) // videoItem.getThumbnailUrl() là URL của hình ảnh
                            .into(image_view_thumbnail_zoom_in);
                    layoutPlayVideoZoomIn.setVisibility(View.VISIBLE);
                }
            }
        });

        layoutPlayVideoZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideoLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                layoutPlayVideoZoomIn.setVisibility(View.GONE);
                scrollView.smoothScrollTo(0, 0);
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchYoutube.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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

        youTubeAPI.searchVideos(APIkey, "ive", "snippet", 20, "video", "viewCount")
                .enqueue(new Callback<YouTubeSearchResponse>() {
                    @Override
                    public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<YouTubeVideo> videos = response.body().getVideos();
                            for (YouTubeVideo video : videos) {
                                String title = video.getSnippet().getTitle().replace("&#39;","'");
                                String thumbnailUrl = video.getSnippet().getThumbnails().getHigh().getUrl();
                                String videoId = video.getId().getVideoId();
                                String channelTitle = video.getSnippet().getChannelTitle();
                                String publishAt = video.getSnippet().getPublishAt();
                                VideoItem videoItem = new VideoItem(title, thumbnailUrl, videoId,channelTitle,publishAt);
                                videoItems.add(videoItem);
                            }
                            videoItemsHome = videoItems;
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

        youTubeAPI.searchVideos(APIkey, keyword, "snippet", 20, "video", "viewCount")
                .enqueue(new Callback<YouTubeSearchResponse>() {
                    @Override
                    public void onResponse(Call<YouTubeSearchResponse> call, Response<YouTubeSearchResponse> response) {
                        videoItems = new ArrayList<>();
                        if (response.isSuccessful() && response.body() != null) {
                            List<YouTubeVideo> videos = response.body().getVideos();
                            for (YouTubeVideo video : videos) {
                                String title = video.getSnippet().getTitle().replace("&#39;","'");
                                String thumbnailUrl = video.getSnippet().getThumbnails().getHigh().getUrl();
                                String videoId = video.getId().getVideoId();
                                String channelTitle = video.getSnippet().getChannelTitle();
                                String publishAt = video.getSnippet().getPublishAt();
                                VideoItem videoItem = new VideoItem(title, thumbnailUrl, videoId,channelTitle,publishAt);
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

    public static String getTimeDifference(String uploadTime) {
        // Chuyển đổi thời gian đăng tải thành đối tượng LocalDateTime
        LocalDateTime uploadDateTime = null;
        long seconds = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            uploadDateTime = LocalDateTime.parse(uploadTime, DateTimeFormatter.ISO_DATE_TIME);


        // Lấy thời gian hiện tại
        Instant currentInstant = Instant.now();
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentInstant, ZoneId.systemDefault());

        // Tính khoảng cách giữa hai thời điểm
        Duration duration = Duration.between(uploadDateTime, currentDateTime);

        // Xử lý kết quả
        seconds = duration.getSeconds();
        }
        if (seconds < 3600) {
            // Dưới 1 giờ
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            // Dưới 1 ngày
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 2592000) {
            // Dưới 1 tháng (30 ngày)
            long days = seconds / 86400;
            return days + " ngày trước";
        } else if (seconds < 31536000) {
            // Dưới 1 năm (365 ngày)
            long months = seconds / 2592000;
            return months + " tháng trước";
        } else {
            // Trên 1 năm
            long years = seconds / 31536000;
            return years + " năm trước";
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
