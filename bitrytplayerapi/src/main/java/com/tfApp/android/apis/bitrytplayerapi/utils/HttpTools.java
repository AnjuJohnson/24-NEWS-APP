/*
 *
 *  * Copyright (c) 2018, Bitryt Solutions Pvt. Ltd. and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions
 *  * are met:
 *  *
 *  *   - Redistributions of source code must retain the above copyright
 *  *     notice, this list of conditions and the following disclaimer.
 *  *
 *  *   - Redistributions in binary form must reproduce the above copyright
 *  *     notice, this list of conditions and the following disclaimer in the
 *  *     documentation and/or other materials provided with the distribution.
 *  *
 *  *   - Neither the name of Bitrate Global Solutions or the names of its
 *  *     contributors may be used to endorse or promote products derived
 *  *     from this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *  * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *  * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.tfApp.android.apis.bitrytplayerapi.utils;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class HttpTools {

    private static final String TAG = HttpTools.class.getName();

    public static void httpGetURL(final String url) {
        if (!TextUtils.isEmpty(url)) {
            new Thread() {
                @Override
                public void run() {
                    HttpURLConnection conn = null;
                    try {
                        URL httpUrl = new URL(url);

                        HttpURLConnection.setFollowRedirects(true);
                        conn = (HttpURLConnection) httpUrl.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setRequestProperty("Connection", "close");
                        conn.setRequestMethod("GET");

                        int code = conn.getResponseCode();
                        VASTLog.v(TAG, "response code:" + code
                                + ", for URL:" + url);
                    } catch (Exception e) {
                        VASTLog.w(TAG, url + ": " + e.getMessage() + ":"
                                + e.toString());
                    } finally {
                        if (conn != null) {
                            try {
                                conn.disconnect();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }.start();
        } else {
            VASTLog.w(TAG, "url is null or empty");

        }
    }


    public static String get(String url) {
        try {
            return new GetUrlContentTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String content = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    content += line + "\n";
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

    }
}
