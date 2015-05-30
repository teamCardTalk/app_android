package com.team.cardTalk;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Created by eunjooim on 15. 5. 15..
 */
public class CardtalkProvider extends ContentProvider{

    private Context context;
    private SQLiteDatabase database;
    private final String TABLE_NAME1 = "Cards";
    private final String TABLE_NAME2 = "Chats";
    private final String TABLE_NAME3 = "Rooms";
    private final String TABLE_NAME4 = "Friends";

    private static final int CARD_LIST = 1;
    private static final int CARD_ID = 2;
    private static final int CHAT_ID = 3;
    private static final int ROOM_LIST = 4;
    private static final int ROOM_ID = 5;
    private static final int FRIENDS_LIST = 6;
    private static final int FRIENDS_ID = 7;
    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Cards", CARD_LIST);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Cards/#", CARD_ID);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Chats", CHAT_ID);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Rooms", ROOM_LIST);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Rooms/#", ROOM_ID);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Friends", FRIENDS_LIST);
    }

    private void sqLiteInitialize() {
        database = context.openOrCreateDatabase("LocalData.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        database.setLocale(Locale.getDefault());
        database.setVersion(1);
    }

    private void createTables() {
        String sql;

        try {
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + " (_id text primary key not null,"
                    + "                                         status int not null,"
                    + "                                         title text not null,"
                    + "                                         nickname text not null,"
                    + "                                         authorid string not null,"
                    + "                                         icon text not null,"
                    + "                                         createtime text not null,"
                    + "                                         content text not null,"
                    + "                                         partynumber integer,"
//                    + "                                         chattingtime text not null,"
//                    + "                                         chatting text,"
                    + "                                         photo text)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE CARDS TABLE FAILED! - " + e);
            e.printStackTrace();
        }

        try {
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " (_id text primary key not null,"
                    + "                                         articleid text not null,"
                    + "                                         nickname text not null,"
                    + "                                         userid int not null,"
                    + "                                         icon text not null,"
                    + "                                         content text not null,"
                    + "                                         time text not null)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE CHATS TABLE FAILED! - " + e);
            e.printStackTrace();
        }

        try {
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME3 + "  (_id text primary key not null,"
                    + "                                         title text not null,"
                    + "                                         authorid text not null,"
                    + "                                         nickname text not null,"
                    + "                                         icon text not null,"
                    + "                                         time text not null,"
                    + "                                         chat text not null)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE ROOMS TABLE FAILED! - " + e);
            e.printStackTrace();
        }

        try {
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME4 + "  (_id string primary key not null ,"
                    + "                                         nickname text not null,"
                    + "                                         icon text not null)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE FRIENDS TABLE FAILED! - " + e);
            e.printStackTrace();
        }
    }

    private boolean isTablesExist() {
        String searchTable;
        Cursor cursor;

        searchTable = "select DISTINCT tbl_name from " +
                "sqLite_master where tbl_name = '" + TABLE_NAME1 + "';";
        cursor = database.rawQuery(searchTable, null);

        if (cursor.getCount() == 0) {
            return false;
        }
        cursor.close();

        searchTable = "select DISTINCT tbl_name from " +
                "sqLite_master where tbl_name = '" + TABLE_NAME2 + "';";
        cursor = database.rawQuery(searchTable, null);

        if (cursor.getCount() == 0) {
            return false;
        }
        cursor.close();

        searchTable = "select DISTINCT tbl_name from " +
                "sqLite_master where tbl_name = '" + TABLE_NAME3 + "';";
        cursor = database.rawQuery(searchTable, null);

        if (cursor.getCount() == 0) {
            return false;
        }

        cursor.close();

        searchTable = "select DISTINCT tbl_name from " +
                "sqLite_master where tbl_name = '" + TABLE_NAME4 + "';";
        cursor = database.rawQuery(searchTable, null);

        if (cursor.getCount() == 0) {
            return false;
        }

        cursor.close();
        return true;
    }

    @Override
    public boolean onCreate() {
        this.context = getContext();
        sqLiteInitialize();

        if (!isTablesExist()) {
            createTables();
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (URI_MATCHER.match(uri)) {
            case CARD_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Cards.SORT_ORDER_DEFAULT;
                }
                cursor = database.query(TABLE_NAME1, CardtalkContract.Cards.PROJECTION_ALL, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                return cursor;

            case CARD_ID:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Cards.SORT_ORDER_DEFAULT;
                }

                if (selection == null) {
                    selection = "_ID = ?";
                    selectionArgs = new String[] {uri.getLastPathSegment()};
                }
                cursor = database.query(TABLE_NAME1, CardtalkContract.Cards.PROJECTION_ALL, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                Log.i("test", "cursor query uri:" + uri);

                return cursor;

            case CHAT_ID:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Chats.SORT_ORDER_DEFAULT;
                }

                if (selection == null) {
                    selection = "ARTICLEID = ?";
                    selectionArgs = new String[] {uri.getLastPathSegment()};
                }
                cursor = database.query(TABLE_NAME2, CardtalkContract.Chats.PROJECTION_ALL, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                Log.i("test", "cursor query uri:" + uri);

                return cursor;

            case ROOM_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Friends.SORT_ORDER_DEFAULT;
                }
                cursor = database.query(TABLE_NAME3, CardtalkContract.Rooms.PROJECTION_ALL, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                Log.i("test", "cursor query uri:" + uri);

                return cursor;

            case FRIENDS_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Friends.SORT_ORDER_DEFAULT;
                }
                cursor = database.query(TABLE_NAME4, CardtalkContract.Friends.PROJECTION_ALL, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                Log.i("test", "cursor query uri:" + uri);

                return cursor;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        Uri itemUri;

        // URI 유효성 검사
        switch (URI_MATCHER.match(uri)) {
            case CARD_LIST:
                id = database.insert("Cards", null, values);
                itemUri = ContentUris.withAppendedId(uri, id);
                break;

//                context.getContentResolver().notifyChange(uri, null);
//                Log.i("test", "insert uri:" + uri);
//
//                return itemUri;

            case CHAT_ID:
                id = database.insert("Chats", null, values);
                itemUri = ContentUris.withAppendedId(uri, id);
                break;


//                if (id != -1) {
//                    context.getContentResolver().notifyChange(uri, null);
//                    Log.i("test", "cursor query uri:" + uri);
//                }
//                return itemUri;

            case ROOM_LIST:
                id = database.insert("Rooms", null, values);
                itemUri = ContentUris.withAppendedId(uri, id);
                break;
//                context.getContentResolver().notifyChange(uri, null);
//                Log.i("test", "cursor query uri:" + uri);
//
//                return itemUri;

            case FRIENDS_LIST:
                id = database.insert("Friends", null, values);
                itemUri = ContentUris.withAppendedId(uri, id);
                break;
//
//                context.getContentResolver().notifyChange(uri, null);
//                Log.i("test", "insert uri:" + uri);
//
//                return itemUri;

            default:
                throw new IllegalArgumentException("[Insert]Insertion을 지원하지 않는 URI입니다: " + uri);
        }

        if (id != -1) context.getContentResolver().notifyChange(uri, null);

        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int id = 0;
        Uri itemUri;

        Log.i("test", "update query: " + uri.toString() + selectionArgs.toString());

        // URI 유효성 검사
        switch (URI_MATCHER.match(uri)) {
//            case CARD_LIST:
//                id = database.update("Cards", null, selection, selectionArgs);
//                itemUri = ContentUris.withAppendedId(uri, id);
//                context.getContentResolver().notifyChange(uri, null);
//                Log.i("test", "insert uri:" + uri);
//                break;
            case CARD_ID:
                id = database.update("Cards", values, "_id = "
                        + uri.getLastPathSegment()
                        + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ')' : ""), selectionArgs);
                Log.i("test", "update card_id: " + "Cards " + values + "_id = " + uri.getLastPathSegment() + " AND (" + selection + ')' + selectionArgs.toString());
                break;

            case CHAT_ID:
                id = database.update("Chats", values, "_id = "
                        + uri.getLastPathSegment()
                        + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ')' : ""), selectionArgs);

                Log.i("test", "update chats_id: " + "Chats " + values + "_id = " + uri.getLastPathSegment() + " AND (" + selection + ')' + selectionArgs.toString());
                break;

            case ROOM_LIST:
            case ROOM_ID:
                id = database.update("Rooms", values, "_id = ?"
//                        + uri.getLastPathSegment()
//                        + (!TextUtils.isEmpty(selection) ? " AND ("
//                        + selection + ')' : "")
                        , selectionArgs);

                Log.i("test", "update room: " + id + values + "_id = " + selectionArgs[0]);
                break;

            case FRIENDS_ID:
                id = database.update("Friends", values, "_id = "
                        + uri.getLastPathSegment()
                        + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ')' : ""), selectionArgs);

                Log.i("test", "update friend_id: " + "Friends " + values + "_id = " + uri.getLastPathSegment() + " AND (" + selection + ')' + selectionArgs.toString());
                break;
//            case ROOM_LIST:
//                id = database.insert("Rooms", null, values);
//                itemUri = ContentUris.withAppendedId(uri, id);
//                context.getContentResolver().notifyChange(uri, null);
//                Log.i("test", "cursor query uri:" + uri);
//                break;
//
//            case FRIENDS_LIST:
//                id = database.insert("Friends", null, values);
//                itemUri = ContentUris.withAppendedId(uri, id);
//                context.getContentResolver().notifyChange(uri, null);
//                Log.i("test", "insert uri:" + uri);
//                break;

            default:
                throw new IllegalArgumentException("[Insert]Update를 지원하지 않는 URI입니다: " + uri);
        }

        if (id != -1) context.getContentResolver().notifyChange(uri, null);

        return id;
    }
}