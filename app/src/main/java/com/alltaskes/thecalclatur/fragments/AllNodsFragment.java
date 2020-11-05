package com.alltaskes.thecalclatur.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.adpters.NodsAdpter;
import com.alltaskes.thecalclatur.databinding.FragmentAllNodsBinding;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModelRoom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class AllNodsFragment extends Fragment implements NodsAdpter.Callback,NodsAdpter.OnItemClickListener{
    private AppDatabase database;
    ArrayList<ModelRoom> cart = new ArrayList<>();
    NodsAdpter mUserAdapter;
    private MediaPlayer player = null;
    public AllNodsFragment() {
        // Required empty public constructor
    }

FragmentAllNodsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_nods, container, false);

        database = AppDatabase.getDatabaseInstance(getActivity());


setUp();
        listIsempty();
        return binding.getRoot();
    }
    private void setUp() {
        cart = (ArrayList<ModelRoom>) database.userDao().getAll();
        Collections.reverse(cart);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rcNods.setLayoutManager(mLayoutManager);
        binding.rcNods.setItemAnimator(new DefaultItemAnimator());
       mUserAdapter = new NodsAdpter(cart, getContext(),this);
        mUserAdapter.notifyDataSetChanged();
        mUserAdapter.setmCallback(this);
        binding.rcNods.setAdapter(mUserAdapter);
}

    @Override
    public void onDeleteClick(ModelRoom mUser) {
        database.userDao().delete(mUser);
        mUserAdapter.addItems(database.userDao().getAll());
        Toast.makeText(getActivity(), "تم الحذف", Toast.LENGTH_SHORT).show();
    }
    private void listIsempty() {
        if (cart.isEmpty()||cart.size()<0){
            binding.tvIsemptty.setVisibility(View.VISIBLE);
        }else {
            binding.tvIsemptty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(ModelRoom item) {
            if (item.getAudio().equalsIgnoreCase("notFound")){
                Toast.makeText(getActivity(), "لايوجد مقطع صوتى ", Toast.LENGTH_SHORT).show();

            }else {
                startPlaying(item.getAudio());
            }


    }
    private void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
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
}