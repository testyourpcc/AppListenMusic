package com.example.applistenmusic.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioAttributes;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import android.view.GestureDetector;
import java.io.IOException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PlayView extends AppCompatActivity {
    private GestureDetector gestureDetector;
    View mainView;

    ImageView Feature, Home,Search,Play,Account;
    private MediaPlayer mediaPlayer;
    private ImageView playButton, songImage;
    private boolean isPlaying = false;
    private String AUDIO_URL = "https://www.ashleecadell.com/xyzstorelibrary/01-01-%20Dear%20Future%20Self%20(Hands%20Up)%20%5bfeat%20Wyclef%20Jean%5d.mp3";
    private String imageUrl = "https://www.thenews.com.pk/assets/uploads/updates/2023-02-19/1042261_2435611_haerin2_updates.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_play);
        setcontrol();



        // Sử dụng Glide để tải và hiển thị ảnh từ URL
        Glide.with(this)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(50, 0))
                .into(songImage);

        mediaPlayer = new MediaPlayer();

        // Đặt các thuộc tính âm thanh cho MediaPlayer
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    startPlaying();
                    playButton.setImageResource(R.drawable.ic_pause_40px);
                } else {
                    stopPlaying();
                }
            }
        });



        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayView.this, SearchView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    private void startPlaying() {
        try {
            mediaPlayer.setDataSource(AUDIO_URL);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    isPlaying = true;
                }
            });
            mediaPlayer.prepareAsync(); // Chuẩn bị mediaPlayer bất đồng bộ

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        isPlaying = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release(); // Giải phóng tài nguyên khi không cần thiết
    }

    public void setcontrol() {
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        playButton = findViewById(R.id.playButton);
        songImage = findViewById(R.id.discImageView);
        mainView = findViewById(R.id.PlayView);
        gestureDetector = new GestureDetector(this, new GestureListener());


    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 50;
        private static final int SWIPE_VELOCITY_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) {
                        // Vuốt sang trái, chuyển sang một activity khác
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        Intent intent = new Intent(PlayView.this, LyricView.class);
                        startActivity(intent);
                        result = true;
                    }  else {
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        // Vuốt sang phải, chuyển sang activity khác
                        Intent intent = new Intent(PlayView.this, SongDetailView.class);
                        startActivity(intent);
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}

