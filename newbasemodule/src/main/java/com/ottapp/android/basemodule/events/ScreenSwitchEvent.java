package com.ottapp.android.basemodule.events;

import android.os.Bundle;

public class ScreenSwitchEvent {
    private Bundle bundle;
    private int id;

    public ScreenSwitchEvent(int id) {
        this.id = id;
    }

    public ScreenSwitchEvent(int id, Bundle bundle) {
        this.id = id;
        this.bundle=bundle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
