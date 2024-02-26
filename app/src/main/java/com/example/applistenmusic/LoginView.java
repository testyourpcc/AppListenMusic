package com.example.applistenmusic;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginView extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;
    TextView registerText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.loginBtn);
        registerText = findViewById(R.id.register);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, RegisterView.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, rpPassword;
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginView.this, "Enter email", Toast.LENGTH_SHORT);
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginView.this, "Enter password", Toast.LENGTH_SHORT);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                        Toast.makeText(LoginView.this, "Login successfully.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), AccountInfo.class);
                                        startActivity(intent);
                                        finish();
                                } else {
                                    Toast.makeText(LoginView.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}