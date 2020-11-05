package com.alltaskes.thecalclatur.shardeditor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ShardEditor {  SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;
    private static final String FILE_NAME = "coursatApp";
    public static final String KEY_USER_Color = "name";
    public static final String KEY_USER_PHONE = "audio";
    public static final String KEY_LOCATION = "address";

    public ShardEditor(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void saveData(int muser) {
        mEditor.putInt(KEY_USER_Color, muser);



        mEditor.commit();
    }
    public void saveDataaudio(String muser) {
        mEditor.putString(KEY_USER_Color, muser);



        mEditor.commit();
    }
    public HashMap<String, Integer> loadData() {
        HashMap<String, Integer> userData = new HashMap<>();
        userData.put(KEY_USER_Color, mSharedPreferences.getInt(KEY_USER_Color, 0));

        return userData;
    }




}
