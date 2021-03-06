package com.team.cardTalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Garam on 2015-04-27.
 */
public class FriendAddingProxy {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private String serverUrl;
    private SharedPreferences pref;
    private Context context;
    private String cookie;

    public FriendAddingProxy(Context context) {
        this.context = context;
        String prefName = context.getResources().getString(R.string.pref_name);
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        serverUrl = pref.getString(context.getResources().getString(R.string.server_url), "");
        cookie = pref.getString(context.getResources().getString(R.string.cookie), "");

        if (cookie == null) {
            Intent intent = new Intent(context, HomeView.class);
            context.startActivity(intent);
        }

        String replacedCookie = cookie.replace("connect.sid=", "");
        CookieStore cookieStore = new PersistentCookieStore(context);
        BasicClientCookie newCookie = new BasicClientCookie("connect.sid", replacedCookie);
        newCookie.setVersion(1);
        newCookie.setDomain("125.209.195.202");
        newCookie.setPath("/");
        Log.i("test", "cookie replaced: " + replacedCookie);

        cookieStore.addCookie(newCookie);
        client.setCookieStore(cookieStore);

    }

    public void addFriend(String friendid) throws IOException {

        try {
            URL url = new URL("http://125.209.195.202:3000/" + "friend/" + friendid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Cookie", cookie);

            int status = conn.getResponseCode();
            Log.i("test", "ProxyResponseCode:" + status);

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.i("test", "addFriend" + sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "NETWORK ERROR:" + e);
        }
    }

    public String joinRoom(String _id) throws IOException {
//        // TODO roomjoin에서 sub하려면 thread 만들어야 하나? 확인
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("125.209.195.202");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        String queueName = channel.queueDeclare().getQueue();
//
//        channel.queueBind(queueName, "notification", _id);

        try {
            URL url = new URL(serverUrl + "room/join/" + _id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Accept-Charset", "application/json");
            conn.setRequestProperty("Cookie", cookie);
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
                    Log.i("test", "join Room response:" + sb.toString());
                    return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "NETWORK ERROR:" + e);
        }

        return null;
    }
}