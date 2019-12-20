package com.eldhopj.fcmnotificationsample.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.eldhopj.fcmnotificationsample.R;

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";

    //Allows to relaunch the app when we click the notification
    private static PendingIntent contentIntent(Context context, Class<?> launchActivity, String channel, int NOTIFICATION_ID) {

        Intent startActivityIntent = new Intent(context, launchActivity);
        startActivityIntent.putExtra("redirection", channel); //Passing data from
        return PendingIntent.getActivity(context,
                NOTIFICATION_ID,
                startActivityIntent,
                //FLAG_UPDATE_CURRENT -> keeps this instance valid and just updates the extra data associated with it coming from new intent
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //Helps to create a bitmap image shown in the Notification
    private static Bitmap largeIcon(Context context) {
        /**Decode an image from the resources to an bitmap image*/
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_android);
        return largeIcon;
    }



    //This method is responsible for creating the notification and notification channel in which the notification belongs to and displaying it
    public static void createNotifications(Context context, String title, String body, String channelId, Class<?> launchActivity) {
        final int NOTIFICATION_ID = (int) System.currentTimeMillis();

        Log.d(TAG, "createNotifications: "+body);

        /**From Oreo we need to display notifications in the notification channel*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, //String ID
                    channelId, //Name for the channel ,(NOTE: here considering channel name same as channel Id)
                    NotificationManager.IMPORTANCE_HIGH); //Importance for the notification , In high we get headsUp notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        //Notification Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_android)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(title) // Title

                // check different styles ref: https://developer.android.com/training/notify-user/expanded
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_VIBRATE) // needed to add vibration permission on the manifest
                .setContentIntent(contentIntent(context,
                        launchActivity,
                        channelId,
                        NOTIFICATION_ID)) //pending Intent (check its fn)
                .setAutoCancel(true); //Notification will go away when user clicks on it

        /**this will give heads up notification on versions below Oreo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        //Notification Manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        //NOTIFICATION_ID -> this ID can be used if the notification have to ba cancelled
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Dismiss any notification which comes in
     */
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }
}

