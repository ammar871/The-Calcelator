package com.alltaskes.thecalclatur.screens;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.databinding.ActivityMainBinding;
import com.alltaskes.thecalclatur.fragments.AddNodsFragment;
import com.alltaskes.thecalclatur.fragments.AllNodsFragment;
import com.alltaskes.thecalclatur.fragments.ObjectFragment;
import com.alltaskes.thecalclatur.fragments.OutlayManthFragment;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModelRoom;
import com.alltaskes.thecalclatur.servec.Myservec;
import com.alltaskes.thecalclatur.shardeditor.MyBroadCast;
import com.alltaskes.thecalclatur.shardeditor.ShardEditor;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final static int ID_HOME = 1;
    private final static int ID_VIDEO = 2;
    private final static int ID_ADD_QUESTION = 3;
    private final static int ID_BOOKS = 4;
    private final static int ID_ACCOUNT = 5;

    ActivityMainBinding binding;
    ShardEditor shardEditor;
    ArrayList<ModelRoom> taskes = new ArrayList<>();
    private static final String LOG_TAG = "AudioRecordTest";
String currentdate;
    AppDatabase databasroom;
    String getBack;
    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shardEditor = new ShardEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        databasroom = AppDatabase.getDatabaseInstance(this);


        inItBottomNav();
        loadFragment(new AddNodsFragment());
        startService(new Intent(MainActivity.this, Myservec.class));
        listTims();


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

            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void listTims() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm", Locale.ENGLISH);
        Date date = new Date();
        currentdate=dateFormat.format(date);

        taskes = (ArrayList<ModelRoom>) databasroom.userDao().getAll();
        AlarmManager mgrAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        int siz = taskes.size();

//        PendingIntent pi;
//        AlarmManager alarmManager;
        if (siz > 0) {
            for (int i = 0; i < siz; i++) {

//                pi=PendingIntent.getBroadcast(getApplicationContext(), i,intent, PendingIntent.FLAG_ONE_SHOT);
//                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), MyBroadCast.class);
                // Loop counter `i` is used as a `requestCode`
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, 0);
                // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)


                intentArray.add(pendingIntent);
                String mydate = taskes.get(i).getDates();
                String time = taskes.get(i).getTimes();
                String dateandtime = mydate + " " + time;
                Log.d(TAG, "listTims: "+dateandtime +"\n"+currentdate);

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
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        listTims();

    }


    private void inItBottomNav() {
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);


        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_baseline_add_circle_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_VIDEO, R.drawable.ic_baseline_format_list_bulleted_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD_QUESTION, R.drawable.ic_baseline_text_fields_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_BOOKS, R.drawable.teee));


        bottomNavigation.setCount(ID_VIDEO, "");
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case ID_HOME:
                        loadFragment(new AddNodsFragment());
                        break;
                    case ID_ADD_QUESTION:
                        loadFragment(new ObjectFragment());
                        break;
//                    case ID_MESSAGE:
//                        name = "MESSAGE";
//                        break;
                    case ID_BOOKS:
                        loadFragment(new OutlayManthFragment());
                        break;
                    case ID_VIDEO:
                        loadFragment(new AllNodsFragment());
                        break;
//                    default:
//                        name = "";
                }
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                String name;
                switch (item.getId()) {
//                    case ID_HOME:
//                        name = "HOME";
//                        break;
//                    case ID_EXPLORE:
//                        name = "EXPLORE";
//                        break;
//                    case ID_MESSAGE:
//                        name = "MESSAGE";
//                        break;
//                    case ID_NOTIFICATION:
//                        name = "NOTIFICATION";
//                        break;
//                    case ID_ACCOUNT:
//                        name = "ACCOUNT";
//                        break;
//                    default:
//                        name = "";
                }
//                tvSelected.setText(getString(R.string.main_page_selected, name));
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(MainActivity.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });


        bottomNavigation.show(ID_HOME, true);
    }


    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutview, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
//        ExampleThread thread = new ExampleThread(10);
//        thread.start();
        super.onDestroy();
    }

    /*   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startNotifyWithAlarm() throws ParseException {

        taskes = (ArrayList<ModelRoom>) databasroom.userDao().getAll();

        int siz = taskes.size();


        listTims(siz);
    }*/
    /*    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void alarmNoty(Long c) {

        Calendar cc =Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (cc.before(Calendar.getInstance())) {
            cc.add(Calendar.DATE, 1);
        }
        c = cc.getTimeInMillis();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c, pendingIntent);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
        Log.d("longdate", c.getTimeInMillis() + "");
    }*/
}



