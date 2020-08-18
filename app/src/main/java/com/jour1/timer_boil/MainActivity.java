package com.jour1.timer_boil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mMoveTimer;
    private TableLayout mTblmenu;
    private ImageView mAsparagus;
    private ImageView mBroccoli;
    private ImageView mGombo;
    private ImageView mMoyashi;
    private ImageView mSpinach;
    private ImageView mCom;
    private ImageView mEgg;
    private AdView mAdView;
    private List<Integer> timerList = new ArrayList<>();
    private List<String> obList = new ArrayList<>();
    private int eggType =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mTblmenu =findViewById(R.id.tblmenu);
        mMoveTimer = findViewById(R.id.move_timer);
        mAsparagus = findViewById(R.id.img_asparagus);
        mBroccoli = findViewById(R.id.img_broccoli);
        mGombo = findViewById(R.id.img_gombo);
        mMoyashi = findViewById(R.id.img_moyashi);
        mSpinach = findViewById(R.id.img_spinach);
        mCom = findViewById(R.id.img_com);
        mEgg = findViewById(R.id.img_egg);



        timerList.add(120000);      //asparagus
        timerList.add(150000);      //broccoli
        timerList.add(105000);      //gombo
        timerList.add(30000);       //moyashi
        timerList.add(60000);       //spinach
        timerList.add(210000);      //com
        timerList.add(270000);      //egg1
        timerList.add(390000);      //egg2
        timerList.add(540000);      //egg3
        timerList.add(720000);      //egg4

        obList.add("アスパラ");
        obList.add("ブロッコリ-");
        obList.add("オクラ");
        obList.add("もやし");
        obList.add("ほうれん草");
        obList.add("トウモロコシ");
        obList.add("ゆで卵(トロトロ)");
        obList.add("ゆで卵(半熟)");
        obList.add("ゆで卵(通常)");
        obList.add("ゆで卵(かため)");



        mMoveTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Timer_function.class);
                startActivity(intent);
            }
        });

        mAsparagus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeScreen(0);
            }
        });

        mBroccoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeScreen(1);
            }
        });

        mGombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeScreen(2);
            }
        });

        mMoyashi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeScreen(3);
            }
        });

        mSpinach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeScreen(4);
            }
        });

        mCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeScreen(5);
            }
        });

        mEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EggSelectFragment dialog = new EggSelectFragment();
                dialog.show(getSupportFragmentManager(),"sample");
                //ChangeScreen(6);
            }
        });
    }

    public void ChangeScreen(int num){
        Intent intent = new Intent(MainActivity.this,Timer_function.class);
        intent.putExtra("timerList", timerList.get(num));
        intent.putExtra("obList", obList.get(num));
        startActivity(intent);
    }
}