package com.example.applistenmusic.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.MenuAdapter;
import com.example.applistenmusic.adapters.SongAdapter;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.MenuItem;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Home extends AppCompatActivity {
    ImageView HomeFeature, HomeFeatureClose, Home,Search,Play,Account;
    LinearLayout menu;
    List<MenuItem> menuItems;
    List<Song> allSong, KpopSong, VpopSong, USUKSong, TrendingSong;
    RecyclerView recyclerViewKpopSong, recyclerViewUSUKSong, recyclerViewVpopSong, recyclerViewTrendingSong, recyclerViewMenuBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_home);
        setcontrol();
        KpopSong = new ArrayList<>();
        VpopSong = new ArrayList<>();
        USUKSong = new ArrayList<>();
        TrendingSong = new ArrayList<>();
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(1,"Playlists","playlist"));
        menuItems.add(new MenuItem(2,"Artists","artist"));
        menuItems.add(new MenuItem(3,"Songs","song"));
        menuItems.add(new MenuItem(4,"Albums","album"));
        menuItems.add(new MenuItem(5,"Downloaded","downloaded"));

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

        Iterator<Song> iterator = allSong.iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            // Kpop
            if(song.getGenres()==1){
                KpopSong.add(song);
            }
            // Vpop
            if(song.getGenres()==31){
                VpopSong.add(song);
            }
            // USUK
            if(song.getGenres()==32){
                USUKSong.add(song);
            }
        }

        SongAdapter adapterKpopSong = new SongAdapter(KpopSong);
        SongAdapter adapterVpopSong = new SongAdapter(VpopSong);
        SongAdapter adapterUSUKSong = new SongAdapter(USUKSong);
        SongAdapter adapterTrendingSong = new SongAdapter(allSong);
        MenuAdapter adapterMenuItem = new MenuAdapter(menuItems);
        LinearLayoutManager layoutManagerKpop = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewKpopSong.setLayoutManager(layoutManagerKpop);
        recyclerViewKpopSong.setAdapter(adapterKpopSong);
        LinearLayoutManager layoutManagerVpop = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVpopSong.setLayoutManager(layoutManagerVpop);
        recyclerViewVpopSong.setAdapter(adapterVpopSong);
        LinearLayoutManager layoutManagerUSUK = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUSUKSong.setLayoutManager(layoutManagerUSUK);
        recyclerViewUSUKSong.setAdapter(adapterUSUKSong);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrendingSong.setLayoutManager(layoutManagerTrending);
        recyclerViewTrendingSong.setAdapter(adapterTrendingSong);
        LinearLayoutManager layoutManagerMenuItem = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMenuBar.setLayoutManager(layoutManagerMenuItem);
        recyclerViewMenuBar.setAdapter(adapterMenuItem);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(Home.this, SearchView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });

        HomeFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.INVISIBLE);
                    adapterMenuItem.setmData(new ArrayList<>());
                } else {
                    menu.setVisibility(View.VISIBLE);
                    adapterMenuItem.setmData(menuItems);
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        HomeFeatureClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.INVISIBLE);
                    adapterMenuItem.setmData(new ArrayList<>());
                } else {
                    menu.setVisibility(View.VISIBLE);
                    adapterMenuItem.setmData(menuItems);
                }
            }
        });

        adapterKpopSong.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        adapterVpopSong.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        adapterUSUKSong.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        adapterTrendingSong.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


        adapterMenuItem.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                switch (id){
                    //playlist
                    case 1: {
                        Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // Arist
                    case 2:{
                        Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // Song
                    case 3: {
                        Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // album
                    case 4: {
                        Intent playIntent = new Intent(Home.this, SearchAlbumView.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // download
                    case 5: {
                        Intent playIntent = new Intent(com.example.applistenmusic.activities.Home.this, PlayView.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                }

            }
        });



    }

    public void setcontrol() {
        recyclerViewKpopSong = findViewById(R.id.recyclerViewInKPOP);
        recyclerViewVpopSong = findViewById(R.id.recyclerViewInVpop);
        recyclerViewUSUKSong = findViewById(R.id.recyclerViewInUSUK);
        recyclerViewTrendingSong = findViewById(R.id.recyclerViewInTrendingNow);
        recyclerViewMenuBar = findViewById(R.id.recyclerViewMenuBar);
        menu = findViewById(R.id.menu);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        HomeFeature = findViewById(R.id.imageView2);
        HomeFeatureClose =  findViewById(R.id.imageViewMenuClose);

    }
}