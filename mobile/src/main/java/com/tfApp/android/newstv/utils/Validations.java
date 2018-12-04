package com.tfApp.android.newstv.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by George PJ on 26-04-2018.
 */

public class Validations {

    public boolean isEditTextContainEmail(String email) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean validatePhone(String phone) {
        int min_phone_number_length = 8;
        int max_phone_number_length = 16;
        return isValidString(phone) && (phone.trim().length() >= min_phone_number_length || phone.trim().length() <= max_phone_number_length);
    }

    public boolean isValidString(String text) {
        return text != null && !text.trim().isEmpty();
    }



}
