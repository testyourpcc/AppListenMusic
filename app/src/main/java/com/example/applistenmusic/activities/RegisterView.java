package com.example.applistenmusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applistenmusic.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class RegisterView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    MailHelper sendOtp = new MailHelper();
    TextView loginText;
    TextInputEditText nameInput, emailInput, passwordInput, rpPasswordInput;
    TextInputLayout nameInputLayout, emailInputLayout, passwordInputLayout, rpPasswordInputLayout;
    Button registerBtn;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.editUsername);
        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.newPasswd);
        rpPasswordInput = findViewById(R.id.repeatPasswd);
        registerBtn = findViewById(R.id.RegisterBtn);
        loginText = findViewById(R.id.loginLabel);

        nameInputLayout = findViewById(R.id.editUsernameLayout);
        passwordInputLayout = findViewById(R.id.newPasswdLayout);
        emailInputLayout = findViewById(R.id.editEmailLayout);
        rpPasswordInputLayout = findViewById(R.id.repeatPasswdLayout);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterView.this, LoginView.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference();


                String name,email, password, rpPassword;
                boolean status = false;
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                rpPassword = rpPasswordInput.getText().toString();
                name = nameInput.getText().toString();
                nameInputLayout.setError(null);
                emailInputLayout.setError(null);
                passwordInputLayout.setError(null);
                rpPasswordInputLayout.setError(null);
                if(TextUtils.isEmpty(name)){
                    nameInputLayout.setError("This field can not be left blank");
                } else if (TextUtils.isEmpty(email)) {
                    emailInputLayout.setError("This field can not be left blank");
                } else if (TextUtils.isEmpty(password)) {
                    passwordInputLayout.setError("This field can not be left blank");
                } else if (TextUtils.isEmpty(rpPassword)) {
                    rpPasswordInputLayout.setError("This field can not be left blank");
                } else if (!email.matches(emailPattern)) {
                    emailInputLayout.setError("Invalid email format");
                } else if (!TextUtils.equals(password, rpPassword)) {
                    rpPasswordInputLayout.setError("Password confirmation does not match");
                } else{
                    String otp = sendOtp.generateOTP();
                    sendOtp.sendEmail(email,otp);


                    Intent intent = new Intent(RegisterView.this, ConfirmOtp.class);
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    intent.putExtra("otp",otp);

                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}