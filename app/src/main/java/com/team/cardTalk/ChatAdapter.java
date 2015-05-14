package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class ChatAdapter extends ArrayAdapter<ChatDTO>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<ChatDTO> chatData;

    public ChatAdapter(Context context, int layoutResourceId, ArrayList<ChatDTO> chatData) {
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

        String icon = chatData.get(position).getIcon();
        icon = icon.replaceAll("icon/", "");
        String iconPath = context.getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            ivArticleIcon.setImageBitmap(bitmap);
        }

        return row;
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
