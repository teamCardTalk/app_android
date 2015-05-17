package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class CardAdapter extends CursorAdapter implements View.OnClickListener {
    private Context context;
    private int layoutResourceId;
    //    private ArrayList<CardDTO> articleData;
    private Cursor cursor;
    private SharedPreferences pref;
    private LayoutInflater mLayoutInflater;

    public CardAdapter(Context context, Cursor cursor, int layoutResourceId) {
        super(context, cursor, layoutResourceId);
        this.context = context;
        this.cursor = cursor;
        this.layoutResourceId = layoutResourceId;
        pref = context.getSharedPreferences(
                context.getString(R.string.pref_name), context.MODE_PRIVATE);
        mLayoutInflater = LayoutInflater.from(context);
    }

    static class ViewHolderItem {
        TextView tvArticleTitle;
        TextView tvArticleDate;
        TextView tvArticleContent;
        ImageView ivArticleIcon;
        ImageView ivArticlePhoto;
        Button bt_peek;
        Button bt_enter;
        String _id;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = mLayoutInflater.inflate(layoutResourceId, parent, false);
        ViewHolderItem viewHolder = new ViewHolderItem();

        viewHolder.tvArticleTitle = (TextView) row.findViewById(R.id.tvArticleTitle);
        viewHolder.tvArticleDate = (TextView) row.findViewById(R.id.tvArticleDate);
        viewHolder.tvArticleContent = (TextView) row.findViewById(R.id.tvArticleContent);

        viewHolder.ivArticleIcon = (ImageView) row.findViewById(R.id.ivArticleIcon);
        viewHolder.ivArticlePhoto = (ImageView) row.findViewById(R.id.ivArticlePhoto);

        viewHolder.bt_peek = (Button) row.findViewById(R.id.bt_peek);
        viewHolder.bt_enter = (Button) row.findViewById(R.id.bt_enter);

        row.setTag(viewHolder);

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem viewHolder = (ViewHolderItem) view.getTag();
        viewHolder.tvArticleTitle.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.TITLE)));
        viewHolder.tvArticleDate.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.CREATETIME)));
        viewHolder.tvArticleContent.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.CONTENT)));
        String icon = cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.ICON));

        icon = icon.replaceAll("icon/", "");
        String iconPath = context.getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            viewHolder.ivArticleIcon.setImageBitmap(bitmap);
        }

        String photo = cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.PHOTO));

        photo = photo.replaceAll("data/", "");
        String photoPath = context.getFilesDir().getPath() + "/" + photo;
        File photoLoadPath = new File(photoPath);

        if (photoLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            viewHolder.ivArticlePhoto.setImageBitmap(bitmap);
        }

        viewHolder.bt_peek.setText("엿보기 (" + cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards.PARTYNUMBER)) + "명 참여 중)");
        viewHolder.bt_peek.setOnClickListener(this);

        String _id = cursor.getString(cursor.getColumnIndex(CardtalkContract.Cards._ID));
        viewHolder._id = _id;
        viewHolder.bt_enter.setTag(_id);
        viewHolder.bt_enter.setOnClickListener(this);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View row = convertView;
//
//        if (row == null) {
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//        }
//
//        TextView tvArticleTitle = (TextView) row.findViewById(R.id.tvArticleTitle);
//        TextView tvArticleDate = (TextView) row.findViewById(R.id.tvArticleDate);
//        TextView tvArticleContent = (TextView) row.findViewById(R.id.tvArticleContent);
//
//        tvArticleTitle.setText(articleData.get(position).getTitle());
//        tvArticleDate.setText(articleData.get(position).getCreatetime());
//        tvArticleContent.setText(articleData.get(position).getContent());
//
//        ImageView ivArticleIcon = (ImageView) row.findViewById(R.id.ivArticleIcon);
//        ImageView ivArticlePhoto = (ImageView) row.findViewById(R.id.ivArticlePhoto);
//
//        String icon = articleData.get(position).getIcon();
//        icon = icon.replaceAll("icon/", "");
//        String iconPath = context.getFilesDir().getPath() + "/" + icon;
//        File iconLoadPath = new File(iconPath);
//
//        if (iconLoadPath.exists()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
//            ivArticleIcon.setImageBitmap(bitmap);
//        }
//
//        String photo = articleData.get(position).getPhoto();
//        photo = photo.replaceAll("data/", "");
//        String photoPath = context.getFilesDir().getPath() + "/" + photo;
//        File photoLoadPath = new File(photoPath);
//
//        if (photoLoadPath.exists()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
//            ivArticlePhoto.setImageBitmap(bitmap);
//        }
//
//        Button bt_peek = (Button) row.findViewById(R.id.bt_peek);
//        bt_peek.setText("엿보기 (" + articleData.get(position).getPartynumber() + "명 참여 중)");
//        bt_peek.setOnClickListener(this);
//
//        Button bt_enter = (Button) row.findViewById(R.id.bt_enter);
//        bt_enter.setTag(articleData.get(position).getId());
//        bt_enter.setOnClickListener(this);
//
//        return row;
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_peek:
                Toast.makeText(context, "chatting peek", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_enter:
                String _id = v.getTag().toString();
                transactChatFragment(_id);
                break;
        }
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

    public void transactChatFragment(String _id) {
        Fragment newFragment = new ChatFragment();

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
