// File: app/src/main/java/com/example/habittracker/ReminderBroadcastReceiver.java
package com.example.to_do;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String habitName = intent.getStringExtra("habitName");
        showNotification(context, habitName);
    }

    private void showNotification(Context context, String habitName) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification(habitName);
    }
}
