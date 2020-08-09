package com.example.timer_boil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class Timer_function extends AppCompatActivity {
    private TextView mTextViewCountDown;
    private ImageView mImageViewStart;
    private ImageView mImageViewPause;
    private Button mButtonReset;
    private Button mButtonList;
    private CountDownTimer mCountDownTimer;
    private Vibrator vibrator;

    private static long start_time=10000;
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

        mImageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageViewStart.setVisibility(View.INVISIBLE);        //hide start icon
                mImageViewPause.setVisibility(View.VISIBLE);           //show pause icon
                mButtonReset.setVisibility(View.VISIBLE);            //hide reset icon

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







    }

    private void updateCountDownText(){
        int minutes = (int)(mTimeLeft/1000)/60;     //calc for msec to minutes
        int seconds = (int)(mTimeLeft/1000)%60;     //calc for msec to second
        String TimerLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);     //make String for left time
        mTextViewCountDown.setText(TimerLeftFormatted);                                                         //set left time to textView
    }
}