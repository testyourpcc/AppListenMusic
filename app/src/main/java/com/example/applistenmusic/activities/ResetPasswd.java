package com.example.applistenmusic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.applistenmusic.R;
import com.example.applistenmusic.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ResetPasswd extends AppCompatActivity {
    Button confirmBtn;
    TextInputEditText oldPasswd, newPasswd, repeatPasswd;
    TextInputLayout oldPasswordLayout,newPasswordLayout,repeatPasswordLayout;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwd);


        confirmBtn = findViewById(R.id.confirmBtn);
        oldPasswd = findViewById(R.id.oldPasswd);
        newPasswd = findViewById(R.id.newPasswd);
        repeatPasswd = findViewById(R.id.repeatPasswd);
        oldPasswordLayout = findViewById(R.id.oldPasswdLayout);
        newPasswordLayout = findViewById(R.id.newPasswdLayout);
        repeatPasswordLayout = findViewById(R.id.repeatPasswdLayout);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswd.getText().toString();
                String repeatPassword = repeatPasswd.getText().toString();
                String oldPassword = oldPasswd.getText().toString();

                if (TextUtils.isEmpty(oldPassword)){
                    oldPasswordLayout.setError("This field cannot be left blank");
                } else if (TextUtils.isEmpty(newPassword)) {
                    newPasswordLayout.setError("This field cannot be left blank");
                } else if (TextUtils.isEmpty(repeatPassword)) {
                    repeatPasswordLayout.setError("This field cannot be left blank");
                } else if (!newPassword.equals(repeatPassword)) {
                    repeatPasswordLayout.setError("Re-entered password does not match");
                } else {
                    // Xác minh mật khẩu cũ và tiến hành đổi mật khẩu mới
                    reauthenticateAndResetPassword(oldPassword,newPassword);
                }
            }
        });

    }
    private void reauthenticateAndResetPassword(String oldPassword, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(auth.getCurrentUser().getEmail(), oldPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUserPassword(newPassword);
                    }
                });
    }
    private void updateUserPassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswd.this, "Reset password successfully", Toast.LENGTH_SHORT);
                            auth.signOut();
                            Intent intent = new Intent(ResetPasswd.this, LoginView.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}