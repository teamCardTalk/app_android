package com.team.cardTalk;

import android.content.Context;

import com.loopj.android.http.PersistentCookieStore;

/**
 * Created by eunjooim on 15. 5. 24..
 */
//http://www.androidpub.com/2217544

public class PersistentCookieStoreStock {
    public static PersistentCookieStore cookieStore;

    public static void initSerializableCookie(PersistentCookieStore cs) {
        cookieStore = cs;
    }

    public static PersistentCookieStore getCookieStore() {
        return cookieStore;
    }
}
