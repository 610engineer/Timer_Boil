package com.jour1.timer_boil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;

import static android.media.AudioManager.STREAM_MUSIC;


public class Timer_function extends AppCompatActivity {
    private TextView mTextViewCountDown;
    private TextView mObNeme;
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
    private AdView mAdView;

    private static long start_time=0;
    private long mTimeLeft = start_time;

    private int time_gap;

    private boolean mTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_function);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mObNeme= findViewById(R.id.objectName);
        mTextViewCountDown = findViewById(R.id.showTimer);
        mImageViewStart = findViewById(R.id.startTimer);
        mImageViewStart.setColorFilter(Color.parseColor("#ff0000"));
        mImageViewPause = findViewById(R.id.pauseTimer);
        mImageViewPause.setColorFilter(Color.parseColor("#ff0000"));
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


        Intent intent = getIntent();
        start_time = intent.getIntExtra("timerList",0);
        mObNeme.setText(intent.getStringExtra("obList"));
        mTimeLeft = start_time;
        updateCountDownText();
        if(start_time == 0){
            mImageViewStart.setColorFilter(Color.parseColor("#fa8072"));
            mImageViewStart.setEnabled(false);
        }


        mImageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSoundPool();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mButtonList.setEnabled(false);
                mButtonList.setBackgroundColor(Color.parseColor("#f4a460"));
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
                            long vibratePattern[] = {500,800,500,800};
                            vibrator.vibrate(vibratePattern,1);
                        }
                        mSoundPool.play(mp3,1f,11f,0,-1,1f);

                    }
                }.start();
            }
        });

        mImageViewPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.cancel();
                mButtonList.setEnabled(true);
                mButtonList.setBackgroundColor(Color.parseColor("#ff8c00"));
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mImageViewStart.setVisibility(View.VISIBLE);        //show start icon
                mImageViewPause.setVisibility(View.INVISIBLE);       //hide pause icon
                mButtonReset.setVisibility(View.VISIBLE);            //show reset icon
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.cancel();
                mButtonList.setEnabled(true);
                mSoundPool.stop(mp3);
                mSoundPool.release();
                mTimeLeft = start_time;
                updateCountDownText();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                finish();

            }
        });

        mMinuteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time += 60000;
                if(start_time != 0){
                    mImageViewStart.setColorFilter(Color.parseColor("#ff0000"));
                    mImageViewStart.setEnabled(true);
                }
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });

        mSecondUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time += 10000;
                if(start_time != 0){
                    mImageViewStart.setColorFilter(Color.parseColor("#ff0000"));
                    mImageViewStart.setEnabled(true);
                }
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });

        mMinuteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time -= 60000;
                if(start_time > 0){
                    mImageViewStart.setColorFilter(Color.parseColor("#ff0000"));
                    mImageViewStart.setEnabled(true);
                }else{
                    start_time = 0;
                    mImageViewStart.setColorFilter(Color.parseColor("#fa8072"));
                    mImageViewStart.setEnabled(false);
                }
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });

        mSecondDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_time -= 10000;
                if(start_time > 0){
                    mImageViewStart.setColorFilter(Color.parseColor("#ff0000"));
                    mImageViewStart.setEnabled(true);
                }else{
                    start_time = 0;
                    mImageViewStart.setColorFilter(Color.parseColor("#fa8072"));
                    mImageViewStart.setEnabled(false);
                }
                mTimeLeft = start_time;
                updateCountDownText();
            }
        });
    }

    public void updateCountDownText(){
        int minutes = (int)(mTimeLeft/1000)/60;     //calc for msec to minutes
        int seconds = (int)(mTimeLeft/1000)%60;     //calc for msec to second
        String TimerLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);     //make String for left time
        mTextViewCountDown.setText(TimerLeftFormatted);                                                         //set left time to textView
    }

    public void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .build();

        } else {
            mSoundPool = new SoundPool(1,STREAM_MUSIC,0);
        }
        mp3 = mSoundPool.load(this, R.raw.bell,1);
    }



    @Override
    public void onBackPressed(){
    }
}