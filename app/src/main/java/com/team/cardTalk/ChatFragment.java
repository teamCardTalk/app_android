package com.team.cardTalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatFragment extends Fragment implements View.OnClickListener {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        Log.i("test", "articleViewFragment-onCreateView");
        view = inflater.inflate(R.layout.fragment_chat_view, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

        _id = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            _id = bundle.getString("_id");
        }

        TextView tvChatTitle = (TextView) view.findViewById(R.id.tvChatTitle);

        Dao dao = new Dao(getActivity());
        CardDTO article = dao.getArticleByArticleId(_id);

        tvChatTitle.setText(article.getTitle());
        tvChatTitle.setTag(_id);
        tvChatTitle.setOnClickListener(this);

        btMember = (Button) view.findViewById(R.id.bt_member);
        btMember.setOnClickListener(this);

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        lvDrawer = (ListView) view.findViewById(R.id.lv_drawer);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        listView();
    }

    private void listView() {
        Dao dao = new Dao(getActivity());
        article = dao.getArticleByArticleId(_id);
        chatListView = (ListView) view.findViewById(R.id.custom_chat_listView);

        chatList = dao.getChatListByArticleId(_id);
        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), R.layout.custom_chat_list, chatList);
        chatListView.setAdapter(chatAdapter);

        btMember.setText(article.getPartynumber() + "");

        Button btSend = (Button) view.findViewById(R.id.btSend);
        btSend.setOnClickListener(this);
    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    private void refreshData() {
        Log.i("test", "refreshChatData");
        String query = "http://125.209.195.202:3000/chat/" + _id;
        Log.i("test", "query: " + query);

        client.get(query, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("test","chat:AsyncHttpClient.get succeeded!");

                String jsonData = new String(bytes);
                Log.i("test", "jsonData: " + jsonData);

                Dao dao = new Dao(getActivity());
                dao.insertJsonChatData(jsonData);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("test", "chat:AsyncHttpClient.get failed!");
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bt_previous:
                getFragmentManager().popBackStack();
                break;

            case R.id.tvChatTitle:
                String _id = v.getTag().toString();
                transactArticleFragment(_id);
                break;

            case R.id.bt_member:
                drawerLayout.openDrawer(lvDrawer);
                break;
        }
    }

    public String parsingDate(String inputDate) {
        try {
            Date date = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z").parse(inputDate);
            return new SimpleDateFormat("MM-dd hh:mm").format(date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
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
