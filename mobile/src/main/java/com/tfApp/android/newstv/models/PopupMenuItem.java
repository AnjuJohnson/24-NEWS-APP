package com.tfApp.android.newstv.models;

import android.support.annotation.DrawableRes;

public class PopupMenuItem {
    private int id;
    private String title,imageUrl;
    private boolean isSelected;
    private int drawable;
    private boolean washOut=true;

    public PopupMenuItem(int id, String title, String imageUrl) {
        this.id=id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public PopupMenuItem(int id, String title, @DrawableRes int drawable) {
        this.id=id;
        this.title = title;
        this.drawable = drawable;
    }

    public PopupMenuItem(int id, String title, String imageUrl, int drawable) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.drawable = drawable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isWashOut() {
        return washOut;
    }

    public void setWashOut(boolean washOut) {
        this.washOut = washOut;
    }
}
