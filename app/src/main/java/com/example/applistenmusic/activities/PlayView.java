package com.example.applistenmusic.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioAttributes;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import android.view.GestureDetector;
import java.io.IOException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.applistenmusic.singletons.MediaPlayerSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PlayView extends AppCompatActivity {
    private GestureDetector gestureDetector;
    View mainView;
    private SeekBar seekBar;
    private Handler handler;
    ImageView Feature, Home,Search,Play,Account;
    private MediaPlayer mediaPlayer;
    private ImageView playButton, songImage;
    private boolean isPlaying = false;
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

        // Khởi tạo MediaPlayer
//        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayerSingleton.getInstance();
        // Thiết lập các thuộc tính âm thanh
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
//                    startPlaying();
                    playButton.setImageResource(R.drawable.ic_pause_40px);
                    // Khởi tạo FirebaseStorage
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    // Tham chiếu đến file nhạc trên Firebase Storage
                    StorageReference storageRef = storage.getReference().child("songs/NewJeans-Hype Boy.mp3");

                    // Tải file nhạc từ Firebase Storage
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                // Reset MediaPlayer trước khi sử dụng
                                mediaPlayer.reset();

                                // Đặt AudioAttributes cho MediaPlayer
                                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build());

                                // Đặt nguồn dữ liệu cho MediaPlayer
                                mediaPlayer.setDataSource(PlayView.this, uri);

                                // Chuẩn bị MediaPlayer
                                mediaPlayer.prepare();

                                handler = new Handler();

                                // Set max duration for seek bar
                                seekBar.setMax(mediaPlayer.getDuration());

                                // Update seek bar every 100 milliseconds
                                handler.postDelayed(updateSeekBar, 100);

                                // Seek bar change listener
                                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        if (fromUser) {
                                            mediaPlayer.seekTo(progress);
                                        }
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                    }
                                });


                                // Bắt đầu phát nhạc
                                mediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlayView.this, "Failed to download music", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    isPlaying = false;
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

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
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
        seekBar = findViewById(R.id.seekBar);

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

