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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView loginText;
    EditText nameInput, emailInput, passwordInput, rpPasswordInput;
    Button registerBtn;

    DatabaseReference reference;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), LoginAndRegister.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.editUsername);
        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editRegisterPasswordLabel);
        rpPasswordInput = findViewById(R.id.editConfirmPasswordLabel);
        registerBtn = findViewById(R.id.RegisterBtn);
        loginText = findViewById(R.id.loginLabel);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterView.this, LoginView.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference();


                String name,email, password, rpPassword;
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                rpPassword = rpPasswordInput.getText().toString();
                name = nameInput.getText().toString();



                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterView.this, "Enter email", Toast.LENGTH_SHORT);
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterView.this, "Enter password", Toast.LENGTH_SHORT);
                    return;
                }
                if(!TextUtils.equals(password, rpPassword)) {
                    Toast.makeText(RegisterView.this, "Password confirmation does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    UserInfo userInfo = new UserInfo(name,email,password);
                                    reference.child("user").child(name).setValue(userInfo);

                                    Toast.makeText(RegisterView.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), RegisterSuccess.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterView.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

}