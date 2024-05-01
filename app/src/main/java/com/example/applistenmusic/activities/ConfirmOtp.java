package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.applistenmusic.R;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ConfirmOtp extends AppCompatActivity {
    PlayList defaultPlaylist;
    ProgressBar progressBar;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    TextView resendOtp;
    Button confirmBtn;
    StorageReference storageReference;
    MailHelper resend = new MailHelper();
    String[] otpDigits = new String[6];
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        confirmBtn = findViewById(R.id.confirmBtn);
        resendOtp = findViewById(R.id.resendOtp);
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        startCountdown(30);

        setupInput();

        storageReference = FirebaseStorage.getInstance().getReference();


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String otpInput = getInputCode();
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
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserInfo userInfo = new UserInfo("USER","1234567890","97 Man Thien",false,0,0);

                                        reference.child("users").child(mAuth.getUid()).setValue(userInfo);

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(ConfirmOtp.this, "Account created",
                                                                    Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ConfirmOtp.this, RegisterSuccess.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                        createDefaultPlaylist(user.getUid());
                                    } else {
                                        Toast.makeText(ConfirmOtp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                } else{
                    Toast.makeText(ConfirmOtp.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OtpIntent = getIntent();
                String email = OtpIntent.getStringExtra("email");
                String otp = resend.generateOTP();
                resend.sendEmail(email,otp);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                resendOtp.setClickable(false);
                startCountdown(30);
            }
        });
        // Thêm sự kiện lắng nghe cho inputCode6
        inputCode6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE||actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    private void startCountdown(int seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendOtp.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                resendOtp.setText("Resend OTP");
                resendOtp.setClickable(true);
            }
        }.start();
    }



    private void setupInput(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otpDigits[0] = s.toString();
                if(!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputCode1.requestFocus();
                }
            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otpDigits[1] = s.toString();
                if(!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputCode1.requestFocus();
                }
            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otpDigits[2] = s.toString();
                if(!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputCode2.requestFocus();
                }
            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otpDigits[3] = s.toString();
                if(!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputCode3.requestFocus();
                }
            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otpDigits[4] = s.toString();
                if(!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputCode4.requestFocus();
                }
            }
        });
        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otpDigits[5] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputCode5.requestFocus();
                }
            }
        });

    }

    private String getInputCode() {
        StringBuilder otpInput = new StringBuilder();
        for (String digit : otpDigits) {
            if (digit != null) {
                otpInput.append(digit);
            }
        }
        return otpInput.toString();
    }

    private void createDefaultPlaylist(String userId) {
        DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference().child("playList").child(userId).child("0");

        // Tạo một đối tượng Playlist mới
        PlayList defaultPlaylist = new PlayList();
        defaultPlaylist.setName("Favorite");
        defaultPlaylist.setId(1);
        defaultPlaylist.setImage("");
        defaultPlaylist.setUser(userId.toString());
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1,0);
        defaultPlaylist.setSongIdList(a);
        // Lưu playlist vào Firebase Realtime Database
        playlistRef.setValue(defaultPlaylist);
    }

}