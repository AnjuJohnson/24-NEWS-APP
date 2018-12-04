package com.ottapp.android.basemodule.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ottapp.android.basemodule.events.OTPReceivedEvent;
import com.ottapp.android.basemodule.utils.Constants;

import org.greenrobot.eventbus.EventBus;

public class OTPBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        StringBuilder sms_str = new StringBuilder();StringBuilder header = new StringBuilder();
        if (bundle != null) {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                smsm = new SmsMessage[pdus.length];
                for (int i = 0; i < smsm.length; i++) {
                    smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    header.append(smsm[i].getOriginatingAddress());
                    sms_str.append(smsm[i].getMessageBody());

                }
            }

            checkAndFill(header.toString(), sms_str.toString());
        }
    }


    private void checkAndFill(String header, String sms_str) {
        if (header != null && header.toLowerCase().contains(Constants.OTP_MAGIC_KEYWORD.toLowerCase())) {
            if (sms_str != null && !sms_str.trim().isEmpty() && sms_str.contains(Constants.OTP_PREFIX_KEYWORD)) {
                String otp = sms_str.substring(Constants.OPT_START_INDEX, Constants.OPT_START_INDEX+ Constants.OTP_LENGTH);
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Integer.parseInt(otp);
                    EventBus.getDefault().post(new OTPReceivedEvent(otp, true));
                } catch (Exception e) {

                }
            }
        }
    }
}
