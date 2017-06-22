package com.synnefx.cqms.event.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.synnefx.cqms.event.R;
import com.synnefx.cqms.event.ui.SettingsActivity;

/**
 * Created by Josekutty on 8/16/2016.
 */
public class NotificationUtils {

    public static Notification getNotification(Context context, String title, String message) {
        final Intent i = new Intent(context, SettingsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        if (Strings.notEmpty(message)) {
            return getNotificationBuilder(context, title, message, pendingIntent)
                    .setContentText(message)
                    .build();
        } else {
            return getNotificationBuilder(context, title, message, pendingIntent).build();
        }
    }

    public static Notification getSoundNotification(Context context, String title, String message) {
        final Intent i = new Intent(context, SettingsActivity.class);
        return getSoundNotification(context, title, message, PendingIntent.getActivity(context, 0, i, 0));
    }

    public static Notification getSoundNotification(Context context, String title, String message, PendingIntent pendingIntent) {
        if (Strings.notEmpty(message)) {
            return getSoundNotificationBuilder(context, title, message, pendingIntent)
                    .setContentText(message)
                    .build();
        } else {
            return getSoundNotificationBuilder(context, title, message, pendingIntent).build();
        }
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String title, String message, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_notification_logo);
        } else {
            builder.setSmallIcon(R.drawable.ic_notification_logo);
        }
        return builder;
    }

    public static NotificationCompat.Builder getSoundNotificationBuilder(Context context, String title, String message, PendingIntent pendingIntent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                        //.setOngoing(true)
                .setLights(Color.RED, 1000, 1000)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_notification_logo);
        } else {
            builder.setSmallIcon(R.drawable.ic_notification_logo);
        }
        return builder;
    }
}