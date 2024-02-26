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
    TextView nameText, emailText;
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

        user = auth.getCurrentUser();

        if(user==null){
            Intent intent = new Intent(getApplicationContext(), LoginAndRegister.class);
            startActivity(intent);
            finish();
        }else{
            getData();
            emailText.setText(user.getEmail());
        }
    }
    void getData(){
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    nameText.setText(String.valueOf(task.getResult().getChildren().iterator().next().getKey()));
                }
            }
        });
    }
}