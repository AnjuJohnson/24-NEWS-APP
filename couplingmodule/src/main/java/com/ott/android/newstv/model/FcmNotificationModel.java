package com.ott.android.newstv.model;

public class FcmNotificationModel {
    private String body;
    private CharSequence title;

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;

    public String getBody() {
        return body;
    }

    public CharSequence getTitle() {
        return title;
    }

    public String getFrom() {
        return from;
    }
}
