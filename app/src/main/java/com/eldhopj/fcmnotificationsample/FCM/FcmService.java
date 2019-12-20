package com.eldhopj.fcmnotificationsample.FCM;

import androidx.annotation.NonNull;

import com.eldhopj.fcmnotificationsample.Utils.Commons;
import com.eldhopj.fcmnotificationsample.Utils.NotificationRedirection;
import com.eldhopj.fcmnotificationsample.Utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String channelId = remoteMessage.getNotification().getChannelId();

            NotificationUtils.createNotifications(getApplicationContext(),
                    title, body, channelId,
                    NotificationRedirection.findClass(channelId));
        }
    }



    @Override
    public void onNewToken(@NonNull String token) { // FCM token
        super.onNewToken(token);
        Commons.TOKEN = token;
    }
}
