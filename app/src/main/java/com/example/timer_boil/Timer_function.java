package com.example.timer_boil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import static android.media.AudioManager.STREAM_MUSIC;
import static com.example.timer_boil.R.raw.*;
import static com.example.timer_boil.R.raw.bell;


public class Timer_function extends AppCompatActivity {
    private TextView mTextViewCountDown;
    private ImageView mImageViewStart;
    private ImageView mImageViewPause;
    private Button mButtonReset;
    private Button mButtonList;
    private ImageView mMinuteUp;
    private ImageView mMinuteDown;
    private ImageView mSecondUp;
    private ImageView mSecondDown;
    private CountDownTimer mCountDownTimer;
    private Vibrator vibrator;
    private SoundPool mSoundPool;
    int mp3;

    private static long start_time=0;
    private long mTimeLeft = start_time;
    private int time_gap;

    private boolean mTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_function);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mTextViewCountDown = findViewById(R.id.showTimer);
        mImageViewStart = findViewById(R.id.startTimer);
        mImageViewPause = findViewById(R.id.pauseTimer);
        mButtonReset = findViewById(R.id.btn_reset);
        mButtonList = findViewById(R.id.btn_list);
        updateCountDownText();
        mImageViewStart.setVisibility(View.VISIBLE);        //show start icon
        mImageViewPause.setVisibility(View.INVISIBLE);       //hide pause icon
        mButtonReset.setVisibility(View.INVISIBLE);            //hide reset icon
        mMinuteUp = findViewById(R.id.minute_up);           //start_time +600000 msec
        mMinuteDown = findViewById(R.id.minute_down);       //start_time -600000 msec
        mSecondUp = findViewById(R.id.second_up);           //start_time +10000 msec
        mSecondDown = findViewById(R.id.second_down);       //start_time -10000 msec

        mImageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageViewStart.setVisibility(View.INVISIBLE);        //hide start icon
                mImageViewPause.setVisibility(View.VISIBLE);           //show pause icon
                mButtonReset.setVisibility(View.INVISIBLE);            //hide reset icon
                mMinuteUp.setVisibility(View.INVISIBLE);
                mMinuteDown.setVisibility(View.INVISIBLE);
                mSecondUp.setVisibility(View.INVISIBLE);
                mSecondDown.setVisibility(View.INVISIBLE);

                mCountDownTimer = new CountDownTimer(mTimeLeft,1000) {
                    @Override
                    public void onTick(long l) {

                        mTimeLeft = l;

                        if(mTimeLeft == 1){
                            mTimeLeft = 0;
                        }
                        updateCountDownText();
                    }

                    @Override
                    public void onFinish() {
                        mTextViewCountDown.setText("00:00");
                        mImageViewStart.setVisibility(View.INVISIBLE);        //show start icon
                        mImageViewPause.setVisibility(View.INVISIBLE);       //hide pause icon
                        mButtonReset.setVisibility(View.VISIBLE);            //show reset icon

                        if(vibrator.hasVibrator()){
                            long vibratePattern[] = {500,1000,500,1000};
                            vibrator.vibrate(vibratePattern,1);
                        }
                        //mSoundPool.play(mp3,1f,11f,0,-1,1f);

                    }
                }.start();
            }
        });

        mImageViewPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.cancel();
                mImageViewStart.setVisibility(View.VISIBLE);        //show start icon
                mImageViewPause.setVisibility(View.INVISIBLE);       //hide pause icon
                mButtonReset.setVisibility(View.VISIBLE);            //show reset icon
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                mTimeLeft = start_time;
                updateCountDownText();
                mImageViewStart.setVisibility(View.VISIBLE);        //show start icon
                mImageViewPause.setVisibility(View.INVISIBLE);       //hide pause icon
                mButtonReset.setVisibility(View.INVISIBLE);            //hide reset icon
                mMinuteUp.setVisibility(View.VISIBLE);
                mMinuteDown.setVisibility(View.VISIBLE);
                mSecondUp.setVisibility(View.VISIBLE);
                mSecondDown.setVisibility(View.VISIBLE);
            }
        });

        mButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                Intent intent = new Intent(Timer_function.this,  MainActivity.class);
                startActivity(intent);

            }
        });

        mMinuteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time += 60000;
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });

        mSecondUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time += 10000;
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });

        mMinuteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time -= 60000;
                if(start_time <= 0){
                    start_time = 0;
                }
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });

        mSecondDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time -= 10000;
                if(start_time <= 0){
                    start_time = 0;
                }
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });
    }

    private void updateCountDownText(){
        int minutes = (int)(mTimeLeft/1000)/60;     //calc for msec to minutes
        int seconds = (int)(mTimeLeft/1000)%60;     //calc for msec to second
        String TimerLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);     //make String for left time
        mTextViewCountDown.setText(TimerLeftFormatted);                                                         //set left time to textView
    }
}