package com.eldhopj.fcmnotificationsample.fcm.notificationUtils;

import com.eldhopj.fcmnotificationsample.MainActivity;
import com.eldhopj.fcmnotificationsample.ProfileActivity;

/**
 * When clicking on the notification this class determines to which Activity to be opened
 */
public class NotificationRedirection {
    public static Class<?> findClass(String channel) {
        if (channel == null) { // If the notification is null
            return MainActivity.class;
        }
        if (channel.equals("main"))
            return MainActivity.class;
        if (channel.equals("profile"))
            return ProfileActivity.class;
        else
            return ProfileActivity.class; // kind of a default case
    }
}
