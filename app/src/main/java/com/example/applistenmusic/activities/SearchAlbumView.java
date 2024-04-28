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
import com.example.applistenmusic.adapters.AlbumAdapter;
import com.example.applistenmusic.adapters.AlbumSearchResultAdapter;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.GenresHelper;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.singletons.AlbumSingleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchAlbumView extends AppCompatActivity {
    ImageView Feature, Home,Search,Play,Account;
    EditText searchEditText;
    TextView textViewSearchResult;
    List<Album> allAlbum, SearchAlbum;
    RecyclerView  recyclerViewAllAlbum, recyclerViewSearchResult;
    AlbumSearchResultAdapter adapterSearchResult , adapterAllAlbum;
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_search_album);
        setcontrol();

        SearchAlbum = new ArrayList<>();
        if (AlbumSingleton.getInstance().hasAlbum()){
            allAlbum = AlbumSingleton.getInstance().getAllAlbumIfExist();
        } else {
            AlbumSingleton.getInstance().getAllAlbum(new AlbumLoadListener() {
                @Override
                public void onAlbumLoaded(List<Album> AlbumList) {
                    allAlbum = AlbumList;
                }
            });
        }


        adapterSearchResult = new AlbumSearchResultAdapter(SearchAlbum);
        LinearLayoutManager layoutManagerSearchResult = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchResult.setLayoutManager(layoutManagerSearchResult);
        recyclerViewSearchResult.setAdapter(adapterSearchResult);

        adapterAllAlbum = new AlbumSearchResultAdapter(allAlbum);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAllAlbum.setLayoutManager(layoutManagerTrending);
        recyclerViewAllAlbum.setAdapter(adapterAllAlbum);


        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchAlbumView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchAlbumView.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchAlbumView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });

        adapterAllAlbum.setOnItemClickListener(new AlbumSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Album album = AlbumHelper.getAlbumByID(id);
                Intent playIntent = new Intent(SearchAlbumView.this, AlbumDetailView.class);
                AlbumSingleton.getInstance().setAlbum(album);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        adapterSearchResult.setOnItemClickListener(new AlbumSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Album album = AlbumHelper.getAlbumByID(id);
                Intent playIntent = new Intent(SearchAlbumView.this, AlbumDetailView.class);
                AlbumSingleton.getInstance().setAlbum(album);
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
                performSearch(keyword,allAlbum);
            }
        });



    }
    private void performSearch(String keyword, List<Album> allAlbum) {

        if (!keyword.isEmpty() ){
            Set<Album> set = new HashSet<>();
            for(Album Album : allAlbum) {
                if (Album.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                    set.add(Album);
                }

            }

            if(!ArtistHelper.getArtistIDByArtistName(keyword).isEmpty()){
                set.addAll(AlbumHelper.getAlbumByArtist(ArtistHelper.getArtistIDByArtistName(keyword)));
            }

            if(!SongHelper.getSongIDListByName(keyword).isEmpty()){
                set.addAll(AlbumHelper.getAlbumBySong(SongHelper.getSongIDListByName(keyword)));
            }

            List<Album> result = new ArrayList<>(set);

            textViewSearchResult.setVisibility(View.VISIBLE);
            recyclerViewSearchResult.setVisibility(View.VISIBLE);
            adapterSearchResult.setmData(result);
        } else {
            textViewSearchResult.setVisibility(View.INVISIBLE);
            recyclerViewSearchResult.setVisibility(View.INVISIBLE);
            adapterSearchResult.setmData(new ArrayList<>());
        }


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
        recyclerViewAllAlbum = findViewById(R.id.recyclerViewAllAlbum);
        recyclerViewSearchResult = findViewById(R.id.recyclerViewInSearchResult);
        textViewSearchResult = findViewById(R.id.textViewSearchResult);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        searchEditText = findViewById(R.id.searchEditText);
    }
}