package com.team.cardTalk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleFragment extends Fragment implements View.OnClickListener {

    private ArrayList<ChatDTO> chatList;
    private ListView chatListView;
    private ArticleDTO article;
    private View articleView;
    private String _id;
    private LayoutInflater inflater;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        Log.i("test", "articleViewFragment-onCreateView");
        view = inflater.inflate(R.layout.fragment_article_view, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

        _id = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            _id = bundle.getString("_id");
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
        article = dao.getArticleByArticleId(_id);
        chatListView = (ListView) view.findViewById(R.id.custom_chat_listView);
        articleView = inflater.inflate(R.layout.fragment_article_detail, chatListView, false);
        Drawable d = null;
        chatList = dao.getChatListByArticleId(_id);
        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), R.layout.custom_chat_list, chatList);
        chatListView.setAdapter(chatAdapter);

        TextView tvArticleDetailTitle = (TextView) articleView.findViewById(R.id.tvArticleDetailTitle);
        TextView tvArticleDetailDate = (TextView) articleView.findViewById(R.id.tvArticleDetailDate);
        TextView tvArticleDetailContent = (TextView) articleView.findViewById(R.id.tvArticleDetailContent);

        tvArticleDetailTitle.setText(article.getTitle());
        tvArticleDetailDate.setText(parsingDate(article.getCreatetime()));
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
}
