package com.alltaskes.thecalclatur.servec;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModelRoom;
import com.alltaskes.thecalclatur.shardeditor.MyBroadCast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Myservec extends Service {

    ArrayList<ModelRoom>taskes;
    AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getDatabaseInstance(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                                     int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm", Locale.ENGLISH);
        Date date = new Date();
     String   currentdate=dateFormat.format(date);

        taskes = (ArrayList<ModelRoom>) database.userDao().getAll();
        AlarmManager mgrAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        int siz = taskes.size();

//        PendingIntent pi;
//        AlarmManager alarmManager;
        if (siz > 0) {
            for (int i = 0; i < siz; i++) {

//                pi=PendingIntent.getBroadcast(getApplicationContext(), i,intent, PendingIntent.FLAG_ONE_SHOT);
//                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent1 = new Intent(getApplicationContext(), MyBroadCast.class);
                // Loop counter `i` is used as a `requestCode`
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent1, 0);
                // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)


                intentArray.add(pendingIntent);
                String mydate = taskes.get(i).getDates();
                String time = taskes.get(i).getTimes();
                String dateandtime = mydate + " " + time;


                if (dateandtime.equalsIgnoreCase(currentdate)){
                    @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
                    try {
                        Date date1 = formatter.parse(dateandtime);

                        mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                Objects.requireNonNull(date1).getTime(),
                                pendingIntent);
                        currentdate="";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                // setAlarm("task", mydate, time);


            }


        }
            return START_NOT_STICKY;


    }
    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyBroadCast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}



