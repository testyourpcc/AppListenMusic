package com.example.applistenmusic.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.models.PlayList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterSuccess extends AppCompatActivity {
    FirebaseAuth auth;
    Button continueBtn;
    FirebaseUser user;
    TextView userInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        auth = FirebaseAuth.getInstance();

        continueBtn = findViewById(R.id.continueBtn);
        userInfo = findViewById(R.id.userAccount);
        user = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = user.getUid(); // replace with actual user ID
        DatabaseReference userPlaylistsRef = database.getReference("playList").child(userId);

        // Create a new Playlist object
        PlayList playlist = new PlayList();
        playlist.setId(0);
        playlist.setName("Favorite");
        playlist.setUser(userId);
        List<Integer> songIdList = new ArrayList<>();
        songIdList.add(0); // Add a child "1" with value "0"
        playlist.setSongIdList(songIdList);

        // Save the Playlist object to Firebase
        userPlaylistsRef.child("0").setValue(playlist);

        if(user==null){
            Intent intent = new Intent(getApplicationContext(), LoginAndRegister.class);
            startActivity(intent);
            finish();
        }else{
            userInfo.setText(user.getDisplayName());
            auth.signOut();
        }




        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(intent);
                finish();
            }
        });
    }

}