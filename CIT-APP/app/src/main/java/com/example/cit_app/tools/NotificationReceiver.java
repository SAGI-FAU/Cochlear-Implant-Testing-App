package com.example.cit_app.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.cit_app.R;

public class NotificationReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
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
