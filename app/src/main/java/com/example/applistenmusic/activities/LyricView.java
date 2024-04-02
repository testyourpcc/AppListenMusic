package com.example.applistenmusic.activities;

import com.example.applistenmusic.models.LyricForSync;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class LyricView extends AppCompatActivity {
    boolean isUserInteracting = false;
    private GestureDetector gestureDetector;
    View mainView;
    ImageView Feature, Home,Search,Play,Account;
    TextView textViewLyricStart, textViewLyricHighLight, textViewLyricEnd;
    DatabaseReference reference;
    private Handler handler;
    private Runnable runnable;
    ScrollView  scrollView;
    int index = 1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_lyric);
        setcontrol();
        getData();
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(LyricView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(LyricView.this, SearchView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(LyricView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        // Thiết lập listener cho sự kiện chạm hoặc cuộn
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Người dùng đang chạm vào ScrollView
                        isUserInteracting = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Người dùng đang cuộn ScrollView
                        isUserInteracting = true;
                        break;
                    default:
                        isUserInteracting = false;
                        break;
                }
                return false;
            }
        });

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 50;
        private static final int SWIPE_VELOCITY_THRESHOLD = 50;


        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) { // Thay đổi điều kiện này từ diffX < 0 thành diffX > 0
                        // Vuốt sang phải, chuyển sang một activity khác
                        Intent intent = new Intent(LyricView.this, PlayView.class);
                        startActivity(intent);
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    void getData(){
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("song").child("1").child("lyric").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String a = String.valueOf(task.getResult().getValue());
                    String[] LyricLRC = a.split("/r/n");
                    int LyricHighlightIndex = 1;
                    // Khởi tạo Handler và Runnable
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            changeColorAndText(LyricLRC, getLyricHighlightIndex(index));
                            handler.postDelayed(this, 1000);
                            // Thực hiện lại sau mỗi giây
                        }
                    };

                    // Bắt đầu việc thay đổi màu và văn bản
                    handler.post(runnable);
                   // textViewLyric.setText(stringBuilder.toString());
                }
            }
        });
    }

    public int getLyricHighlightIndex(int i) {

        index += 2;
        return i;


    }


    private void changeColorAndText(String[] LyricLRC, int LyricHighlightIndex) {
        StringBuilder LyricStart = new StringBuilder();
        StringBuilder LyricEnd = new StringBuilder();
        for (int i = 1; i < LyricLRC.length; i += 2) {
            // Lấy các phần tử ở vị trí lẻ (vị trí bắt đầu từ 0)
            if (i < LyricHighlightIndex) {
                LyricStart.append(LyricLRC[i]).append("\n"); // Nối mỗi chuỗi và thêm dấu xuống dòng
            }

            if (i == LyricHighlightIndex) {
                // Tạo một SpannableString từ dòng hiện tại
                SpannableString spannableString = new SpannableString(LyricLRC[i]);
                spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewLyricHighLight.setText(spannableString);
                textViewLyricHighLight.setTextSize(24F);
            }
            if (i > LyricHighlightIndex){
                LyricEnd.append(LyricLRC[i]).append("\n"); // Nối mỗi chuỗi và thêm dấu xuống dòng
            }
        }
        textViewLyricStart.setText(LyricStart);
        textViewLyricEnd.setText(LyricEnd);

        if (!isUserInteracting) { // Kiểm tra xem người dùng có đang giữ tay trên ScrollView không
            int scrollViewHeight = scrollView.getHeight();
            int textViewHeight = textViewLyricHighLight.getHeight();
            int scrollY = textViewLyricHighLight.getTop() - (scrollViewHeight - textViewHeight) /4;
            scrollView.smoothScrollTo(0, scrollY);
        }


    }

    String formatData(String Lyric){
        ArrayList<String> LyricLRC = new ArrayList<>();

        return Lyric;
    }

    public void setcontrol() {
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        mainView = findViewById(R.id.LyricView);
        scrollView = findViewById(R.id.ScrollLyric);
        textViewLyricStart = findViewById(R.id.textViewLyricsStart);
        textViewLyricHighLight = findViewById(R.id.textViewLyricsHighLight);
        textViewLyricEnd = findViewById(R.id.textViewLyricsEnd);
        gestureDetector = new GestureDetector(this, new GestureListener());
    }
}