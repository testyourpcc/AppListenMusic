package com.example.applistenmusic;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAndRegister extends AppCompatActivity {
    Button LoginBtn,RegisterBtn;
    private FirebaseAuth mAuth;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);

        LoginBtn = findViewById(R.id.LoginBtn);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginAndRegister.this, LoginView.class);
                startActivity(loginIntent);
                finish();
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerItent = new Intent(LoginAndRegister.this, RegisterView.class);
                startActivity(registerItent);
                finish();
            }
        });
    }
}