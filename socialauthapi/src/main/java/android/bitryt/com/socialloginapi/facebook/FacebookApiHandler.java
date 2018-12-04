package android.bitryt.com.socialloginapi.facebook;

import android.app.Activity;
import android.bitryt.com.socialloginapi.events.SocialAuthCompletedEvent;
import android.bitryt.com.socialloginapi.model.UserProfileInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class FacebookApiHandler {

    private CallbackManager callbackManager;
    private WeakReference<Activity> activityWeakReference;

    public FacebookApiHandler(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)&&callbackManager!=null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void loginFacebook() {
        AccessToken accessToken = isAlreadySignedIn();
        if (accessToken != null) {
            getProfileDetails(accessToken);
        } else {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            getProfileDetails(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            SocialAuthCompletedEvent authCompleted = new SocialAuthCompletedEvent(SocialAuthCompletedEvent.TYPE.FACEBOOK);
                            EventBus.getDefault().post(authCompleted);
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            SocialAuthCompletedEvent authCompleted = new SocialAuthCompletedEvent(SocialAuthCompletedEvent.TYPE.FACEBOOK);
                            EventBus.getDefault().post(authCompleted);
                        }
                    });
            LoginManager.getInstance().logInWithReadPermissions(activityWeakReference.get(), Arrays.asList(
                    "public_profile", "email", "user_birthday", "user_gender"));
        }
    }

    private void getProfileDetails(AccessToken authToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                authToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());
                        SocialAuthCompletedEvent authCompleted = new SocialAuthCompletedEvent(SocialAuthCompletedEvent.TYPE.FACEBOOK);
                        try {
                            // Application code
                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String gender = object.getString("gender");
                            String birthday = object.getString("birthday"); // 01/31/1980 format
                            String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
                            authCompleted.setSuccess(true);
                            UserProfileInfo userProfileInfo = new UserProfileInfo();
                            userProfileInfo.setPersonEmail(email);
                            userProfileInfo.setPersonLastName(last_name);
                            userProfileInfo.setPersonFirstName(name == null ? first_name : name);
                            userProfileInfo.setPersonId(id);
                            userProfileInfo.setPersonPhoto(image_url);
                            userProfileInfo.setGender(gender);
                            userProfileInfo.setBirthday(birthday);
                            authCompleted.setUserProfileInfo(userProfileInfo);
                        } catch (JSONException ignored) {

                        } finally {
                            EventBus.getDefault().post(authCompleted);
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private AccessToken isAlreadySignedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            return accessToken;
        }
        return null;
    }
}
