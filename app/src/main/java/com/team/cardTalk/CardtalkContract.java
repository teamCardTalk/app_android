package com.team.cardTalk;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eunjooim on 15. 5. 15..
 */
public final class CardtalkContract {
    public static final String AUTHORITY = "com.team.cardTalk";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Cards implements BaseColumns {
        public static final String _ID = "_id";
        public static final String STATUS = "status";
        public static final String TITLE = "title";
        public static final String NICKNAME = "nickname";
        public static final String AUTHORID = "authorid";
        public static final String ICON = "icon";
        public static final String CREATETIME = "createtime";
        public static final String CONTENT = "content";
        public static final String PARTYNUMBER = "partynumber";
//        public static final String CHATTINGTIME = "chattingtime";
//        public static final String CHATTING = "chatting";
        public static final String PHOTO = "photo";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                CardtalkContract.CONTENT_URI, Cards.class.getSimpleName()
        );

        public static final String[] PROJECTION_ALL = {
                _ID, STATUS, TITLE, NICKNAME, AUTHORID, ICON, CREATETIME, CONTENT, PARTYNUMBER, PHOTO
//                _ID, STATUS, TITLE, NICKNAME, AUTHORID, ICON, CREATETIME, CONTENT, PARTYNUMBER, CHATTINGTIME, CHATTING, PHOTO
        };

        public static final String SORT_ORDER_DEFAULT = CREATETIME + " DESC";
    }

    public static final class Chats implements BaseColumns {
        public static final String _ID= "_id";
        public static final String ARTICLEID = "articleid";
        public static final String NICKNAME = "nickname";
        public static final String USERID = "userid";
        public static final String ICON = "icon";
        public static final String CONTENT = "content";
        public static final String TIME = "time";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                CardtalkContract.CONTENT_URI, Chats.class.getSimpleName()
        );

        public static final String[] PROJECTION_ALL = {
                _ID, ARTICLEID, NICKNAME, USERID, ICON, CONTENT, TIME
        };

        public static final String SORT_ORDER_DEFAULT = TIME + " ASC";
    }
}
