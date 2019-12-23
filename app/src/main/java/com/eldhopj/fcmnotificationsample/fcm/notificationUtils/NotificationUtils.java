package com.eldhopj.fcmnotificationsample.fcm.notificationUtils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.eldhopj.fcmnotificationsample.R;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";

    //Helps to create a bitmap image shown in the Notification
    private static Bitmap largeIcon(Context context) {
        /**Decode an image from the resources to an bitmap image*/
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_android);
        return largeIcon;
    }



    //This method is responsible for creating the notification and notification channel in which the notification belongs to and displaying it
    public static void createNotifications(Context context, RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() == null) {
            return;
        }
        final int NOTIFICATION_ID = (int) System.currentTimeMillis();
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String channelId = "general";
        if (remoteMessage.getNotification().getChannelId() != null)
            channelId = remoteMessage.getNotification().getChannelId();

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
                .setContentIntent(NotificationPendingIntent.getIntent(context, remoteMessage,
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

