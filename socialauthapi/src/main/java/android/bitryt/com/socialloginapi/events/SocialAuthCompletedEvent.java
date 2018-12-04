package android.bitryt.com.socialloginapi.events;

import android.bitryt.com.socialloginapi.model.UserProfileInfo;
public class SocialAuthCompletedEvent {
    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    private TYPE type;
    private boolean success;
    private UserProfileInfo userProfileInfo;

    public SocialAuthCompletedEvent(TYPE type) {
        this.type = type;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserProfileInfo getUserProfileInfo() {
        return userProfileInfo;
    }

    public void setUserProfileInfo(UserProfileInfo userProfileInfo) {
        this.userProfileInfo = userProfileInfo;
    }

   public enum TYPE{
        FACEBOOK,GOOGLE
    }
}
