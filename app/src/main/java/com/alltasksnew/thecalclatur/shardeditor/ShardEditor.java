package com.alltasksnew.thecalclatur.shardeditor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ShardEditor {
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;
    private static final String FILE_NAME = "coursatApp";
    public static final String KEY_USER_Color = "name";
    public static final String KEY_USER_salary = "salary";
    public static final String KEY_AUDIO = "audio";
    public static final String KEY_LOCATION = "address";
    public static final String LANGUAGE_PREFERENCES = "language_preferences";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String WELCOME_PREFERENCES = "saft_mall_preferences";
    public ShardEditor(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void saveData(int muser) {
        mEditor.putInt(KEY_USER_Color, muser);
        mEditor.commit();
    }

    public void saveSalary(String muser) {
        mEditor.putString(KEY_USER_salary, muser);
        mEditor.commit();
    }
    public HashMap<String, String> getSalary() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_USER_salary, mSharedPreferences.getString(KEY_USER_salary, ""));

        return userData;
    }


    public HashMap<String, Integer> loadData() {
        HashMap<String, Integer> userData = new HashMap<>();
        userData.put(KEY_USER_Color, mSharedPreferences.getInt(KEY_USER_Color, 0));


        return userData;
    }


    public static String getLanguage(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(LANGUAGE_PREFERENCES,MODE_PRIVATE);
        return sharedpreferences.getString("lang","0");
    }
    public static void saveLanguage(Context context, String language) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(LANGUAGE_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lang",language);
        editor.commit();
    }

    public void setLanguageSelect(Context context,boolean isFirstTime) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(WELCOME_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        editor.commit();
    }

    public static boolean getLanguageSelect(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(WELCOME_PREFERENCES,MODE_PRIVATE);
        return sharedpreferences.getBoolean(IS_FIRST_TIME_LAUNCH,false);

    }

}
