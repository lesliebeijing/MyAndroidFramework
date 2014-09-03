package com.leslie.androidframework.activity;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leslie.androidframework.R;
import com.leslie.androidframework.config.MenuConfig;
import com.leslie.androidframework.sliddingmenu.SlidingBaseActivity;

public class MainActivity extends SlidingBaseActivity {
	private long lastTime = 0;
	private String currentTitle;
	/**
	 * header view
	 */
	private ImageView leftMenuImg, rightMenuImg;
	private TextView title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// header view
		leftMenuImg = (ImageView) findViewById(R.id.header_left_img);
		rightMenuImg = (ImageView) findViewById(R.id.header_right_img);
		title = (TextView) findViewById(R.id.header_title);

		leftMenuImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getSlidingMenu().showMenu();
			}
		});
		rightMenuImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getSlidingMenu().showSecondaryMenu();
			}
		});

		// 默认加载首页
		loadFragment(MenuConfig.getLeftMenus().get(0));
	}

	public void loadFragment(Map<String, Object> map) {
		if (map == null) {
			return;
		}

		try {
			getSlidingMenu().toggle();

			String title = (String) map.get("title");

			if (currentTitle == null || !currentTitle.equals(title)) {
				currentTitle = title;
				// set title
				this.title.setText(title);

				Class<?> cls = (Class<?>) map.get("class");
				String tag = (String) map.get("tag");
				Fragment fragment = (Fragment) cls.newInstance();
				// Insert the fragment by replacing any existing fragment
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.fragment_content, fragment, tag).commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - lastTime > 2000
					&& !getSlidingMenu().isMenuShowing()) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				lastTime = System.currentTimeMillis();

				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (getSlidingMenu().isSecondaryMenuShowing()) {
				getSlidingMenu().showContent();
			} else if (!getSlidingMenu().isMenuShowing()) {
				getSlidingMenu().showSecondaryMenu();
			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
