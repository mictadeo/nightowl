package com.michaeltadeo.nightowl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class NotificationHelper extends ContextWrapper {
    public static final String channel1Id = "channel 1";
    public static final String channel1name = "Course Notification";

    public static final String channel2Id = "channel 2";
    public static final String channel2name = "Assessment Notification";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(channel1Id,
                    channel1name, NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This channel shows course notifications for the nightowl App");

            NotificationChannel channel2 = new NotificationChannel(channel2Id,
                    channel2name, NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("This channel shows assessment notifications for the nightowl App");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

            getManager().createNotificationChannel(channel1);
            getManager().createNotificationChannel(channel2);
        }
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        return  new NotificationCompat.Builder(getApplicationContext(), channel1Id)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo);

    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message) {
        return  new NotificationCompat.Builder(getApplicationContext(), channel2Id)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo);

    }
}
