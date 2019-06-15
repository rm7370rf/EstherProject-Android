package org.rm7370rf.estherproject.util;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.other.Config;

public class Notification {
    //From http://thetechnocafe.com/how-to-use-workmanager-in-android/
    public static void show(Context context, int id, int title, int message) {
        show(context, id, context.getString(title), context.getString(message));
    }

    public static void show(Context context, int id, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Config.NOTIFICATION_CHANNEL, Config.NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, Config.NOTIFICATION_CHANNEL)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(id, notification.build());
    }
}
