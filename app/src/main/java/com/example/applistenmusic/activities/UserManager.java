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
import android.widget.Toast;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.UserAdapter;
import com.example.applistenmusic.models.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class UserManager extends AppCompatActivity {
    RecyclerView listUser;
    DatabaseReference userRef;
    List<UserInfo> userInfos = new ArrayList<>();
    UserAdapter userAdapter;

    ImageView home, account;

    String uid;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        listUser = findViewById(R.id.recyclerview);
        search = findViewById(R.id.searchUser);
        home = findViewById(R.id.imageViewHome);
        account = findViewById(R.id.imageViewAccount);

        listUser.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(this,userInfos);

        listUser.setAdapter(userAdapter);
        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UserInfo clickedUser = userInfos.get(position);
                Intent intent = new Intent(UserManager.this, UserDetail.class);
                intent.putExtra("name", clickedUser.getName());
                intent.putExtra("email", clickedUser.getEmail());
                intent.putExtra("phone", clickedUser.getPhone());
                intent.putExtra("premium",  Boolean.toString(clickedUser.isPremium()));
                intent.putExtra("address", clickedUser.getAddress());
                intent.putExtra("image", clickedUser.getImage());
                intent.putExtra("coin", Integer.toString(clickedUser.getCoin()));
                intent.putExtra("role", clickedUser.getRole());
                intent.putExtra("uid", uid);

                startActivity(intent);
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInfos.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserInfo user = userSnapshot.getValue(UserInfo.class);
                    userInfos.add(user);
                    uid = userSnapshot.getKey();
                }
                userAdapter.notifyDataSetChanged();
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
                List<UserInfo> searchResults = new ArrayList<>();

                // Lặp qua danh sách người dùng để tìm kiếm
                for (UserInfo user : userInfos) {
                    // Kiểm tra xem thuộc tính name và email có null không trước khi thực hiện các thao tác xử lý
                    if (user.getName() != null && user.getEmail() != null) {
                        // Nếu tên hoặc email của người dùng chứa searchText, thêm vào danh sách kết quả
                        if (user.getName().toLowerCase().contains(searchText) || user.getEmail().toLowerCase().contains(searchText)) {
                            searchResults.add(user);
                        }
                    }
                }

                userAdapter.updateList(searchResults);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(UserManager.this, HomeAdmin.class);
                startActivity(playIntent);
                finish();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(UserManager.this, AccountInfoAdmin.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
}