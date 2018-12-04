package com.tfApp.android.newstv.utils.preference;//package com.bitryt.android.flowerstv.utils.preference;
//
//import android.content.SharedPreferences;
//
//import com.bitryt.android.flowerstv.app.FlowersTvApp;
//
///**
// * Created by George PJ on 23-02-2018.
// */
//
//public class PreferenceManager {
//    private static PreferenceManager manager;
//    private SharedPreferences sharedPreferences;
//
//    private PreferenceManager() {
//        sharedPreferences = FlowersTvApp.getApplication().getSharedPreferences("FolowersStone", 0);
//    }
//
//    public static PreferenceManager getManager() {
//        if (manager == null) {
//            manager = new PreferenceManager();
//        }
//        return manager;
//    }
//
//    public boolean isFccInitialised() {
//        return sharedPreferences.getBoolean(PREF_KEY.KEY_FIREBASE_INITIALISED, false);
//    }
//
//    public void setFcmInitialised() {
//        sharedPreferences.edit().putBoolean(PREF_KEY.KEY_FIREBASE_INITIALISED, true).apply();
//    }
//
//    public String getUserEmailId() {
//        return sharedPreferences.getString(PREF_KEY.KEY_USER_EMAIL_ID, null);
//    }
//
//    public void setUserEmailId(String emailId) {
//        sharedPreferences.edit().putString(PREF_KEY.KEY_USER_EMAIL_ID, emailId).apply();
//    }
//
//    public String getUserMobileNumber() {
//        return sharedPreferences.getString(PREF_KEY.KEY_USER_MOBILE_NUMBER, null);
//    }
//
//    public void setUserMobileNumber(String userMobileNumber) {
//        sharedPreferences.edit().putString(PREF_KEY.KEY_USER_MOBILE_NUMBER, userMobileNumber).apply();
//    }
//
//    public void setUserId(long id){
//        sharedPreferences.edit().putLong(PREF_KEY.KEY_USER_ID, id).apply();
//    }
//    public long getUserId(){
//        return sharedPreferences.getLong(PREF_KEY.KEY_USER_ID, 0);
//    }
//
//
//}
