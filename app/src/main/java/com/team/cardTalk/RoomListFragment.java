package com.team.cardTalk;

import android.os.Bundle;
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

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_room_list, container, false);

        mainListView = (ListView) view.findViewById(R.id.custom_room_listView);

		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        listView();
    }

    private void listView() {
        Dao dao = new Dao(getActivity());
        roomList = dao.getRoomList();

        final RoomAdapter roomAdapter = new RoomAdapter(getActivity(), R.layout.custom_room_list, roomList);
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

    private void refreshData() {
        Dao dao = new Dao(getActivity());
        dao.insertJsonRoomTestData();
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
}
