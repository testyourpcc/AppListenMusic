package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.ArtistAdapter;
import com.example.applistenmusic.adapters.ArtistManagerAdapter;
import com.example.applistenmusic.adapters.UserAdapter;
import com.example.applistenmusic.models.Artist;
import com.example.applistenmusic.models.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistManager extends AppCompatActivity {
    RecyclerView listArtist;
    DatabaseReference userRef;
    List<Artist> artistList = new ArrayList<>();
    ArtistManagerAdapter artistManagerAdapter;

    ImageView home, account;

    String uid;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_manager);

        listArtist = findViewById(R.id.recyclerview);
        search = findViewById(R.id.searchUser);
        home = findViewById(R.id.imageViewHome);
        account = findViewById(R.id.imageViewAccount);

        listArtist.setLayoutManager(new LinearLayoutManager(this));

        artistManagerAdapter = new ArtistManagerAdapter(this,artistList);

        listArtist.setAdapter(artistManagerAdapter);
//        artistManagerAdapter.setOnItemClickListener(new ArtistManagerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Artist clickedUser = artistList.get(position);
//                Intent intent = new Intent(ArtistManager.this, UserDetail.class);
//                intent.putExtra("name", clickedUser.getName());
//                startActivity(intent);
//            }
//        });

        userRef = FirebaseDatabase.getInstance().getReference().child("artist");
        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artistList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = userSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                artistManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase();
                List<Artist> searchResults = new ArrayList<>();

                // Lặp qua danh sách người dùng để tìm kiếm
                for (Artist artist : artistList) {
                    // Kiểm tra xem thuộc tính name và email có null không trước khi thực hiện các thao tác xử lý
                    if (artist.getName() != null ) {
                        // Nếu tên hoặc email của người dùng chứa searchText, thêm vào danh sách kết quả
                        if (artist.getName().toLowerCase().contains(searchText) ) {
                            searchResults.add(artist);
                        }
                    }
                }

                artistManagerAdapter.updateList(searchResults);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(ArtistManager.this, HomeAdmin.class);
                startActivity(playIntent);
                finish();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(ArtistManager.this, AccountInfoAdmin.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
}