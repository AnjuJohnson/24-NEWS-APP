package com.ott.android.flowerstv.events;

public class NewFcmTokenEvent {
    private String token;

    public NewFcmTokenEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
