package com.team.cardTalk;


import android.app.Activity;
import android.database.Cursor;
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
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ArticleViewFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Chat> chatList;
    private ListView chatListView;
    private Article article;
    private View articleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("test", "articleViewFragment-onCreateView");
        View view = inflater.inflate(R.layout.fragment_article_view, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

        String _id = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            _id = bundle.getString("_id");
        }

        Dao dao = new Dao(getActivity());
        refreshData(_id);

        article = dao.getArticleByArticleId(_id);

        chatListView = (ListView) view.findViewById(R.id.custom_chat_listView);
//        String chatJsonData = dao.getJsonChatData();
//        dao.insertJsonChatData(chatJsonData);

        articleView = inflater.inflate(R.layout.fragment_article_detail, chatListView, false);
        Drawable d = null;
        dao = new Dao(getActivity());

        chatList = dao.getChatListByArticleId(_id);

        CustomChatAdapter customChatAdapter= new CustomChatAdapter(getActivity(), R.layout.custom_chat_list, chatList);
        chatListView.setAdapter(customChatAdapter);


        TextView tvArticleDetailTitle = (TextView) articleView.findViewById(R.id.tvArticleDetailTitle);
        TextView tvArticleDetailDate = (TextView) articleView.findViewById(R.id.tvArticleDetailDate);
        TextView tvArticleDetailContent = (TextView) articleView.findViewById(R.id.tvArticleDetailContent);

        tvArticleDetailTitle.setText(article.getTitle());
        tvArticleDetailDate.setText(article.getCreatetime());
        tvArticleDetailContent.setText(article.getContent());

        ImageView ivArticleDetailIcon = (ImageView) articleView.findViewById(R.id.ivArticleDetailIcon);
        ImageView ivArticleDetailPhoto = (ImageView) articleView.findViewById(R.id.ivArticleDetailPhoto);

//        try {
//            InputStream is = getActivity().getAssets().open(article.getIcon());
//            d = Drawable.createFromStream(is, null);
//            ivArticleDetailIcon.setImageDrawable(d);
//
//            is = getActivity().getAssets().open(article.getPhoto());
//            d = Drawable.createFromStream(is, null);
//            ivArticleDetailPhoto.setImageDrawable(d);
//
//        } catch (IOException e) {
//            Log.e("ERROR", "ERROR: " + e);
//        }
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

        return view;
    }

//    private void listView(String _id) {
//        Drawable d = null;
//        Dao dao = new Dao(getActivity());
//
//        chatList = dao.getChatListByArticleId(_id);
//
//        CustomChatAdapter customChatAdapter= new CustomChatAdapter(getActivity(), R.layout.custom_chat_list, chatList);
//        chatListView.setAdapter(customChatAdapter);
//
//
//        TextView tvArticleDetailTitle = (TextView) articleView.findViewById(R.id.tvArticleDetailTitle);
//        TextView tvArticleDetailDate = (TextView) articleView.findViewById(R.id.tvArticleDetailDate);
//        TextView tvArticleDetailContent = (TextView) articleView.findViewById(R.id.tvArticleDetailContent);
//
//        tvArticleDetailTitle.setText(article.getTitle());
//        tvArticleDetailDate.setText(article.getCreatetime());
//        tvArticleDetailContent.setText(article.getContent());
//
//        ImageView ivArticleDetailIcon = (ImageView) articleView.findViewById(R.id.ivArticleDetailIcon);
//        ImageView ivArticleDetailPhoto = (ImageView) articleView.findViewById(R.id.ivArticleDetailPhoto);
//
//        try {
//            InputStream is = getActivity().getAssets().open(article.getIcon());
//            d = Drawable.createFromStream(is, null);
//            ivArticleDetailIcon.setImageDrawable(d);
//
//            is = getActivity().getAssets().open(article.getPhoto());
//            d = Drawable.createFromStream(is, null);
//            ivArticleDetailPhoto.setImageDrawable(d);
//
//        } catch (IOException e) {
//            Log.e("ERROR", "ERROR: " + e);
//        }
//
//        chatListView.addHeaderView(articleView);
//
//    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    private void refreshData(final String _id) {
        Log.i("test", "refreshData");
        String query = "http://125.209.195.202:3000/chat/" + "_id=" + _id;
        client.get(query, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("test","chat:AsyncHttpClient.get succeeded!");

                String jsonData = new String(bytes);
                Log.i("test", "jsonData: " + jsonData);

                Dao dao = new Dao(getActivity());
                dao.insertJsonChatData(jsonData);

//                listView(_id);
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
}
