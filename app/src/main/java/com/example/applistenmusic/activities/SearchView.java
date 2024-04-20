package com.example.applistenmusic.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.SongAdapter;
import com.example.applistenmusic.dialogs.AlertDialogManager;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.GenresHelper;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchView extends AppCompatActivity {
    ImageView Feature, Home,Search,Play,Account;
    EditText searchEditText;
    TextView textViewSearchResult;
    List<Song> allSong, KpopSong, VpopSong, USUKSong, TrendingSong;
    RecyclerView recyclerViewKpopSong, recyclerViewUSUKSong, recyclerViewVpopSong, recyclerViewTrendingSong, recyclerViewSearchResult;

    SongAdapter adapterKpopSong, adapterVpopSong, adapterUSUKSong,adapterTrendingSong, adapterSearchResult;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_search);
        setcontrol();

        KpopSong = new ArrayList<>();
        VpopSong = new ArrayList<>();
        USUKSong = new ArrayList<>();
        TrendingSong = new ArrayList<>();
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

        adapterKpopSong = new SongAdapter(KpopSong);
        LinearLayoutManager layoutManagerKpop = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewKpopSong.setLayoutManager(layoutManagerKpop);
        recyclerViewKpopSong.setAdapter(adapterKpopSong);

        adapterVpopSong = new SongAdapter(VpopSong);
        LinearLayoutManager layoutManagerVpop = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVpopSong.setLayoutManager(layoutManagerVpop);
        recyclerViewVpopSong.setAdapter(adapterVpopSong);

        adapterUSUKSong = new SongAdapter(USUKSong);
        LinearLayoutManager layoutManagerUSUK = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUSUKSong.setLayoutManager(layoutManagerUSUK);
        recyclerViewUSUKSong.setAdapter(adapterUSUKSong);

        adapterTrendingSong = new SongAdapter(allSong);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrendingSong.setLayoutManager(layoutManagerTrending);
        recyclerViewTrendingSong.setAdapter(adapterTrendingSong);

        adapterSearchResult = new SongAdapter(USUKSong);
        LinearLayoutManager layoutManagerSearchResult = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSearchResult.setLayoutManager(layoutManagerSearchResult);
        recyclerViewSearchResult.setAdapter(adapterSearchResult);




        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchView.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });


        adapterKpopSong.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
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
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
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
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
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
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        adapterSearchResult.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Xử lý tìm kiếm bài hát khi người dùng thay đổi nội dung của EditText
                String keyword = s.toString();
                // Thực hiện tìm kiếm bài hát dựa trên keyword ở đây
                performSearch(keyword,allSong);
            }
        });



    }
    private void performSearch(String keyword, List<Song> allSong) {
        List<Song> result = new ArrayList<>();
        for(Song song : allSong){
            if(song.getName().toLowerCase().contains(keyword.trim().toLowerCase())){
                result.add(song);
                continue;
            }

//            if(ArtistHelper.containKeyWord(keyword.trim())){
//                result.add(song);
//                continue;
//            }
//            if(AlbumHelper.containKeyWord(keyword.trim())){
//                result.add(song);
//                continue;
//            }
//            if(GenresHelper.containKeyWord(keyword.trim())){
//                result.add(song);
//            }

        }
        textViewSearchResult.setVisibility(View.VISIBLE);
        recyclerViewSearchResult.setVisibility(View.VISIBLE);
        adapterSearchResult.setmData(result);


    }
    public static boolean canParseLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void setcontrol() {
        recyclerViewKpopSong = findViewById(R.id.recyclerViewInKPOP);
        recyclerViewVpopSong = findViewById(R.id.recyclerViewInVpop);
        recyclerViewUSUKSong = findViewById(R.id.recyclerViewInUSUK);
        recyclerViewTrendingSong = findViewById(R.id.recyclerViewInTrendingNow);
        recyclerViewSearchResult = findViewById(R.id.recyclerViewInSearchResult);
        textViewSearchResult = findViewById(R.id.textViewSearchResult);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        searchEditText = findViewById(R.id.searchEditText);
    }
}