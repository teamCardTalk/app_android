package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class CustomChatAdapter extends ArrayAdapter<Chat>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Chat> chatData;

    public CustomChatAdapter(Context context, int layoutResourceId, ArrayList<Chat> chatData) {
        super(context, layoutResourceId, chatData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.chatData = chatData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Drawable d = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvChatNickname = (TextView) row.findViewById(R.id.tvChatNickname);
        TextView tvChatContent = (TextView) row.findViewById(R.id.tvChatContent);
        TextView tvChatTime = (TextView) row.findViewById(R.id.tvChatTime);

        tvChatNickname.setText(chatData.get(position).getNickname());
        tvChatContent.setText(chatData.get(position).getContent());
        tvChatTime.setText(chatData.get(position).getTime());

        ImageView ivArticleIcon = (ImageView) row.findViewById(R.id.ivChatIcon);

        try {
            InputStream is = context.getAssets().open(chatData.get(position).getIcon());
            d = Drawable.createFromStream(is, null);
            ivArticleIcon.setImageDrawable(d);

        } catch (IOException e) {
            Log.e("ERROR", "ERROR: " + e);
        }

        return row;
    }
}
