package com.team.cardTalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class HomeView extends FragmentActivity implements OnClickListener {

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_ARTICLELIST = 0;
	public final static int FRAGMENT_CHATROOMS = 1;
	public final static int FRAGMENT_FRIENDS = 2;
	public final static int FRAGMENT_SETTING = 3;
    private SharedPreferences pref;
	private static final String EXCHANGE_NAME = "push";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		if (android.os.Build.VERSION.SDK_INT > 9) {
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//		}

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

		// service 실행
        final Context context = getApplicationContext();
        Intent intentSync = new Intent(context, SyncDataService.class);
        context.startService(intentSync);

        new Thread(new Runnable()  {
            public void run() {
                ConnectionFactory factory = new ConnectionFactory();
				try {
                    factory.setHost("125.209.195.202");
                    Connection connection = factory.newConnection();
					Channel channel = connection.createChannel();
//					channel.exchangeDeclare(EXCHANGE_NAME, "direct");

                    String queueName = pref.getString("name", "");

					Log.i("test", "queueName: " + queueName);

                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume("user02", true, consumer);

                    while (true) {
                        QueueingConsumer.Delivery delivery= consumer.nextDelivery();
                        String message = new String(delivery.getBody());
                        String routingKey = delivery.getEnvelope().getRoutingKey();

                        //TODO 실제 채팅 메시지로 삽입 되는지 확인
                        Log.i("Test", "rabbitmq" + routingKey + message);
						ProviderDao dao = new ProviderDao(context);
						dao.insertJsonChatData(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
			newFragment = new RoomListFragment();
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
