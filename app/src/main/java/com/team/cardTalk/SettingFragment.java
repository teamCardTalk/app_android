package com.team.cardTalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener{

	private SharedPreferences sharedpreferences;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();

		View v = inflater.inflate(R.layout.fragment_setting, container, false);
		
		Button button = (Button)v.findViewById(R.id.bt_logout);
		button.setOnClickListener(this);
		
		
		return v;
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.bt_logout:

			sharedpreferences = context.getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.remove("cookie");
			editor.commit();

			Toast.makeText(context, "logout", Toast.LENGTH_SHORT).show();

			Intent i = new Intent(context, com.team.cardTalk.LoginActivity.class);
			startActivity(i);
			break;
		
		}
	}
}
