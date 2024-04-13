package com.example.applistenmusic.activities;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import com.example.applistenmusic.models.UserInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginView extends AppCompatActivity {
    TextInputEditText passwordInput, emailInput;
    TextInputLayout passwordInputLayout, emailInputLayout;
    CheckBox rememberCheckBox;
    Button loginBtn,googleBtn;
    TextView registerText,forgotPasswd;
    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.loginBtn);
        googleBtn = findViewById(R.id.google);
        registerText = findViewById(R.id.register);
        forgotPasswd = findViewById(R.id.forgot);
        progressBar = findViewById(R.id.progressBar);
        rememberCheckBox = findViewById(R.id.checkBox);
        emailInputLayout = findViewById(R.id.editEmailLayout);
        passwordInputLayout = findViewById(R.id.editPasswordLayout);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        progressBar.setVisibility(View.GONE);

        // Đọc trạng thái đã lưu của checkbox từ SharedPreferences và thiết lập lại trạng thái của checkbox
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rememberChecked = preferences.getBoolean("rememberChecked", false);
        rememberCheckBox.setChecked(rememberChecked);

        if (rememberChecked) {
            String savedEmail = preferences.getString("email", "");
            String savedPassword = preferences.getString("password", "");
            emailInput.setText(savedEmail);
            passwordInput.setText(savedPassword);
        }

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("37669953608-ts4oirtc74b16fpk1nc7uirvqsgqa74f.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginView.this, options);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, RegisterView.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password, rpPassword;
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();

                if (rememberCheckBox.isChecked()) {
                    // Lưu trạng thái của checkbox vào SharedPreferences nếu người dùng đã chọn
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("rememberChecked", true);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                } else {
                    // Xóa trạng thái của checkbox từ SharedPreferences nếu người dùng không chọn
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("rememberChecked");
                    editor.remove("email");
                    editor.remove("password");
                    editor.apply();
                }

                if(TextUtils.isEmpty(email)){
                    emailInputLayout.setError("This field can not be left blank");
                }else if (!email.matches(emailPattern)) {
                    emailInputLayout.setError("Invalid email format");
                } else if (TextUtils.isEmpty(password)) {
                    passwordInputLayout.setError("This field can not be left blank");
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginBtn.setClickable(false);
                    registerText.setClickable(false);
                    forgotPasswd.setClickable(false);
                    rememberCheckBox.setClickable(false);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child(mAuth.getUid());
                                        mDatabase.child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.e("firebase", "Error getting data", task.getException());
                                                }
                                                else {
                                                    String userRole =  String.valueOf(task.getResult().getValue());
                                                    if (userRole.equals("ADMIN")){
                                                        Intent intent = new Intent(LoginView.this, HomeAdmin.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }else{
                                                        Intent intent = new Intent(LoginView.this, Home.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }
                                        });

                                    }else {
                                        Toast.makeText(LoginView.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        loginBtn.setClickable(true);
                                        registerText.setClickable(true);
                                        forgotPasswd.setClickable(true);
                                        rememberCheckBox.setClickable(true);
                                    }
                                }
                            });
                }
            }
        });
        forgotPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

                    mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginView.this, Home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginView.this, "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

}