package com.project.lrnshub.ui.alarm;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.lrnshub.R;
import com.project.lrnshub.databinding.ActivityAlarmBinding;

import java.util.Calendar;

public class NotificationPublisher extends BroadcastReceiver {

    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION_TEXT = "notification_text";

    @Override
    public void onReceive(Context context, Intent intent) {


        String channelId = "myNotificationChannel";
        NotificationChannel notificationChannel = new NotificationChannel(channelId , "Notify", NotificationManager.IMPORTANCE_HIGH);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        String notificationText = intent.getStringExtra(NOTIFICATION_TEXT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Scheduled Notification")
                .setContentText(notificationText)
                .setSound(defaultSoundUri);
        notificationManager.notify(notificationId, notification.build());
    }
}
