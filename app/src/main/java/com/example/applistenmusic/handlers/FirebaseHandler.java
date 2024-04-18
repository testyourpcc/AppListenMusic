package com.example.applistenmusic.handlers;

import com.example.applistenmusic.models.Song;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHandler {
    private DatabaseReference mDatabase;

    public FirebaseHandler() {
        // Khởi tạo Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
    }

    public void readData(String path) {
        // Truy vấn dữ liệu từ Firebase
        List<Song> List = new ArrayList<>();
        mDatabase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                        Song song = songSnapshot.getValue(Song.class);
                        List.add(song);
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }
}