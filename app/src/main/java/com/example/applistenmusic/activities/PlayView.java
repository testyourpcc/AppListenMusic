package com.example.applistenmusic.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioAttributes;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import android.view.GestureDetector;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.applistenmusic.dialogs.AlertDialogManager;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.MediaPlayerSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PlayView extends AppCompatActivity {
    private GestureDetector gestureDetector;
    View mainView;
    private SeekBar seekBar;
    TextView startTime, endTime, songName;
    private Handler handler, handler1;

    ImageView repeatImg, shuffleImg;
    private MediaPlayer mediaPlayer;
    private ImageView playButton, songImage, playNext, Home, Search, Play, Account;
    private DatabaseReference databaseReference;

    private String imageUrl;
    private String Url;
    private List<Song> songs = new ArrayList<>();
    Animation animation;
    Song song;

    boolean repeatSong = false;

    boolean shufferSong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_play);
        setcontrol();

        mediaPlayer = MediaPlayerSingleton.getInstance().getMediaPlayer();
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
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
        Intent intent = getIntent();
        boolean playNow = intent.getBooleanExtra("playNow", false);

        if (SongSingleton.getInstance().getSong() != null && playNow) {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            Song song = SongSingleton.getInstance().getSong();
            imageUrl = song.getImage();
            songName.setText(song.getName());
            playButton.setImageResource(R.drawable.ic_pause_40px);
            // Sử dụng Glide để tải và hiển thị ảnh từ URL
            int sizeInPixels = getResources().getDimensionPixelSize(R.dimen.image_size); // Kích thước cố định của hình ảnh
            Glide.with(this)
                    .load(imageUrl)
                    .override(sizeInPixels, sizeInPixels) // Đặt kích thước cố định cho hình ảnh
                    .transform(new RoundedCornersTransformation(50, 0))
                    .circleCrop() // Chuyển đổi hình ảnh thành hình tròn
                    .into(songImage);

            Url = song.getUrl();
            SongSingleton.getInstance().setSong(song);
            getAndPlaySong(Url);

        } else {
            if (SongSingleton.getInstance().getSong() != null) {
                imageUrl = SongSingleton.getInstance().getSong().getImage();
                songName.setText(SongSingleton.getInstance().getSong().getName());
                // Sử dụng Glide để tải và hiển thị ảnh từ URL
                int sizeInPixels = getResources().getDimensionPixelSize(R.dimen.image_size); // Kích thước cố định của hình ảnh
                Glide.with(this)
                        .load(imageUrl)
                        .override(sizeInPixels, sizeInPixels) // Đặt kích thước cố định cho hình ảnh
                        .transform(new RoundedCornersTransformation(50, 0))
                        .circleCrop() // Chuyển đổi hình ảnh thành hình tròn
                        .into(songImage);
            }
        }


        // Nếu có bài hát đang chạy khi khởi tạo view thì đồng bộ hóa với seek bar
        if (mediaPlayer.isPlaying()) {
            playButton.setImageResource(R.drawable.ic_pause_40px);

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

        } else {
            // đồng bộ thanh seek bar khi nhạc đang pause
            Intent LyricIntent = getIntent();
            int seekBarProcess = LyricIntent.getIntExtra("seekBarProcess", 0);
            if (seekBarProcess != 0) {
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(seekBarProcess);
                mediaPlayer.seekTo(seekBarProcess);
                updateTime();
            }
        }
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    if (mediaPlayer.getCurrentPosition() > 0) {
                        playButton.setImageResource(R.drawable.ic_pause_40px);
                        mediaPlayer.seekTo(seekBar.getProgress());
                        mediaPlayer.start();
                        updateTime();
                    } else {
                        if (!SongSingleton.getInstance().hasSong()) {
                            AlertDialogManager.showAlert(PlayView.this, "Cảnh báo", "Bạn phải chọn bài hát trước khi phát");
                            SongSingleton.getInstance().clearSong();
                        } else {
                            playButton.setImageResource(R.drawable.ic_pause_40px);
                            Url = SongSingleton.getInstance().getSong().getUrl();
                            getAndPlaySong(Url);
                        }
                    }
                } else {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.play_icon);
                }
            }
        });

//        playNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.reset();
//                Song s = SongHelper.getRandomSong(songs);
//                SongSingleton.getInstance().setSong(s);
//                imageUrl = s.getImage();
//                int sizeInPixels = getResources().getDimensionPixelSize(R.dimen.image_size); // Kích thước cố định của hình ảnh
//                Glide.with(PlayView.this)
//                        .load(imageUrl)
//                        .override(sizeInPixels, sizeInPixels) // Đặt kích thước cố định cho hình ảnh
//                        .transform(new RoundedCornersTransformation(50, 0))
//                        .circleCrop() // Chuyển đổi hình ảnh thành hình tròn
//                        .into(songImage);
//                Url = s.getUrl();
//                songName.setText(s.getName());
//                getAndPlaySong(Url);
//            }
//        });

        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy danh sách bài hát
                List<Song> songs = SongListSingleton.getInstance().getAllSongIfExist();

                // Kiểm tra xem danh sách bài hát có tồn tại và không rỗng
                if (songs != null && !songs.isEmpty()) {
                    // Nếu shuffle không được bật
                    if (!shufferSong) {
                        // Lấy vị trí hiện tại của bài hát
                        int currentIndex = SongListSingleton.getInstance().getCurrentIndex();

                        // Tăng vị trí lên 1 để lấy bài hát tiếp theo
                        currentIndex++;
                        // Kiểm tra nếu đã đến cuối danh sách thì quay lại bài đầu tiên
                        if (currentIndex >= songs.size()) {
                            currentIndex = 0;
                        }
                        // Lấy bài hát tiếp theo từ danh sách
                        Song nextSong = songs.get(currentIndex);

                        // Cập nhật vị trí hiện tại của bài hát trong danh sách
                        SongListSingleton.getInstance().setCurrentIndex(currentIndex);

                        // Cập nhật thông tin bài hát và phát bài hát tiếp theo
                        updateAndPlayNextSong(nextSong);
                    } else {
                        // Nếu shuffle được bật
                        // Lấy một bài hát ngẫu nhiên từ danh sách
                        Song nextSong = SongHelper.getRandomSong(songs);

                        // Cập nhật thông tin và phát bài hát tiếp theo
                        updateAndPlayNextSong(nextSong);
                    }
                }
            }
        });


        shuffleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shufferSong){
                    shuffleImg.setImageResource(R.drawable.ic_shuffer_on);
                    shufferSong =true;
                } else {
                    shuffleImg.setImageResource(R.drawable.ic_shuffer_off);
                    shufferSong =false;
                }
            }
        });

        repeatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!repeatSong){
                    repeatImg.setImageResource(R.drawable.ic_repeat_on);
                    repeatSong =true;
                } else {
                    repeatImg.setImageResource(R.drawable.ic_repeat_off);
                    repeatSong =false;
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (repeatSong) {
                    // Nếu chế độ lặp lại được bật, quay lại đầu bài hát và phát lại
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                } else {
                    // Lấy danh sách bài hát
                    List<Song> songs = SongListSingleton.getInstance().getAllSongIfExist();

                    // Kiểm tra xem danh sách bài hát có tồn tại và không rỗng
                    if (songs != null && !songs.isEmpty()) {
                        // Nếu shuffle không được bật
                        if (!shufferSong) {
                            // Lấy vị trí hiện tại của bài hát
                            int currentIndex = SongListSingleton.getInstance().getCurrentIndex();

                            // Tăng vị trí lên 1 để lấy bài hát tiếp theo
                            currentIndex++;
                            // Kiểm tra nếu đã đến cuối danh sách thì quay lại bài đầu tiên
                            if (currentIndex >= songs.size()) {
                                currentIndex = 0;
                            }
                            // Lấy bài hát tiếp theo từ danh sách
                            Song nextSong = songs.get(currentIndex);

                            // Cập nhật vị trí hiện tại của bài hát trong danh sách
                            SongListSingleton.getInstance().setCurrentIndex(currentIndex);

                            // Cập nhật thông tin bài hát và phát bài hát tiếp theo
                            updateAndPlayNextSong(nextSong);
                        } else {
                            // Nếu shuffle được bật
                            // Lấy một bài hát ngẫu nhiên từ danh sách
                            Song nextSong = SongHelper.getRandomSong(songs);

                            // Cập nhật thông tin và phát bài hát tiếp theo
                            updateAndPlayNextSong(nextSong);
                        }
                    }
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


    // Phương thức để cập nhật thông tin bài hát và phát bài hát tiếp theo
    private void updateAndPlayNextSong(Song nextSong) {
        // Cập nhật thông tin bài hát mới
        SongSingleton.getInstance().setSong(nextSong);
        imageUrl = nextSong.getImage();
        int sizeInPixels = getResources().getDimensionPixelSize(R.dimen.image_size);
        Glide.with(PlayView.this)
                .load(imageUrl)
                .override(sizeInPixels, sizeInPixels)
                .transform(new RoundedCornersTransformation(50, 0))
                .circleCrop()
                .into(songImage);
        songName.setText(nextSong.getName());
        // Lấy URL của bài hát mới và phát nó
        String nextSongUrl = nextSong.getUrl();
        getAndPlaySong(nextSongUrl);
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(this, 100);
            updateTime();
        }
    };

    private void updateTime() {
        handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    startTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    endTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
                    handler1.postDelayed(this, 100);
                }
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(updateSeekBar);
        }
        if (handler1 != null) {
            handler1.removeCallbacksAndMessages(null);
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
                mediaPlayer.setDataSource(PlayView.this, uri);

                // Chuẩn bị MediaPlayer
                mediaPlayer.prepare();

                handler = new Handler();

                // Set max duration for seek bar
                seekBar.setMax(mediaPlayer.getDuration());

                // Update seek bar every 100 milliseconds
                handler.postDelayed(updateSeekBar, 250);

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
                updateTime();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure (@NonNull Exception e){
            Toast.makeText(PlayView.this, "Failed to download music", Toast.LENGTH_SHORT).show();
        }
    });
}
    public void setcontrol() {
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        playButton = findViewById(R.id.playButton);
        playNext = findViewById(R.id.nextImg);
        songImage = findViewById(R.id.discImageView);
        songName = findViewById(R.id.songNameTextView);
        mainView = findViewById(R.id.PlayView);
        gestureDetector = new GestureDetector(this, new GestureListener());
        seekBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        shuffleImg = findViewById(R.id.shuffleImg);
        repeatImg = findViewById(R.id.repeatImg);



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
                        intent.putExtra("seekBarProcess", seekBar.getProgress());
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

