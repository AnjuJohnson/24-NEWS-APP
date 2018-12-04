package com.tfApp.android.newstv.models;

public class BrandKeyResponseModel {
    private String menu_default_icon_color;
    private String menu_active_icon_color;

    public String getMenu_default_icon_color() {
        return menu_default_icon_color;
    }

    public void setMenu_default_icon_color(String menu_default_icon_color) {
        this.menu_default_icon_color = menu_default_icon_color;
    }

    public String getMenu_active_icon_color() {
        return menu_active_icon_color;
    }

    public void setMenu_active_icon_color(String menu_active_icon_color) {
        this.menu_active_icon_color = menu_active_icon_color;
    }

    public String getWindow_background() {
        return window_background;
    }

    public void setWindow_background(String window_background) {
        this.window_background = window_background;
    }

    public String getMenu_background() {
        return menu_background;
    }

    public void setMenu_background(String menu_background) {
        this.menu_background = menu_background;
    }

    public String getTitle_text_color() {
        return title_text_color;
    }

    public void setTitle_text_color(String title_text_color) {
        this.title_text_color = title_text_color;
    }

    private String window_background;
    private String menu_background;
    private String title_text_color;
}
