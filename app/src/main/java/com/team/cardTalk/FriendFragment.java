package com.team.cardTalk;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendFragment extends Fragment implements OnClickListener{
	private ProviderDao dao;
	private ListView friendListView;
	private View view;
	private Cursor cursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dao = new ProviderDao(getActivity());
		view = inflater.inflate(R.layout.fragment_friend_list, container, false);
		friendListView = (ListView) view.findViewById(R.id.custom_friend_listView);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshData();
		listView();
	}

	private void listView() {

		cursor = dao.getFriendList();

		FriendAdapter friendAdapter = new FriendAdapter(getActivity(), cursor, R.layout.custom_friend_list);
		friendListView.setAdapter(friendAdapter);

		Button bt_addfriend = (Button)view.findViewById(R.id.bt_addfriend);
		bt_addfriend.setOnClickListener(this);
	}

	private void refreshData() {
		String friendid;
		String friendDetail;

		Proxy proxy = new Proxy(getActivity());
		String jsonData = proxy.getFriendJSON();
		ArrayList<String> friendList = dao.splitJsonFriendListDataToArrayList(jsonData);
		for (int i = 0; i < friendList.size(); ++i) {
			friendid = friendList.get(i);
			friendDetail = proxy.getFriendDetailJSON(friendid);
			dao.insertJsonFriendData(friendDetail);
		}

		listView();
	}



	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.bt_addfriend:
			transactArticleFragment();
			break;
		}
		
	}

	public void transactArticleFragment() {
		Fragment newFragment = new FriendAddingFragment();

		final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();

		transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
		transaction.replace(R.id.ll_fragment, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}
}
