package com.team.cardTalk;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ArticleFragment extends Fragment implements View.OnClickListener {
    private Cursor cursor;
    private ArrayList<ChatDTO> chatList;
    private ListView chatListView;
    private CardDTO article;
    private View articleView;
    private String _id;
    private LayoutInflater inflater;
    private View view;
    private Button btMember;
    private DrawerLayout drawerLayout;
    private ListView lvDrawer;
    private ProviderDao dao;
    private Proxy proxy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dao = new ProviderDao(getActivity());

        this.inflater = inflater;
        Log.i("test", "articleViewFragment-onCreateView");
        view = inflater.inflate(R.layout.fragment_article_view, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

        btMember = (Button) view.findViewById(R.id.bt_member);
        btMember.setOnClickListener(this);

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        lvDrawer = (ListView) view.findViewById(R.id.lv_drawer);

        Button btSend = (Button) view.findViewById(R.id.btSend);
        btSend.setOnClickListener(this);

        _id = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            _id = bundle.getString("_id");
        }

        registerObserver("Chats");

        proxy = new Proxy(getActivity());
        dao = new ProviderDao(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listView();
        listChatView();
        userListView();
    }

    private void listView() {

        article = dao.getArticleByArticleId(_id);
        chatListView = (ListView) view.findViewById(R.id.custom_chat_listView);
        articleView = inflater.inflate(R.layout.fragment_article_detail, chatListView, false);

        cursor = dao.getChatListByArticleId(_id);

        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), cursor, R.layout.custom_chat_list);
        chatListView.setAdapter(chatAdapter);

        TextView tvArticleDetailTitle = (TextView) articleView.findViewById(R.id.tvArticleDetailTitle);
        TextView tvArticleDetailDate = (TextView) articleView.findViewById(R.id.tvArticleDetailDate);
        TextView tvArticleDetailContent = (TextView) articleView.findViewById(R.id.tvArticleDetailContent);

        tvArticleDetailTitle.setText(article.getTitle());
        tvArticleDetailDate.setText(article.getCreatetime());
        tvArticleDetailContent.setText(article.getContent());

        ImageView ivArticleDetailIcon = (ImageView) articleView.findViewById(R.id.ivArticleDetailIcon);
        ImageView ivArticleDetailPhoto = (ImageView) articleView.findViewById(R.id.ivArticleDetailPhoto);

        String icon = article.getIcon();
        icon = icon.replaceAll("icon/", "");
        String iconPath = getActivity().getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            ivArticleDetailIcon.setImageBitmap(bitmap);
        }

        String photo = article.getPhoto();
        photo = photo.replaceAll("data/", "");
        String photoPath = getActivity().getFilesDir().getPath() + "/" + photo;
        File photoLoadPath = new File(photoPath);

        if (photoLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            ivArticleDetailPhoto.setImageBitmap(bitmap);
        }

        chatListView.addHeaderView(articleView);
        btMember.setText(article.getPartynumber() + "");
    }

    private void listChatView() {
        article = dao.getArticleByArticleId(_id);
        chatListView = (ListView) view.findViewById(R.id.custom_chat_listView);
        articleView = inflater.inflate(R.layout.fragment_article_detail, chatListView, false);

        cursor = dao.getChatListByArticleId(_id);

        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), cursor, R.layout.custom_chat_list);
        chatListView.setAdapter(chatAdapter);

    }

    private void userListView() {

        Log.i("userListView", "started");
        String userJsonData = proxy.getParticipantJSON(_id);
        Log.i("userListView", "userJasonData: " + userJsonData);
        ArrayList<String> userList = dao.splitJsonUserListDataToArrayList(userJsonData);
        ArrayList<UserDTO> userDataList = new ArrayList<>();
        String userData;

        for (String s : userList) {
            userData = proxy.getUserDetailJSON(s);
            Log.i("userListView", "userData: " + userData);
            userDataList.add(dao.convertJsonUserData(userData));
        }

        UserAdapter userAdapter = new UserAdapter(getActivity(), R.layout.custom_user_list, userDataList);
        lvDrawer.setAdapter(userAdapter);
    }

    private void refreshData() {

        String jsonData = proxy.getChatJSON(_id);
        dao.insertJsonChatListData(jsonData);

//        Log.i("test", "refreshChatData");
//        String query = "http://125.209.195.202:3000/chat/" + _id;
//        Log.i("test", "query: " + query);
//
//        client.get(query, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("test","chat:AsyncHttpClient.get succeeded!");
//
//                String jsonData = new String(bytes);
//                Log.i("test", "jsonData: " + jsonData);
//
//                ProviderDao dao = new ProviderDao(getActivity());
//                dao.insertJsonChatData(jsonData);
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("test", "chat:AsyncHttpClient.get failed!");
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        EditText editChat = (EditText) view.findViewById(R.id.editChat);

        switch(v.getId()){

            case R.id.bt_previous:
                hideKeyboard(editChat);
                getFragmentManager().popBackStack();
                break;

            case R.id.bt_member:
                hideKeyboard(editChat);
                drawerLayout.openDrawer(lvDrawer);
                break;

            case R.id.btSend:
                ChatWritingProxy proxy = new ChatWritingProxy(getActivity());
                editChat = (EditText) view.findViewById(R.id.editChat);
                try {
                    proxy.joinRoom(_id);
                    String content = editChat.getText().toString();
                    proxy.uploadChat(_id, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                hideKeyboard(editChat);
                editChat.setText("", null);
                refreshData();
                listChatView();

                break;
        }
    }

    public void hideKeyboard(EditText target) {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(target.getApplicationWindowToken(), 0);
    }

    public void registerObserver(String tableName) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentObserver myObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);

                Log.i("test", "Observer onChange");
                refreshData();
                listChatView();
            }
        };

        contentResolver.registerContentObserver(Uri.withAppendedPath(
                CardtalkContract.CONTENT_URI, tableName), true, myObserver);
    }
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
