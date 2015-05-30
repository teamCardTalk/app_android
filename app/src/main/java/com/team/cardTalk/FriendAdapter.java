package com.team.cardTalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class FriendAdapter extends CursorAdapter{
    private Context context;
    private int layoutResourceId;
    private Cursor cursor;
    private SharedPreferences pref;
    private LayoutInflater mLayoutInflater;

    public FriendAdapter(Context context, Cursor cursor, int layoutResourceId) {
        super(context, cursor, layoutResourceId);
        this.context = context;
        this.cursor = cursor;
        this.layoutResourceId = layoutResourceId;
        pref = context.getSharedPreferences(
                context.getString(R.string.pref_name), context.MODE_PRIVATE);
        mLayoutInflater = LayoutInflater.from(context);
    }

    static class ViewHolderItem {
        TextView tvFriendNickname;
        ImageView ivFriendIcon;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = mLayoutInflater.inflate(layoutResourceId, parent, false);
        ViewHolderItem viewHolder = new ViewHolderItem();

        viewHolder.tvFriendNickname = (TextView) row.findViewById(R.id.tvFriendNickname);
        viewHolder.ivFriendIcon = (ImageView) row.findViewById(R.id.ivFriendIcon);

        row.setTag(viewHolder);

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem viewHolder = (ViewHolderItem) view.getTag();
        viewHolder.tvFriendNickname.setText(cursor.getString(cursor.getColumnIndex(CardtalkContract.Friends.NICKNAME)));
        String icon = cursor.getString(cursor.getColumnIndex(CardtalkContract.Friends.ICON));

        icon = icon.replaceAll("icon/", "");
        String iconPath = context.getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            viewHolder.ivFriendIcon.setImageBitmap(bitmap);
        }
    }
}
