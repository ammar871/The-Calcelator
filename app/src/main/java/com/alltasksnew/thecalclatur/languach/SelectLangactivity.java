package com.alltasksnew.thecalclatur.languach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.databinding.ActivitySelectLangactivityBinding;
import com.alltasksnew.thecalclatur.screens.MainActivity;
import com.alltasksnew.thecalclatur.shardeditor.ShardEditor;

public class SelectLangactivity extends AppCompatActivity {
ActivitySelectLangactivityBinding binding;
ShardEditor shardEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shardEditor=new ShardEditor(this);
        if (ShardEditor.getLanguageSelect(this)){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_langactivity);

        binding.cardArbic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShardEditor.saveLanguage(SelectLangactivity.this,"ar");
                shardEditor.setLanguageSelect(SelectLangactivity.this,true);
                startActivity(new Intent(SelectLangactivity.this, MainActivity.class));
                finish();
            }
        });
        binding.cardEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShardEditor.saveLanguage(SelectLangactivity.this,"en");
                shardEditor.setLanguageSelect(SelectLangactivity.this,true);
                startActivity(new Intent(SelectLangactivity.this, MainActivity.class));
                finish();
            }
        });
    }



}