package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.applistenmusic.R;
import com.example.applistenmusic.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ConfirmOtp extends AppCompatActivity {
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    EditText otp;
    Button confirmBtn;
    UserInfo user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        confirmBtn = findViewById(R.id.confirmBtn);
        otp = findViewById(R.id.editOTP);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpInput = otp.getText().toString();
                Intent OtpIntent = getIntent();
                String name = OtpIntent.getStringExtra("name");
                String email = OtpIntent.getStringExtra("email");
                String password = OtpIntent.getStringExtra("password");
                String otp = OtpIntent.getStringExtra("otp");
                //trường hợp xác nhận otp thành công
                if(otp!=null&&otp.equalsIgnoreCase(otpInput)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        UserInfo userInfo = new UserInfo(name,email,password);
                                        reference.child("user").child(mAuth.getUid()).setValue(userInfo);
                                        Toast.makeText(ConfirmOtp.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmOtp.this, RegisterSuccess.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(ConfirmOtp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else{
                    Toast.makeText(ConfirmOtp.this, "m còn lần nữa thôi con ạ!",
                            Toast.LENGTH_SHORT).show();
                    //thông báo sau mỗi lần user nhập sai
                }
            }
        });

    }

    UserInfo getOtpData(){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user").child(Objects.requireNonNull(mAuth.getUid()));
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(UserInfo.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.print("error get data");
            }
        });
        return user;
    }
}