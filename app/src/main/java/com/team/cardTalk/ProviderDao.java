package com.team.cardTalk;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 5. 15..
 */
public class ProviderDao {
    private Context context;
    private SQLiteDatabase database;
    private final String TABLE_NAME = "Cards";

    public ProviderDao(Context context) {
        this.context = context;
        database = context.openOrCreateDatabase("LocalData.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
//        String sql;
//
//        database = context.openOrCreateDatabase("LocalData.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
//
//        try {
//            sql = "CREATE TABLE IF NOT EXISTS" + TABLE_NAME + "(_id text primary key not null,"
//                    + "                                         status int not null,"
//                    + "                                         title text not null,"
//                    + "                                         nickname text not null,"
//                    + "                                         authorid int not null,"
//                    + "                                         icon text not null,"
//                    + "                                         createtime text not null,"
//                    + "                                         content text not null,"
//                    + "                                         partynumber integer,"
//                    + "                                         chattingtime text not null,"
//                    + "                                         chatting text,"
//                    + "                                         photo text)";
//            database.execSQL(sql);
//        } catch (Exception e) {
//            Log.e("test", "CREATE CARDS TABLE FAILED! - " + e);
//            e.printStackTrace();
//        }
//
//        // 채팅
//        try {
//            sql = "CREATE TABLE IF NOT EXISTS Chats (_id text primary key not null,"
//                    + "                                         articleid text not null,"
//                    + "                                         nickname text not null,"
//                    + "                                         userid int not null,"
//                    + "                                         icon text not null,"
//                    + "                                         content text not null,"
//                    + "                                         time text not null)";
//            database.execSQL(sql);
//        } catch (Exception e) {
//            Log.e("test", "CREATE CHATS TABLE FAILED! - " + e);
//            e.printStackTrace();
//        }
//
//        // 채팅룸
//        try {
//            sql = "CREATE TABLE IF NOT EXISTS Rooms (articleid text primary key not null,"
//                    + "                                         authorid int not null,"
//                    + "                                         icon text not null,"
//                    + "                                         title text not null,"
//                    + "                                         time text not null,"
//                    + "                                         chat text not null)";
//            database.execSQL(sql);
//        } catch (Exception e) {
//            Log.e("test", "CREATE ROOMS TABLE FAILED! - " + e);
//            e.printStackTrace();
//        }
    }

    public void close() {
        database.close();
    }

    public void insertJsonData(String jsonData) {

        if (jsonData == null || jsonData.isEmpty()) return;

        String _id;
        int status;
        String title;
        String nickname;
        int authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
        String chattingtime;
        String chatting;
        String photo;
        SharedPreferences pref;

        FileDownloader fileDownloader = new FileDownloader(context);

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);
                _id = jObj.getString("_id");
                status = jObj.getInt("status");
                title = jObj.getString("title");
                nickname = jObj.getString("nickname");
                authorid = jObj.getInt("authorid");
                icon = jObj.getString("icon");
                createtime = jObj.getString("createtime");
                content = jObj.getString("content");
                partynumber = jObj.getInt("partynumber");
                chattingtime = jObj.getString("chattingtime");
                chatting = jObj.getString("chatting");
                JSONArray photoArray = jObj.getJSONArray("file");
                photo = photoArray.getJSONObject(0).getString("path");

                Log.i("test", "jArr length: " + jArr.length() + ", i: " + i);

                if (i == jArr.length() - 1) {
                    pref = context.getSharedPreferences(context.getResources().getString(R.string.pref_name), context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(context.getResources().getString(R.string.pref_cardkey), _id);
                    editor.commit();

                    Log.i("test", "updated cardkey: " + pref.getString(context.getResources().getString(R.string.pref_cardkey), ""));
                }

                ContentValues values = new ContentValues();
                icon = icon.replaceAll("icon/", "");
                photo = photo.replaceFirst("data/", "");
                values.put("_id", _id);
                values.put("Status", status);
                values.put("Title", title);
                values.put("Nickname", nickname);
                values.put("Authorid", authorid);
                values.put("Icon", icon);
                values.put("Createtime", createtime);
                values.put("Content", content);
                values.put("Partynumber", partynumber);
                values.put("Chattingtime", chattingtime);
                values.put("Chatting", chatting);
                values.put("Photo", photo);

                context.getContentResolver().insert(CardtalkContract.Cards.CONTENT_URI, values);

                Log.i("test", "icon: " + icon);
                fileDownloader.downFile("http://125.209.195.202:3000/image/icon=" + icon, icon);

                if (photo != null) {
                    Log.i("test", "photo: " + photo);
                    fileDownloader.downFile("http://125.209.195.202:3000/image/photo=" + photo, photo);

                    Log.i("test", "insertJsonData: " + title + ", " + _id);
                }
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }
    }

    public ArrayList<CardDTO> getArticleList() {

        ArrayList<CardDTO> cardList = new ArrayList<>();

        String _id;
        int status;
        String title;
        String nickname;
        int authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
        String chattingtime;
        String chatting;
        String photo;
//        String modtime;

        Cursor cursor = context.getContentResolver().query(
                CardtalkContract.Cards.CONTENT_URI,
                CardtalkContract.Cards.PROJECTION_ALL, null, null,
                CardtalkContract.Cards.SORT_ORDER_DEFAULT
        );

//        String sql = "SELECT * FROM CARDS order by createtime DESC;";
//        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null) {
            cursor.moveToFirst();

            while (!(cursor.isAfterLast())) {
                _id = cursor.getString(0);
                status = cursor.getInt(1);
                title = cursor.getString(2);
                nickname = cursor.getString(3);
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

                cardList.add(new CardDTO(_id, status, title, nickname, authorid, icon, createtime, content, partynumber, chattingtime, chatting, photo));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return cardList;
    }

    public CardDTO getArticleByArticleId(String _id) {
        CardDTO article = null;

        int status;
        String title;
        String nickname;
        int authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
        String chattingtime;
        String chatting;
        String photo;

//        String sql = "SELECT * FROM CARDS WHERE _id = '" + _id + "';";
//        Cursor cursor = database.rawQuery(sql, null);
//
//        cursor.moveToNext();

        String where = "_id='" + _id + "'";

        Cursor cursor = context.getContentResolver().query(
                CardtalkContract.Cards.CONTENT_URI,
                CardtalkContract.Cards.PROJECTION_ALL, where, null,
                CardtalkContract.Cards.SORT_ORDER_DEFAULT
        );

        if (cursor != null) {
            cursor.moveToFirst();
            status = cursor.getInt(1);
            title = cursor.getString(2);
            nickname = cursor.getString(3);
            authorid = cursor.getInt(4);
            icon = cursor.getString(5);
            createtime = cursor.getString(6);
            content = cursor.getString(7);
            partynumber = cursor.getInt(8);
            chattingtime = cursor.getString(9);
            chatting = cursor.getString(10);
            photo = cursor.getString(11);

            article = new CardDTO(_id, status, title, nickname, authorid, icon, createtime, content, partynumber, chattingtime, chatting, photo);
        }
        cursor.close();

        return article;
    }

    public void insertJsonChatData(String jsonData) {

        String _id;
        String articleid;
        String nickname;
        int userid;
        String icon;
        String content;
        String time;
        String modtime;

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);
                _id = jObj.getString("_id");
                articleid = jObj.getString("articleid");
                nickname = jObj.getString("nickname");
                userid = jObj.getInt("userid");
                icon = jObj.getString("icon");
                content = jObj.getString("content");
                time = jObj.getString("time");

                Log.i("test", "title: " + content);

                String sql = "INSERT INTO Chats(_id, articleid, nickname, userid, icon, content,time)"
                        + " VALUES('" + _id + "', '"+ articleid + "', '" + nickname + "', '" + userid + "', '" + icon + "', '" + content + "', '" + time + "');";

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

    public ArrayList getChatListByArticleId(String _id) {

        ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();

        String chatid;
        String articleid;
        String nickname;
        int nicknameid;
        String icon;
        String content;
        String time;

        String sql = "SELECT * FROM CHATS WHERE articleid = '" + _id + "';";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            chatid = cursor.getString(0);
            articleid = cursor.getString(1);
            nickname = cursor.getString(2);
            nicknameid = cursor.getInt(3);
            icon = cursor.getString(4);
            content = cursor.getString(5);
            time = cursor.getString(6);

            chatList.add(new ChatDTO(chatid, articleid, nickname, nicknameid, icon, content, time));

            Log.i("test", content);
        }
        cursor.close();

        return chatList;
    }

    public void insertJsonRoomTestData() {

        String articleid = "5534781e6c881dab0ffe6403";
        int authorid = 00000002;
        String icon = "icon4.png";
        String title = "붉은 왕의 고혹적인 침실";
        String time = "04-20 12:53";
        String chat = "형진이는 못하는게 뭐지?";

        String sql = "INSERT INTO Rooms(articleid, authorid, icon, title, time, chat)"
                + " VALUES('" + articleid + "', " + authorid + ", '" + icon + "', '" + title + "', '" + time + "', '" + chat + "');";

        Log.i("test", sql);

        try {
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        articleid = "553522479f9a77b90f8e7b18";
        authorid = 00000002;
        icon = "icon1.png";
        title = "붉은 왕의 허리케인";
        time = "04-21 01:14";
        chat = "채팅방 테스트";

        sql = "INSERT INTO Rooms(articleid, authorid, icon, title, time, chat)"
                + " VALUES('" + articleid + "', " + authorid + ", '" + icon + "', '" + title + "', '" + time + "', '" + chat + "');";

        Log.i("test", sql);

        try {
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertJsonRoomData(String jsonData) {

        String articleid;
        int authorid;
        String icon;
        String title;
        String time;
        String chat;

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);
                articleid = jObj.getString("articleid");
                authorid = jObj.getInt("authorid");
                icon = jObj.getString("icon");
                title = jObj.getString("title");
                time = jObj.getString("time");
                chat = jObj.getString("chat");

                Log.i("test", "chat: " + chat);

                String sql = "INSERT INTO Rooms(articleid, authorid, icon, title, time, chat)"
                        + " VALUES('" + articleid + "', " + authorid + ", '" + icon + "', '" + title + "', '" + time + "', '" + chat + "');";

                Log.i("test", "insert sql: " + sql);

                try {
                    database.execSQL(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                    sql = "UPDATE Rooms set time='" + time + "', chat = '" + chat + "' where articleid='" + articleid + "';";
                    try {
                        database.execSQL(sql);
                        Log.i("test", "update sql: " + sql);
                    } catch (Exception ec) {
                        ec.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }
    }

    public ArrayList<RoomDTO> getRoomList() {

        ArrayList<RoomDTO> roomList = new ArrayList<>();

        String articleid;
        int authorid;
        String icon;
        String title;
        String time;
        String chat;

        String sql = "SELECT * FROM ROOMS ORDER BY time DESC;";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            articleid = cursor.getString(0);
            authorid = cursor.getInt(1);
            icon = cursor.getString(2);
            title = cursor.getString(3);
            time = cursor.getString(4);
            chat = cursor.getString(5);

            roomList.add(new RoomDTO(articleid, authorid, icon, title, time, chat));
        }
        cursor.close();

        return roomList;
    }
//
//    public String parsingDate(String inputDate) {
//        try {
//            Date date = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z").parse(inputDate);
//            return new SimpleDateFormat("MM-dd hh:mm").format(date).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return inputDate;
//    }
}