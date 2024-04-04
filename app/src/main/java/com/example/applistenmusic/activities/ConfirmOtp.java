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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.applistenmusic.R;
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
import java.util.Objects;

public class ConfirmOtp extends AppCompatActivity {
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    Button confirmBtn;
    StorageReference storageReference;
    String[] otpDigits = new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        confirmBtn = findViewById(R.id.confirmBtn);
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        setupInput();


        storageReference = FirebaseStorage.getInstance().getReference();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                        uploadImageFromDrawable();

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
    private void uploadImageFromDrawable(){
        // Chuyển đổi Drawable thành Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);

        // Tạo URI từ Bitmap
        Uri fileUri = getImageUri(ConfirmOtp.this, bitmap);

        // Tạo đường dẫn tới thư mục trên Firebase Storage
        StorageReference ref = storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/avatar");

        // Tải ảnh lên Firebase Storage từ URI
        ref.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ConfirmOtp.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConfirmOtp.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm này để chuyển đổi Bitmap thành URI
    public Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
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

}