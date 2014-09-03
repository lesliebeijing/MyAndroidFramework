package com.leslie.androidframework.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.leslie.androidframework.util.MyToast;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 用于接收微信的请求或响应结果
 * 
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, MyWXApi.APP_ID, false);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	/**
	 * 微信主动发起请求
	 */
	@Override
	public void onReq(BaseReq req) {

	}

	/**
	 * 微信响应结果
	 */
	@Override
	public void onResp(BaseResp resp) {
		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消分享";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "授权失败";
			break;
		default:
			result = "分享失败";
			break;
		}

		MyToast.showToast(this, result, 2);
		this.finish();
	}
}
