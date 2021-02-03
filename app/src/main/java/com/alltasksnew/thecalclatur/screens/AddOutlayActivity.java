package com.alltasksnew.thecalclatur.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.adpters.AdpterDays;
import com.alltasksnew.thecalclatur.adpters.AdpterIcons;
import com.alltasksnew.thecalclatur.adpters.AdpterIcons.OnClickListener;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.ActivityAddOutlayBinding;
import com.alltasksnew.thecalclatur.modls.Days;
import com.alltasksnew.thecalclatur.modls.Outaly;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.DaysModle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class AddOutlayActivity extends AppCompatActivity implements AdpterDays.OnItemClickListener, OnClickListener {
    ActivityAddOutlayBinding binding;
    String nub_Munth, getKey;
    int kindoutlay=0;
    ArrayList<Days> list;
    AdpterDays adpter;
    ArrayList<Outaly> listoutaly;
    AdpterIcons adpterIcons;
    private AppDatabase database;
    DaysModle days;
    private InterstitialAd mInterstitialAd;
    private MediaPlayer player = null;
    private AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Commen.setlang(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_outlay);
        player = new MediaPlayer();
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        database = AppDatabase.getDatabaseInstance(this);

        //adds

        mInterstitialAd = new InterstitialAd(this);
        MobileAds.initialize(this,"ca-app-pub-5582512134167391~5930186201");
        mInterstitialAd.setAdUnitId("ca-app-pub-5582512134167391/9969503565");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
               // mInterstitialAd.show();

            }
        });

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, initializationStatus -> {

        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();
        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);

        getIntentData();
        onClicksButtons();


    }

    @SuppressLint("SetTextI18n")
    private void getIntentData() {
        if (getIntent() != null) {
            nub_Munth = getIntent().getStringExtra("numberMunth");
            getKey = getIntent().getStringExtra("key");
            binding.munthTv.setText(getString(R.string.mounthe) + nub_Munth);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void onClicksButtons() {
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edtQutalyMony.getText().toString();
                if (!binding.edtQutalyMony.getText().toString().equalsIgnoreCase("")){


                    days=new DaysModle(0,binding.tvNumDays.getText().toString(),kindoutlay,getKey,
                            Double.parseDouble(binding.edtQutalyMony.getText().toString()));
                    database.daysDao().insertUser(days);
                   binding.edtQutalyMony.setText("");
                    Toast.makeText(AddOutlayActivity.this, R.string.Added, Toast.LENGTH_SHORT).show();
                     playAudio(R.raw.saveotlay);
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();
                    }

                     Intent intent=new Intent(AddOutlayActivity.this, MenuOutalyActivity.class);
                    intent.putExtra("numberMunth",nub_Munth);
                    intent.putExtra("key",getKey);
                    startActivity(intent);
                    finish();

                }else {
                    binding.edtQutalyMony.setError(getString(R.string.fil_box));
                }
            }
        });
        binding.tvNumDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cardRc.setVisibility(View.VISIBLE);
                setRC_Days();
            }
        });
        binding.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cardRc.setVisibility(View.VISIBLE);
                setRc_Icons();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
                onBackPressed();
            }
        });
    }

    private void setRc_Icons() {
        binding.showingRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.showingRv.setHasFixedSize(true);
        listoutaly = new ArrayList<>();

        listoutaly.add(new Outaly(R.drawable.ic_noun_shopping_bags_1453373, 0));
        listoutaly.add(new Outaly(R.drawable.ic_grocery, 1));
        listoutaly.add(new Outaly(R.drawable.ic_work_from_home, 2));
        listoutaly.add(new Outaly(R.drawable.ic_avatar, 3));
        listoutaly.add(new Outaly(R.drawable.ic_family, 4));
        listoutaly.add(new Outaly(R.drawable.ic_suitcase, 5));
        listoutaly.add(new Outaly(R.drawable.ic_bill, 6));
        listoutaly.add(new Outaly(R.drawable.ic_medicine, 7));
        listoutaly.add(new Outaly(R.drawable.ic_car, 8));
        listoutaly.add(new Outaly(R.drawable.ic_chairs, 9));
        listoutaly.add(new Outaly(R.drawable.ic_public_transport, 10));
        listoutaly.add(new Outaly(R.drawable.ic_file, 11));


        adpterIcons = new AdpterIcons(listoutaly, this, this);
        binding.showingRv.setAdapter(adpterIcons);
        adpterIcons.notifyDataSetChanged();
        binding.showingRv.scheduleLayoutAnimation();

    }

    private void setRC_Days() {

        binding.showingRv.setLayoutManager(new GridLayoutManager(this, 4));
        binding.showingRv.setHasFixedSize(true);
        list = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {

            int index = i;

            list.add(new Days(index));

        }


        adpter = new AdpterDays(list, this, this);
        binding.showingRv.setAdapter(adpter);
        adpter.notifyDataSetChanged();
        binding.showingRv.scheduleLayoutAnimation();
    }

    @Override
    public void onItemClick(int item) {
        binding.cardRc.setVisibility(View.GONE);
        binding.tvNumDays.setText(item + "");

    }

    @Override
    public void onClick(Outaly item) {
        binding.cardRc.setVisibility(View.GONE);
        binding.imgIcon.setImageResource(item.getIcon());
        kindoutlay=item.getKindOutaly();
        Log.d("uridrawb", kindoutlay+"");

    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                player.pause();
                player.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                player.start();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener
            = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();

        }
    };
    private void playAudio(int savenod) {


        releaseMediaPlayer();
        player = MediaPlayer.create(getApplicationContext(), savenod);
//        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
//                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//
//
//            player = MediaPlayer.create(getApplicationContext(), savenod);




        player.start();
        //  player.setOnCompletionListener(mCompletionListener);



    }

    private void releaseMediaPlayer() {

        if (player != null) {

            player.release();


            player = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }
}