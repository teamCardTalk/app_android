package com.team.cardTalk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Garam on 2015-05-14.
 */
public class SyncDataService extends Service {

    private TimerTask mTask;
    private Timer mTimer;
    private Proxy proxy;
    private ProviderDao dao;

    @Override
    public void onCreate() {
        super.onCreate();
        proxy = new Proxy(getApplicationContext());
        dao = new ProviderDao(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTask = new TimerTask() {
            @Override
            public void run() {
                String jsonData = proxy.getJSON();
                dao.insertJsonData(jsonData);
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 1000 * 10, 1000 * 10);

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
