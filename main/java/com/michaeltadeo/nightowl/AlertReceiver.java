package com.michaeltadeo.nightowl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    public static final String alert = "alert";
    public static final String newAlert = "newAlertId";

    /*These functions will trigger the type of alert to show, depending on the incoming intent, whether
        it is a course or assessment alert*/
    @Override
    public void onReceive(Context context, Intent intent) {
        Integer alarmId;
        NotificationHelper notificationHelper = new NotificationHelper(context);

        if (intent.hasExtra("id") && intent.hasExtra("courseTitle")
                && intent.hasExtra("startDateAlert")) {

            String courseName = intent.getStringExtra("courseTitle");
            String termName = intent.getStringExtra("termTitle");
            String title = intent.getStringExtra("startDateAlert");
            alarmId = intent.getIntExtra("id", -1);
            NotificationCompat.Builder nb = notificationHelper.getChannel1Notification
                    (title, "Your " + courseName + " course in " + termName + " starts " +
                            "today.");
            notificationHelper.getManager().notify(alarmId, nb.build());

        }
        else if (intent.hasExtra("id") && intent.hasExtra("courseTitle")
                && intent.hasExtra("endDateAlert")) {

            String courseName = intent.getStringExtra("courseTitle");
            String termName = intent.getStringExtra("termTitle");
            String title = intent.getStringExtra("endDateAlert");
            alarmId = intent.getIntExtra("id", -1);
            NotificationCompat.Builder nb = notificationHelper.getChannel1Notification
                    (title, "Your " + courseName + " course in " + termName + " ends " +
                            "today.");
            notificationHelper.getManager().notify(alarmId, nb.build());
        }
        else if (intent.hasExtra("assessmentTitle")) {
            String assessmentName = intent.getStringExtra("assessmentTitle");
            String courseName = intent.getStringExtra("courseTitle");
            alarmId = intent.getIntExtra("id", -1);

            NotificationCompat.Builder nb = notificationHelper.getChannel2Notification
                    ("Assessment Alert!", "Your goal date to finish "
                            + assessmentName + " for " + courseName + " is today!");
            notificationHelper.getManager().notify(alarmId, nb.build());
        }

    }

    public static int getNewAlertId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alert, Context.MODE_PRIVATE);
        int newAlertId = alarmPrefs.getInt(newAlert, 1);
        return newAlertId;
    }

    public static void incrementNewAlertId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alert, Context.MODE_PRIVATE);
        int newAlertId = alarmPrefs.getInt(newAlert, 1);
        SharedPreferences.Editor alarmEditor = alarmPrefs.edit();
        alarmEditor.putInt(newAlert, newAlertId + 1);
        alarmEditor.commit();
    }
}