package com.example.applistenmusic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.R.anim;
import com.example.applistenmusic.R.id;
import com.example.applistenmusic.R.layout;
import com.example.applistenmusic.adapters.SongSearchResultAdapter;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import java.util.List;

public class PlayList extends AppCompatActivity {
    ImageView Home, Play, Account;
    View mainView;
    private GestureDetector gestureDetector;
    List<Song> allSong;
    RecyclerView recyclerViewPlayList;
    SongSearchResultAdapter adapterAllSong;
    ImageView ivClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_play_list);
        setcontrol();

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        if (SongListSingleton.getInstance().hasSong()){
            allSong = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> songList) {
                    allSong = songList;
                }
            });
        }

        adapterAllSong = new SongSearchResultAdapter(allSong);
        LinearLayoutManager layoutManagerAllSong = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewPlayList.setLayoutManager(layoutManagerAllSong);
        recyclerViewPlayList.setAdapter(adapterAllSong);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayList.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(anim.slide_in_down, anim.slide_out_up);
                finish();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayList.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayList.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(anim.slide_in_right, anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayList.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(anim.slide_in_right, anim.slide_out_left);
                finish();
            }
        });

        adapterAllSong.setOnItemClickListener(new SongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.PlayList.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(anim.slide_in_right, anim.slide_out_left);
                finish();

                // Cập nhật RecyclerView với 10 bài hát tiếp theo
                updateRecyclerViewWithNextSongs(id);
            }
        });
    }

    public void updateRecyclerViewWithNextSongs(int currentSongId) {
        // Tìm vị trí của bài hát đang phát trong danh sách tất cả các bài hát
        int currentSongIndex = -1;
        for (int i = 0; i < allSong.size(); i++) {
            if (allSong.get(i).getId() == currentSongId) {
                currentSongIndex = i;
                break;
            }
        }

        // Kiểm tra xem có thể lấy thêm 10 bài hát tiếp theo hay không
        if (currentSongIndex != -1 && currentSongIndex + 10 <= allSong.size()) {
            // Lấy danh sách 10 bài hát tiếp theo
            List<Song> nextSongs = allSong.subList(currentSongIndex + 1, currentSongIndex + 11);

            // Cập nhật RecyclerView với danh sách mới
            adapterAllSong.setmData(nextSongs);
            adapterAllSong.notifyDataSetChanged();
        }
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
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        // Swipe down, switch to another activity
                        overridePendingTransition(anim.slide_in_down, anim.slide_out_up);
                        Intent intent = new Intent(PlayList.this, PlayView.class);
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

    public void setcontrol() {
        recyclerViewPlayList = findViewById(id.recyclerViewPlayList);
        Home = findViewById(id.imageViewHome);
        Play = findViewById(id.imageViewHeadPhone);
        Account = findViewById(id.imageViewAccount);
        mainView = findViewById(id.PlayListView);
        gestureDetector = new GestureDetector(this, new GestureListener());
        ivClose = findViewById(id.ivClose);
    }
}