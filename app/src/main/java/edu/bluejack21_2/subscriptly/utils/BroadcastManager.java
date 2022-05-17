package edu.bluejack21_2.subscriptly.utils;

import static android.os.Build.VERSION_CODES.R;

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
import androidx.core.app.NotificationManagerCompat;

public class BroadcastManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "subscriptly")
                .setSmallIcon(edu.bluejack21_2.subscriptly.R.drawable.ic_upload_icon_foreground)
                .setContentTitle("Subscriptly")
                .setContentText("Hey, remember to pay your " + intent.getStringExtra("name") + " subscription!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
