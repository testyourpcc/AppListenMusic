package com.example.applistenmusic.activities;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.applistenmusic.R;
import com.example.applistenmusic.singletons.TimerSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SongDetailView extends AppCompatActivity {
    private GestureDetector gestureDetector;
    View mainView;
    ImageView Feature, Home,Search,Play,Account;
    TextView textViewLyric;
    DatabaseReference reference;
    private TimerSingleton timerSingleton;
    private TextView textViewTimer;
    private Switch switchTimer;
    ImageView BottomSheet;
    LinearLayout henGio, addToPlaylist, openPlaylist;
    private long timeLeftInMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);
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
                Intent playIntent = new Intent(SongDetailView.this, Home.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SongDetailView.this, SearchView.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(SongDetailView.this, AccountInfo.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(timerUpdateReceiver,
                new IntentFilter("com.example.timer.UPDATE"));
        LocalBroadcastManager.getInstance(this).registerReceiver(timerUpdateReceiver,
                new IntentFilter("com.example.timer.FINISHED"));
        BottomSheetDialog optionDialog = new BottomSheetDialog(this);
        BottomSheetDialog henGioDialog = new BottomSheetDialog(this);
        optionDialog.setContentView(R.layout.play_bottom_sheet_layout);
        henGioDialog.setContentView(R.layout.layout_hen_gio);
        BottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.show();
            }
        });

        henGio = optionDialog.findViewById(R.id.hengio);
        addToPlaylist = optionDialog.findViewById(R.id.addPlaylist);
        openPlaylist = optionDialog.findViewById(R.id.openPlaylist);
        henGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                henGioDialog.show();
            }
        });
        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        openPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        timerSingleton = TimerSingleton.getInstance();
        textViewTimer = henGioDialog.findViewById(R.id.textViewTimer);
        switchTimer = henGioDialog.findViewById(R.id.switch_timer);
        if (timerSingleton.isTimerRunning()){
            switchTimer.setChecked(timerSingleton.isTimerRunning());
            timeLeftInMillis = timerSingleton.getTimeLeftInMillis();
            updateCountDownText(timeLeftInMillis);
        }

        switchTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTimePickerDialog();
                } else {
                    timerSingleton.stopTimer();
                    textViewTimer.setText("00:00:00");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timerUpdateReceiver);
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
                    if (diffX < 0) {
                        // Vuốt sang trái, chuyển sang một activity khác
                        Intent intent = new Intent(SongDetailView.this, PlayView.class);
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
//                    textViewLyric.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    public void setcontrol() {
        Home = findViewById(R.id.imageViewHome);
        Search = findViewById(R.id.imageViewSearch);
        Play = findViewById(R.id.imageViewHeadPhone);
        Account = findViewById(R.id.imageViewAccount);
        mainView = findViewById(R.id.SongDetailView);
        textViewLyric = findViewById(R.id.textViewLyrics);
        gestureDetector = new GestureDetector(this, new GestureListener());
        textViewTimer = findViewById(R.id.textViewTimer);
        BottomSheet = findViewById(R.id.navigationButton);
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeLeftInMillis = (hourOfDay * 3600 + minute * 60) * 1000L;
                timerSingleton.startTimer(timeLeftInMillis, 1000);
            }
        }, 0, 0, true);
        timePickerDialog.show();
    }

    private void updateCountDownText(long timeLeftInMillis) {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        textViewTimer.setText(timeFormatted);
    }

    private BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.timer.UPDATE")) {
                long timeLeftInMillis = intent.getLongExtra("timeLeft", 0);
                updateCountDownText(timeLeftInMillis);
            }else if (intent.getAction().equals("com.example.timer.FINISHED")) {
                handleTimerFinished();
            }
        }
    };
    private void handleTimerFinished() {
        Toast.makeText(this, "Timer has finished!", Toast.LENGTH_SHORT).show();
        switchTimer.setChecked(false);
    }
}