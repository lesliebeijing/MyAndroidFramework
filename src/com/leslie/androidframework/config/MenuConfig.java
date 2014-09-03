package com.leslie.androidframework.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leslie.androidframework.fragment.HomePageFragment;
import com.leslie.androidframework.fragment.InfoFragment;
import com.leslie.androidframework.fragment.TreasureFragment;
import com.leslie.androidframework.fragment.ViewPointFragment;

public class MenuConfig {
	/**
	 * 左侧划出菜单设置
	 */
	public static List<Map<String, Object>> getLeftMenus() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tag", "homepage");
		map.put("title", "首页");
		map.put("class", HomePageFragment.class);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("tag", "info");
		map.put("title", "资讯");
		map.put("class", InfoFragment.class);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("tag", "viewpoint");
		map.put("title", "观点");
		map.put("class", ViewPointFragment.class);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("tag", "treasure");
		map.put("title", "理财");
		map.put("class", TreasureFragment.class);
		list.add(map);

		return list;
	}
}
