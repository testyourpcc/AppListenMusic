package com.example.applistenmusic.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class SongEdit extends AppCompatActivity {

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
        setContentView(R.layout.activity_song_edit);
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
        editTextGenres.setAdapter(adapterGenres);

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
        editTextAlbum.setAdapter(adapterAlbum);

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
        editTextArtist.setAdapter(adapterArtist);

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
    }

    // Phương thức lấy dữ liệu từ các EditText và hiển thị thông báo
    private void saveData() {

    }

    // Phương thức xử lý khi nhấn nút Cancel
    private void cancelOperation() {
        // Xử lý ở đây (ví dụ: finish activity)
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


    }
}
