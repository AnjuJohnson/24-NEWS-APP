package com.tfApp.android.newstv.utils;

import android.os.Build;

/**
 * Created by George PJ on 26-04-2018.
 */

public final class StaticValues {
    public static final int MEDIA_STREAM_REQUEST_CODE = 123;
    public static String emailId,mobileNumber;
    public static long userId;
    public static long last_cache;

    public static String menu_service ="menu";
    public static String category_service ="category";
    public static String assosiation_service ="menu";

    public static int youtube_activity_id = 3;
    public  static int exo_player_video_id =2;
    public static int version = Build.VERSION.SDK_INT ;    // API Level
    public static String device = android.os.Build.DEVICE ;          // Device
    public static String model = android.os.Build.MODEL ;           // Model
}
