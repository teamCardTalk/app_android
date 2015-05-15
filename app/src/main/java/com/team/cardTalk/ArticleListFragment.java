package com.team.cardTalk;

import android.os.Bundle;
import android.os.StrictMode;
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

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;


public class ArticleListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<CardDTO> articleList;
    private ListView mainListView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        refreshData();
                    }
                }, 100);
            }
        });

        mainListView = (ListView) view.findViewById(R.id.custom_list_listView);

        ImageButton bt_write = (ImageButton) view.findViewById(R.id.bt_write);
        bt_write.setOnClickListener(this);

		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void listView() {
        ProviderDao dao = new ProviderDao(getActivity());
        articleList = dao.getArticleList();

        Log.i("TEST", "articleList.size: " + articleList.size());

        CardAdapter cardAdapter = new CardAdapter(getActivity(), R.layout.custom_article_list, articleList);
        mainListView.setAdapter(cardAdapter);
        mainListView.setOnItemClickListener(this);
        dao.close();
    }


//    private static AsyncHttpClient client = new AsyncHttpClient();

    private void refreshData() {

//        Log.i("test", "refreshData");
//        client.get("http://125.209.195.202:3000/card/all", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("test","AsyncHttpClient.get succeeded!");
//                String jsonData = new String(bytes);
//                Log.i("test", "jsonData: " + jsonData);

        Proxy proxy = new Proxy(getActivity());
        ProviderDao dao = new ProviderDao(getActivity());

        String jsonData = proxy.getJSON();
        dao.insertJsonData(jsonData);

//                ProviderDao dao = new ProviderDao(getActivity());
//                dao.insertJsonData(jsonData);

                listView();
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("test", "AsyncHttpClient.get failed!");
//            }
//        });
        dao.close();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment newFragment = new ArticleFragment();

        // pass data(extras) to a fragment
        Bundle bundle = new Bundle();
        String _id = articleList.get(position).getId();
        bundle.putString("_id", _id);
        newFragment.setArguments(bundle);

        final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();

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

            Fragment newFragment = new WritingArticleFragment();

            // replace fragment
        final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
