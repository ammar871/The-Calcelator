package com.alltasksnew.thecalclatur.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.adpters.AdpterObejct;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.FragmentObjectBinding;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.ModleObjct;
import com.alltasksnew.thecalclatur.screens.AddObjectActiviy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Collections;


public class ObjectFragment extends Fragment implements AdpterObejct.Callback{

FragmentObjectBinding binding;
    private AppDatabase database;
    ArrayList<ModleObjct> cart = new ArrayList<>();
    AdpterObejct mUserAdapter;
    private AudioManager mAudioManager;
    private MediaPlayer player = null;
    InterstitialAd mInterstitialAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Commen.setlang(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_object, container, false);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        database = AppDatabase.getDatabaseInstance(getActivity());
        getAdds();
        setUp();
        listIsempty();
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mInterstitialAd.isLoaded())
               //     mInterstitialAd.show();

                startActivity(new Intent(getActivity(), AddObjectActiviy.class));

            }
        });

        return binding.getRoot();
    }

    private void getAdds() {
        //adds
        mInterstitialAd = new InterstitialAd(getActivity());
        MobileAds.initialize(getActivity(),"ca-app-pub-5582512134167391~5930186201");
        mInterstitialAd.setAdUnitId("ca-app-pub-5582512134167391/6540167085");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                mInterstitialAd.show();


            }
        });

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
    private void listIsempty() {
        if (cart.isEmpty()||cart.size()<0){
            binding.tvIsemptty.setVisibility(View.VISIBLE);
        }else {
            binding.tvIsemptty.setVisibility(View.GONE);
        }
    }
    private void setUp() {
        cart = (ArrayList<ModleObjct>) database.obejectDao().getAll();
        Collections.reverse(cart);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvObeject.setLayoutManager(mLayoutManager);
        binding.rvObeject.setItemAnimator(new DefaultItemAnimator());
        mUserAdapter = new AdpterObejct(cart, getContext());
        mUserAdapter.notifyDataSetChanged();
        mUserAdapter.setmCallback(this);
        binding.rvObeject.setAdapter(mUserAdapter);
    }

    @Override
    public void onDeleteClick(ModleObjct mUser) {
        database.obejectDao().delete(mUser);
        mUserAdapter.addItems(database.obejectDao().getAll());
        playAudio(R.raw.delete);
        Toast.makeText(getActivity(), R.string.deleting, Toast.LENGTH_SHORT).show();
        setUp();
        listIsempty();
    }

    @Override
    public void onResume() {
        super.onResume();
        cart.clear();
        setUp();
        listIsempty();
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

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


            player = MediaPlayer.create(getActivity(), savenod);




            player.start();
            player.setOnCompletionListener(mCompletionListener);



        }
    }
    private void releaseMediaPlayer() {

        if (player != null) {

            player.release();


            player = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }
}