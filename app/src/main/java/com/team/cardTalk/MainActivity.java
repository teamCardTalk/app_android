package com.team.cardTalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_ARTICLELIST = 0;
	public final static int FRAGMENT_CHATROOMS = 1;
	public final static int FRAGMENT_FRIENDS = 2;
	public final static int FRAGMENT_SETTING = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

        Stock.initiateFragmentManager(getSupportFragmentManager());
	}

	public void fragmentReplace(int reqNewFragmentIndex) {

		Fragment newFragment = null;

		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);

		newFragment = getFragment(reqNewFragmentIndex);

		// replace fragment
		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}

    public void fragmentAdd(int reqNewFragmentIndex) {

        Fragment newFragment = null;

        Log.d(TAG, "fragmentAdd " + reqNewFragmentIndex);

        newFragment = getFragment(reqNewFragmentIndex);

        // add fragment
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.ll_fragment, newFragment);

        // Commit the transaction
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
            Toast.makeText(getApplicationContext(), "Article List", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_chatRooms:
			mCurrentFragmentIndex = FRAGMENT_CHATROOMS;
            fragmentReplace(mCurrentFragmentIndex);
            Toast.makeText(getApplicationContext(), "Chat Rooms", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_friends:
			mCurrentFragmentIndex = FRAGMENT_FRIENDS;
            fragmentReplace(mCurrentFragmentIndex);
            Toast.makeText(getApplicationContext(), "Friends", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_setting:
			mCurrentFragmentIndex = FRAGMENT_SETTING;
            fragmentReplace(mCurrentFragmentIndex);
            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
			break;

		}

	}

}
