package com.team.cardTalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Garam on 2015-05-10.
 */

public class Proxy {
    public String getJSON() {

        try {
            URL url = new URL("http://10.0.3.2:3000/card/all");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Accept-Charset", "application/json");
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            Log.i("test", "ProxyResponseCode:" + status);

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "NETWORK ERROR:" + e);
        }

        return null;
    }
}
