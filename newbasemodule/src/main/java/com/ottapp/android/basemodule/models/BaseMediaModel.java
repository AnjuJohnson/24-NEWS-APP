package com.ottapp.android.basemodule.models;

public interface BaseMediaModel<M extends BaseMediaModel> extends Comparable<M> {
    long getVId();
    String getVastUrl();
    String getSubTitleUrl();
    String getContentUrl();
    int getMediaType();
    String getDefaultThumbnailUrl();
    String getMediaName();

    String getSubTitleLanguage();
}
