package com.eldhopj.fcmnotificationsample.fcm.notificationUtils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationPendingIntent {
    //Allows to relaunch the app when we click the notification
    public static PendingIntent getIntent(Context context, RemoteMessage remoteMessage, int NOTIFICATION_ID) {
        String channel = remoteMessage.getNotification().getChannelId();

        Intent startActivityIntent = new Intent(context, NotificationRedirection.findClass(channel));
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP); // needed flags while passing data, so only the data will be received on onNewIntent
        startActivityIntent.putExtras(getDataBundle(remoteMessage.getData())); //Passing data to activity
        return PendingIntent.getActivity(context,
                NOTIFICATION_ID,
                startActivityIntent,
                //FLAG_UPDATE_CURRENT -> keeps this instance valid and just updates the extra data associated with it coming from new intent
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * NOTE : Create different kinds of bundles according to the needs
     */
    private static Bundle getDataBundle(Map<String, String> data) {
        Bundle bundle = new Bundle();
        String id = "";
        String value = "";
        if (data != null) {
            id = data.get("id");
            value = data.get("value");
        }
        bundle.putString("id", id);
        bundle.putString("value", value);
        return bundle;
    }
}
