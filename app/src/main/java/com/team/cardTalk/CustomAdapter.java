package com.team.cardTalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class CustomAdapter extends ArrayAdapter<Article>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Article> articleData;

    public CustomAdapter(Context context, int layoutResourceId, ArrayList<Article> articleData) {
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
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvArticleTitle = (TextView) row.findViewById(R.id.tvArticleTitle);
        TextView tvArticleDate = (TextView) row.findViewById(R.id.tvArticleDate);
        TextView tvArticleContent = (TextView) row.findViewById(R.id.tvArticleContent);

        tvArticleTitle.setText(articleData.get(position).getTitle());
        tvArticleDate.setText(articleData.get(position).getCreatetime());
        tvArticleContent.setText(articleData.get(position).getContent());

        ImageView ivArticleIcon = (ImageView) row.findViewById(R.id.ivArticleIcon);
        ImageView ivArticlePhoto = (ImageView) row.findViewById(R.id.ivArticlePhoto);

//        try {
//            InputStream is = context.getAssets().open(articleData.get(position).getIcon());
//            d = Drawable.createFromStream(is, null);
//            ivArticleIcon.setImageDrawable(d);
//
//            is = context.getAssets().open(articleData.get(position).getPhoto());
//            d = Drawable.createFromStream(is, null);
//            ivArticlePhoto.setImageDrawable(d);
//
//        } catch (IOException e) {
//            Log.e("ERROR", "ERROR: " + e);
//        }

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


        return row;
    }
}
