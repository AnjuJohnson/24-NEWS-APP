package com.ott.android.newstv;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ott.android.newstv.events.NewFcmTokenEvent;
import com.ott.android.newstv.model.FcmNotificationModel;
import com.ottapp.android.basemodule.utils.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

public class FirebaseMessageService extends FirebaseMessagingService {
    public static final String TOPIC_UPDATE = "update2";
    public static final String TOPIC_CATEGORY_UPDATE = "24NEWS_CATEGORY";
    public static final String TOPIC_MENU_UPDATE = "24NEWS_APPMENU";
    public static final String TOPIC_MENU_CATEGORY_ASSOSIATION_UPDATE = "24NEWS_APPMENU_CATEGORY_ASSOCIATION";
    public static final String TOPIC_SUB_CATEGORY_UPDATE = "24NEWS_APPMENU_CATEGORY_ASSOCIATION";
    public static final String TOPIC_ASSETS_UPDATE = "24NEWS_ASSET";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("FCM", "Token > " + s);
        EventBus.getDefault().post(new NewFcmTokenEvent(s));
        if (!PreferenceManager.getManager().isFccInitialised()) {
            PreferenceManager.getManager().setFcmInitialised();
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_UPDATE);
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_CATEGORY_UPDATE);
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_MENU_UPDATE);
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_MENU_CATEGORY_ASSOSIATION_UPDATE);
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_SUB_CATEGORY_UPDATE);
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_ASSETS_UPDATE);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (PreferenceManager.getManager().getUserId() > 0)
            processUpdateNotification(remoteMessage);
    }


    private void processUpdateNotification(RemoteMessage remoteMessage) {
        Log.e("FCM", "Notification Received");
        String imageUri = remoteMessage.getData().get("image");
        FcmNotificationModel fcmNotificationModel = new FcmNotificationModel();
        if (remoteMessage.getNotification() != null)
            fcmNotificationModel.setBody(remoteMessage.getNotification().getBody());
        fcmNotificationModel.setFrom(remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null)
            fcmNotificationModel.setTitle(remoteMessage.getNotification().getTitle());
        EventBus.getDefault().post(fcmNotificationModel);

    }
}
