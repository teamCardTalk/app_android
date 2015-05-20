package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
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
public class ChatAdapter extends CursorAdapter{
    private Context context;
    private int layoutResourceId;
    private Cursor cursor;
    private SharedPreferences pref;
    private LayoutInflater mLayoutInflater;

    public ChatAdapter(Context context, Cursor cursor, int layoutResourceId) {
        super(context, cursor, layoutResourceId);
        this.context = context;
        this.cursor = cursor;
        this.layoutResourceId = layoutResourceId;
        pref = context.getSharedPreferences(
                context.getString(R.string.pref_name), context.MODE_PRIVATE);
        mLayoutInflater = LayoutInflater.from(context);
    }

    static class ViewHolderItem {
        TextView tvChatNickname;
        TextView tvChatContent;
        TextView tvChatTime;
        ImageView ivChatIcon;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = mLayoutInflater.inflate(layoutResourceId, parent, false);
        ViewHolderItem viewHolder = new ViewHolderItem();

        viewHolder.tvChatNickname = (TextView) row.findViewById(R.id.tvChatNickname);
        viewHolder.tvChatContent = (TextView) row.findViewById(R.id.tvChatContent);
        viewHolder.tvChatTime = (TextView) row.findViewById(R.id.tvChatTime);

        viewHolder.ivChatIcon = (ImageView) row.findViewById(R.id.ivChatIcon);

        row.setTag(viewHolder);

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem viewHolder = (ViewHolderItem) view.getTag();
        viewHolder.tvChatNickname.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Chats.NICKNAME)));
        viewHolder.tvChatContent.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Chats.CONTENT)));
        viewHolder.tvChatTime.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Chats.TIME)));
        String icon = cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.ICON));

        icon = icon.replaceAll("icon/", "");
        String iconPath = context.getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            viewHolder.ivChatIcon.setImageBitmap(bitmap);
        }
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        Drawable d = null;
//
//        if (row == null) {
//            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//        }
//
//        TextView tvChatNickname = (TextView) row.findViewById(R.id.tvChatNickname);
//        TextView tvChatContent = (TextView) row.findViewById(R.id.tvChatContent);
//        TextView tvChatTime = (TextView) row.findViewById(R.id.tvChatTime);
//
//        tvChatNickname.setText(chatData.get(position).getNickname());
//        tvChatContent.setText(chatData.get(position).getContent());
//        tvChatTime.setText(chatData.get(position).getTime());
//
//        ImageView ivArticleIcon = (ImageView) row.findViewById(R.id.ivChatIcon);
//
//        String icon = chatData.get(position).getIcon();
//        icon = icon.replaceAll("icon/", "");
//        String iconPath = context.getFilesDir().getPath() + "/" + icon;
//        File iconLoadPath = new File(iconPath);
//
//        if (iconLoadPath.exists()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
//            ivArticleIcon.setImageBitmap(bitmap);
//        }
//
//        return row;
//    }

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
