package com.example.applistenmusic.activities;

import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applistenmusic.models.PlayList;

import android.media.AudioAttributes;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;

import android.view.GestureDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.applistenmusic.dialogs.AlertDialogManager;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.interfaces.PlayListLoadListener;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.DownloadListSingleton;
import com.example.applistenmusic.singletons.MediaPlayerSingleton;
import com.example.applistenmusic.singletons.PlayListSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;
import com.example.applistenmusic.singletons.TimerSingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PlayView extends AppCompatActivity {
    private GestureDetector gestureDetector;
    View mainView;
    private SeekBar seekBar;
    TextView startTime, endTime, songName;
    private Handler handler, handler1;

    ImageView repeatImg, shuffleImg, ivBack;
    private MediaPlayer mediaPlayer;
    private ImageView playButton, songImage, Home, Search, Play, Account, Download;

    private ImageView playPrevious, playNext, ivFavorite;
    private DatabaseReference databaseReference;

    private String imageUrl;
    private String Url;
    private List<Song> songs = new ArrayList<>();
    Animation animation;
    Song song;

    boolean repeatSong;
    boolean shufferSong;
    boolean favorite = false;
    PlayList fvr;
    String currentUserId;
    List<PlayList> allPlayList;
    List<Song> allDownloads;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Lấy thông tin người dùng hiện tại
    private TimerSingleton timerSingleton;
    private TextView textViewTimer;
    private Switch switchTimer;
    ImageView BottomSheet;
    LinearLayout henGio, addToPlaylist, openPlaylist;
    private long timeLeftInMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setcontrol();

        // lấy danh sách bài hát từ điện thoại
        SongListSingleton.getInstance().getAllDownLoadSongIfExist();

        // Lấy ID của người dùng hiện tại
        if (user != null) {
            currentUserId = user.getUid();
            // Sử dụng currentUserId ở đây
        }

        // lấy nhạc từ playList
        if (PlayListSingleton.getInstance().hasPlayList()) {
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener() {
                @Override
                public void onPlayListLoaded(List<com.example.applistenmusic.models.PlayList> songList) {
                    allPlayList = songList;
                }
            });
        }


        // lấy danh sách nhạc đã tải về từ điện thoại allDownl;
        if (DownloadListSingleton.getInstance().hasDownload()) {
            allDownloads = DownloadListSingleton.getInstance().getAllDownloadIfExist();
        } else {
            DownloadListSingleton.getInstance().getAllDownload(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> downloadList) {
                    allDownloads = downloadList;
                    Log.d("DownloadList", "Download list size: " + allDownloads.size());
                }
            });
        }

        // Kiểm tra giá trị của repeat trên Firebase và cập nhật nút repeatImg
        checkRepeatFromFirebase();

        // Kiểm tra giá trị của shuffle trên Firebase và cập nhật nút shuffleImg
        checkShuffleFromFirebase();

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

        if (intent != null) {
            // Get the song URL and download list from the Intent
            String songUrl = intent.getStringExtra("url");
            if (songUrl != null) {
                song = SongSingleton.getInstance().getSong();
                songName.setText(song.getName());
                getAndPlaySongLocal(songUrl);
            }
        }


        if (SongSingleton.getInstance().getSong() != null && playNow) {
            song = SongSingleton.getInstance().getSong();
            if (song.getUrl().startsWith("songs")) {
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
            }

        } else {
            if (SongSingleton.getInstance().getSong() != null) {
                if (SongSingleton.getInstance().getSong().getUrl().startsWith("songs")) {
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
                            return;
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

        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SongSingleton.getInstance().getSong().getUrl().startsWith("songs")) {
                    // Lấy danh sách bài hát
                    List<Song> songs = SongListSingleton.getInstance().getAllSongIfExist();

                    // Kiểm tra xem danh sách bài hát có tồn tại và không rỗng
                    if (songs != null && !songs.isEmpty()) {
                        // Kiểm tra xem shuffle có được bật hay không
                        if (!shufferSong) {
                            if (SongSingleton.getInstance().getSong() != null) {
                                // Lấy ID của bài hát hiện tại
                                int currentSongId = SongSingleton.getInstance().getSong().getId();

                                // Tìm vị trí của bài hát có ID tiếp theo trong danh sách
                                int nextSongIndex = -1;
                                for (int i = 0; i < songs.size(); i++) {
                                    if (songs.get(i).getId() == currentSongId + 1) {
                                        nextSongIndex = i;
                                        break;
                                    }
                                }

                                // Nếu không tìm thấy bài hát có ID tiếp theo, chọn bài hát đầu tiên trong danh sách
                                if (nextSongIndex == -1) {
                                    nextSongIndex = 0;
                                }

                                // Cập nhật vị trí hiện tại của bài hát trong danh sách
                                SongListSingleton.getInstance().setCurrentIndex(nextSongIndex);

                                // Lấy bài hát tiếp theo từ danh sách
                                Song nextSong = songs.get(nextSongIndex);

                                // Cập nhật thông tin và phát bài hát tiếp theo
                                updateAndPlayNextSong(nextSong);
                            }
                        } else {
                            // Nếu shuffle được bật
                            // Chọn một bài hát ngẫu nhiên từ danh sách
                            Song nextSong = SongHelper.getRandomSong(songs);

                            // Cập nhật thông tin và phát bài hát tiếp theo
                            updateAndPlayNextSong(nextSong);
                        }
                    }
                } else {
                    // Get the download list
                    List<Song> downloads = SongListSingleton.getInstance().getAllDownLoadSongIfExist();

                    // Check if the download list exists and is not empty
                    if (downloads != null && !downloads.isEmpty()) {
                        // Get the current song
                        Song currentSong = SongSingleton.getInstance().getSong();

                        // Find the index of the current song in the download list
                        int currentIndex = downloads.indexOf(currentSong);
                        // Check if the current song is not the last song in the list
                        if (currentIndex < downloads.size() - 1) {
                            // Get the next song
                            Song nextSong = downloads.get(currentIndex + 1);

                            // Play the next song
                            String nextSongFilePath = nextSong.getUrl(); // Assuming Song has a getFilePath() method
                            updateAndPlayNextSongLocal(nextSong);
                        } else {
                            // If the current song is the last song in the list, play the first song
                            Song firstSong = downloads.get(0);
                            updateAndPlayNextSongLocal(firstSong);

//
                        }
                    }
                }
            }
        });

        playPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy danh sách bài hát
                if (SongSingleton.getInstance().getSong().getUrl().startsWith("songs")) {
                    List<Song> songs = SongListSingleton.getInstance().getAllSongIfExist();

                    // Kiểm tra xem danh sách bài hát có tồn tại và không rỗng
                    if (songs != null && !songs.isEmpty()) {
                        // Kiểm tra xem shuffle có được bật hay không
                        if (!shufferSong) {
                            if (SongSingleton.getInstance().getSong() != null) {
                                // Lấy ID của bài hát hiện tại
                                int currentSongId = SongSingleton.getInstance().getSong().getId();

                                // Tìm vị trí của bài hát có ID trước đó trong danh sách
                                int previousSongIndex = -1;
                                for (int i = 0; i < songs.size(); i++) {
                                    if (songs.get(i).getId() == currentSongId - 1) {
                                        previousSongIndex = i;
                                        break;
                                    }
                                }

                                // Nếu không tìm thấy bài hát có ID trước đó, chọn bài hát cuối cùng trong danh sách
                                if (previousSongIndex == -1) {
                                    previousSongIndex = songs.size() - 1;
                                }

                                // Cập nhật vị trí hiện tại của bài hát trong danh sách
                                SongListSingleton.getInstance().setCurrentIndex(previousSongIndex);

                                // Lấy bài hát trước đó từ danh sách
                                Song previousSong = songs.get(previousSongIndex);

                                // Cập nhật thông tin và phát bài hát trước đó
                                updateAndPlayNextSong(previousSong);
                            }
                        } else {
                            // Nếu shuffle được bật
                            // Chọn một bài hát ngẫu nhiên từ danh sách (ở đây bạn có thể sử dụng logic khác để chọn bài hát trước đó)
                            Song previousSong = SongHelper.getRandomSong(songs);

                            // Cập nhật thông tin và phát bài hát trước đó
                            updateAndPlayNextSong(previousSong);
                        }
                    }
                } else {
                    // Get the download list
                    List<Song> downloads = SongListSingleton.getInstance().getAllDownLoadSongIfExist();

                    // Check if the download list exists and is not empty
                    if (downloads != null && !downloads.isEmpty()) {
                        // Get the current song
                        Song currentSong = SongSingleton.getInstance().getSong();

                        // Find the index of the current song in the download list
                        int currentIndex = downloads.indexOf(currentSong);
                        // Check if the current song is not the first song in the list
                        if (currentIndex > 0) {
                            // Get the previous song
                            Song previousSong = downloads.get(currentIndex - 1);

                            // Play the previous song
                            String previousSongFilePath = previousSong.getUrl(); // Assuming Song has a getFilePath() method
                            updateAndPlayNextSongLocal(previousSong);
                        } else {
                            // If the current song is the first song in the list, play the last song
                            Song lastSong = downloads.get(downloads.size() - 1);
                            updateAndPlayNextSongLocal(lastSong);
                        }
                    }
                }
            }
        });


        shuffleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference shuffleRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("shuffle");
                shuffleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int shuffleValue = dataSnapshot.getValue(Integer.class);

                            // Cập nhật giao diện người dùng tùy thuộc vào giá trị của repeatValue
                            if (shuffleValue == 0) {
                                shuffleImg.setImageResource(R.drawable.ic_shuffer_on);
                                shuffleRef.setValue(1);
                                shufferSong = true;
                            } else {
                                shuffleImg.setImageResource(R.drawable.ic_shuffer_off);
                                shuffleRef.setValue(0);
                                shufferSong = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra trong quá trình đọc từ Firebase
                    }
                });
            }
        });

        repeatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference repeatRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("repeat");
                repeatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int repeatValue = dataSnapshot.getValue(Integer.class);

                            // Cập nhật giao diện người dùng tùy thuộc vào giá trị của repeatValue
                            if (repeatValue == 0) {
                                repeatImg.setImageResource(R.drawable.ic_repeat_on);
                                repeatRef.setValue(1);
                                repeatSong = true;
                            } else {
                                repeatImg.setImageResource(R.drawable.ic_repeat_off);
                                repeatRef.setValue(0);
                                repeatSong = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra trong quá trình đọc từ Firebase
                    }
                });
            }
        });


        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favorite) {
                    saveData(song.getId());

                    ivFavorite.setImageResource(R.drawable.ic_heart_on);
                    favorite = true;


                } else {
                    removeData(song.getId());
                    ivFavorite.setImageResource(R.drawable.ic_heart_off);
                    favorite = false;
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                if(SongSingleton.getInstance().hasSong()){
//                    if (repeatSong) {
//                        // Nếu chế độ lặp lại được bật, quay lại đầu bài hát và phát lại
//                        mediaPlayer.seekTo(0);
//                        mediaPlayer.start();
//                    } else {
//                        // Lấy danh sách bài hát
//                        List<Song> songs = SongListSingleton.getInstance().getAllSongIfExist();
//
//                        // Kiểm tra xem danh sách bài hát có tồn tại và không rỗng
//                        if (songs != null && !songs.isEmpty()) {
//                            // Kiểm tra xem shuffle có được bật hay không
//                            if (!shufferSong) {
//                                // Lấy ID của bài hát hiện tại
//                                int currentSongId = SongSingleton.getInstance().getSong().getId();
//
//                                // Tìm vị trí của bài hát có ID tiếp theo trong danh sách
//                                int nextSongIndex = -1;
//                                for (int i = 0; i < songs.size(); i++) {
//                                    if (songs.get(i).getId() == currentSongId + 1) {
//                                        nextSongIndex = i;
//                                        break;
//                                    }
//                                }
//
//                                // Nếu không tìm thấy bài hát có ID tiếp theo, chọn bài hát đầu tiên trong danh sách
//                                if (nextSongIndex == -1) {
//                                    nextSongIndex = 0;
//                                }
//
//                                // Cập nhật vị trí hiện tại của bài hát trong danh sách
//                                SongListSingleton.getInstance().setCurrentIndex(nextSongIndex);
//
//                                // Lấy bài hát tiếp theo từ danh sách
//                                Song nextSong = songs.get(nextSongIndex);
//
//                                // Cập nhật thông tin và phát bài hát tiếp theo
//                                updateAndPlayNextSong(nextSong);
//                            } else {
//                                // Nếu shuffle được bật
//                                // Chọn một bài hát ngẫu nhiên từ danh sách
//                                Song nextSong = SongHelper.getRandomSong(songs);
//
//                                // Cập nhật thông tin và phát bài hát tiếp theo
//                                updateAndPlayNextSong(nextSong);
//                            }
//                        }
//
//                    }
//                }
//            }
//        });


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

        LocalBroadcastManager.getInstance(this).registerReceiver(timerUpdateReceiver,
                new IntentFilter("com.example.timer.UPDATE"));
        LocalBroadcastManager.getInstance(this).registerReceiver(timerUpdateReceiver,
                new IntentFilter("com.example.timer.FINISHED"));
        BottomSheetDialog optionDialog = new BottomSheetDialog(this);
        BottomSheetDialog henGioDialog = new BottomSheetDialog(this);
        optionDialog.setContentView(R.layout.play_bottom_sheet_layout);
        henGioDialog.setContentView(R.layout.layout_hen_gio);
        BottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.show();
            }
        });

        henGio = optionDialog.findViewById(R.id.hengio);
        addToPlaylist = optionDialog.findViewById(R.id.addPlaylist);
        openPlaylist = optionDialog.findViewById(R.id.openPlaylist);
        henGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                henGioDialog.show();
            }
        });
        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        openPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        timerSingleton = TimerSingleton.getInstance();
        textViewTimer = henGioDialog.findViewById(R.id.textViewTimer);
        switchTimer = henGioDialog.findViewById(R.id.switch_timer);
        if (timerSingleton.isTimerRunning()) {
            switchTimer.setChecked(timerSingleton.isTimerRunning());
            timeLeftInMillis = timerSingleton.getTimeLeftInMillis();
            updateCountDownText(timeLeftInMillis);
        }

        switchTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTimePickerDialog();
                } else {
                    timerSingleton.stopTimer();
                    textViewTimer.setText("00:00:00");
                }
            }
        });


        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayView.this, "Download.",
                        Toast.LENGTH_SHORT).show();
                String songname = SongSingleton.getInstance().getSong().getUrl();
                Toast.makeText(PlayView.this, songname,
                        Toast.LENGTH_SHORT).show();
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://applistenmusic-b4e45.appspot.com/" + songname);

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songname); //xoá phần thừa mp3

                        downloadManager.enqueue(request);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Xử lý lỗi nếu có
                        Log.e("TAG", "Error downloading image", exception);
                    }
                });


            }
        });

    }

    public void getAndPlaySongLocal(String filePath) {
        try {
            // Reset MediaPlayer before use
            mediaPlayer.reset();

            // Set AudioAttributes for MediaPlayer
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());

            // Set the data source to the file path
            mediaPlayer.setDataSource(filePath);

            // Prepare the MediaPlayer
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

            // Start playing the song
            mediaPlayer.start();
            updateTime();

            // Set the play button to display the pause icon because the song is playing
            playButton.setImageResource(R.drawable.ic_pause_40px);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void checkShuffleFromFirebase() {
        DatabaseReference shuffleRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("shuffle");

        shuffleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int shuffleValue = dataSnapshot.getValue(Integer.class);

                    // Cập nhật giao diện người dùng tùy thuộc vào giá trị của repeatValue
                    if (shuffleValue == 1) {
                        shuffleImg.setImageResource(R.drawable.ic_shuffer_on);
                        shufferSong = true;
                    } else {
                        shuffleImg.setImageResource(R.drawable.ic_shuffer_off);
                        shufferSong = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc từ Firebase
                Log.e("FirebaseError", "Error reading repeat value: " + databaseError.getMessage());
            }
        });
    }

    private void checkRepeatFromFirebase() {
        DatabaseReference repeatRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("repeat");

        repeatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int repeatValue = dataSnapshot.getValue(Integer.class);

                    // Cập nhật giao diện người dùng tùy thuộc vào giá trị của repeatValue
                    if (repeatValue == 1) {
                        repeatImg.setImageResource(R.drawable.ic_repeat_on);
                        repeatSong = true;
                    } else {
                        repeatImg.setImageResource(R.drawable.ic_repeat_off);
                        repeatSong = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc từ Firebase
                Log.e("FirebaseError", "Error reading repeat value: " + databaseError.getMessage());
            }
        });
    }


    // Phương thức để cập nhật thông tin bài hát và phát bài hát tiếp theo
    private void updateAndPlayNextSong(Song nextSong) {
        // Cập nhật thông tin bài hát mới
        SongSingleton.getInstance().setSong(nextSong);
        song = SongSingleton.getInstance().getSong();
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

    private void updateAndPlayNextSongLocal(Song nextSong) {
        // Cập nhật thông tin bài hát mới
        SongSingleton.getInstance().setSong(nextSong);
        song = SongSingleton.getInstance().getSong();
        songName.setText(nextSong.getName());
        // Lấy đường dẫn file của bài hát mới và phát nó
        String nextSongFilePath = nextSong.getUrl();
        getAndPlaySongLocal(nextSongFilePath);
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
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timerUpdateReceiver);
    }

    public void getAndPlaySong(String Url) {
        // Khởi tạo FirebaseStorage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Tham chiếu đến file nhạc trên Firebase Storage
        StorageReference storageRef = storage.getReference().child(Url);

        // Cập nhật SongSingleton với bài hát mới
        SongSingleton.getInstance().setSong(song);
        // Kiểm tra trạng thái yêu thích cho bài hát mới
        checkFavoriteStatus(song.getId());
        //

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
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PlayView.this, "Failed to download music", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveData(int songId) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("playList").child(currentUserId);

        // Tạo một song mới và đặt giá trị của nó vào Realtime Firebase
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                //  Lấy số lượng children hiện có trong "songs" để xác định ID tiếp theo
                boolean flag = true;
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    PlayList playList = songSnapshot.getValue(PlayList.class);
                    if (playList.getId() == 0) {
                        if (playList.getSongIdList().contains(0)) {
                            flag = false;
                            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference().child("playList").child(currentUserId).child("0").child("songIdList").child("0");
                            playlistRef.setValue(songId);
                            List<Integer> a = new ArrayList<>();
                            a.add(songId);
                            playList.setSongIdList(a);

                        }
                    }
                }

                if (flag) {
                    DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference().child("playList").child(currentUserId).child("0").child("songIdList");

                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        PlayList song = songSnapshot.getValue(PlayList.class);
                        if (song.getId() == 0) {
                            List<Integer> a = song.getSongIdList();
                            a.add(songId);
                            song.setSongIdList(a);
                            allPlayList.get(0).setSongIdList(a);
                            playlistRef.setValue(a);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@android.support.annotation.NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }


    private void removeData(int songId) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("playList").child(currentUserId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    PlayList playList = songSnapshot.getValue(PlayList.class);
                    if (playList.getId() == 0) {
                        List<Integer> songIdList = playList.getSongIdList();
                        if (songIdList.contains(songId)) {
                            // Check if songIdList only contains one element
                            if (songIdList.size() == 1) {
                                // If songIdList only contains one element, set the value of this element to 0
                                List<Integer> a = new ArrayList<>();//playList.getSongIdList();
                                a.add(0);
                                //playList.setSongIdList(a);
                                allPlayList.get(0).setSongIdList(a);
                                songIdList.set(0, 0);
                            } else {
                                // Remove the songId from the list
                                List<Integer> a = playList.getSongIdList();
                                if (a.contains(songId)) {
                                    a.remove(Integer.valueOf(songId));
                                }
                                //playList.setSongIdList(a);
                                allPlayList.get(0).setSongIdList(a);
                                songIdList.remove(Integer.valueOf(songId));
                            }
                            // Update the songIdList in Firebase
                            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference().child("playList").child(currentUserId).child("0").child("songIdList");
                            playlistRef.setValue(songIdList);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@android.support.annotation.NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void checkFavoriteStatus(int currentSongId) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("playList").child(currentUserId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    PlayList playList = songSnapshot.getValue(PlayList.class);
                    if (playList.getId() == 0) {
                        List<Integer> songIdList = playList.getSongIdList();
                        if (songIdList.contains(currentSongId)) {
                            // If the current song ID is in the songIdList, set the heart icon to red and favorite to true
                            favorite = true;
                            ivFavorite.setImageResource(R.drawable.ic_heart_on);
                        } else {
                            // If the current song ID is not in the songIdList, set the heart icon to white and favorite to false
                            favorite = false;
                            ivFavorite.setImageResource(R.drawable.ic_heart_off);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@android.support.annotation.NonNull DatabaseError databaseError) {
                // Handle error
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
        playPrevious = findViewById(R.id.previousImg);
        songImage = findViewById(R.id.discImageView);
        songName = findViewById(R.id.songNameTextView);
        mainView = findViewById(R.id.PlayView);
        gestureDetector = new GestureDetector(this, new GestureListener());
        seekBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        shuffleImg = findViewById(R.id.shuffleImg);
        repeatImg = findViewById(R.id.repeatImg);
        ivFavorite = findViewById(R.id.ivFavorite);
        textViewTimer = findViewById(R.id.textViewTimer);
        BottomSheet = findViewById(R.id.navigationButton);
        ivBack = findViewById(R.id.ivBack);
        Download = findViewById(R.id.download);
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
            Log.d("12345", "onFling called");
            boolean result = false;
            try {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY(); // Calculate vertical difference
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) {
                        // Vuốt sang trái, chuyển sang một activity khác
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        Intent intent = new Intent(PlayView.this, LyricView.class);
                        intent.putExtra("seekBarProcess", seekBar.getProgress());
                        startActivity(intent);
                        result = true;
                    } else {

                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        //Log.d("12345", "onFling: sang trái");

                        // Vuốt sang phải, chuyển sang activity khác
                        //Intent intent = new Intent(PlayView.this, SongDetailView.class);

                        //startActivity(intent);
                        //result = true;
                    }
                }
                //vuốt lên và xuống
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY < 0) {
                        // Swipe up, switch to PlayList activity
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                        Log.d("12345", "onFling: vuot len");
                        Intent intent = new Intent(PlayView.this, MusicPlaying.class);
                        startActivity(intent);
                        result = true;
                    } else {
                        // Swipe down, switch to another activity
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                        Intent intent = new Intent(PlayView.this, Home.class);
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

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeLeftInMillis = (hourOfDay * 3600 + minute * 60) * 1000L;
                timerSingleton.startTimer(timeLeftInMillis, 1000);
            }
        }, 0, 0, true);
        timePickerDialog.show();
    }

    private void updateCountDownText(long timeLeftInMillis) {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        textViewTimer.setText(timeFormatted);
    }

    private BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.timer.UPDATE")) {
                long timeLeftInMillis = intent.getLongExtra("timeLeft", 0);
                updateCountDownText(timeLeftInMillis);
            } else if (intent.getAction().equals("com.example.timer.FINISHED")) {
                handleTimerFinished();
            }
        }
    };

    private void handleTimerFinished() {
        Toast.makeText(this, "Timer has finished!", Toast.LENGTH_SHORT).show();
        playButton.setImageResource(R.drawable.play_icon);
        switchTimer.setChecked(false);
    }

}

