package com.leslie.androidframework.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class MyToast {
	private static Toast toast;
	private static Handler handler = new Handler();

	private static Runnable r = new Runnable() {

		@Override
		public void run() {
			toast.cancel();
		}
	};

	/**
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 *            显示的秒数
	 */
	public static void showToast(Context context, String message, int duration) {
		handler.removeCallbacks(r);

		if (toast != null) {
			toast.setText(message);
		} else {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}

		toast.show();

		handler.postDelayed(r, duration * 1000);
	}
}
