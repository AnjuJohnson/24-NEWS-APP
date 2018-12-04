package com.ottapp.android.basemodule.events;

import com.ottapp.android.basemodule.repository.responses.BaseResponse;

public class OTPReceivedEvent extends BaseResponse {
    public OTPReceivedEvent(String otp, boolean sucess) {
        super(null,sucess);
        this.otp = otp;
    }

    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
