package com.example.applistenmusic.activities;

import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applistenmusic.R;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.MediaPlayerSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LyricView extends AppCompatActivity {
    boolean isUserInteracting = false;
    private GestureDetector gestureDetector;
    View mainView;
    ImageView Feature, Home,Search,Play,Account,playButton, playNext;
    TextView textViewLyric;
    DatabaseReference reference;
    private Handler handler;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    ScrollView  scrollView;
    String[] LyricLRC;
    Song song;
    private String Url;
    private List<Song> songs = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);
        setcontrol();
        mediaPlayer = MediaPlayerSingleton.getInstance().getMediaPlayer();

        getData();
        if(mediaPlayer.isPlaying()){
            playButton.setImageResource(R.drawable.ic_pause_40px);
        } else {
            Intent intent = getIntent();
            int seekBarProcess = intent.getIntExtra("seekBarProcess", 0);
            if (seekBarProcess != 0){
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(seekBarProcess);
                mediaPlayer.seekTo(seekBarProcess);
            }
        }

        if (SongListSingleton.getInstance().hasSong()) {
            songs = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> songList) {
                    songs = songList;
                }
            });
        }
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.play_icon);
                } else {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.ic_pause_40px);
                }
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(LyricView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(LyricView.this, SearchView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(LyricView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        // Thiết lập listener cho sự kiện chạm hoặc cuộn
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Người dùng đang chạm vào ScrollView
                        isUserInteracting = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Người dùng đang cuộn ScrollView
                        isUserInteracting = true;
                        break;
                    default:
                        isUserInteracting = false;
                        break;
                }
                return false;
            }
        });

        handler = new Handler();

        // Set max duration for seek bar
        seekBar.setMax(mediaPlayer.getDuration());

        // Update seek bar and media player every 100 milliseconds
        handler.postDelayed(updateSeekBarAndMediaPlayer, 100);

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
        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                Song s = SongHelper.getRandomSong(songs);
                SongSingleton.getInstance().setSong(s);
                Url = s.getUrl();
                getAndPlaySong(Url);
                getData();
            }
        });

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
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
                    if (diffX > 0) { // Thay đổi điều kiện này từ diffX < 0 thành diffX > 0
                        // Vuốt sang phải, chuyển sang một activity khác
                        Intent intent = new Intent(LyricView.this, PlayView.class);
                        intent.putExtra("seekBarProcess", seekBar.getProgress());
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

    void getData(){
        song = SongSingleton.getInstance().getSong();
        if (song == null) {
            textViewLyric.setText("Không có bài hát nào đang được phát");
        } else {
            String a = song.getLyric();
            if (a.isEmpty()){
                textViewLyric.setText("Chưa có lời bài hát cho bài hát này");
            } else {
                LyricLRC = a.split("/r/n");
                // Khởi tạo Handler và Runnable
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        changeColorAndText(LyricLRC, getLyricHighlightIndex(mediaPlayer.getCurrentPosition()));
                        handler.postDelayed(this, 150);
                        // Thực hiện lại sau mỗi giây
                    }
                };
                // Bắt đầu việc thay đổi màu và văn bản
                handler.post(runnable);
            }
        }

    }

    public int getLyricHighlightIndex(long currentTime) {
        int currentIndex = 0;
        for (int i = LyricLRC.length - 2 ; i >= 0; i = i - 2) {
            String startLyricTime = LyricLRC[i];
            long startLyricTimeFormat = convertTimeToMilliseconds(startLyricTime);
            if(currentTime > startLyricTimeFormat){
                currentIndex = i + 1 ;
                break;
            }
        }
        return currentIndex;


    }

    private void changeColorAndText(String[] LyricLRC, int LyricHighlightIndex) {
        if (LyricHighlightIndex >= 0 && LyricHighlightIndex <= LyricLRC.length) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i = 1; i < LyricLRC.length; i = i + 2) {
                String lyric = LyricLRC[i];
                if (i == LyricHighlightIndex) {
                    SpannableString spannableLyricHighLight = new SpannableString(lyric);
                    spannableLyricHighLight.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableLyricHighLight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.append(spannableLyricHighLight).append("\n");
                } else {
                    SpannableString spannableString = new SpannableString(lyric);
                    spannableString.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.append(spannableString).append("\n");
                }
            }

            textViewLyric.setText(spannableStringBuilder);

            if (!isUserInteracting) {
                int scrollViewHeight = scrollView.getHeight();
                int textViewHeight;
                if(textViewLyric.getLayout() != null) {
                    textViewHeight = textViewLyric.getLayout().getLineTop((LyricHighlightIndex - 1) / 2);// Chiều cao của mỗi dòng trong textViewLyric
                } else {
                    textViewHeight = 0;
                }
                int scrollY = textViewHeight - scrollViewHeight / 15 ;
                scrollView.smoothScrollTo(0, scrollY);
            }
        }
    }

    public static long convertTimeToMilliseconds(String timeString) {
        String[] parts = timeString.split(":");
        int minutes = Integer.parseInt(parts[0].trim());
        int seconds = Integer.parseInt(parts[1].trim());
        return (minutes * 60 + seconds) * 1000;
    }

    private Runnable updateSeekBarAndMediaPlayer = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
            }
            handler.postDelayed(this, 250);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerSingleton.getInstance().setMediaPlayer(mediaPlayer);
        if(handler!= null) {
            handler.removeCallbacks(updateSeekBarAndMediaPlayer);
            handler.removeCallbacksAndMessages(null);
        }
    }
    public void getAndPlaySong(String Url) {
        // Khởi tạo FirebaseStorage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Tham chiếu đến file nhạc trên Firebase Storage
        StorageReference storageRef = storage.getReference().child(Url);

        // Tải file nhạc từ Firebase Storage
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess (Uri uri){
                try {

                    // Reset MediaPlayer trước khi sử dụng
                    mediaPlayer.reset();

                    // Đặt AudioAttributes cho MediaPlayer
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());

                    // Đặt nguồn dữ liệu cho MediaPlayer
                    mediaPlayer.setDataSource(LyricView.this, uri);

                    // Chuẩn bị MediaPlayer
                    mediaPlayer.prepare();

                    handler = new Handler();

                    // Set max duration for seek bar
                    seekBar.setMax(mediaPlayer.getDuration());

                    // Update seek bar and media player every 100 milliseconds
                    handler.postDelayed(updateSeekBarAndMediaPlayer, 100);

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
            public void onFailure (@NonNull Exception e){
                Toast.makeText(LyricView.this, "Failed to download music", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setcontrol() {
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        playNext = findViewById(R.id.nextButton);
        Account = findViewById(R.id.imageViewAccount);
        mainView = findViewById(R.id.LyricView);
        scrollView = findViewById(R.id.ScrollLyric);
        textViewLyric = findViewById(R.id.textViewLyric);
        gestureDetector = new GestureDetector(this, new GestureListener());
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
    }
}
