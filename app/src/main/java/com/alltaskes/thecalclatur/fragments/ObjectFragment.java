package com.alltaskes.thecalclatur.fragments;

import android.content.Intent;
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

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.adpters.AdpterObejct;
import com.alltaskes.thecalclatur.databinding.FragmentObjectBinding;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModleObjct;
import com.alltaskes.thecalclatur.screens.AddObjectActiviy;

import java.util.ArrayList;
import java.util.Collections;


public class ObjectFragment extends Fragment implements AdpterObejct.Callback{

FragmentObjectBinding binding;
    private AppDatabase database;
    ArrayList<ModleObjct> cart = new ArrayList<>();
    AdpterObejct mUserAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_object, container, false);

        database = AppDatabase.getDatabaseInstance(getActivity());
        setUp();
        listIsempty();
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddObjectActiviy.class));

            }
        });

        return binding.getRoot();
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
        Toast.makeText(getActivity(), "تم الحذف", Toast.LENGTH_SHORT).show();
        listIsempty();
    }

    @Override
    public void onResume() {
        super.onResume();
        cart.clear();
        setUp();
    }
}