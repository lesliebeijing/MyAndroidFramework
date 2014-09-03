package com.leslie.androidframework.wxapi;

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.leslie.androidframework.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

public class MyWXApi {
	public static final String APP_ID = "rx66666cb3fd0efb56";
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	private Context context;

	public MyWXApi(Context context) {
		this.context = context;
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(context, APP_ID, false);
	}

	public void regToWx() {
		// 将该app注册到微信
		api.registerApp(APP_ID);
	}

	public boolean isWXInstalled() {
		return api.isWXAppInstalled();
	}

	/**
	 * 发送文本信息
	 * 
	 * @param text
	 * @param scene
	 *            1 表示分享到微信 2表示分享到微信朋友圈
	 */
	public void sendText(String text, int scene) {
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;

		if (scene == 1) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else if (scene == 2) {
			if (isTimeLineSupport()) {
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			} else {
				Toast.makeText(context, "您当前的微信版本不支持分享到朋友圈，请升级后再操作",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}

		// 调用api接口发送数据到微信
		api.sendReq(req);
	}

	/**
	 * 发送图片
	 * 
	 * @param bitmap
	 * @param scene
	 *            1 表示分享到微信 2表示分享到微信朋友圈
	 */
	public void sendImg(Bitmap bitmap, int scene) {
		WXImageObject imgObj = new WXImageObject(bitmap);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		// msg.title = title;

		Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
		bitmap.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;

		if (scene == 1) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else if (scene == 2) {
			if (isTimeLineSupport()) {
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			} else {
				Toast.makeText(context, "您当前的微信版本不支持分享到朋友圈，请升级后再操作",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}

		api.sendReq(req);
	}

	/**
	 * 发送网页
	 * 
	 * @param map
	 * @param scene
	 */
	public void sendWebPage(Map<String, String> map, int scene) {
		if (map == null) {
			return;
		}

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = map.get("url");
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = map.get("title");
		msg.description = map.get("abstract");

		String filepath = map.get("filepath");

		if (filepath == null || !(new File(filepath).exists())) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher);
			Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
			bitmap.recycle();
			msg.thumbData = bmpToByteArray(thumb, true);
		} else {
			Bitmap bitmap = BitmapFactory.decodeFile(filepath);
			Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
			bitmap.recycle();
			msg.thumbData = bmpToByteArray(thumb, true);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;

		if (scene == 1) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else if (scene == 2) {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}

		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/**
	 * 是否支持发送到朋友圈， 4.2 以上支持发送到朋友圈
	 * 
	 * @return
	 */
	public boolean isTimeLineSupport() {
		int wxSdkVersion = api.getWXAppSupportAPI();

		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
			return true;
		}

		return false;
	}

	private byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
		java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);

		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
