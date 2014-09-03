package com.leslie.androidframework.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 
 * 微博授权基类，需要授权的页面可继承该类
 */
public class AuthBaseActivity extends Activity {
	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
	}

	public void doWebAuth(OnBindFinishListener onBindFinishListener) {
		mWeiboAuth.anthorize(new AuthListener(onBindFinishListener));
	}

	/**
	 * SSO 认证， 若未安装微博 3.0 以上版本，则自动转为 web auth
	 */
	public void doSSOAuth(OnBindFinishListener onBindFinishListener) {
		mSsoHandler = new SsoHandler(this, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener(onBindFinishListener));
	}

	/**
	 * 取消绑定，删除保存的 token
	 */
	public void cancelBind() {
		AccessTokenKeeper.clear(this);
	}

	public boolean hasAuthed() {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);

		if (token.getToken().equals("")) {
			return false;
		}

		return true;
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 微博绑定成功回调接口，子类可在此做一些处理
	 * 
	 */
	public interface OnBindFinishListener {
		public void onBindFinished();
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		private OnBindFinishListener onBindFinishListener = null;

		public AuthListener(OnBindFinishListener onBindFinishListener) {
			this.onBindFinishListener = onBindFinishListener;
		}

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(AuthBaseActivity.this,
						mAccessToken);

				if (onBindFinishListener != null) {
					onBindFinishListener.onBindFinished();
				}
			} else {
				// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
				String code = values.getString("code");
				Toast.makeText(AuthBaseActivity.this, code, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(AuthBaseActivity.this, "已取消认证", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(AuthBaseActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}
}
