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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class CardAdapter extends ArrayAdapter<ArticleDTO> implements View.OnClickListener {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ArticleDTO> articleData;

    public CardAdapter(Context context, int layoutResourceId, ArrayList<ArticleDTO> articleData) {
        super(context, layoutResourceId, articleData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.articleData = articleData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Drawable d = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvArticleTitle = (TextView) row.findViewById(R.id.tvArticleTitle);
        TextView tvArticleDate = (TextView) row.findViewById(R.id.tvArticleDate);
        TextView tvArticleContent = (TextView) row.findViewById(R.id.tvArticleContent);

        tvArticleTitle.setText(articleData.get(position).getTitle());
        tvArticleDate.setText(parsingDate(articleData.get(position).getCreatetime()));
//        tvArticleDate.setText(articleData.get(position).getCreatetime());
        tvArticleContent.setText(articleData.get(position).getContent());

        ImageView ivArticleIcon = (ImageView) row.findViewById(R.id.ivArticleIcon);
        ImageView ivArticlePhoto = (ImageView) row.findViewById(R.id.ivArticlePhoto);

        String icon = articleData.get(position).getIcon();
        icon = icon.replaceAll("icon/", "");
        String iconPath = context.getFilesDir().getPath() + "/" + icon;
        File iconLoadPath = new File(iconPath);

        if (iconLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            ivArticleIcon.setImageBitmap(bitmap);
        }

        String photo = articleData.get(position).getPhoto();
        photo = photo.replaceAll("data/", "");
        String photoPath = context.getFilesDir().getPath() + "/" + photo;
        File photoLoadPath = new File(photoPath);

        if (photoLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            ivArticlePhoto.setImageBitmap(bitmap);
        }

        Button bt_peek = (Button) row.findViewById(R.id.bt_peek);
        bt_peek.setOnClickListener(this);

        Button bt_enter = (Button) row.findViewById(R.id.bt_enter);
        bt_enter.setOnClickListener(this);

        return row;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_peek:
                Toast.makeText(getContext(), "chatting peek", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_enter:
                Toast.makeText(getContext(), "enter to chatting room", Toast.LENGTH_SHORT).show();
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
