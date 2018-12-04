package com.ottapp.android.basemodule.models;

public class ConfirmOtp {
    private String mobileNumber;

    private String emailId;

    private String otp;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public ConfirmOtp(String phNumber, String emailId, String otp) {
        this.mobileNumber = phNumber;
        this.emailId = emailId;
        this.otp = otp;
    }
}
