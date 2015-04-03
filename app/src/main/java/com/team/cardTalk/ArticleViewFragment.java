package com.team.cardTalk;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ArticleViewFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Drawable d = null;
        View view = inflater.inflate(R.layout.fragment_article_view, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

        int id = 0;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
        }

        Dao dao = new Dao(getActivity());

        Article article = dao.getArticleByArticleNumber(id);

        ImageView ivArticleDetailIcon = null;
        ImageView ivArticleDetailPhoto = null;


        TextView tvArticleDetailTitle = (TextView) view.findViewById(R.id.tvArticleDetailTitle);
        TextView tvArticleDetailDate = (TextView) view.findViewById(R.id.tvArticleDetailDate);
        TextView tvArticleDetailContent = (TextView) view.findViewById(R.id.tvArticleDetailContent);

        tvArticleDetailTitle.setText(article.getTitle());
        tvArticleDetailDate.setText(article.getCreatetime());
        tvArticleDetailContent.setText(article.getContent());

        ivArticleDetailIcon = (ImageView) view.findViewById(R.id.ivArticleDetailIcon);
        ivArticleDetailPhoto = (ImageView) view.findViewById(R.id.ivArticleDetailPhoto);

        try {
            InputStream is = getActivity().getAssets().open(article.getIcon());
            d = Drawable.createFromStream(is, null);
            ivArticleDetailIcon.setImageDrawable(d);

            is = getActivity().getAssets().open(article.getPhoto());
            d = Drawable.createFromStream(is, null);
            ivArticleDetailPhoto.setImageDrawable(d);

        } catch (IOException e) {
            Log.e("ERROR", "ERROR: " + e);
        }



        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bt_previous:
                getFragmentManager().popBackStack();
                break;

        }
    }
}
