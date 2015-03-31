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
public class CustomAdapterAL extends ArrayAdapter<ArticleListData>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<ArticleListData> articleListData;

    public CustomAdapterAL(Context context, int layoutResourceId, ArrayList<ArticleListData> articleListData) {
        super(context, layoutResourceId, articleListData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.articleListData = articleListData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvArticleTitle = (TextView) row.findViewById(R.id.tvArticleTitle);
        TextView tvArticleDate = (TextView) row.findViewById(R.id.tvArticleDate);
        TextView tvArticleDetail = (TextView) row.findViewById(R.id.tvArticleDetail);

        tvArticleTitle.setText(articleListData.get(position).getTitle());
        tvArticleDate.setText(articleListData.get(position).getDate());
        tvArticleDetail.setText(articleListData.get(position).getDetail());

        ImageView ivArticleIcon = (ImageView) row.findViewById(R.id.ivArticleIcon);

        try {
            InputStream is = context.getAssets().open(articleListData.get(position).getImgName());
            Drawable d = Drawable.createFromStream(is, null);
            ivArticleIcon.setImageDrawable(d);
        } catch (IOException e) {
            Log.e("ERROR", "ERROR: " + e);
        }

        return row;
    }
}
