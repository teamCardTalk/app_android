package com.team.cardTalk;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
    private static final int CARD_LIST = 1;
    private static final int CARD_ID = 2;
    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Cards", CARD_LIST);
        URI_MATCHER.addURI(CardtalkContract.AUTHORITY, "Cards/#", CARD_ID);
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
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME3 + "  (articleid text primary key not null,"
                    + "                                         authorid int not null,"
                    + "                                         icon text not null,"
                    + "                                         title text not null,"
                    + "                                         time text not null,"
                    + "                                         chat text not null)";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE ROOMS TABLE FAILED! - " + e);
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
        switch (URI_MATCHER.match(uri)) {
            case CARD_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Cards.SORT_ORDER_DEFAULT;
                }
                break;
            case CARD_ID:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardtalkContract.Cards.SORT_ORDER_DEFAULT;
                }

                if (selection == null) {
                    selection = "_ID = ?";
                    selectionArgs = new String[] {uri.getLastPathSegment()};
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = database.query(TABLE_NAME1, CardtalkContract.Cards.PROJECTION_ALL, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // URI 유효성 검사
        if (URI_MATCHER.match(uri) != CARD_LIST) {
            throw new IllegalArgumentException("[Insert]Insertion을 지원하지 않는 URI입니다: " + uri);
        }

        else {
            long id = database.insert("Cards", null, values);
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);

            return itemUri;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}