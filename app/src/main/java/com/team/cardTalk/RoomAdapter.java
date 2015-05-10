package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by eunjooim on 15. 5. 10..
 */
public class RoomAdapter extends ArrayAdapter<RoomDTO> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<RoomDTO> roomData;

    public RoomAdapter(Context context, int layoutResourceId, ArrayList<RoomDTO> roomData) {
        super(context, layoutResourceId, roomData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.roomData = roomData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvRoomTitle = (TextView) row.findViewById(R.id.tvRoomTitle);
        TextView tvRoomTime = (TextView) row.findViewById(R.id.tvRoomTime);
        TextView tvRoomChat = (TextView) row.findViewById(R.id.tvRoomChat);

        tvRoomTitle.setText(roomData.get(position).getTitle());
        tvRoomTime.setText(roomData.get(position).getTime());
        tvRoomChat.setText(roomData.get(position).getChat());

        ImageView ivRoomIcon = (ImageView) row.findViewById(R.id.ivRoomIcon);

        String icon = roomData.get(position).getIcon();
        icon = icon.replaceAll("icon/", "");
        String iconPath = context.getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            ivRoomIcon.setImageBitmap(bitmap);
        }

        return row;
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

//    public void transactChatFragment(String _id) {
//        Fragment newFragment = new ChatFragment();
//
//        // pass data(extras) to a fragment
//        Bundle bundle = new Bundle();
//        bundle.putString("_id", _id);
//        newFragment.setArguments(bundle);
//
//        final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();
//
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
//        transaction.replace(R.id.ll_fragment, newFragment);
//        transaction.addToBackStack(null);
//
//        // Commit the transaction
//        transaction.commit();
//
//        Log.i("TEST", "_id : <" + _id + "> 채팅방 detail");
//    }

}
