package com.team.cardTalk;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ArticleWriteFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Drawable d = null;
        View view = inflater.inflate(R.layout.fragment_article_write, container, false);

        ImageButton bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_previous.setOnClickListener(this);

//        int id = 0;
//
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            id = bundle.getInt("id");
//        }

        ImageView ivArticleWriteIcon = (ImageView) view.findViewById(R.id.ivArticleWriteIcon);

        try {
            InputStream is = getActivity().getAssets().open("icon1.png");
            d = Drawable.createFromStream(is, null);
            ivArticleWriteIcon.setImageDrawable(d);

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