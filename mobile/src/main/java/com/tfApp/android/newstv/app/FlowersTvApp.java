package com.tfApp.android.newstv.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.ott.android.newstv.FirebaseMessageService;
import com.ott.android.newstv.app.BaseCouplingApplication;
import com.ott.android.newstv.model.FcmNotificationModel;
import com.ottapp.android.basemodule.apis.RetrofitEngine;
import com.ottapp.android.basemodule.receiver.UpdateRequestReceiver;
import com.ottapp.android.basemodule.repository.RepoRequestEvent;
import com.ottapp.android.basemodule.repository.RepoRequestType;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.utils.StaticValues;
import com.tfApp.android.newstv.view.activity.SplashActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import io.fabric.sdk.android.Fabric;

/**
 * Created by George PJ on 21-02-2018.
 */

public class FlowersTvApp extends BaseCouplingApplication {
    private static final int MESSAGE_ID_UPDATE = 123;
    private static final int MESSAGE_ID_TEXT = 124;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        //String API_BASE_URL = DXDecryptorcOqy3J7P.decode("7QaEzUVInQ3p4cR4f4tEXS4b0oBSGCqNnPJrEgUXF9qnpuA9ZHmk")/*"http://202.65.132.179:8080/flowerstvws/"*/;
       // String API_BASE_URL ="http://192.168.1.7:5000/flowersTvws/v1/";//http://202.65.132.179:8080/flowerstvws/"*/;
        String API_BASE_URL = "http://ws.twentyfournews.com/flowersTvws/v1/";//http://202.65.132.179:8080/flowerstvws/"*/;

        RetrofitEngine.init(API_BASE_URL);
        StaticValues.last_cache = System.currentTimeMillis();
    }

    @Override
    public String deviceType() {
        return "Mobile";
    }

    @Override
    public int getAppVersion() {
        return 1;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void manageNotifications(FcmNotificationModel remoteMessage) {
        String topics_prefix = DXDecryptorcOqy3J7P.decode("qgafzRYEwRA=")/*"/topics/"*/;
        RepoRequestEvent repoRequestEvent;
        if (remoteMessage.getFrom() != null && remoteMessage.getFrom().equalsIgnoreCase(topics_prefix + FirebaseMessageService.TOPIC_CATEGORY_UPDATE)) {
            repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                    REQUEST_TYPE_LOAD_CATEGORIES_ONLINE, null);
            EventBus.getDefault().post(repoRequestEvent);

        } else if (remoteMessage.getFrom() != null && remoteMessage.getFrom().equalsIgnoreCase(topics_prefix + FirebaseMessageService.TOPIC_MENU_UPDATE)) {
            repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                    REQUEST_TYPE_LOAD_MENUS_ONLINE, null);
            EventBus.getDefault().post(repoRequestEvent);
        } else if (remoteMessage.getFrom() != null && remoteMessage.getFrom().equalsIgnoreCase(topics_prefix + FirebaseMessageService.TOPIC_MENU_CATEGORY_ASSOSIATION_UPDATE)) {
            repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                    REQUEST_TYPE_LOAD_APP_CATEGORY_ASSOSIATION_ONLINE, null);
            EventBus.getDefault().post(repoRequestEvent);

        } else if (remoteMessage.getFrom() != null && remoteMessage.getFrom().equalsIgnoreCase(topics_prefix + FirebaseMessageService.TOPIC_ASSETS_UPDATE)) {
            repoRequestEvent = new RepoRequestEvent<>(RepoRequestType.
                    REQUEST_ASSETS_UPDATION, null);
            EventBus.getDefault().post(repoRequestEvent);

        } else if (remoteMessage.getFrom() != null) {

            Log.e(DXDecryptorcOqy3J7P.decode("wzG9")/*"FCM"*/, DXDecryptorcOqy3J7P.decode("1xeT2BYR11v5soQqasYHC31BitkMAnzS0KsiHQoZFNa6uw==")/*"Received and creating notification"*/);

            String CHANNEL_ID = DXDecryptorcOqy3J7P.decode("4x6fyhoVwUuvjIkmK8sbC3BqjtIYUXPawQ==")/*"flowerstv_channel_message"*/;// The id of the channel.
            CharSequence name = DXDecryptorcOqy3J7P.decode("wx6fyhoVwWuP")/*"FlowersTV"*/;// The user-visible name of the channel.

            String content = remoteMessage.getBody();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.whitelogo_24);
            Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
                    R.drawable.rsz_24logo);
            mBuilder.setLargeIcon(icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.whitelogo_24);
                mBuilder.setColor(getResources().getColor(R.color.primary));
            } else {
                mBuilder.setSmallIcon(R.drawable.whitelogo_24);
            }
            mBuilder.setContentTitle(remoteMessage.getTitle());
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(content != null ? content : ""));
            mBuilder.setSound(alarmSound);
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Intent intent = new Intent(this, SplashActivity.class);
            intent.putExtra(DXDecryptorcOqy3J7P.decode("yy25+Q==")/*"N_ID"*/, MESSAGE_ID_TEXT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            mBuilder.setContentIntent(pendingIntent);
            int MESSAGE_ID = MESSAGE_ID_TEXT;
            Log.e(DXDecryptorcOqy3J7P.decode("yAGXnTkV3w==")/*"Msg Frm"*/, remoteMessage.getFrom());
            if (remoteMessage.getFrom() != null && remoteMessage.getFrom().equalsIgnoreCase(topics_prefix + FirebaseMessageService.TOPIC_UPDATE)) {
                Intent updateReceive = new Intent(this, UpdateRequestReceiver.class);
                updateReceive.setAction(UpdateRequestReceiver.UPDATE_ACTION);
                updateReceive.putExtra(DXDecryptorcOqy3J7P.decode("yy25+Q==")/*"N_ID"*/, MESSAGE_ID_UPDATE);
                PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, updateReceive, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.addAction(R.drawable.ic_action_next_hint, DXDecryptorcOqy3J7P.decode("0AKU3AsCknG2pA==")/*"Update Now"*/, pendingIntentYes);
                // Apply the media style template
                MESSAGE_ID = MESSAGE_ID_UPDATE;
            }


            NotificationManager mNotificationManager =
                    (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }
            if (mNotificationManager != null) {
                mNotificationManager.notify(MESSAGE_ID, mBuilder.build());
            }
        }
    }
}
//created by Dingxiang Technologies Co., Ltd.
//please visit http://www.dingxiang-inc.com for more products.

class DXDecryptorcOqy3J7P {
    static String algo = "ARCFOUR";
    static String kp = "aI5HNCOpILM4MCsE";

    public static String decode(String s) {
        String str;
        String key = "IxVAWEe3+QmCe/FnyGECwA==";
        try {
            Cipher rc4 = Cipher.getInstance(algo);
            Key kpk = new SecretKeySpec(kp.getBytes(), algo);
            rc4.init(Cipher.DECRYPT_MODE, kpk);
            byte[] bck = Base64.decode(key, Base64.DEFAULT);
            byte[] bdk = rc4.doFinal(bck);
            Key dk = new SecretKeySpec(bdk, algo);
            rc4.init(Cipher.DECRYPT_MODE, dk);
            byte[] bcs = Base64.decode(s, Base64.DEFAULT);
            byte[] byteDecryptedString = rc4.doFinal(bcs);
            str = new String(byteDecryptedString);
        } catch (Exception e) {
            str = "";
        }
        return str;
    }

}