package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class UserDetail extends AppCompatActivity {

    TextView username, useremail,userUid, userAddress,userPhone,userPremium;
    ImageView userImage;
    Button back;
    ImageView userAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        FirebaseApp.initializeApp(UserDetail.this);

        username = findViewById(R.id.nameText);
        useremail = findViewById(R.id.emailText);
        userUid = findViewById(R.id.uid);
        userAddress = findViewById(R.id.address);
        userPhone = findViewById(R.id.phone);
        userPremium = findViewById(R.id.premium);
        userAvatar = findViewById(R.id.noImageIcon);




        userImage = findViewById(R.id.noImageIcon);

        back = findViewById(R.id.backBtn);



        Intent OtpIntent = getIntent();
        String name = OtpIntent.getStringExtra("name");
        String email = OtpIntent.getStringExtra("email");
        String address = OtpIntent.getStringExtra("address");
        String uid = OtpIntent.getStringExtra("uid");
        String phone = OtpIntent.getStringExtra("phone");
        String premium = OtpIntent.getStringExtra("premium");
        String image = OtpIntent.getStringExtra("image");

        if (image!=null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(UserDetail.this).load(uri).into(userAvatar);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Xử lý lỗi nếu có
                    Log.e("TAG", "Error downloading image", exception);
                }
            });
        }
        username.setText("Name: "+name);
        useremail.setText("Email: "+email);
        userUid.setText("UID: "+uid);
        userAddress.setText("Address: "+address);
        userPhone.setText("Phone: "+phone);
        userPremium.setText("Premium: "+premium);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetail.this, UserManager.class);
                startActivity(intent);
                finish();
            }
        });


    }
}