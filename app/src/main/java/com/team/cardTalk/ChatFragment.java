package com.team.cardTalk;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ChatFragment extends Fragment implements View.OnClickListener {

    private ListView chatListView;
    private CardDTO article;
    private String _id;
    private LayoutInflater inflater;
    private View view;
    private Button btMember;
    private DrawerLayout drawerLayout;
    private ListView lvDrawer;
    private Proxy proxy;
    private ProviderDao dao;
    private Cursor cursor;
    private ContentObserver myObserver;
    private EditText editChat;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _id = null;
        this.inflater = inflater;
        Log.i("test", "articleViewFragment-onCreateView");
        view = inflater.inflate(R.layout.fragment_chat_view, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            _id = bundle.getString("_id");
        }

        TextView tvChatTitle = (TextView) view.findViewById(R.id.tvChatTitle);

        editChat = (EditText) view.findViewById(R.id.editChat);

        dao = new ProviderDao(getActivity());
        CardDTO article = dao.getArticleByArticleId(_id);

        tvChatTitle.setText(article.getTitle());
        tvChatTitle.setTag(_id);
        tvChatTitle.setOnClickListener(this);

        btMember = (Button) view.findViewById(R.id.bt_member);
        btMember.setOnClickListener(this);

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        lvDrawer = (ListView) view.findViewById(R.id.lv_drawer);

//        ContentResolver contentResolver = getActivity().getContentResolver();
//        myObserver = new ContentObserver(new Handler()) {
//            @Override
//            public void onChange(boolean selfChange) {
//                super.onChange(selfChange);
//            }
//        };
//
//        contentResolver.registerContentObserver(URI, true, myObserver);

        proxy = new Proxy(getActivity());
        dao = new ProviderDao(getActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        listView();
    }

    private void listView() {

        article = dao.getArticleByArticleId(_id);
        chatListView = (ListView) view.findViewById(R.id.custom_chat_listView);
        cursor = dao.getChatListByArticleId(_id);

        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), cursor, R.layout.custom_chat_list);
        chatListView.setAdapter(chatAdapter);

        btMember.setText(article.getPartynumber() + "");

        Button btSend = (Button) view.findViewById(R.id.btSend);
        btSend.setOnClickListener(this);
    }

    private void refreshData() {
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {

                String jsonData = proxy.getChatJSON(_id);
                dao.insertJsonChatData(jsonData);
            }
        };

        Timer mTimer = new Timer();
        mTimer.schedule(mTask, 1000 * 10, 1000 * 10);
//        Proxy proxy = new Proxy(getActivity());
//        ProviderDao dao = new ProviderDao(getActivity());
//        String jsonData = proxy.getChatJSON(_id);
//        dao.insertJsonChatData(jsonData);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bt_previous:
                getFragmentManager().popBackStack();
                break;

            case R.id.tvChatTitle:
                transactArticleFragment(_id);
                break;

            case R.id.bt_member:
                drawerLayout.openDrawer(lvDrawer);
                break;

            case R.id.btSend:
                ChatWritingProxy proxy = new ChatWritingProxy(getActivity());
                EditText editChat = (EditText) view.findViewById(R.id.editChat);
                try {
                    proxy.joinRoom(_id);
                    String content = editChat.getText().toString();
                    proxy.uploadChat(_id, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                editChat.setText("", null);
                listView();

                break;

//                progressDialog = ProgressDialog.show(getActivity(), "", "업로드중입니다...");
//
//                String ID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
//                String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).format(new Date());
//
//                ChatDTO chat = new ChatDTO(
//                        "temp",
//                        _id,
//                        "노란 조커",
//                        "user02",
//                        "icon/icon2.png",
//                        editChat.getText().toString(),
//                        "temp"
//                );
//
//                ChatWritingProxy proxy = new ChatWritingProxy(getActivity());
//
//                proxy.joinRoom(_id);
//
//                proxy.uploadArticle(chat,
//                        new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                                Log.e("uploadChat", "success: " + i);
//                                progressDialog.cancel();
//                                Toast.makeText(getActivity(), "onSuccess", Toast.LENGTH_SHORT).show();
//                                getFragmentManager().popBackStack();
//                            }
//
//                            @Override
//                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                Log.e("uploadChat", "fail: " + i);
//                                progressDialog.cancel();
//                                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                break;
        }
    }

    public void transactArticleFragment(String _id) {
        Fragment newFragment = new ArticleFragment();

        // pass data(extras) to a fragment
        Bundle bundle = new Bundle();
        bundle.putString("_id", _id);
        newFragment.setArguments(bundle);

        final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        Log.i("TEST", "_id : <" + _id + "> 채팅방 입장");
    }
}
