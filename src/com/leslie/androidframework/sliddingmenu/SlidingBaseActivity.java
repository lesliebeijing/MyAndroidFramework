package com.leslie.androidframework.sliddingmenu;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.leslie.androidframework.R;

public class SlidingBaseActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		SlidingMenu sm = getSlidingMenu();

		// customize the SlidingMenu
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_right);
		// sm.setBehindWidth(240);
		// sm.setSecondaryBehindWidth(520);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// 左侧菜单
		sm.setMenu(R.layout.menu_frame);
		sm.setShadowDrawable(R.drawable.shadow);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftMenuFragment()).commit();

		// 右侧菜单
		sm.setSecondaryMenu(R.layout.menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		sm.setSecondaryBehindWidthRes(R.dimen.slidingmenu_offset_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_two, new RightMenuFragment()).commit();
	}
}
