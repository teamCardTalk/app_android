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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.http.Header;

import java.util.ArrayList;


public class ArticleListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<Article> articleList;
    private ListView mainListView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 100);
            }
        });

        mainListView = (ListView) view.findViewById(R.id.custom_list_listView);

        ImageButton bt_write = (ImageButton) view.findViewById(R.id.bt_write);
        bt_write.setOnClickListener(this);

//        Dao dao = new Dao(getActivity());
//        String testJsonData = dao.getJsonTestData();
//        dao.insertJsonData(testJsonData);
//        articleList = dao.getArticleList();
//        CustomAdapter customAdapter= new CustomAdapter(getActivity(), R.layout.custom_article_list, articleList);
//        mainListView.setAdapter(customAdapter);
//        mainListView.setOnItemClickListener(this);

        refreshData();
//        listView();

		return view;
	}

    // network 4/16
    private void listView() {
        Dao dao = new Dao(getActivity());
        articleList = dao.getArticleList();

        CustomAdapter customAdapter= new CustomAdapter(getActivity(), R.layout.custom_article_list, articleList);
        mainListView.setAdapter(customAdapter);
        mainListView.setOnItemClickListener(this);

    }

    // network 4/16
    private static AsyncHttpClient client = new AsyncHttpClient();

    private void refreshData() {

        Log.i("test", "refreshData");
        client.get("http://125.209.195.202:3000/card/all", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("test","AsyncHttpClient.get succeeded!");
//                Proxy proxy = new Proxy();
//                String jsonData = proxy.getJSON();
                String jsonData = new String(bytes);
                Log.i("test", "jsonData: " + jsonData);

                Dao dao = new Dao(getActivity());
                dao.insertJsonData(jsonData);

                listView();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("test", "AsyncHttpClient.get failed!");
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment newFragment = new ArticleViewFragment();

        // pass data(extras) to a fragment
        Bundle bundle = new Bundle();
        String _id = articleList.get(position).getId();
        bundle.putString("_id", _id);
        newFragment.setArguments(bundle);

        final FragmentTransaction transaction = Stock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        Log.i("TEST", "_id : <" + _id + "> 선택됨");
    }

        @Override
        public void onClick(View v) {

            Toast.makeText(getActivity(), "Write", Toast.LENGTH_SHORT).show();

            Fragment newFragment = new ArticleWriteFragment();

            // replace fragment
        final FragmentTransaction transaction = Stock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
