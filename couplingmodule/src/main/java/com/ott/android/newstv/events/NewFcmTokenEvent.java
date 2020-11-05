package com.ott.android.newstv.events;

public class NewFcmTokenEvent {
    private String token;

    public NewFcmTokenEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
