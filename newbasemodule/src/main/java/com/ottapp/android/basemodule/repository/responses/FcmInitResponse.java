package com.ottapp.android.basemodule.repository.responses;

public class FcmInitResponse {
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private boolean success;
    public FcmInitResponse(boolean success) {
        this.success = success;
    }
}
