package com.team.cardTalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Garam on 2015-04-27.
 */
public class ArticleWritingProxy {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private String serverUrl;
    private SharedPreferences pref;
    private Context context;
    private String cookie;

    public ArticleWritingProxy(Context context) {
        this.context = context;
        String prefName = context.getResources().getString(R.string.pref_name);
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        serverUrl = pref.getString(context.getResources().getString(R.string.server_url), "");
        cookie = pref.getString(context.getResources().getString(R.string.cookie), "");

        if (cookie == null) {
            Intent intent = new Intent(context, com.team.cardTalk.HomeView.class);
            context.startActivity(intent);
        }

        Log.i("test", "cookie: " + cookie);
        CookieStore cookieStore = new PersistentCookieStore(context);
        BasicClientCookie newCookie = new BasicClientCookie("connect.sid", cookie = cookie.replace("connect.sid=", ""));
        newCookie.setVersion(1);
        newCookie.setDomain("125.209.195.202");
        newCookie.setPath("/");
        Log.i("test", "cookie replaced: " + cookie);

        cookieStore.addCookie(newCookie);
        client.setCookieStore(cookieStore);
    }

    public void uploadArticle(CardDTO article, String filePath, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("authorid", article.getAuthorid());
        params.put("nickname", article.getAuthor());
        params.put("icon", article.getIcon());
        params.put("title", article.getTitle());
        params.put("content", article.getContent());
        params.put("partynumber", article.getPartynumber());
        try {
            params.put("upload", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post(serverUrl + "card/", params, responseHandler);
    }
}
