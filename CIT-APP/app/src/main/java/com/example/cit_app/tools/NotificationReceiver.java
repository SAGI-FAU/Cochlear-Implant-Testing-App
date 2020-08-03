package com.example.cit_app.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cit_app.R;
import com.example.cit_app.other_activities.MainActivity;

import java.util.Calendar;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "notifyCIT";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channelCIT", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is description");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Intent activity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, activity, 0);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.arrow_left)
                .setContentTitle("This is title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManagerCompat.notify(1, notification);
    }*/
        Notifier notifications =new Notifier(context);
        if (intent.getAction() != null && context != null) {

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                notifications.setReminder(context, NotificationReceiver.class,
                        9, 0);
                return;

            }

        }

        String Title_notification=context.getResources().getString(R.string.title_notify);
        String content_notification=context.getResources().getString(R.string.content_notify);

        //Trigger the notification
        notifications.notifyUser(Title_notification, content_notification);

    }
}
