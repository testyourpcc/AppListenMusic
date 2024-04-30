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
import com.example.applistenmusic.adapters.AlbumSearchResultAdapter;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.PlayListLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.singletons.AlbumSingleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.applistenmusic.adapters.PlayListAdapter;
import com.example.applistenmusic.helpers.PlayListHelper;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.singletons.PlayListSingleton;

public class SearchPlayList extends AppCompatActivity {
    ImageView Feature, Home,Search,Play,Account;
    EditText searchEditText;
    TextView textViewSearchResult;
    List<PlayList> allPlayList, SearchPlayList;
    RecyclerView  recyclerViewAllPlayList, recyclerViewSearchResult;
    PlayListAdapter adapterSearchResult , adapterAllPlayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_play_list);
        setcontrol();


        SearchPlayList = new ArrayList<>();
        if (PlayListSingleton.getInstance().hasPlayList()){
            allPlayList = PlayListSingleton.getInstance().getAllPlayListIfExist();
        } else {
            PlayListSingleton.getInstance().getAllPlayList(new PlayListLoadListener() {
                @Override
                public void onPlayListLoaded(List<PlayList> playList) {
                    allPlayList = playList;
                }
            });
        }

        adapterSearchResult = new PlayListAdapter(SearchPlayList);
        LinearLayoutManager layoutManagerSearchResult = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchResult.setLayoutManager(layoutManagerSearchResult);
        recyclerViewSearchResult.setAdapter(adapterSearchResult);

        adapterAllPlayList = new PlayListAdapter(allPlayList);
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

//        adapterAllPlayList.setOnItemClickListener(new AlbumSearchResultAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int id) {
//                Album album = AlbumHelper.getAlbumByID(id);
//                Intent playIntent = new Intent(SearchPlayList.this, AlbumDetailView.class);
//                AlbumSingleton.getInstance().setAlbum(album);
//                startActivity(playIntent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//
//            @Override
//            public void onButtonClick(int id) {
//
//            }
//        });
//        adapterSearchResult.setOnItemClickListener(new AlbumSearchResultAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int id) {
//                Album album = AlbumHelper.getAlbumByID(id);
//                Intent playIntent = new Intent(SearchAlbumView.this, AlbumDetailView.class);
//                AlbumSingleton.getInstance().setAlbum(album);
//                startActivity(playIntent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//
//            @Override
//            public void onButtonClick(int id) {
//
//            }
//        });
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Xử lý tìm kiếm bài hát khi người dùng thay đổi nội dung của EditText
//                String keyword = s.toString();
//                // Thực hiện tìm kiếm bài hát dựa trên keyword ở đây
//                performSearch(keyword,allPlayList);
//            }
//        });

    }
//    private void performSearch(String keyword, List<Album> allAlbum) {
//
//        if (!keyword.isEmpty() ){
//            Set<Album> set = new HashSet<>();
//            for(Album Album : allAlbum) {
//                if (Album.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
//                    set.add(Album);
//                }
//
//            }
//
//            if(!ArtistHelper.getArtistIDByArtistName(keyword).isEmpty()){
//                set.addAll(AlbumHelper.getAlbumByArtist(ArtistHelper.getArtistIDByArtistName(keyword)));
//            }
//
//            if(!SongHelper.getSongIDListByName(keyword).isEmpty()){
//                set.addAll(AlbumHelper.getAlbumBySong(SongHelper.getSongIDListByName(keyword)));
//            }
//
//            List<Album> result = new ArrayList<>(set);
//
//            textViewSearchResult.setVisibility(View.VISIBLE);
//            recyclerViewSearchResult.setVisibility(View.VISIBLE);
//            adapterSearchResult.setmData(result);
//        } else {
//            textViewSearchResult.setVisibility(View.INVISIBLE);
//            recyclerViewSearchResult.setVisibility(View.INVISIBLE);
//            adapterSearchResult.setmData(new ArrayList<>());
//        }
//
//    }

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