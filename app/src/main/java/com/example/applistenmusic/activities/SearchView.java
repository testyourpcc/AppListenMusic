package com.example.applistenmusic.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.MenuAdapter;
import com.example.applistenmusic.adapters.SongAdapter;
import com.example.applistenmusic.adapters.SongSearchResultAdapter;
import com.example.applistenmusic.dialogs.ConfirmDialogManager;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.GenresHelper;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.models.MenuItem;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchView extends AppCompatActivity {
    ImageView Feature, Home,Search,Play,Account;
    EditText searchEditText;
    TextView textViewSearchResult;
    List<Song> allSong, USUKSong, TrendingSong;
    RecyclerView recyclerViewTrendingSong, recyclerViewSearchResult, recyclerViewMenubar;
    SongSearchResultAdapter  adapterSearchResult , adapterTrendingSong;
    MenuAdapter adapterMenuBar;
    List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setcontrol();

        USUKSong = new ArrayList<>();
        TrendingSong = new ArrayList<>();
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(1,"Add to Playlist","playlist"));
        menuItems.add(new MenuItem(2,"Show Song info","artist"));
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
            if(song.getGenres()==32){
                USUKSong.add(song);
            }
        }

        adapterSearchResult = new SongSearchResultAdapter(USUKSong);
        LinearLayoutManager layoutManagerSearchResult = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSearchResult.setLayoutManager(layoutManagerSearchResult);
        recyclerViewSearchResult.setAdapter(adapterSearchResult);

        adapterTrendingSong = new SongSearchResultAdapter(allSong);
        LinearLayoutManager layoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTrendingSong.setLayoutManager(layoutManagerTrending);
        recyclerViewTrendingSong.setAdapter(adapterTrendingSong);

        adapterMenuBar = new MenuAdapter(menuItems);
        LinearLayoutManager layoutMenuItems = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMenubar.setLayoutManager(layoutMenuItems);
        recyclerViewMenubar.setAdapter(adapterMenuBar);
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

        adapterTrendingSong.setOnItemClickListener(new SongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }

            @Override
            public void onButtonClick(int id, View view) {
                if (recyclerViewMenubar.getVisibility() == View.VISIBLE) {
                    recyclerViewMenubar.setVisibility(View.INVISIBLE);
                    adapterMenuBar.setmData(new ArrayList<>());
                } else {
                    recyclerViewMenubar.setVisibility(View.VISIBLE);
                    adapterMenuBar.setmData(menuItems);
                    adapterMenuBar.setSongId(id);
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    int x = location[0];
                    int y = location[1];

                    // Set vị trí mới cho RecyclerView
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recyclerViewMenubar.getLayoutParams();
                    layoutParams.leftMargin = x - 510; // Vị trí x - chiều rộng RecyclerView
                    layoutParams.topMargin = y; // Vị trí y

                    recyclerViewMenubar.setLayoutParams(layoutParams);
                }
            }
        });
        adapterSearchResult.setOnItemClickListener(new SongSearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent playIntent = new Intent(com.example.applistenmusic.activities.SearchView.this, PlayView.class);
                SongSingleton.getInstance().setSong(SongHelper.getSongById(SongListSingleton.getInstance().getAllSongIfExist(),id));
                playIntent.putExtra("playNow",true);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }

            @Override
            public void onButtonClick(int id, View view) {
                if (recyclerViewMenubar.getVisibility() == View.VISIBLE) {
                    recyclerViewMenubar.setVisibility(View.INVISIBLE);
                    adapterMenuBar.setmData(new ArrayList<>());
                } else {
                    recyclerViewMenubar.setVisibility(View.VISIBLE);
                    adapterMenuBar.setmData(menuItems);
                    adapterMenuBar.setSongId(id);
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    int x = location[0];
                    int y = location[1];

                    // Set vị trí mới cho RecyclerView
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recyclerViewMenubar.getLayoutParams();
                    layoutParams.leftMargin = x - 510; // Vị trí x - chiều rộng RecyclerView
                    layoutParams.topMargin = y; // Vị trí y

                    recyclerViewMenubar.setLayoutParams(layoutParams);
                }
            }

        });

        adapterMenuBar.setOnItemClickListener(new MenuAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int id, int songId) {
                adapterMenuBar.setmData(new ArrayList<>());
                switch (id) {
                    //edit
                    case 1: {
//                        Intent playIntent = new Intent(SearchView.this, SongEdit.class);
//                        playIntent.putExtra("id",songId);
//                        startActivity(playIntent);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        finish();
//                        break;
                    }
                    // delete
                    case 2: {
//                        ConfirmDialogManager.showDialog(SearchView.this, "Xác nhận xóa", "Bài hát sẽ bị xóa vĩnh viễn, bạn chắc chứ?", new ConfirmDialogManager.OnClickListener() {
//                            @Override
//                            public void onCancel() {
//                            }
//
//                            @Override
//                            public void onOK() {
//
//                            }
//                        });
//                        break;
                    }
                }
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
        Set<Song> set = new HashSet<>();
        for(Song song : allSong){
            if(song.getName().toLowerCase().contains(keyword.trim().toLowerCase())){
                set.add(song);
            }
        }

        if(!ArtistHelper.getArtistIDByArtistName(keyword).isEmpty()){
            set.addAll(SongHelper.getSongByArtist(ArtistHelper.getArtistIDByArtistName(keyword)));
        }
        if(!AlbumHelper.getAlbumIDByAlbumName(keyword).isEmpty()){
            set.addAll(SongHelper.getSongByAlbum(AlbumHelper.getAlbumIDByAlbumName(keyword)));
        }
        if(!GenresHelper.getGenresIDByGenresName(keyword).isEmpty()){
            set.addAll(SongHelper.getSongByGenres(GenresHelper.getGenresIDByGenresName(keyword)));
        }

        List<Song> result = new ArrayList<>(set);

        textViewSearchResult.setVisibility(View.VISIBLE);
        recyclerViewSearchResult.setVisibility(View.VISIBLE);
        adapterSearchResult.setmData(result);

    }

    public void setcontrol() {
        recyclerViewTrendingSong = findViewById(R.id.recyclerViewInTrendingNow);
        recyclerViewSearchResult = findViewById(R.id.recyclerViewInSearchResult);
        textViewSearchResult = findViewById(R.id.textViewSearchResult);
        recyclerViewMenubar = findViewById(R.id.recyclerViewMenuBar);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        searchEditText = findViewById(R.id.searchEditText);
    }
}