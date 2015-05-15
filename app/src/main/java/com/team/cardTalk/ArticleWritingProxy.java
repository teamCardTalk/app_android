package com.team.cardTalk;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Garam on 2015-04-27.
 */
public class ArticleWritingProxy {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private String serverUrl;
    private SharedPreferences pref;
    private Context context;

    public ArticleWritingProxy(Context context) {
        this.context = context;
        String prefName = context.getResources().getString(R.string.pref_name);
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        serverUrl = pref.getString(context.getResources().getString(R.string.server_url), "");
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
