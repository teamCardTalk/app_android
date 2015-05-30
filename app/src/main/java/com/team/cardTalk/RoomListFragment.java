package com.team.cardTalk;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class RoomListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<RoomDTO> roomList;
    private ListView mainListView;
    private ProviderDao dao;
    private Proxy proxy;
    private Context context;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        context = getActivity();
        proxy = new Proxy(context);
        dao = new ProviderDao(context);
        View view = inflater.inflate(R.layout.fragment_room_list, container, false);

        mainListView = (ListView) view.findViewById(R.id.custom_room_listView);

        registerObserver("Rooms");

		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        listView();
    }

    private void listView() {
        roomList = dao.getRoomList();

        final RoomAdapter roomAdapter = new RoomAdapter(context, R.layout.custom_room_list, roomList);
        mainListView.setAdapter(roomAdapter);
        mainListView.setOnItemClickListener(this);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        mainListView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    roomAdapter.remove(position);
                                }
                                roomAdapter.notifyDataSetChanged();
                            }
                        });
        mainListView.setOnTouchListener(touchListener);
        mainListView.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment newFragment = new ChatFragment();

        // pass data(extras) to a fragment
        Bundle bundle = new Bundle();
        String _id = roomList.get(position).getArticleid();
        bundle.putString("_id", _id);
        newFragment.setArguments(bundle);

        final FragmentTransaction transaction = FragmentManagerStock.getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.ll_fragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        Log.i("TEST", "articleid : <" + _id + "> 선택됨");
    }

    public void registerObserver(String tableName) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentObserver myObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);

                Log.i("test", "Observer onChange");
                listView();
            }
        };

        contentResolver.registerContentObserver(Uri.withAppendedPath(
                CardtalkContract.CONTENT_URI, tableName), true, myObserver);
    }
}
