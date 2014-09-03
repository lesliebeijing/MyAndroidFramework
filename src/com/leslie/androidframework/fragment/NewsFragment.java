package com.leslie.androidframework.fragment;

import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.leslie.androidframework.R;
import com.leslie.androidframework.activity.MainActivity;
import com.leslie.androidframework.adapter.NewsAdapter;
import com.leslie.androidframework.entity.News;
import com.leslie.androidframework.net.MyJsonArrayRequest;
import com.leslie.androidframework.util.Const;
import com.leslie.androidframework.util.MyToast;

public class NewsFragment extends Fragment {
	private ListView listView;
	private LinearLayout loadingLayout;
	private RequestQueue requestQueue;
	private List<News> data;
	private MainActivity main;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		main = (MainActivity) activity;
		requestQueue = Volley.newRequestQueue(activity);
	}

	private void loadData() {
		loadingLayout.setVisibility(View.VISIBLE);
		MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
				Const.URL_INFO, new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray jsonArray) {
						loadingLayout.setVisibility(View.INVISIBLE);

						if (jsonArray != null) {
							Log.i("leslie", jsonArray.toString());
							try {
								data = JSON.parseArray(jsonArray.toString(),
										News.class);
								setData();
							} catch (Exception e) {
								e.printStackTrace();
								Log.i("leslie", "parse json failed");
							}
						} else {
							Log.i("leslie", "return data is null");
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("leslie", "error");
						MyToast.showToast(main, "请求超时", 2);
						loadingLayout.setVisibility(View.INVISIBLE);
					}
				});

		requestQueue.add(jsonArrayRequest);
	}

	private void setData() {
		if (data == null) {
			return;
		}

		NewsAdapter adapter = new NewsAdapter(main, data);
		listView.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		listView = (ListView) view.findViewById(R.id.listview_info);
		loadingLayout = (LinearLayout) view.findViewById(R.id.loading_layout);

		if (data != null) {
			setData();
		} else {
			loadData();
		}

		return view;
	}
}
