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
    private SharedPreferences pref;
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
        String authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
//        String chattingtime;
//        String chatting;
        String photo;

        String author;

        FileDownloader fileDownloader = new FileDownloader(context);

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);
                _id = jObj.getString("_id");
                status = jObj.getInt("status");
                title = jObj.getString("title");

                author = jObj.getString("author");
                JSONObject authorObj = new JSONObject(author);

                nickname = authorObj.getString("nickname");
                authorid = authorObj.getString("userid");
                icon = authorObj.getString("icon");

                Log.i("test", "author: " + nickname + ", " + authorid + ", " + icon);
                createtime = jObj.getString("createtime");
                content = jObj.getString("content");
                partynumber = jObj.getInt("partynumber");

//                if (jObj.getString("chattingtime") != null) {
//                    chattingtime = jObj.getString("chattingtime");
//                }
//                chattingtime = "null";
//                chatting = jObj.getString("chatting");
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
                photo = photo.replaceFirst("../data/", "");
                values.put("_id", _id);
                values.put("Status", status);
                values.put("Title", title);
                values.put("Nickname", nickname);
                values.put("Authorid", authorid);
                values.put("Icon", icon);
                values.put("Createtime", createtime);
                values.put("Content", content);
                values.put("Partynumber", partynumber);
//                values.put("Chattingtime", chattingtime);
//                values.put("Chatting", chatting);
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

    public Cursor getArticleList() {

        Cursor cursor = context.getContentResolver().query(
                CardtalkContract.Cards.CONTENT_URI,
                CardtalkContract.Cards.PROJECTION_ALL, null, null,
                CardtalkContract.Cards.SORT_ORDER_DEFAULT
        );

        return cursor;
    }

    public CardDTO getArticleByArticleId(String _id) {
        CardDTO article = null;

        int status;
        String title;
        String nickname;
        String authorid;
        String icon;
        String createtime;
        String content;
        int partynumber;
//        String chattingtime;
//        String chatting;
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
            authorid = cursor.getString(4);
            icon = cursor.getString(5);
            createtime = cursor.getString(6);
            content = cursor.getString(7);
            partynumber = cursor.getInt(8);
//            chattingtime = cursor.getString(9);
//            chatting = cursor.getString(10);
            photo = cursor.getString(9);

            article = new CardDTO(_id, status, title, nickname, authorid, icon, createtime, content, partynumber, photo);
//            article = new CardDTO(_id, status, title, nickname, authorid, icon, createtime, content, partynumber, chattingtime, chatting, photo);
        }
        cursor.close();

        return article;
    }

    public ContentValues insertJsonChatListData(String jsonData) {

        ContentValues values = new ContentValues();
        String _id;
        String articleid;
        String nickname;
        String userid;
        String icon;
        String content;
        String time;
        String user;

        FileDownloader fileDownloader = new FileDownloader(context);

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);

            _id = jObj.getString("_id");
            articleid = jObj.getString("articleid");

            user = jObj.getString("user");
            JSONObject userObj = new JSONObject(user);

            nickname = userObj.getString("nickname");
            userid = userObj.getString("userid");
            icon = userObj.getString("icon");

            Log.i("test", "user: " + nickname + ", " + userid + ", " + icon);

//                nickname = jObj.getString("nickname");
//                userid = jObj.getInt("userid");
//                icon = jObj.getString("icon");
            content = jObj.getString("content");
            time = jObj.getString("time");

            Log.i("test", "title: " + content);

//                if (i == jArr.length() - 1) {
//                    pref = context.getSharedPreferences(context.getResources().getString(R.string.pref_name), context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString(context.getResources().getString(R.string.pref_chatkey), _id);
//                    editor.commit();
//
//                    Log.i("test", "updated chatkey: " + pref.getString(context.getResources().getString(R.string.pref_chatkey), ""));
//                }

            icon = icon.replaceAll("icon/", "");
            values.put("_id", _id);
            values.put("Articleid", articleid);
            values.put("Nickname", nickname);
            values.put("Userid", userid);
            values.put("Icon", icon);
            values.put("Content", content);
            values.put("Time", time);

            context.getContentResolver().insert(CardtalkContract.Chats.CONTENT_URI, values);

            Log.i("test", "icon: " + icon);
            fileDownloader.downFile("http://125.209.195.202:3000/image/icon=" + icon, icon);
//
//                String sql = "INSERT INTO Chats(_id, articleid, nickname, userid, icon, content, time)"
//                        + " VALUES('" + _id + "', '"+ articleid + "', '" + nickname + "', '" + userid + "', '" + icon + "', '" + content + "', '" + time + "');";
//
//                Log.i("test", sql);
//
//                try {
//                    database.execSQL(sql);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }

        return values;
    }

    public ContentValues insertJsonChatData(String jsonData) {

        ContentValues values = new ContentValues();
        String _id;
        String articleid;
        String nickname;
        String userid;
        String icon;
        String content;
        String time;
        String user;

        FileDownloader fileDownloader = new FileDownloader(context);

        try {
//            JSONArray jArr = new JSONArray(jsonData);
//
//            for (int i = 0; i < jArr.length(); ++i) {
//                JSONObject jObj = jArr.getJSONObject(i);
            JSONObject jObj = new JSONObject(jsonData);
                _id = jObj.getString("_id");
                articleid = jObj.getString("articleid");

                user = jObj.getString("user");
                JSONObject userObj = new JSONObject(user);

                nickname = userObj.getString("nickname");
                userid = userObj.getString("userid");
                icon = userObj.getString("icon");

                Log.i("test", "user: " + nickname + ", " + userid + ", " + icon);

//                nickname = jObj.getString("nickname");
//                userid = jObj.getInt("userid");
//                icon = jObj.getString("icon");
                content = jObj.getString("content");
                time = jObj.getString("time");

                Log.i("test", "title: " + content);
//                if (i == jArr.length() - 1) {
//                    pref = context.getSharedPreferences(context.getResources().getString(R.string.pref_name), context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString(context.getResources().getString(R.string.pref_chatkey), _id);
//                    editor.commit();
//
//                    Log.i("test", "updated chatkey: " + pref.getString(context.getResources().getString(R.string.pref_chatkey), ""));
//                }
                icon = icon.replaceAll("icon/", "");
                values.put("_id", _id);
                values.put("Articleid", articleid);
                values.put("Nickname", nickname);
                values.put("Userid", userid);
                values.put("Icon", icon);
                values.put("Content", content);
                values.put("Time", time);

                context.getContentResolver().insert(CardtalkContract.Chats.CONTENT_URI, values);

                Log.i("test", "icon: " + icon);
                fileDownloader.downFile("http://125.209.195.202:3000/image/icon=" + icon, icon);

                Log.i("test", "rabbitmq chatvalues: " + values.toString());
//
//                String sql = "INSERT INTO Chats(_id, articleid, nickname, userid, icon, content, time)"
//                        + " VALUES('" + _id + "', '"+ articleid + "', '" + nickname + "', '" + userid + "', '" + icon + "', '" + content + "', '" + time + "');";
//
//                Log.i("test", sql);
//
//                try {
//                    database.execSQL(sql);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }

        return values;
    }

    public Cursor getChatListByArticleId(String _id) {

        String where = "articleid='" + _id + "'";

        Log.i("test", "chatList where: " + where);
        Cursor cursor = context.getContentResolver().query(
                CardtalkContract.Chats.CONTENT_URI,
                CardtalkContract.Chats.PROJECTION_ALL, where, null,
                CardtalkContract.Chats.SORT_ORDER_DEFAULT
        );

        return cursor;
    }

//    public ArrayList getChatListByArticleId2(String _id) {
//
//        ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();
//
//        String chatid;
//        String articleid;
//        String nickname;
//        int nicknameid;
//        String icon;
//        String content;
//        String time;
//
////        String sql = "SELECT * FROM CHATS WHERE articleid = '" + _id + "';";
//
//        String where = "articleid='" + _id + "'";
//
//        Log.i("test", "chatList where: " + where);
//        Cursor cursor = context.getContentResolver().query(
//                CardtalkContract.Chats.CONTENT_URI,
//                CardtalkContract.Chats.PROJECTION_ALL, where, null,
//                CardtalkContract.Chats.SORT_ORDER_DEFAULT
//        );
//
////        Cursor cursor = database.rawQuery(sql, null);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//
//            while (cursor.moveToNext()) {
//
//                chatid = cursor.getString(0);
//                articleid = cursor.getString(1);
//                nickname = cursor.getString(2);
//                nicknameid = cursor.getInt(3);
//                icon = cursor.getString(4);
//                content = cursor.getString(5);
//                time = cursor.getString(6);
//
//                chatList.add(new ChatDTO(chatid, articleid, nickname, nicknameid, icon, content, time));
//
//                Log.i("test", content);
//            }
//        }
//
//        cursor.close();
//
//        return chatList;
//    }

//    public void insertJsonRoomTestData() {
//
//        String articleid = "5534781e6c881dab0ffe6403";
//        int authorid = 00000002;
//        String icon = "icon4.png";
//        String title = "붉은 왕의 고혹적인 침실";
//        String time = "04-20 12:53";
//        String chat = "형진이는 못하는게 뭐지?";
//
//        String sql = "INSERT INTO Rooms(articleid, authorid, icon, title, time, chat)"
//                + " VALUES('" + articleid + "', " + authorid + ", '" + icon + "', '" + title + "', '" + time + "', '" + chat + "');";
//
//        Log.i("test", sql);
//
//        try {
//            database.execSQL(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        articleid = "553522479f9a77b90f8e7b18";
//        authorid = 00000002;
//        icon = "icon1.png";
//        title = "붉은 왕의 허리케인";
//        time = "04-21 01:14";
//        chat = "채팅방 테스트";
//
//        sql = "INSERT INTO Rooms(articleid, authorid, icon, title, time, chat)"
//                + " VALUES('" + articleid + "', " + authorid + ", '" + icon + "', '" + title + "', '" + time + "', '" + chat + "');";
//
//        Log.i("test", sql);
//
//        try {
//            database.execSQL(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void insertDTORoomData(RoomDTO roomDTO) {
        String _id = roomDTO.getArticleid();
        String title = roomDTO.getTitle();
        String nickname = roomDTO.getNickname();
        String authorid = roomDTO.getAuthorid();
        String icon = roomDTO.getIcon();
        String time = roomDTO.getTime();
        String chat = roomDTO.getChat();

        FileDownloader fileDownloader = new FileDownloader(context);

        ContentValues values = new ContentValues();
        icon = icon.replaceAll("icon/", "");
        values.put("_id", _id);
        values.put("Title", title);
        values.put("Nickname", nickname);
        values.put("Authorid", authorid);
        values.put("Icon", icon);
        values.put("Time", time);
        values.put("Chat", chat);

        context.getContentResolver().insert(CardtalkContract.Rooms.CONTENT_URI, values);

        Log.i("test", "icon: " + icon);
        fileDownloader.downFile("http://125.209.195.202:3000/image/icon=" + icon, icon);
    }

    public void updateContentValueRoomData(ContentValues values) {
        ContentValues roomValues = new ContentValues();
        roomValues.put("Chat", values.getAsString("Content"));
        roomValues.put("Time", values.getAsString("Time"));

        context.getContentResolver().update(
                CardtalkContract.Rooms.CONTENT_URI, roomValues, null, new String[] {values.getAsString("Articleid")}
        );
    }

    public RoomDTO getRoomDetail(String jsonData) {
        String _id = "";
        String title = "";
        String nickname = "";
        String authorid = "";
        String icon = "";
        String time = "";
        String chat = "";
        String author;

        try {
            JSONObject jObj = new JSONObject(jsonData);
            _id = jObj.getString("_id");
            title = jObj.getString("title");
            author = jObj.getString("author");

            JSONObject authorObj = new JSONObject(author);

            nickname = authorObj.getString("nickname");
            authorid = authorObj.getString("userid");
            icon = authorObj.getString("icon");

            time = jObj.getString("createtime");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new RoomDTO(_id, title, nickname, authorid, icon, time, chat);
    }

    public ArrayList<RoomDTO> getRoomList() {

        ArrayList<RoomDTO> roomList = new ArrayList<>();

        String articleid;
        String title;
        String nickname;
        String authorid;
        String icon;
        String time;
        String chat;

        String sql = "SELECT * FROM ROOMS ORDER BY time DESC;";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            articleid = cursor.getString(0);
            title = cursor.getString(1);
            nickname = cursor.getString(2);
            authorid = cursor.getString(3);
            icon = cursor.getString(4);
            time = cursor.getString(5);
            chat = cursor.getString(6);

            roomList.add(new RoomDTO(articleid, title, nickname, authorid, icon, time, chat));
        }
        cursor.close();

        return roomList;
    }

    public ArrayList<String> splitJsonFriendListDataToArrayList(String jsonData) {

        ArrayList<String> friendList = new ArrayList<>();

        if (jsonData == null || jsonData.isEmpty()) return null;
        JSONObject jObj;
        JSONArray friendsArr, jArr;

        try {
            jArr = new JSONArray(jsonData);
            Log.i("test", "jArr: " + jArr.toString());

            for (int i = 0; i < jArr.length(); ++i) {
                jObj = jArr.getJSONObject(i);
                Log.i("test", "jObj: " + jObj.toString());

                friendsArr = new JSONArray(jObj.getString("friends"));
                Log.i("test", "friendsArr: " + friendsArr.toString());

                for (int j = 0; i < friendsArr.length(); ++j) {
                    String friendid = friendsArr.getString(j);
                    Log.i("test", "friendid: " + friendid);

                    friendList.add(friendid);
                }
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }

        return friendList;
    }

    public void insertJsonFriendData(String jsonData) {

        if (jsonData == null || jsonData.isEmpty()) return;

        String nickname;
        String _id;
        String icon;

        FileDownloader fileDownloader = new FileDownloader(context);

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);

                nickname = jObj.getString("nickname");
                _id = jObj.getString("userid");
                icon = jObj.getString("icon");

                Log.i("test", "author: " + nickname + ", " + _id + ", " + icon);

                ContentValues values = new ContentValues();
                icon = icon.replaceAll("icon/", "");

                values.put("Nickname", nickname);
                values.put("_Id", _id);
                values.put("Icon", icon);
                context.getContentResolver().insert(CardtalkContract.Friends.CONTENT_URI, values);

                Log.i("test", "icon: " + icon);
                fileDownloader.downFile("http://125.209.195.202:3000/image/icon=" + icon, icon);
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }
    }

    public Cursor getFriendList() {

        Cursor cursor = context.getContentResolver().query(
                CardtalkContract.Friends.CONTENT_URI,
                CardtalkContract.Friends.PROJECTION_ALL, null, null,
                CardtalkContract.Friends.SORT_ORDER_DEFAULT
        );

        return cursor;
    }

    public ArrayList<String> splitJsonUserListDataToArrayList(String jsonData) {

        ArrayList<String> userList = new ArrayList<>();

        if (jsonData == null || jsonData.isEmpty()) return null;
        JSONObject jObj;
        JSONArray friendsArr;

        try {
            jObj = new JSONObject(jsonData);
            friendsArr = new JSONArray(jObj.getString("userList"));

            for (int i = 0; i < friendsArr.length(); ++i) {
                userList.add(friendsArr.getString(i));
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }

        return userList;
    }

    public UserDTO convertJsonUserData(String jsonData) {

        if (jsonData == null || jsonData.isEmpty()) return null;

        String nickname;
        String _id;
        String icon;
        UserDTO userDTO = null;

        FileDownloader fileDownloader = new FileDownloader(context);

        try {
            JSONArray jArr = new JSONArray(jsonData);

            for (int i = 0; i < jArr.length(); ++i) {
                JSONObject jObj = jArr.getJSONObject(i);

                nickname = jObj.getString("nickname");
                _id = jObj.getString("userid");
                icon = jObj.getString("icon");

                Log.i("test", "author: " + nickname + ", " + _id + ", " + icon);

                userDTO = new UserDTO(_id, nickname, icon);

                Log.i("test", "icon: " + icon);
                fileDownloader.downFile("http://125.209.195.202:3000/image/icon=" + icon, icon);
            }
        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }

        return userDTO;
    }
//    public String findArticleId(String jsonData) {
//
//        if (jsonData == null || jsonData.isEmpty()) return null;
//
//        String _id;
//
//        try {
//            JSONObject jsonObject = new JSONObject(jsonData);
//            _id = jsonObject.getString("_id");
//            Log.i("test", "findArticleid: " + _id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//    }

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