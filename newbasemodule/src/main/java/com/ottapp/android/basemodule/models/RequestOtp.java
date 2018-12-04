package com.ottapp.android.basemodule.models;

public class RequestOtp {
    private String mobileNumber;

    private String emailId;

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


    private String otp;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    private String brand;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }



    private String model;

    public int getOs_version() {
        return os_version;
    }

    public void setOs_version(int os_version) {
        this.os_version = os_version;
    }

    private int os_version;

    public RequestOtp(String phNumber, String emailId) {
        this.mobileNumber = phNumber;
        this.emailId = emailId;
    }

    public RequestOtp(String phNumber, String emailId, String device,String model,int version) {
        this.mobileNumber = phNumber;
        this.emailId = emailId;
        this.brand = device;
        this.model = model;
        this.os_version = version;
    }
    /**
     * phNumber : 9895174242
     * emailId : sanoj619@gmail.com
     */

}
