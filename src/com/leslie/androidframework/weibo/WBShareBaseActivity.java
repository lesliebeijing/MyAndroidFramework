package com.leslie.androidframework.weibo;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.leslie.androidframework.R;
import com.leslie.androidframework.util.MyToast;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

public class WBShareBaseActivity extends AuthBaseActivity implements
		IWeiboHandler.Response {
	private IWeiboShareAPI mWeiboShareAPI = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

		// 如果未安装微博客户端，设置下载微博对应的回调
		if (!mWeiboShareAPI.isWeiboAppInstalled()) {
			mWeiboShareAPI
					.registerWeiboDownloadListener(new IWeiboDownloadListener() {
						@Override
						public void onCancel() {
							Toast.makeText(WBShareBaseActivity.this, "已取消下载",
									Toast.LENGTH_SHORT).show();
						}
					});
		}

		// 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
		// 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
		// 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
		// 失败返回 false，不调用上述回调
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
	}

	/**
	 * 接收微客户端博请求的数据。 当微博客户端唤起当前应用并进行分享时，该方法被调用。
	 * 
	 * @param baseRequest
	 *            微博请求数据对象
	 * @see {@link IWeiboShareAPI#handleWeiboRequest}
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			MyToast.showToast(this, "分享成功", 2);

			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			MyToast.showToast(this, "已取消分享", 2);

			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			MyToast.showToast(this, "分享失败", 2);
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	public void sendMessage(String text) {
		// 检查微博客户端环境是否正常,如果未安装微博,弹出对话框询问用户下载微博客户端
		if (mWeiboShareAPI.checkEnvironment(true)) {
			// 注册第三方应用 到微博客户端中,注册成功后该应用将显示在微博的应用列表中。
			mWeiboShareAPI.registerApp();

			// 1. 初始化微博的分享消息
			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
			weiboMessage.textObject = getTextObj(text);

			// 2. 初始化从第三方到微博的消息请求
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest(); // 用transaction唯一标识一个请求
			request.transaction = String.valueOf(System.currentTimeMillis());
			request.multiMessage = weiboMessage;

			// 3. 发送请求消息到微博,唤起微博分享界面
			mWeiboShareAPI.sendRequest(request);
		}
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj(Map<String, String> map) {
		if (map == null) {
			return null;
		}

		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = System.currentTimeMillis() + "";
		mediaObject.title = map.get("title") != null ? map.get("title") : "";
		mediaObject.description = map.get("abstract") != null ? map
				.get("abstract") : "";

		String filepath = map.get("filepath");

		if (filepath == null || !(new File(filepath).exists())) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);
			// 设置 Bitmap 类型的图片到视频对象里
			mediaObject.setThumbImage(bitmap);
		} else {
			Bitmap bitmap = BitmapFactory.decodeFile(filepath);
			// 设置 Bitmap 类型的图片到视频对象里
			mediaObject.setThumbImage(bitmap);
		}

		mediaObject.actionUrl = map.get("url");
		mediaObject.defaultText = "";

		return mediaObject;
	}

	public void sendMultiMessage(Map<String, String> map) {
		if (map == null) {
			return;
		}
		// 检查微博客户端环境是否正常,如果未安装微博,弹出对话框询问用户下载微博客户端
		if (mWeiboShareAPI.checkEnvironment(true)) {
			try {
				mWeiboShareAPI.registerApp();

				String title = map.get("title") != null ? map.get("title") : "";
				// 1. 初始化微博的分享消息
				WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
				weiboMessage.textObject = getTextObj(title);
				// weiboMessage.imageObject = getImageObj(bitmap);
				weiboMessage.mediaObject = getWebpageObj(map);

				// 2. 初始化从第三方到微博的消息请求
				SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
				// 用transaction唯一标识一个请求
				request.transaction = String
						.valueOf(System.currentTimeMillis());
				request.multiMessage = weiboMessage;

				// 3. 发送请求消息到微博，唤起微博分享界面
				mWeiboShareAPI.sendRequest(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建文本消息对象。
	 * 
	 * @return 文本消息对象。
	 */
	private TextObject getTextObj(String text) {
		TextObject textObject = new TextObject();
		textObject.text = text;
		return textObject;
	}
}
