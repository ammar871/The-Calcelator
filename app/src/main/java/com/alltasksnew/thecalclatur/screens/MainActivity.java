package com.alltasksnew.thecalclatur.screens;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.ActivityMainBinding;
import com.alltasksnew.thecalclatur.fragments.AddNodsFragment;
import com.alltasksnew.thecalclatur.fragments.AllNodsFragment;
import com.alltasksnew.thecalclatur.fragments.ObjectFragment;
import com.alltasksnew.thecalclatur.fragments.OutlayManthFragment;
import com.alltasksnew.thecalclatur.fragments.SittingFragment;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.ModelRoom;
import com.alltasksnew.thecalclatur.shardeditor.MyBroadCast;
import com.alltasksnew.thecalclatur.shardeditor.ShardEditor;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.InterstitialAd;

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
    private final static int ID_SITTING = 5;

    ActivityMainBinding binding;
    ShardEditor shardEditor;
    ArrayList<ModelRoom> taskes = new ArrayList<>();
    private static final String LOG_TAG = "AudioRecordTest";
    String currentdate;
    AppDatabase databasroom;
    String getBack;
    private static final String TAG = "MainActivity";
    private InterstitialAd mInterstitialAd;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shardEditor = new ShardEditor(this);

        if (ShardEditor.getLanguage(this).equals("en")){
            Commen.setLocale(this,"en");
        }else if (ShardEditor.getLanguage(this).equals("ar")){
            Commen.setLocale(this,"ar");
        }



//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-5582512134167391/9969503565");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        if (mInterstitialAd.isLoaded())
//           // mInterstitialAd.show();



        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        databasroom = AppDatabase.getDatabaseInstance(this);


        inItBottomNav();
        loadFragment(new AddNodsFragment());
    /*    startService(new Intent(MainActivity.this, MyServec.class));
        listTims();
*/

    }




    private void listTims() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm", Locale.ENGLISH);
        Date date = new Date();
        currentdate = dateFormat.format(date);

        taskes = (ArrayList<ModelRoom>) databasroom.userDao().getAll();
        AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
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
                Log.d(TAG, "listTims: " + dateandtime + "\n" + currentdate);

                if (dateandtime.equalsIgnoreCase(currentdate)) {
                    @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
                    try {
                        Date date1 = formatter.parse(dateandtime);

                        mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                Objects.requireNonNull(date1).getTime(),
                                pendingIntent);
                        currentdate = "";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                // setAlarm("task", mydate, time);


            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Commen.setlang(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        //listTims();
        Commen.setlang(this);

    }


    private void inItBottomNav() {
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);


        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_baseline_add_circle_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_VIDEO, R.drawable.ic_menu));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD_QUESTION, R.drawable.ic_title_24px));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_BOOKS, R.drawable.ic_wallet));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_SITTING, R.drawable.siting));



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
                    case ID_BOOKS:
                        loadFragment(new OutlayManthFragment());
                        break;
                    case ID_VIDEO:
                        loadFragment(new AllNodsFragment());
                        break;
                    case ID_SITTING:
                        loadFragment(new SittingFragment());
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
               // Toast.makeText(MainActivity.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
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

    private void loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layoutview, fragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        //listTims();
        super.onDestroy();
    }



}



