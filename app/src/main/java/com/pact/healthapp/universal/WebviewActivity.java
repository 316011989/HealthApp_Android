package com.pact.healthapp.universal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;

import com.pact.healthapp.R;
import com.pact.healthapp.components.topicdetail.UMCustomShareBoard;
import com.pact.healthapp.data.Constants;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 基础webview,用于显示不需要做处理的网页
 * 
 * @author zhangyl
 * 
 */
public class WebviewActivity extends BaseFragmentActivity {
	private String url;
	public String title;
	private final String ICICLE_KEY = "stateInfo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContenierView(1);
		setBackType(0);
		getState(savedInstanceState);
		if (url.equals(Constants.SERVICE_TERMS_URL)) {
			setTitle("服务条款");
			btn_right.setVisibility(View.GONE);
		} else if (url.equals(Constants.PRIVACY_ANNOUNCE_URL)) {
			setTitle("隐私条款");
			btn_right.setVisibility(View.GONE);
		} else if (url.equals(Constants.SET_ABOUTUS_URL)) {
			setTitle("关于我们");
			btn_right.setVisibility(View.GONE);
		} else {
			setTitle("资讯详情");
			btn_right.setVisibility(View.VISIBLE);
		}
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new WebviewFragment(url))
				.addToBackStack("WebviewFragment").commit();
		btn_right.setBackgroundResource(R.mipmap.btn_more);
		btn_right.setOnClickListener(new OnClickListener() {// 分享
					@Override
					public void onClick(View v) {
						postShare();
					}
				});

	}

	/**
	 * @author zhangyl
	 */
	private void getState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			// We were just launched -- set up a new game
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
		} else {
			// We are being restored
			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
			if (map != null) {
				url = map.getString("url");
				title = map.getString("title");
			} else {
				url = "";
				title = "";
			}
		}
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		UMCustomShareBoard shareBoard = new UMCustomShareBoard(this, url, title);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		shareBoard.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
				0, 0);
		shareBoard.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, intent);
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		// Store the game state
		outState.putBundle(ICICLE_KEY, saveState());
	}

	public Bundle saveState() {
		Bundle map = new Bundle();
		map.putString("url", url);
		map.putString("title", title);
		return map;
	}
}
