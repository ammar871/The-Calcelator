package com.alltaskes.thecalclatur.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.adpters.AdpterDays;
import com.alltaskes.thecalclatur.adpters.AdpterIcons;
import com.alltaskes.thecalclatur.adpters.AdpterIcons.OnClickListener;
import com.alltaskes.thecalclatur.databinding.ActivityAddOutlayBinding;
import com.alltaskes.thecalclatur.modls.Days;
import com.alltaskes.thecalclatur.modls.Outaly;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.DaysModle;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_outlay);
        database = AppDatabase.getDatabaseInstance(this);
        getIntentData();
        onClicksButtons();


    }

    private void getIntentData() {
        if (getIntent() != null) {
            nub_Munth = getIntent().getStringExtra("numberMunth");
            getKey = getIntent().getStringExtra("key");
            binding.munthTv.setText("  شهر :" + nub_Munth);


        }
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
                    Toast.makeText(AddOutlayActivity.this, "تمت الاضافة ", Toast.LENGTH_SHORT).show();
                   Intent intent=new Intent(AddOutlayActivity.this, MenuOutalyActivity.class);
                    intent.putExtra("numberMunth",nub_Munth);
                    intent.putExtra("key",getKey);
                    startActivity(intent);
                    finish();

                }else {
                    binding.edtQutalyMony.setError("املئ الحقل ...");
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
                onBackPressed();
            }
        });
    }

    private void setRc_Icons() {
        binding.showingRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.showingRv.setHasFixedSize(true);
        listoutaly = new ArrayList<>();

        listoutaly.add(new Outaly(R.drawable.one, 0));
        listoutaly.add(new Outaly(R.drawable.two, 1));
        listoutaly.add(new Outaly(R.drawable.three, 2));
        listoutaly.add(new Outaly(R.drawable.four, 3));
        listoutaly.add(new Outaly(R.drawable.five, 4));
        listoutaly.add(new Outaly(R.drawable.sex, 5));
        listoutaly.add(new Outaly(R.drawable.seven, 6));
        listoutaly.add(new Outaly(R.drawable.eatht, 7));
        listoutaly.add(new Outaly(R.drawable.nine, 8));
        listoutaly.add(new Outaly(R.drawable.ten, 9));
        listoutaly.add(new Outaly(R.drawable.elevan, 10));
        listoutaly.add(new Outaly(R.drawable.twalf, 11));


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


}