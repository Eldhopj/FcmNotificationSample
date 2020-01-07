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
//        String clickAction = remoteMessage.getNotification().getClickAction();  -> NOTE : for redirection in production use this
        String clickAction = remoteMessage.getNotification().getChannelId(); // redirection using channel id for testing purpose, Don't use this for production, use getClickAction()

        Intent startActivityIntent = new Intent(context, NotificationRedirection.findClass(clickAction));
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
     * NOTE : Passing notification data into activity
     */
    private static Bundle getDataBundle(Map<String, String> data) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String > entry : data.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue()); //Here the Map key is setting as bundle key and map value as bundle values
        }
        return bundle;
    }
}
