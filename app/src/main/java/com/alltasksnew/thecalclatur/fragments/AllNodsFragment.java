package com.alltasksnew.thecalclatur.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
import com.alltasksnew.thecalclatur.adpters.NodsAdpter;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.FragmentAllNodsBinding;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.ModelRoom;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class AllNodsFragment extends Fragment implements NodsAdpter.Callback, NodsAdpter.OnItemClickListener {
    private AppDatabase database;
    ArrayList<ModelRoom> cart = new ArrayList<>();
    NodsAdpter mUserAdapter;
    private MediaPlayer player = null;
    private AudioManager mAudioManager;

    public AllNodsFragment() {
        // Required empty public constructor
    }

    FragmentAllNodsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Commen.setlang(getActivity());
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_nods, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        database = AppDatabase.getDatabaseInstance(getActivity());
        //adds
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();


        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest);


        setUp();
        listIsempty();
        return binding.getRoot();
    }

    private void setUp() {
        cart.clear();
        cart = (ArrayList<ModelRoom>) database.userDao().getAll();
        Collections.reverse(cart);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rcNods.setLayoutManager(mLayoutManager);
        binding.rcNods.setItemAnimator(new DefaultItemAnimator());
        mUserAdapter = new NodsAdpter(cart, getContext(), this);
        mUserAdapter.notifyDataSetChanged();
        mUserAdapter.setmCallback(this);
        binding.rcNods.setAdapter(mUserAdapter);
    }

    @Override
    public void onDeleteClick(ModelRoom mUser) {
        database.userDao().delete(mUser);
        setUp();
        playAudio(R.raw.delete);
        Toast.makeText(getActivity(), R.string.deleting, Toast.LENGTH_SHORT).show();
    }

    private void listIsempty() {
        if (cart.isEmpty() || cart.size() < 0) {
            binding.tvIsemptty.setVisibility(View.VISIBLE);
        } else {
            binding.tvIsemptty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(ModelRoom item) {
        if (item.getAudio().equalsIgnoreCase("notFound")) {
            Toast.makeText(getActivity(), R.string.not_audio, Toast.LENGTH_SHORT).show();

        } else {
            startPlaying(item.getAudio());

        }


    }

    private void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            player.setOnCompletionListener(mCompletionListener);
        } catch (IOException e) {
            Log.e("audio", "prepare() failed");
        }
    }

    @Override
    public void onStop() {
        super.onStop();


        if (player != null) {
            player.release();
            player = null;
        }
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
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener
            = mediaPlayer -> releaseMediaPlayer();

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