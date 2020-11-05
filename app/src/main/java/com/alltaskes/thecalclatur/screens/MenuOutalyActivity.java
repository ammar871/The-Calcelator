package com.alltaskes.thecalclatur.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.adpters.AdpterOutalyOfMounth;
import com.alltaskes.thecalclatur.databinding.ActivityMenuOutalyBinding;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.DaysModle;

import java.util.ArrayList;

public class MenuOutalyActivity extends AppCompatActivity implements AdpterOutalyOfMounth.Callback {
    String nub_Munth;
    String getKey;
    ActivityMenuOutalyBinding binding;
    ArrayList<DaysModle> list1;
    ArrayList<DaysModle> list2;
    private AppDatabase database;
    DaysModle daysModle;
    AdpterOutalyOfMounth adpterOutalyOfMounth;

    int sizeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_outaly);
        database = AppDatabase.getDatabaseInstance(this);
        list1 = (ArrayList<DaysModle>) database.daysDao().getAll();
        list2 = new ArrayList<>();
        if (getIntent() != null) {
            nub_Munth = getIntent().getStringExtra("numberMunth");
            binding.munthTv.setText("  شهر :" + nub_Munth);
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
                adpterOutalyOfMounth.notifyDataSetChanged();
                adpterOutalyOfMounth.setmCallback(this);
                binding.rcMenu.setAdapter(adpterOutalyOfMounth);

            }
        }

    }

    @Override
    public void onDeleteClick(DaysModle mUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Deleting")
                .setMessage("هل تريد الحذف؟")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        database.daysDao().delete(mUser);
                        getDataAgain();
                        adpterOutalyOfMounth.addItems(list2);
                        Toast.makeText(MenuOutalyActivity.this, "تم الحذف", Toast.LENGTH_SHORT).show();
                        getTotalOutaly();
                        listIsempty();


                    }
                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
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
}