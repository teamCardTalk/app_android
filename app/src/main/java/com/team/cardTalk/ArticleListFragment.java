package com.team.cardTalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ArticleListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private ArrayList<Article> articleList;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.custom_list_listView);

        ImageButton bt_write = (ImageButton) view.findViewById(R.id.bt_write);
        bt_write.setOnClickListener(this);

        Dao dao = new Dao(getActivity());
//        String testJsonData = dao.getJsonTestData();
//        dao.insertJsonData(testJsonData);

        articleList = dao.getArticleList();

        CustomAdapter customAdapter= new CustomAdapter(getActivity(), R.layout.custom_article_list, articleList);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);

		return view;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment newFragment = new ArticleViewFragment();

        // pass data(extras) to a fragment
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(articleList.get(position).getId() + ""));
        newFragment.setArguments(bundle);

        final FragmentTransaction transaction = Stock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        Log.i("TEST", position + "번 리스트 선택됨");
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(getActivity(), "Write", Toast.LENGTH_SHORT).show();

        Fragment newFragment = new TwoFragment();

        // replace fragment
        final FragmentTransaction transaction = Stock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
