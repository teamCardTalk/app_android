package com.team.cardTalk;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import com.loopj.android.http.PersistentCookieStore;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eunjooim on 15. 5. 22..
 */
public class LoginActivity extends Activity {
    private EditText editId, editPassword;
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    public SharedPreferences sharedpreferences;
    public PersistentCookieStore cookieStore;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);
        editId = (EditText)findViewById(R.id.editId);
        editPassword = (EditText)findViewById(R.id.editPassword);

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE);
        if (sharedpreferences.contains(context.getString(R.string.cookie))) {
            editId.setText(sharedpreferences.getString("name", ""));
            editPassword.setText(sharedpreferences.getString("pass", ""));

            Log.i("test", "cookie: " + sharedpreferences.getString("cookie", ""));
            Intent i = new Intent(this, com.team.cardTalk.HomeView.class);
            startActivity(i);

        }
        super.onResume();
    }

    // http://www.tutorialspoint.com/android/android_session_management.htm
    public void login(View view) throws Exception {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String id = editId.getText().toString();
        String password = editPassword.getText().toString();
        editor.putString(name, id);
        editor.putString(pass, password);
        editor.commit();

        Log.i("test", "login name: " + sharedpreferences.getString("name", ""));
        // http://loopj.com/android-async-http/
        // http://loopj.com/android-async-http/doc/com/loopj/android/http/PersistentCookieStore.html
//        AsyncHttpClient client = new AsyncHttpClient();
//        cookieStore = new PersistentCookieStore(this);
//
//        client.setCookieStore(cookieStore);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userid", id);
//        jsonObject.put("password", password);
//        Log.i("test", "entity: " + jsonObject.toString());
//        StringEntity entity = new StringEntity(jsonObject.toString());
//        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//
//        final Context context = this;
//        client.post(context, "http://125.209.195.202:3000/login", entity, "application/json", new AsyncHttpResponseHandler() {
//            ProgressDialog progressDialog = ProgressDialog.show(context, "", "로그인 중입니다...");
//
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                String bytesTostring = "";
//                for (int j = 0; j < bytes.length; j++)
//                    bytesTostring += (char)bytes[j];
//
//                Log.e("login", "success: " + i + ", " + bytesTostring);
//
//                progressDialog.cancel();
//                Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
//
//                PersistentCookieStoreStock.initSerializableCookie(cookieStore);
//                getFragmentManager().popBackStack();
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.e("login", "fail: " + i);
//                progressDialog.cancel();
//                Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
//            }
//        });

        String prefName = context.getResources().getString(R.string.pref_name);
        SharedPreferences pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);

        try {
            URL url = new URL("http://125.209.195.202:3000/" + "login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userid", id);
            jsonObject.put("password", password);

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();

            int status = conn.getResponseCode();
            Log.i("test", "ProxyResponseCode:" + status);

            int i = 0;
            while (conn.getHeaderField(i) != null) {
                String key = conn.getHeaderFieldKey(i);
                String data = conn.getHeaderField(i);
                if (key != null && key.compareToIgnoreCase("Set-Cookie") == 0) {
                    String cookie = data.substring(0, data.indexOf(";"));
                    cookie.trim();

                    Log.i("test", "data: " + data);
                    Log.i("test", "cookie: " + cookie);

                    editor = sharedpreferences.edit();
                    editor.putString("cookie", cookie);
                    editor.commit();
                    Log.i("test", "pref: " + pref.getString("cookie", ""));
                }
                i++;
            }

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.i("test", "login" + sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "NETWORK ERROR:" + e);
        }

        Intent intent = new Intent(this, com.team.cardTalk.HomeView.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
