package com.team.cardTalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomFragment extends Fragment {

    private ArrayList<RoomDTO> roomList;
    private ListView roomListView;
    private ArticleDTO room;
    private String articleid;
    private LayoutInflater inflater;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        Log.i("test", "roomViewFragment-onCreateView");
        view = inflater.inflate(R.layout.fragment_room_view, container, false);

        articleid = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            articleid = bundle.getString("articleid");
        }

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
        room = dao.getArticleByArticleId(articleid);
        roomListView = (ListView) view.findViewById(R.id.custom_room_listView);

        roomList = dao.getChatListByArticleId(articleid);
        RoomAdapter roomAdapter = new RoomAdapter(getActivity(), R.layout.custom_room_list, roomList);
        roomListView.setAdapter(roomAdapter);
    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    private void refreshData() {
        Log.i("test", "refreshChatData");
        String query = "http://125.209.195.202:3000/chat/" + articleid;
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

    public String parsingDate(String inputDate) {
        try {
            Date date = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z").parse(inputDate);
            return new SimpleDateFormat("MM-dd hh:mm").format(date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }
}
