package android.bitryt.com.socialloginapi.google;

import android.app.Activity;
import android.bitryt.com.socialloginapi.events.SocialAuthCompletedEvent;
import android.bitryt.com.socialloginapi.model.UserProfileInfo;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class GoogleAccountHelper {
    private static final int RC_SIGN_IN = 789;
    private static final String TAG = "GoogleAccount-MODULE";
    private WeakReference<Activity> referenceActivity;

    public GoogleAccountHelper(Activity activity) {
        this.referenceActivity = new WeakReference<>(activity);
    }

    public void signIn() {
        GoogleSignInAccount account = getExistingAccount();
        if (account != null) {
            sendLoginSuccessful(account);
        } else {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(referenceActivity.get(), gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            referenceActivity.get().startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            sendLoginSuccessful(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            sendLoginSuccessful(null);
        }
    }

    private void sendLoginSuccessful(GoogleSignInAccount account) {
        SocialAuthCompletedEvent authCompleted = new SocialAuthCompletedEvent(SocialAuthCompletedEvent.TYPE.GOOGLE);
        if (account != null) {
            authCompleted.setSuccess(true);
            UserProfileInfo userProfileInfo = new UserProfileInfo();
            userProfileInfo.setPersonEmail(account.getEmail());
            String displayName = account.getDisplayName();
            String givenName = account.getGivenName();
            userProfileInfo.setPersonLastName(account.getFamilyName());
            userProfileInfo.setPersonFirstName(displayName == null ? givenName : displayName);
            userProfileInfo.setPersonId(account.getId());
            userProfileInfo.setPersonIdToken(account.getIdToken());
            Uri uri = account.getPhotoUrl();
            if (uri != null)
                userProfileInfo.setPersonPhoto(uri.toString());
            authCompleted.setUserProfileInfo(userProfileInfo);
        }
        EventBus.getDefault().post(authCompleted);
    }

    private GoogleSignInAccount getExistingAccount() {
        return GoogleSignIn.getLastSignedInAccount(referenceActivity.get());
    }
}
