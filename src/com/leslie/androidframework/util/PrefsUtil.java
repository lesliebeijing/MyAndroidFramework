package com.leslie.androidframework.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefsUtil {

	private static final String PREFS_NAME = "myprefs";

	public static void setString(Context context, String key, String value) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		return prefs.getString(key, null);
	}

	public static void setInt(Context context, String key, int value) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		return prefs.getInt(key, 0);
	}

	public static void setBoolean(Context context, String key, Boolean value) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		return prefs.getBoolean(key, true);
	}

	/**
	 * 清除指定 key 的记录
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.remove(key);
		edit.commit();
	}
}
