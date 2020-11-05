package com.alltaskes.thecalclatur.shardeditor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.room.RoomDatabase;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModelRoom;
import com.alltaskes.thecalclatur.screens.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyBroadCast extends BroadcastReceiver {
    ArrayList<ModelRoom> taskes;
    AppDatabase databasroom;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());


    /*    databasroom = AppDatabase.getDatabaseInstance(context);

        taskes = (ArrayList<ModelRoom>) databasroom.userDao().getAll();
        String today = (String) android.text.format.DateFormat.format(
                "hh:mm", new java.util.Date());

        Date  c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        int siz = taskes.size();


        if (siz > 0) {
            for (int i = 0; i < siz; i++) {
                Date strDate = null;
                Date date1 = null;
                Date date2 = null;
                String mydate = taskes.get(i).getDate();
                String time = taskes.get(i).getTime();
                SimpleDateFormat sdftime = new SimpleDateFormat("hh:mm");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date1 = sdftime.parse(time);
                    date2 = sdftime.parse(today);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                try {
                    strDate = sdf.parse(mydate);
                    Log.d("datedd",""+mydate+"" + "/"+strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert date1 != null;
                if (new Date().equals(strDate) ) {
                    Intent it = new Intent(context, MainActivity.class);
                    createNotification(context, it, "new mensage", "body!", "this is a mensage");
                }

            }



        }
*/

    }

    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence descricao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.logotaskes);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.logotaskes, n);
        //create a vibration
        try {

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }
    }
}

