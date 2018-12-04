package com.ottapp.android.basemodule.events;

public class ActivateFCMEvent {
    private boolean activateRequest;

    public ActivateFCMEvent(boolean activateRequest) {
        this.activateRequest = activateRequest;
    }

    public boolean isActivateRequest() {
        return activateRequest;
    }

    public void setActivateRequest(boolean activateRequest) {
        this.activateRequest = activateRequest;
    }
}
