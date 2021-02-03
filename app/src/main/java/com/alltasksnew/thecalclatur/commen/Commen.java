package com.alltasksnew.thecalclatur.commen;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.alltasksnew.thecalclatur.shardeditor.ShardEditor;

import java.util.Locale;

public class Commen {

    final static String nams="";

    public static void updateLanguage(Context context, String selectedLanguage) {
        if (!"".equals(selectedLanguage)) {
            if ("English".equals(selectedLanguage)) {
                selectedLanguage = "en";
            } else if ("Traditional Chinese".equals(selectedLanguage)) {
                selectedLanguage = "zh";
            }
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, null);
        }
    }
    public static void setLocale(Context context,String lang) {
        Locale myLocale = new Locale(lang);
        Resources res =context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    public static void setlang(Context context){
        if (ShardEditor.getLanguage(context).equals("en")){
            Commen.setLocale(context,"en");
        }else if (ShardEditor.getLanguage(context).equals("ar")){
            Commen.setLocale(context,"ar");
        }

    }
}
