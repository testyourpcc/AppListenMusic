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
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.singletons.AlbumSingleton;
import com.example.applistenmusic.singletons.ArtistSingleton;
import com.example.applistenmusic.singletons.GenresSingleton;
import com.example.applistenmusic.singletons.SongListSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SongAdd extends AppCompatActivity {
    ImageView Feature, Home,Account, btnBack;
    private EditText editText1, editText3, editText6;
    AutoCompleteTextView editTextArtist ,editTextAlbum, editTextGenres ;
    private Button buttonSave, buttonCancel, buttonUpload;
    RelativeLayout mainLayout;
    Spinner spinnerGenres, spinnerAlbum, spinnerArtist;
    List<Genres> genres;
    List<Album> albums;
    List<Artist> artists;
    List<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_add);
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

        if (AlbumSingleton.getInstance().hasAlbum()){
            albums = AlbumSingleton.getInstance().getAllAlbumIfExist();
        } else {
            AlbumSingleton.getInstance().getAllAlbum(new AlbumLoadListener() {
                @Override
                public void onAlbumLoaded(List<Album> List) {
                    albums = List;
                }
            });
        }

        if (ArtistSingleton.getInstance().hasArtist()){
            artists = ArtistSingleton.getInstance().getAllArtistIfExist();
        } else {
            ArtistSingleton.getInstance().getAllArtist(new ArtistLoadListener(){
                @Override
                public void onArtistLoaded(List<Artist> List) {
                    artists = List;
                }
            });
        }

        if (GenresSingleton.getInstance().hasGenres()){
            genres = GenresSingleton.getInstance().getAllGenresIfExist();
        } else {
            GenresSingleton.getInstance().getAllGenres(new GenresLoadListener() {
                @Override
                public void onGenresLoaded(List<Genres> List) {
                    genres = List;
                }
            });
        }

        List<String> genresToStringList = new ArrayList<>();
        genresToStringList.add("");
        for(Genres g : genres){
            genresToStringList.add(g.getName());
        }

        ArrayAdapter<String> adapterGenres = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genresToStringList);
        adapterGenres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGenres.setAdapter(adapterGenres);

        spinnerGenres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGenre = genresToStringList.get(position);
                editTextGenres.setText(selectedGenre);
                Toast.makeText(getApplicationContext(), "Selected Genre: " + selectedGenre, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });

        List<String> albumToStringList = new ArrayList<>();
        albumToStringList.add("");
        for(Album album : albums){
            albumToStringList.add(album.getName());
        }

        ArrayAdapter<String> adapterAlbum = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albumToStringList);
        adapterAlbum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAlbum.setAdapter(adapterAlbum);

        spinnerAlbum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGenre = albumToStringList.get(position);
                editTextAlbum.setText(selectedGenre);
                Toast.makeText(getApplicationContext(), "Selected Genre: " + selectedGenre, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });

        List<String> artistToStringList = new ArrayList<>();
        artistToStringList.add("");
        for(Artist artist : artists){
            artistToStringList.add(artist.getName());
        }

        ArrayAdapter<String> adapterArtist = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, artistToStringList);
        adapterArtist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerArtist.setAdapter(adapterArtist);

        spinnerArtist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGenre = artistToStringList.get(position);
                editTextArtist.setText(selectedGenre);
                Toast.makeText(getApplicationContext(), "Selected Genre: " + selectedGenre, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có gì được chọn
            }
        });

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
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        // Xử lý khi nhấn nút Cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOperation();
            }
        });

        // Xử lý khi nhấn nút Upload
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SongAdd.this, HomeAdmin.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SongAdd.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SongAdd.this, SongManagement.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
    }

    // Phương thức lấy dữ liệu từ các EditText và hiển thị thông báo
    private void saveData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("song");

        // Tạo một song mới và đặt giá trị của nó vào Realtime Firebase
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy số lượng children hiện có trong "songs" để xác định ID tiếp theo
                long count = dataSnapshot.getChildrenCount();

                // Tạo một child mới với ID tăng dần từ 1
                String newSongId = String.valueOf(count + 1);

                Song song = new Song();
                song.setImage(editText3.getText().toString());
                song.setName(editText1.getText().toString());
                song.setAlbum(spinnerAlbum.getSelectedItemPosition());
                song.setArtist(spinnerArtist.getSelectedItemPosition());
                song.setGenres(spinnerGenres.getSelectedItemPosition());
                song.setId(Integer.valueOf(newSongId));
                song.setUrl("");
                song.setView(1);
                song.setLyric("");
                myRef.child(newSongId).setValue(song);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    // Phương thức xử lý khi nhấn nút Cancel
    private void cancelOperation() {
        Intent playIntent = new Intent(SongAdd.this, SongManagement.class);
        startActivity(playIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    // Phương thức xử lý khi nhấn nút Upload
    private void uploadFile() {
        // Xử lý ở đây (ví dụ: hiển thị dialog upload file)
        Toast.makeText(this, "Upload file", Toast.LENGTH_SHORT).show();
    }

    private void setControl(){
        editText1 = findViewById(R.id.editText1);
        editTextAlbum = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editTextArtist = findViewById(R.id.editText4);
        editTextGenres = findViewById(R.id.editText5);

        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonUpload = findViewById(R.id.buttonUpLoad);

        spinnerGenres= findViewById(R.id.spinnerGenres);
        spinnerAlbum= findViewById(R.id.spinnerAlbum);
        spinnerArtist= findViewById(R.id.spinnerArtist);

        mainLayout = findViewById(R.id.mainLayout);

        Home = findViewById(R.id.imageViewHome);
        Account = findViewById(R.id.imageViewAccount);
        btnBack = findViewById(R.id.backButton);
    }
}

