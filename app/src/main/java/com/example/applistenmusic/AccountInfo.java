package com.example.applistenmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountInfo extends AppCompatActivity {
    TextView nameText, emailText, uploadText;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        auth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        uploadText = findViewById(R.id.uploadImgText);

        user = auth.getCurrentUser();

        if(user==null){
            Intent intent = new Intent(getApplicationContext(), LoginAndRegister.class);
            startActivity(intent);
            finish();
        }else{
            getData();
            emailText.setText(user.getEmail());
        }
        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AccountInfo.this, LoginAndRegister.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void getData(){
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("user").child(auth.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    nameText.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}