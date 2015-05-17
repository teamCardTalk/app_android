package com.team.cardTalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Garam on 2015-05-10.
 */

public class Proxy {

    private String serverUrl;
    private SharedPreferences pref;
    private Context context;

    public Proxy(Context context) {
        this.context = context;
        String prefName = context.getResources().getString(R.string.pref_name);
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        serverUrl = pref.getString(
                context.getResources().getString(R.string.server_url), "");

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
    }

    public String getJSON() {

        try {
            URL url = new URL(serverUrl + "card/all");
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

    public String getNewJSON() {

        try {
            String prefCardKey = pref.getString(context.getResources().getString(R.string.pref_cardkey), "");
            String query = serverUrl + "card/nid=" + prefCardKey;
            Log.i("text", "getNewJson query:" + query);

            URL url = new URL(query);

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
