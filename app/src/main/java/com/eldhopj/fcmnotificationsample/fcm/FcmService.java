package com.eldhopj.fcmnotificationsample.fcm;

import androidx.annotation.NonNull;

import com.eldhopj.fcmnotificationsample.fcm.notificationUtils.NotificationUtils;
import com.eldhopj.fcmnotificationsample.utils.Commons;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            NotificationUtils.createNotifications(getApplicationContext(),
                    remoteMessage);
        }
    }



    @Override
    public void onNewToken(@NonNull String token) { // FCM token
        super.onNewToken(token);
        Commons.TOKEN = token;
    }
}
