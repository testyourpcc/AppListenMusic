package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.applistenmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    MailHelper sendOtp = new MailHelper();
    Button confirmBtn;
    EditText emailInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        confirmBtn = findViewById(R.id.confirmBtn);
        emailInput = findViewById(R.id.editEmail);
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = emailInput.getText().toString();
                if(TextUtils.isEmpty(emailUser)){
                    emailInput.setError("This field can not be left blank");
                }else if (!emailUser.matches(emailPattern)) {
                    emailInput.setError("Invalid email format");
                } else{

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(emailUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(ForgotPassword.this, ResetPasswdNotify.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                }
            }
        });
    }
}