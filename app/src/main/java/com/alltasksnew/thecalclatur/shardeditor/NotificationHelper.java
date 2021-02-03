package com.alltasksnew.thecalclatur.shardeditor;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.screens.MainActivity;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }else {

        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification(String title,String body) {
        Uri uri  = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.sound_noty);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        i,
                        PendingIntent.FLAG_ONE_SHOT
                );


        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSound(uri)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.logotaskes)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }
}