package com.alltaskes.thecalclatur.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.adpters.AdpterMunths;
import com.alltaskes.thecalclatur.databinding.FragmentOutlayManthBinding;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.DaysModle;
import com.alltaskes.thecalclatur.roomdatabase.Modle_itemMunth;

import java.util.ArrayList;


public class OutlayManthFragment extends Fragment {
    private AppDatabase database;
    ArrayList<Modle_itemMunth> getList;

    public OutlayManthFragment() {
        // Required empty public constructor
    }

    ArrayList<Modle_itemMunth> list;
    ArrayList<DaysModle> listdays;
    FragmentOutlayManthBinding binding;
    AdpterMunths adpter;
    private  double totalOutaly=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outlay_manth, container, false);
        database = AppDatabase.getDatabaseInstance(getActivity());

        datMaunth();


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
       // listdays.clear();
        listdays = (ArrayList<DaysModle>) database.daysDao().getAll();
        getTotalOfYear();
    }

    @Override
    public void onResume() {
        super.onResume();
        listdays.clear();
        listdays = (ArrayList<DaysModle>) database.daysDao().getAll();
        getTotalOfYear();

    }

    private void getTotalOfYear() {
        double totalOutaly=0.0;

            for (DaysModle days  : listdays) {
                totalOutaly +=  days.getOutalymony();
            }
            binding.tvResults.setText(totalOutaly + "");



    }

    private void datMaunth() {
        binding.rvMunths.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rvMunths.setHasFixedSize(true);
        list = new ArrayList<>();
        database.mounthDao().insertUser(new Modle_itemMunth(1, "one", "1", "Jan"));
        database.mounthDao().insertUser(new Modle_itemMunth(2, "two", "2", "Feb"));
        database.mounthDao().insertUser(new Modle_itemMunth(3, "vfdcc", "3", "Mar"));
        database.mounthDao().insertUser(new Modle_itemMunth(4, "yyyy", "4", "Apr"));
        database.mounthDao().insertUser(new Modle_itemMunth(5, "rrrr", "5", "May"));
        database.mounthDao().insertUser(new Modle_itemMunth(6, "jjj", "6", "June"));
        database.mounthDao().insertUser(new Modle_itemMunth(7, "uuuuu", "7", "July"));
        database.mounthDao().insertUser(new Modle_itemMunth(8, "dsdwed", "8", "Aug"));
        database.mounthDao().insertUser(new Modle_itemMunth(9, "ytjyh", "9", "Sept"));
        database.mounthDao().insertUser(new Modle_itemMunth(10, "hrehsrhsgr", "10", "Oct"));
        database.mounthDao().insertUser(new Modle_itemMunth(11, "hsrgrre", "11", "Nov"));
        database.mounthDao().insertUser(new Modle_itemMunth(12, "hsrehgh", "12", "Dec"));

        list = (ArrayList<Modle_itemMunth>) database.mounthDao().getAll();


        adpter = new AdpterMunths(list, getActivity());
        binding.rvMunths.setAdapter(adpter);
        adpter.notifyDataSetChanged();
        binding.rvMunths.scheduleLayoutAnimation();
    }
}