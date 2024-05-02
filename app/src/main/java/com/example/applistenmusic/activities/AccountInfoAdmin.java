package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import com.example.applistenmusic.models.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountInfoAdmin extends AppCompatActivity {
    TextView nameText, emailText, uploadText, logoutText, resetPasswdText, changePhoneNumber,changeAddress;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ImageView Home, Search, Play, Account, noImage, backgroundAcountImg;
    EditText address,phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info_admin);
        auth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        uploadText = findViewById(R.id.uploadImgText);
        Home = findViewById(R.id.imageViewHome);
        Account = findViewById(R.id.imageViewAccount);
        logoutText = findViewById(R.id.logoutText);
        resetPasswdText = findViewById(R.id.resetPasswdText);
        noImage = findViewById(R.id.noImageIcon);
        backgroundAcountImg = findViewById(R.id.backgroundAccountImg);
        changePhoneNumber = findViewById(R.id.changePhoneNumber);
        changeAddress = findViewById(R.id.changeAddress);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);

        user = auth.getCurrentUser();
        if (user != null && user.getUid() != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://applistenmusic-b4e45.appspot.com/images/" + user.getUid() + "/avatar");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(AccountInfoAdmin.this).load(uri).into(noImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Xử lý lỗi nếu có
                    Log.e("TAG", "Error downloading image", exception);
                }
            });
        }
        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfoAdmin.this, UploadImg.class);
                startActivity(intent);
            }
        });

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginAndRegister.class);
            startActivity(intent);
            finish();
        } else {
            if (user.getProviderData().get(1).getProviderId().equals("google.com")) {
                emailText.setText(user.getProviderData().get(1).getEmail());
                resetPasswdText.setVisibility(View.GONE);
            } else {
                emailText.setText(user.getEmail());
            }
            nameText.setText(user.getDisplayName());
        }


        reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
                if(userInfo!=null) {
                    phoneNumber.setText(userInfo.getPhone());
                    address.setText(userInfo.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AccountInfoAdmin.this, LoginAndRegister.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        resetPasswdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfoAdmin.this, ResetPasswd.class);
                startActivity(intent);
            }
        });

        changePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePhoneNumber.getText().toString().equals("change")) {
                    changePhoneNumber.setText("done");
                    phoneNumber.setFocusableInTouchMode(true);
                    phoneNumber.setFocusable(true);
                    phoneNumber.requestFocus();
                    phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(phoneNumber, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    String phoneChanged = phoneNumber.getText().toString();
                    reference.child("phone").setValue(phoneChanged);
                    changePhoneNumber.setText("change");
                    phoneNumber.setFocusableInTouchMode(false);
                    phoneNumber.setFocusable(false);
                    phoneNumber.setKeyListener(null); // Vô hiệu hóa bàn phím
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneNumber.getWindowToken(), 0);
                }
            }
        });
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeAddress.getText().toString().equals("change")) {
                    changeAddress.setText("done");
                    address.setFocusableInTouchMode(true);
                    address.setFocusable(true);
                    address.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(address, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    String addressChanged = address.getText().toString();
                    reference.child("address").setValue(addressChanged);
                    changeAddress.setText("change");
                    address.setFocusableInTouchMode(false);
                    address.setFocusable(false);
                    address.setKeyListener(null); // Vô hiệu hóa bàn phím
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(address.getWindowToken(), 0);
                }
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(AccountInfoAdmin.this, HomeAdmin.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();

            }
        });

    }
}