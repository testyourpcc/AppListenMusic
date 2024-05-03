package com.example.applistenmusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.PlayListSongSearchResultAdapter;
import com.example.applistenmusic.adapters.SongSearchResultAdapter;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.PlayListSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import java.util.ArrayList;
import java.util.List;

public class PlayListDetailView extends AppCompatActivity {
    ImageView Feature, Home, Search, Play, Account, PlayListImage;
    Button btnPlay, btnShuffle;
    TextView textViewPlayListName, textViewSongSize;
    List<Song> allSongInPlayList, allSong;
    PlayList playList;
    RecyclerView recyclerViewAllSongInPlayList;
    SongSearchResultAdapter adapterAllSongInPlayList;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_detail_view);
        setcontrol();
        allSongInPlayList = new ArrayList<>();
        if (SongListSingleton.getInstance().hasSong()) {
            allSong = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> songList) {
                    allSong = songList;
                }
            });
        }
        playList = PlayListSingleton.getInstance().getPlayList();
        if(playList != null) {
            textViewPlayListName.setText(playList.getName());
            Glide.with(this)
                    .load(playList.getImage())
                    .override(300, 300) // Kích thước mới
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(PlayListImage);
            textViewSongSize.setText(playList.getSongIdList().size()   + " bài hát");
            for (Integer songid : playList.getSongIdList()) {
                if(songid != null) {
                    if(!SongHelper.getSongById(allSong, songid.intValue()).equals(new Song())){
                        allSongInPlayList.add(SongHelper.getSongById(allSong, songid.intValue()));
                    }
                }
            }

        }
        adapterAllSongInPlayList = new SongSearchResultAdapter(allSongInPlayList);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAllSongInPlayList.setLayoutManager(layoutManagerTrending);
        recyclerViewAllSongInPlayList.setAdapter(adapterAllSongInPlayList);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayListDetailView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayListDetailView.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayListDetailView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayListDetailView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(allSong,playList.getSongIdList().get(1).intValue()));
                playIntent.putExtra("playNow", true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayListDetailView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(allSong,playList.getSongIdList().get(1).intValue()));
                playIntent.putExtra("playNow", true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayListDetailView.this, SearchPlayList.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });


        adapterAllSongInPlayList.setOnItemClickListener(new SongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(PlayListDetailView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(), id));
                playIntent.putExtra("playNow", true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }

            @Override
            public void onButtonClick(int id, View view) {

            }

        });

    }

    public void setcontrol() {
        recyclerViewAllSongInPlayList = findViewById(R.id.recyclerViewAllSongInPlayList);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        btnPlay = findViewById(R.id.buttonPlay);
        btnShuffle = findViewById(R.id.buttonShuffle);
        Account = findViewById(R.id.imageViewAccount);
        textViewPlayListName = findViewById(R.id.PlayListNameTextView);
        PlayListImage = findViewById(R.id.PlayListImage);
        textViewSongSize = findViewById(R.id.textViewSongSize);
        ivBack = findViewById(R.id.ivBack);
    }
}