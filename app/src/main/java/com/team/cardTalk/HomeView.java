package com.team.cardTalk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HomeView extends FragmentActivity implements OnClickListener {

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_ARTICLELIST = 0;
	public final static int FRAGMENT_CHATROOMS = 1;
	public final static int FRAGMENT_FRIENDS = 2;
	public final static int FRAGMENT_SETTING = 3;
    private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getResources().getString(R.string.server_url), getResources().getString(R.string.server_url_value));
        editor.commit();

		ImageButton bt_articleList = (ImageButton) findViewById(R.id.bt_articleList);
        bt_articleList.setOnClickListener(this);
        ImageButton bt_chatRooms = (ImageButton) findViewById(R.id.bt_chatRooms);
        bt_chatRooms.setOnClickListener(this);
        ImageButton bt_friends = (ImageButton) findViewById(R.id.bt_friends);
        bt_friends.setOnClickListener(this);
        ImageButton bt_setting = (ImageButton) findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(this);

		mCurrentFragmentIndex = FRAGMENT_ARTICLELIST;
		fragmentReplace(mCurrentFragmentIndex);
        FragmentManagerStock.initiateFragmentManager(getSupportFragmentManager());
	}

	public void fragmentReplace(int reqNewFragmentIndex) {
		Fragment newFragment = null;
		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
		newFragment = getFragment(reqNewFragmentIndex);

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);
		transaction.commit();

	}

    public void fragmentAdd(int reqNewFragmentIndex) {

        Fragment newFragment = null;
        Log.d(TAG, "fragmentAdd " + reqNewFragmentIndex);
        newFragment = getFragment(reqNewFragmentIndex);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ll_fragment, newFragment);
        transaction.commit();
    }

	private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_ARTICLELIST:
			newFragment = new ArticleListFragment();
			break;
		case FRAGMENT_CHATROOMS:
			newFragment = new TwoFragment();
			break;
		case FRAGMENT_FRIENDS:
			newFragment = new ThreeFragment();
			break;
        case FRAGMENT_SETTING:
			newFragment = new FourFragment();
			break;

		default:
			Log.d(TAG, "Unhandle case");
			break;
		}
		return newFragment;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_articleList:
			mCurrentFragmentIndex = FRAGMENT_ARTICLELIST;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.bt_chatRooms:
			mCurrentFragmentIndex = FRAGMENT_CHATROOMS;
            fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.bt_friends:
			mCurrentFragmentIndex = FRAGMENT_FRIENDS;
            fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.bt_setting:
			mCurrentFragmentIndex = FRAGMENT_SETTING;
            fragmentReplace(mCurrentFragmentIndex);
			break;

		}
	}
}