package com.project.lrnshub.service;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.project.lrnshub.R;
import com.project.lrnshub.ResultActivity;

public class InstallAppReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_TEXT = "notification_text";

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = "myInstallAppNotificationChannel";
        Intent notifyIntent = new Intent(context, ResultActivity.class);
        // Set the Activity to start in a new, empty task
        String pkg = intent.getStringExtra("pkg");
        notifyIntent.putExtra("pkg", pkg);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("LRNSHUB Notification")
                .setContentText("Install This New App Recommended For You")
                .setSound(defaultSoundUri);
        builder.setContentIntent(notifyPendingIntent);
        NotificationChannel notificationChannel = new NotificationChannel(channelId, "Notify", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
