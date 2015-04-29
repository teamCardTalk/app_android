package com.team.cardTalk;

import android.util.Log;
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

    public static void uploadArticle(Article article, String filePath, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("authorid", article.getAuthorid());
        params.put("nickname", article.getAuthor());
        params.put("icon", article.getIcon());
        params.put("title", article.getTitle());
//        params.put("createtime", article.getCreatetime());
        params.put("content", article.getContent());
        params.put("partynumber", article.getPartynumber());
//        params.put("chattingtime", article.getChattingtime());
//        params.put("status", article.getStatus());
//        params.put("chatting", article.getChatting());
        try {
            params.put("upload", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post("http://125.209.195.202:3000/card", params, responseHandler);
        Log.e("test", "card upload");
    }
}
