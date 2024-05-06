package com.example.applistenmusic.activities;

import static com.google.gson.internal.$Gson$Types.arrayOf;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.DownloadAdapter;
import com.example.applistenmusic.adapters.SongSearchResultAdapter;
import com.example.applistenmusic.helpers.SongDownloadHelper;
import com.example.applistenmusic.helpers.SongHelper;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.models.SongDownload;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.example.applistenmusic.singletons.SongSingleton;

import org.apache.tika.Tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchDownload extends AppCompatActivity {
    ImageView Feature, Home, Search, Play, Account;
    Button btnPlay, btnShuffle;
    TextView textViewSongSize;
    List<SongDownload> allSongInPlayList;
    RecyclerView recyclerViewAllSongInPlayList;
    ImageView ivBack;

    private static final int MY_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_download);
        setcontrol();
        setupListeners();
        allSongInPlayList = new ArrayList<>();
        recyclerViewAllSongInPlayList.setLayoutManager(new LinearLayoutManager(this));
        if (ContextCompat.checkSelfPermission(SearchDownload.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SearchDownload.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SearchDownload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(SearchDownload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {

        }
        loadSongs();

    }


    private void setupListeners() {
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchDownload.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchDownload.this, PlayView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchDownload.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchDownload.this, PlayView.class);
                SongDownload song = SongDownloadHelper.getSongByFilePath(allSongInPlayList, "filePath"); // replace "filePath" with the actual file path
                playIntent.putExtra("song", SongDownloadHelper.songToBundle(song));
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchDownload.this, PlayView.class);
                SongDownload song = SongDownloadHelper.getSongByFilePath(allSongInPlayList, "filePath"); // replace "filePath" with the actual file path
                playIntent.putExtra("song", SongDownloadHelper.songToBundle(song));
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SearchDownload.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    public void setcontrol() {
        recyclerViewAllSongInPlayList = findViewById(R.id.recyclerViewAllSongInDownload);
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        btnPlay = findViewById(R.id.buttonPlay);
        btnShuffle = findViewById(R.id.buttonShuffle);
        Account = findViewById(R.id.imageViewAccount);
        textViewSongSize = findViewById(R.id.textViewSongSize);
        ivBack = findViewById(R.id.ivBack);
    }


    private void loadSongs() {
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File songsFolder = new File(downloadFolder, "songs");
        File[] files1 = songsFolder.listFiles();
        if (files1 != null) {
            for (File file : files1) {
                if (file.getName().endsWith(".mp3")) {
                    Log.d("DownloadFolder", "File name: " + file.getName());
                    SongDownload song = SongDownload.fromFile(file); // Assuming you have a method to convert a File to a Song
                    allSongInPlayList.add(song);
                    // Set the text for the TextView
                    textViewSongSize.setText( allSongInPlayList.size()+" bài hát");
                }
            }
        }
        DownloadAdapter downloadAdapter = new DownloadAdapter(allSongInPlayList);
        downloadAdapter.setOnItemClickListener(new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SongDownload clickedSong = allSongInPlayList.get(position);
                // Handle the click event here
                // For example, start a new activity and pass the clicked song data
                Intent playIntent = new Intent(SearchDownload.this, PlayView.class);
                playIntent.putExtra("song", SongDownloadHelper.songToBundle(clickedSong));
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onButtonClick(int position) {
                // Handle the button click event here
            }
        });
        recyclerViewAllSongInPlayList.setAdapter(downloadAdapter);
    }



}