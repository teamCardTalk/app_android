package com.team.cardTalk;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;

public class FriendAddingFragment extends Fragment implements OnClickListener{
	private ProviderDao dao;
	private View view;
	private EditText editFriendId;
	private Button btEnter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_friend_add, container, false);

		editFriendId = (EditText)view.findViewById(R.id.editFriendId);
		btEnter = (Button)view.findViewById(R.id.btEnter);
		btEnter.setOnClickListener(this);

		return view;
	}


	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btEnter:

			Log.i("test", "friendid: " + editFriendId.getText().toString());
			String friendid = editFriendId.getText().toString();
			FriendAddingProxy proxy = new FriendAddingProxy(getActivity());
			try {
				proxy.addFriend(friendid);
			} catch (IOException e) {
				e.printStackTrace();
			}

			transactArticleFragment();
			break;
		}
		
	}

	public void hideKeyboard(EditText target) {
		InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(target.getApplicationWindowToken(), 0);
	}

	public void transactArticleFragment() {
		hideKeyboard(editFriendId);
		Fragment newFragment = new FriendFragment();

		final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();

		transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
		transaction.replace(R.id.ll_fragment, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}
}
