package com.ottapp.android.basemodule.utils;

import android.net.Uri;
import android.webkit.URLUtil;

public class ValidatorUrl {

    public boolean isValidUrl(String url) {

        if (url == null) {
            return false;
        }
        if (URLUtil.isValidUrl(url)) {
            // Check host of url if youtube exists
            Uri uri = Uri.parse(url);
            if ("www.youtube.com".equals(uri.getHost())) {
                return true;
            }
            // Other way You can check into url also like
            //if (url.startsWith("https://www.youtube.com/")) {
            //return true;
            //}
        }
        // In other any case
        return false;
    }
}
