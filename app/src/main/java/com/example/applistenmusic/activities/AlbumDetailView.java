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
import com.example.applistenmusic.adapters.SongSearchResultAdapter;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailView extends AppCompatActivity {
    ImageView Feature, Home,Search,Play,Account, AlbumImage;
    Button btnPlay, btnShuffle;
    TextView textViewAlbumName, textViewSongSize;
    List<Song> allSongInAlbum, allSong;
    Album album;
    RecyclerView recyclerViewAllSongInAlbum;
    SongSearchResultAdapter adapterAllSongInAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        setcontrol();
        allSongInAlbum = new ArrayList<>();
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
        album = AlbumSingleton.getInstance().getAlbum();
        if(album != null) {
            textViewAlbumName.setText(album.getName());
            Glide.with(this)
                    .load(album.getImage())
                    .override(300, 300) // Kích thước mới
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(AlbumImage);
            textViewSongSize.setText(album.getSongIdList().size() - 1  + " bài hát");
            for (Long songid : album.getSongIdList()) {
                if(songid != null) {
                    allSongInAlbum.add(SongHelper.getSongById(allSong, songid.intValue()));
                }
            }

        }
        adapterAllSongInAlbum = new SongSearchResultAdapter(allSongInAlbum);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAllSongInAlbum.setLayoutManager(layoutManagerTrending);
        recyclerViewAllSongInAlbum.setAdapter(adapterAllSongInAlbum);


        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(AlbumDetailView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(AlbumDetailView.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(AlbumDetailView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(AlbumDetailView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(allSong,album.getSongIdList().get(1).intValue()));
                playIntent.putExtra("playNow", true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(AlbumDetailView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(allSong,album.getSongIdList().get(1).intValue()));
                playIntent.putExtra("playNow", true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });


        adapterAllSongInAlbum.setOnItemClickListener(new SongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(AlbumDetailView.this, PlayView.class);
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
        recyclerViewAllSongInAlbum = findViewById(R.id.recyclerViewAllSongInAlbum);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        btnPlay = findViewById(R.id.buttonPlay);
        btnShuffle = findViewById(R.id.buttonShuffle);
        Account = findViewById(R.id.imageViewAccount);
        textViewAlbumName = findViewById(R.id.AlbumNameTextView);
        AlbumImage = findViewById(R.id.AlbumImage);
        textViewSongSize = findViewById(R.id.textViewSongSize);
    }
}