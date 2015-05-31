package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 5. 31..
 */
public class UserAdapter extends ArrayAdapter<UserDTO> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<UserDTO> userData;


    public UserAdapter(Context context, int layoutResourceId, ArrayList<UserDTO> userData) {
            super(context, layoutResourceId, userData);
            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.userData = userData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
            }

            TextView tvUserNickname = (TextView) row.findViewById(R.id.tvUserNickname);

            tvUserNickname.setText(userData.get(position).getNickname());

            ImageView ivUserIcon = (ImageView) row.findViewById(R.id.ivUserIcon);

            String icon = userData.get(position).getIcon();
            icon = icon.replaceAll("icon/", "");
            String iconPath = context.getFilesDir().getPath() + "/" + icon;
            File iconLoadPath = new File(iconPath);

            if (iconLoadPath.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
                ivUserIcon.setImageBitmap(bitmap);
            }

            return row;
        }

    public void remove(int position) {
        userData.remove(userData.get(position));
    }

    public UserDTO getItem(int position) {
        return userData.get(position);
    }
}

