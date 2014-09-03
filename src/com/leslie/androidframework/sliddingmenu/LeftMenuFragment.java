package com.leslie.androidframework.sliddingmenu;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.leslie.androidframework.R;
import com.leslie.androidframework.activity.MainActivity;
import com.leslie.androidframework.config.MenuConfig;

public class LeftMenuFragment extends Fragment {
	private ListView listView;
	private MainActivity main;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		main = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_leftmenu, container,
				false);

		listView = (ListView) view.findViewById(R.id.leftmenu_listview);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(),
				MenuConfig.getLeftMenus(), R.layout.menu_left_item,
				new String[] { "title" },
				new int[] { R.id.menu_left_item_title });
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				// 加载相应的 fragment 到 MainActivity
				Map<String, Object> map = (Map<String, Object>) parent
						.getItemAtPosition(position);
				main.loadFragment(map);
			}
		});

		return view;
	}
}
