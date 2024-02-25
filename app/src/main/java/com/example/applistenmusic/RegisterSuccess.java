package com.example.applistenmusic;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterSuccess extends AppCompatActivity {
    FirebaseAuth auth;
    Button logoutBtn;
    FirebaseUser user;
    TextView userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        auth = FirebaseAuth.getInstance();

        logoutBtn = findViewById(R.id.logoutBtn);
        userInfo = findViewById(R.id.userAccount);
        user = auth.getCurrentUser();

        if(user==null){
            Intent intent = new Intent(getApplicationContext(), LoginAndRegister.class);
            startActivity(intent);
            finish();
        }else{
            userInfo.setText(user.getEmail());
        }
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(intent);
                finish();
            }
        });
    }
}