package com.example.applistenmusic.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.applistenmusic.R;
import com.example.applistenmusic.interfaces.AlbumLoadListener;
import com.example.applistenmusic.interfaces.ArtistLoadListener;
import com.example.applistenmusic.interfaces.DataLoadListener;
import com.example.applistenmusic.interfaces.GenresLoadListener;
import com.example.applistenmusic.models.Album;
import com.example.applistenmusic.models.Artist;
import com.example.applistenmusic.models.Genres;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.ArtistSingleton;
import com.example.applistenmusic.singletons.GenresSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAddSong extends AppCompatActivity {
    ImageView Feature, Home,Account, btnBack;
    private EditText edtPlaylistName;
    private Button buttonAdd, buttonCancel;
    RelativeLayout mainLayout;
    Spinner spinnerGenres, spinnerAlbum, spinnerArtist;
    List<Song> songList;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Lấy thông tin người dùng hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_add_song);
        setControl();
        if (SongListSingleton.getInstance().hasSong()){
            songList = SongListSingleton.getInstance().getAllSongIfExist();
        } else {
            SongListSingleton.getInstance().getAllSong(new DataLoadListener() {
                @Override
                public void onDataLoaded(List<Song> List) {
                    songList = List;
                }
            });
        }


        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Ẩn bàn phím khi chạm vào vị trí khác ngoài EditText
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        // Xử lý khi nhấn nút Save
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
                Toast.makeText(PlaylistAddSong.this, "Playlist added successfully", Toast.LENGTH_SHORT).show();
                Intent playIntent = new Intent(PlaylistAddSong.this, SearchPlayList.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        // Xử lý khi nhấn nút Cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOperation();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlaylistAddSong.this, SearchPlayList.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
    }

    private void saveData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = user.getUid(); // replace with actual user ID
        DatabaseReference userPlaylistsRef = database.getReference("playList").child(userId);

        // Get the playlist name entered by the user
        String playlistName = edtPlaylistName.getText().toString();

        // Create a new Playlist object
        PlayList playlist = new PlayList();
        playlist.setName(playlistName);
        List<Integer> songIdList = new ArrayList<>();
        songIdList.add(0); // Add a child "1" with value "0"
        playlist.setSongIdList(songIdList);
        playlist.setUser(userId);

        // Generate a unique key for the new child node
        userPlaylistsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                String newPlaylistId = String.valueOf(count + 1);
                playlist.setId(Integer.parseInt(newPlaylistId));

                // Save the Playlist object to Firebase
                userPlaylistsRef.child(newPlaylistId).setValue(playlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Phương thức xử lý khi nhấn nút Cancel
    private void cancelOperation() {
        Intent playIntent = new Intent(PlaylistAddSong.this, SearchPlayList.class);
        startActivity(playIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void setControl(){
        edtPlaylistName = findViewById(R.id.edtPlaylistName);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonCancel = findViewById(R.id.buttonCancel);

        spinnerGenres= findViewById(R.id.spinnerGenres);
        spinnerAlbum= findViewById(R.id.spinnerAlbum);
        spinnerArtist= findViewById(R.id.spinnerArtist);

        mainLayout = findViewById(R.id.mainLayout);

        Home = findViewById(R.id.imageViewHome);
        Account = findViewById(R.id.imageViewAccount);
        btnBack = findViewById(R.id.backButton);
    }
}

