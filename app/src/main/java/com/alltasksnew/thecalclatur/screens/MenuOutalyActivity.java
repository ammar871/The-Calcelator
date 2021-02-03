package com.alltasksnew.thecalclatur.screens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.adpters.AdpterOutalyOfMounth;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.ActivityMenuOutalyBinding;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.DaysModle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Collections;

public class MenuOutalyActivity extends AppCompatActivity implements AdpterOutalyOfMounth.Callback {
    String nub_Munth;
    String getKey;
    ActivityMenuOutalyBinding binding;
    ArrayList<DaysModle> list1;
    ArrayList<DaysModle> list2;
    private AppDatabase database;
    DaysModle daysModle;
    AdpterOutalyOfMounth adpterOutalyOfMounth;
    private MediaPlayer player = null;
    private AudioManager mAudioManager;
    int sizeList;
    InterstitialAd mInterstitialAd;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Commen.setlang(this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5582512134167391/2923737156");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        if (mInterstitialAd.isLoaded())
         //   mInterstitialAd.show();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_outaly);
        database = AppDatabase.getDatabaseInstance(this);
        player = new MediaPlayer();
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        list1 = (ArrayList<DaysModle>) database.daysDao().getAll();
        list2 = new ArrayList<>();
        if (getIntent() != null) {
            nub_Munth = getIntent().getStringExtra("numberMunth");
            binding.munthTv.setText(getString(R.string.mounthe) + nub_Munth);
            getKey = getIntent().getStringExtra("key");
            Log.d("keyyy", getKey + "");
        }
        getListFromRoom();
        getTotalOutaly();
        listIsempty();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuOutalyActivity.this, AddOutlayActivity.class);
                intent.putExtra("numberMunth", nub_Munth);
                intent.putExtra("key", getKey);
                startActivity(intent);
                finish();
            }
        });
    }

    private void listIsempty() {
        if (list2.isEmpty() || list2.size() < 0) {
            binding.tvIsemptty.setVisibility(View.VISIBLE);
        } else {
            binding.tvIsemptty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTotalOutaly();
    }

    private void getTotalOutaly() {
        double totalOutaly = 0.0;
        for (DaysModle daysModle : list2) {
            totalOutaly += daysModle.getOutalymony();
        }
        binding.tvResults.setText(totalOutaly + "");


    }

    private void getDataAgain() {
        list1.clear();
        list2.clear();
        list1 = (ArrayList<DaysModle>) database.daysDao().getAll();
        sizeList = list1.size();
        if (sizeList >= 0) {
            for (int i = 0; i < sizeList; i++) {
                if (list1.get(i).getKey().equals(getKey)) {

                    daysModle = new DaysModle(list1.get(i).getId()
                            , list1.get(i).getNubDay(),
                            list1.get(i).getKindOutlay(),
                            list1.get(i).getKey(),
                            list1.get(i).getOutalymony());
                    list2.add(daysModle);


                }
                adpterOutalyOfMounth = new AdpterOutalyOfMounth(list2, this);
                Collections.reverse(list2);
                adpterOutalyOfMounth.notifyDataSetChanged();
                adpterOutalyOfMounth.setmCallback(this);
                binding.rcMenu.setAdapter(adpterOutalyOfMounth);

            }
        }


    }

    private void getListFromRoom() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rcMenu.setLayoutManager(mLayoutManager);
        binding.rcMenu.setHasFixedSize(true);

        sizeList = list1.size();
        if (sizeList >= 0) {
            for (int i = 0; i < sizeList; i++) {
                if (list1.get(i).getKey().equals(getKey)) {

                    daysModle = new DaysModle(list1.get(i).getId()
                            , list1.get(i).getNubDay(),
                            list1.get(i).getKindOutlay(),
                            list1.get(i).getKey(),
                            list1.get(i).getOutalymony());
                    list2.add(daysModle);


                }
                adpterOutalyOfMounth = new AdpterOutalyOfMounth(list2, this);
                Collections.reverse(list2);
                adpterOutalyOfMounth.notifyDataSetChanged();
                adpterOutalyOfMounth.setmCallback(this);
                binding.rcMenu.setAdapter(adpterOutalyOfMounth);

            }
        }

    }

    @Override
    public void onDeleteClick(DaysModle mUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.are_you_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        database.daysDao().delete(mUser);
                        getDataAgain();
                        adpterOutalyOfMounth.addItems(list2);
                        Toast.makeText(MenuOutalyActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
                        playAudio(R.raw.deleteoutaly);
                        getTotalOutaly();
                        listIsempty();



                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getTotalOutaly();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
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