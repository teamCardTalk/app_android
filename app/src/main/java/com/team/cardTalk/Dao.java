package com.team.cardTalk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 3. 31..
 */
public class Dao {
    private Context context;
    private SQLiteDatabase database;

    public Dao(Context context) {
        this.context = context;
        String sql;

        database = context.openOrCreateDatabase("LocalData.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        try {
            sql = "CREATE TABLE IF NOT EXISTS Articles (id integer primary key autoincrement,"
                    + "                                         status int not null,"
                    + "                                         title text not null,"
                    + "                                         author text not null,"
                    + "                                         authorid int not null,"
                    + "                                         icon text not null,"
                    + "                                         createtime text not null,"
                    + "                                         content text not null,"
                    + "                                         partynumber integer,"
                    + "                                         chattingtime text not null,"
                    + "                                         chatting text,"
                    + "                                         photo text)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE ARTICLES TABLE FAILED! - " + e);
            e.printStackTrace();
        }

        // 채팅
        try {
            sql = "CREATE TABLE IF NOT EXISTS Chats (id integer primary key autoincrement,"
                    + "                                         articleid integer not null,"
                    + "                                         nickname text not null,"
                    + "                                         nicknameid int not null,"
                    + "                                         icon text not null,"
                    + "                                         content text not null,"
                    + "                                         time text not null)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE CHATS TABLE FAILED! - " + e);
            e.printStackTrace();
        }
    }

    public String getJsonTestData() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("[");

        sb.append("     {");
        sb.append("       'status':'1',");
        sb.append("       'title':'녹색 여왕의 활기찬 정원',");
        sb.append("       'author':'녹색 여왕',");
        sb.append("       'authorid':'1',");
        sb.append("       'icon':'icon1.png',");
        sb.append("       'createtime':'2015-03-20-09-10',");
        sb.append("       'content':'NEXT MOVIE NIGHT (feat. 빅히어로 & pizza)\n13일의 금요일 저녁을 빅히어로와 함께!\n무슨 영화??? 빅 히어로\n> 말랑말랑 마시멜로같은 히어로 본 적 있나요?',");
        sb.append("       'partynumber':'9',");
        sb.append("       'chattingtime':'2015-03-20-12-30',");
        sb.append("       'chatting':'',");
        sb.append("       'photo':'img1.png'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'status':'1',");
        sb.append("       'title':'핑크 마법사의 거품 욕조',");
        sb.append("       'author':'핑크 마법사',");
        sb.append("       'authorid':'2',");
        sb.append("       'icon':'icon2.png',");
        sb.append("       'createtime':'2015-03-20-09-20',");
        sb.append("       'content':'삼시세끼가 끝나서 산체 보는 낙이 없네 ㅠㅠ',");
        sb.append("       'partynumber':'3',");
        sb.append("       'chattingtime':'2015-03-20-13-30',");
        sb.append("       'chatting':'',");
        sb.append("       'photo':'img2.png'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'status':'1',");
        sb.append("       'title':'노란 조커의 은밀한 화장실',");
        sb.append("       'author':'노란 조커',");
        sb.append("       'authorid':'3',");
        sb.append("       'icon':'icon3.png',");
        sb.append("       'createtime':'2015-03-20-09-30',");
        sb.append("       'content':'형진이는 못하는게 뭐지',");
        sb.append("       'partynumber':'2',");
        sb.append("       'chattingtime':'2015-03-21-09-30',");
        sb.append("       'chatting':'',");
        sb.append("       'photo':''");
        sb.append("     }");

        sb.append("]");

        return sb.toString();
    }

    public void insertJsonData(String jsonData) {
        int status;
        String title;
        String author;
        int authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
        String chattingtime;
        String chatting;
        String photo;

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);
                status = jObj.getInt("status");
                title = jObj.getString("title");
                author = jObj.getString("author");
                authorid = jObj.getInt("authorid");
                icon = jObj.getString("icon");
                createtime = jObj.getString("createtime");
                content = jObj.getString("content");
                partynumber = jObj.getInt("partynumber");
                chattingtime = jObj.getString("chattingtime");
                chatting = jObj.getString("chatting");
                photo = jObj.getString("photo");

                Log.i("test", "title: " + title);

                String sql = "INSERT INTO Articles(status, title, author, authorid, icon, createtime, content, partynumber, chattingtime, chatting, photo)"
                        + " VALUES(" + status + ", '" + title + "', '" + author + "', " + authorid + ", '" + icon + "', '" + createtime + "', '" + content + "', "
                        + partynumber + ", '" + chattingtime + "', '" + chatting + "', '" + photo + "');";

                Log.i("test", sql);

                try {
                    database.execSQL(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }
    }

    public ArrayList<Article> getArticleList() {

        ArrayList<Article> articleList = new ArrayList<Article>();

        int id;
        int status;
        String title;
        String author;
        int authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
        String chattingtime;
        String chatting;
        String photo;

        String sql = "SELECT * FROM ARTICLES;";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            status = cursor.getInt(1);
            title = cursor.getString(2);
            author = cursor.getString(3);
            authorid = cursor.getInt(4);
            icon = cursor.getString(5);
            createtime = cursor.getString(6);
            content = cursor.getString(7);
            if (content.length() > 60) {
                content = content.substring(0, 60) + "  ...더보기";
            }
            partynumber = cursor.getInt(8);
            chattingtime = cursor.getString(9);
            chatting = cursor.getString(10);
            photo = cursor.getString(11);

            articleList.add(new Article(id, status, title, author, authorid, icon, createtime, content, partynumber, chattingtime, chatting, photo));
        }
        cursor.close();

        return articleList;

    }


    public Article getArticleByArticleNumber(int id) {
        Article article = null;

        int status;
        String title;
        String author;
        int authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
        String chattingtime;
        String chatting;
        String photo;

        String sql = "SELECT * FROM ARTICLES WHERE id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToNext();

        status = cursor.getInt(1);
        title = cursor.getString(2);
        author = cursor.getString(3);
        authorid = cursor.getInt(4);
        icon = cursor.getString(5);
        createtime = cursor.getString(6);
        content = cursor.getString(7);
        partynumber = cursor.getInt(8);
        chattingtime = cursor.getString(9);
        chatting = cursor.getString(10);
        photo = cursor.getString(11);

        article = new Article(id, status, title, author, authorid, icon, createtime, content, partynumber, chattingtime, chatting, photo);

        cursor.close();

        return article;
    }


    // 채팅

    public String getJsonChatData() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("[");

        sb.append("     {");
        sb.append("       'articleid':'1',");
        sb.append("       'nickname':'녹색 여왕',");
        sb.append("       'nicknameid':'1',");
        sb.append("       'icon':'icon1.png',");
        sb.append("       'content':'NEXT MOVIE NIGHT (feat. 빅히어로 & pizza)\n13일의 금요일 저녁을 빅히어로와 함께!\n무슨 영화??? 빅 히어로\n> 말랑말랑 마시멜로같은 히어로 본 적 있나요?',");
        sb.append("       'time':'2015-03-20-09-10'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'articleid':'1',");
        sb.append("       'nickname':'핑크 마법사',");
        sb.append("       'nicknameid':'2',");
        sb.append("       'icon':'icon2.png',");
        sb.append("       'content':'1삼시세끼가 끝나서 ㅠㅠ',");
        sb.append("       'time':'2015-03-20-09-20'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'articleid':'1',");
        sb.append("       'nickname':'노란 조커',");
        sb.append("       'nicknameid':'3',");
        sb.append("       'icon':'icon3.png',");
        sb.append("       'content':'형진이는 못하는게 뭐지',");
        sb.append("       'time':'2015-03-20-09-30'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'articleid':'2',");
        sb.append("       'nickname':'핑크 마법사',");
        sb.append("       'nicknameid':'2',");
        sb.append("       'icon':'icon2.png',");
        sb.append("       'content':'삼시세끼가 끝나서 산체 보는 낙이 없네 ㅠㅠ',");
        sb.append("       'time':'2015-03-20-09-20'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'articleid':'2',");
        sb.append("       'nickname':'노란 조커',");
        sb.append("       'nicknameid':'3',");
        sb.append("       'icon':'icon3.png',");
        sb.append("       'content':'형진이는 못하는게 뭐지',");
        sb.append("       'time':'2015-03-20-09-30'");
        sb.append("     },");

        sb.append("     {");
        sb.append("       'articleid':'3',");
        sb.append("       'nickname':'노란 조커',");
        sb.append("       'nicknameid':'3',");
        sb.append("       'icon':'icon3.png',");
        sb.append("       'content':'형진이는 못하는게 뭐지',");
        sb.append("       'time':'2015-03-20-09-30'");
        sb.append("     }");

        sb.append("]");

        return sb.toString();
    }

    public void insertJsonChatData(String jsonData) {

        int articleid;
        String nickname;
        int nicknameid;
        String icon;
        String content;
        String time;

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);
                articleid = jObj.getInt("articleid");
                nickname = jObj.getString("nickname");
                nicknameid = jObj.getInt("nicknameid");
                icon = jObj.getString("icon");
                content = jObj.getString("content");
                time = jObj.getString("time");

                Log.i("test", "title: " + content);

                String sql = "INSERT INTO Chats(articleid, nickname, nicknameid, icon, content,time)"
                        + " VALUES(" + articleid + ", '" + nickname + "', '" + nicknameid + "', '" + icon + "', '" + content + "', '" + time + "');";

                Log.i("test", sql);

                try {
                    database.execSQL(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }
    }

    public ArrayList getChatListByArticleNumber(int id) {

        ArrayList<Chat> chatList = new ArrayList<Chat>();

        int chatid;
        int articleid;
        String nickname;
        int nicknameid;
        String icon;
        String content;
        String time;

        String sql = "SELECT * FROM CHATS WHERE articleid = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            chatid = cursor.getInt(0);
            articleid = cursor.getInt(1);
            nickname = cursor.getString(2);
            nicknameid = cursor.getInt(3);
            icon = cursor.getString(4);
            content = cursor.getString(5);
            time = cursor.getString(6);

            chatList.add(new Chat(chatid, articleid, nickname, nicknameid, icon, content, time));

            Log.i("test", content);
        }
        cursor.close();

        return chatList;
    }
}