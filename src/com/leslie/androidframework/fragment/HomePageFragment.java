package com.leslie.androidframework.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leslie.androidframework.R;
import com.leslie.androidframework.util.MyToast;
import com.viewpagerindicator.TabPageIndicator;

public class HomePageFragment extends Fragment {
	private ViewPager pager;
	private TabPageIndicator indicator;
	private ImageView moreColumnImg;
	private String[] columns = new String[] { "头条", "推荐", "军事", "汽车", "旅游",
			"博客", "社会", "教育" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homepage, container,
				false);
		pager = (ViewPager) view.findViewById(R.id.viewpager);
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		moreColumnImg = (ImageView) view.findViewById(R.id.info_morecolumns);
		moreColumnImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MyToast.showToast(getActivity(), "更多栏目", 2);
			}
		});

		ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity()
				.getSupportFragmentManager());
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		return view;
	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			NewsFragment fragment = new NewsFragment();
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return columns[position];
		}

		@Override
		public int getCount() {
			return columns.length;
		}
	}
}
