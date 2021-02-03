package com.alltasksnew.thecalclatur.servec;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.screens.MainActivity;

import java.util.Date;

public class NotiyWorkerManger {
    Context context;

    public NotiyWorkerManger(Context context) {
        this.context = context;
    }

    public static void notyFication(Context context){
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");


        mBuilder.setSmallIcon(R.drawable.logotaskes);
        mBuilder.setAutoCancel(true);


        mBuilder.setContentTitle("جارى التسجيل ");


        mBuilder.setPriority(Notification.PRIORITY_HIGH);

        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;

        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);

        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Notification notification = mBuilder.build();
        notificationManager.notify(m, notification);
    }
}
