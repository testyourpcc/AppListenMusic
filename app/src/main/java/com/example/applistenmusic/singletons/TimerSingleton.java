package com.example.applistenmusic.singletons;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.applistenmusic.activities.PlayView;

public class TimerSingleton {

    private MediaPlayer mediaPlayer;
    private static volatile TimerSingleton instance;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private Context context;

    private TimerSingleton() {
        // Private constructor to prevent instantiation outside this class.
    }

    public static TimerSingleton getInstance() {
        if (instance == null) {
            synchronized (TimerSingleton.class) {
                if (instance == null) {
                    instance = new TimerSingleton();
                }
            }
        }
        return instance;
    }

    public synchronized void startTimer(long millisInFuture, long countDownInterval) {
        this.context = context;
        MediaPlayerSingleton mediaPlayerSingleton = MediaPlayerSingleton.getInstance();
        mediaPlayer = mediaPlayerSingleton.getMediaPlayer();
        if (!timerRunning) {
            countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    sendBroadcastToUpdateUI(timeLeftInMillis);
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                    sendBroadcastOnTimerFinish();
                }
            }.start();
            timerRunning = true;
            timeLeftInMillis = millisInFuture;
        }
    }

    public synchronized void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }

    public synchronized boolean isTimerRunning() {
        return timerRunning;
    }

    public synchronized long getTimeLeftInMillis() {
        return timeLeftInMillis;
    }
    private void sendBroadcastToUpdateUI(long timeLeftInMillis) {
        Intent intent = new Intent("com.example.timer.UPDATE");
        intent.putExtra("timeLeft", timeLeftInMillis);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private void sendBroadcastOnTimerFinish() {
        Intent intent = new Intent("com.example.timer.FINISHED");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

