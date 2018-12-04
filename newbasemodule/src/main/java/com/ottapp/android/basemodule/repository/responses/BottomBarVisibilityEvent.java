package com.ottapp.android.basemodule.repository.responses;

public class BottomBarVisibilityEvent {
    public BottomBarVisibilityEvent(boolean isShowRequest) {
        this.isShowResquest = isShowRequest;
    }

    private boolean isShowResquest;

    public boolean isShowResquest() {
        return isShowResquest;
    }

    public void setShowResquest(boolean showResquest) {
        isShowResquest = showResquest;
    }
}
