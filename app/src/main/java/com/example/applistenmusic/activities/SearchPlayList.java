package com.example.applistenmusic.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.AlbumSearchResultAdapter;
import com.example.applistenmusic.adapters.PlayListSongSearchResultAdapter;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.interfaces.PlayListFetchListener;
import com.example.applistenmusic.interfaces.PlayListLoadListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.applistenmusic.adapters.PlayListAdapter;
import com.example.applistenmusic.helpers.PlayListHelper;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.PlayListSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SearchPlayList extends AppCompatActivity {
    ImageView Feature, Home,Search,Play,Account;
    EditText searchEditText;
    TextView textViewSearchResult;
    List<PlayList> allPlayList, SearchPlayList;
    RecyclerView  recyclerViewAllPlayList, recyclerViewSearchResult;
    PlayListSongSearchResultAdapter adapterSearchResult , adapterAllPlayList;
    List<Song> allSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_play_list);
        setcontrol();


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

        if(PlayListSingleton.getInstance().hasPlayList()){
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener() {
                @Override
                public void onPlayListLoaded(List<PlayList> songList) {
                    allPlayList = songList;
                }
            });
        }

        SearchPlayList = new ArrayList<>();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            PlayListHelper.getPlayListByUserId(userId, new PlayListFetchListener() {
//                @Override
//                public void onPlayListFetched(List<PlayList> playLists) {
//                    allPlayList = playLists;
//                    adapterAllPlayList = new PlayListAdapter(allPlayList);
//                    LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(SearchPlayList.this, LinearLayoutManager.VERTICAL, false);
//                    recyclerViewAllPlayList.setLayoutManager(layoutManagerTrending);
//                    recyclerViewAllPlayList.setAdapter(adapterAllPlayList);
//                }
//            });
//        } else {
//            allPlayList = new ArrayList<>();
//        }

        adapterSearchResult = new PlayListSongSearchResultAdapter(SearchPlayList);
        LinearLayoutManager layoutManagerSearchResult = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchResult.setLayoutManager(layoutManagerSearchResult);
        recyclerViewSearchResult.setAdapter(adapterSearchResult);

        adapterAllPlayList = new PlayListSongSearchResultAdapter(allPlayList);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAllPlayList.setLayoutManager(layoutManagerTrending);
        recyclerViewAllPlayList.setAdapter(adapterAllPlayList);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchPlayList.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchPlayList.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchPlayList.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });

        adapterAllPlayList.setOnItemClickListener(new PlayListSongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                PlayList playList = PlayListHelper.getPlayListByID(id);
                if (playList != null) {
                    PlayListSingleton.getInstance().setPlayList(playList);
                    Intent playIntent = new Intent(SearchPlayList.this, PlayListDetailView.class);
                    startActivity(playIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }

            @Override
            public void onButtonClick(int id) {
                // Implement your logic here when button is clicked
            }
        });

        adapterSearchResult.setOnItemClickListener(new PlayListSongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Album album = AlbumHelper.getAlbumByID(id);
                Intent playIntent = new Intent(SearchPlayList.this, AlbumDetailView.class);
                AlbumSingleton.getInstance().setAlbum(album);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }

            @Override
            public void onButtonClick(int id) {

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
                performSearch(keyword,allPlayList);
            }
        });

    }
    private void performSearch(String keyword, List<PlayList> allAlbum) {

        if (!keyword.isEmpty() ){
            Set<PlayList> set = new HashSet<>();
            for(PlayList Album : allAlbum) {
                if (Album.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                    set.add(Album);
                }

            }

            if(!ArtistHelper.getArtistIDByArtistName(keyword).isEmpty()){
              //  set.addAll();
            }

            if(!SongHelper.getSongIDListByName(keyword).isEmpty()){
              //  set.addAll(AlbumHelper.getAlbumBySong(SongHelper.getSongIDListByName(keyword)));
            }

            List<PlayList> result = new ArrayList<>(set);

            textViewSearchResult.setVisibility(View.VISIBLE);
            recyclerViewSearchResult.setVisibility(View.VISIBLE);
            adapterSearchResult.setmData(result);
        } else {
            textViewSearchResult.setVisibility(View.INVISIBLE);
            recyclerViewSearchResult.setVisibility(View.INVISIBLE);
            adapterSearchResult.setmData(new ArrayList<>());
        }

    }
    public void setcontrol() {
        recyclerViewAllPlayList = findViewById(R.id.recyclerViewAllPlayList);
        recyclerViewSearchResult = findViewById(R.id.recyclerViewInSearchResult);
        textViewSearchResult = findViewById(R.id.textViewSearchResult);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        searchEditText = findViewById(R.id.searchEditText);
    }
}
