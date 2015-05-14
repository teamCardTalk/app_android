package com.team.cardTalk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Garam on 2015-05-14.
 */
public class SyncDataService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("test", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("test", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("test", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
