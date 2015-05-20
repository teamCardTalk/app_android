package com.team.cardTalk;

import android.database.Cursor;
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

import com.yalantis.phoenix.PullToRefreshView;


public class ArticleListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
//    private ArrayList<CardDTO> articleList;
    private ProviderDao dao;
    private ListView mainListView;
    private Cursor cursor;

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
        cursor = dao.getArticleList();

        CardAdapter cardAdapter = new CardAdapter(getActivity(), cursor, R.layout.custom_article_list);
        mainListView.setAdapter(cardAdapter);
        mainListView.setOnItemClickListener(this);
    }

    private void refreshData() {
        Proxy proxy = new Proxy(getActivity());
        dao = new ProviderDao(getActivity());
        String jsonData = proxy.getJSON();
        dao.insertJsonData(jsonData);

        listView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment newFragment = new ArticleFragment();

        // pass data(extras) to a fragment
        Bundle bundle = new Bundle();
        String _id = (((CardAdapter.ViewHolderItem)view.getTag())._id);
//        String _id = articleList.get(position).getId();
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
