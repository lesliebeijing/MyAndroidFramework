package com.leslie.androidframework.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpService {
	private static HttpService instance = null;

	private HttpService() {
	}

	public static HttpService getInstance() {
		if (instance == null) {
			instance = new HttpService();
		}

		return instance;
	}

	/**
	 * 是否有可用网络
	 */
	public boolean hasActiveNet(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isConnected();
		}

		return false;
	}

	public int getNetWorkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null) {
			return networkInfo.getType();
		}

		return -1000;
	}

	public boolean hasWifi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}
}
