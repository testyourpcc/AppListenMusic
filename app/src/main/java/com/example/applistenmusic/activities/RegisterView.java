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
    TextView loginText;
    EditText nameInput, emailInput, passwordInput, rpPasswordInput;
    Button registerBtn;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.editUsername);
        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editRegisterPasswordLabel);
        rpPasswordInput = findViewById(R.id.editConfirmPasswordLabel);
        registerBtn = findViewById(R.id.RegisterBtn);
        loginText = findViewById(R.id.loginLabel);

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

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterView.this, "Enter email", Toast.LENGTH_SHORT);
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterView.this, "Enter password", Toast.LENGTH_SHORT);
                    return;
                }
                if(!TextUtils.equals(password, rpPassword)) {
                    Toast.makeText(RegisterView.this, "Password confirmation does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                String otp = generateOTP();
                sendEmail(email,otp);


                Intent intent = new Intent(RegisterView.this, ConfirmOtp.class);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                intent.putExtra("otp",otp);

                startActivity(intent);
                finish();
            }
        });


    }

        public String generateOTP() {
            Random random = new Random();
            StringBuilder otp = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                otp.append(random.nextInt(10));
            }
            return otp.toString();
        }

    public void sendEmail(String email, String num){
        try {
            String stringSenderEmail = "ngoductuanvn2@gmail.com";
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = "fphdnfbybudsdpep";
            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Subject: Verify Email");
            mimeMessage.setText("Your otp is: " + num);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
