package com.pact.healthapp.components.login;

import android.content.Intent;
import android.os.Bundle;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

public class LoginActivity extends BaseFragmentActivity {
	public UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.login");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContenierView(3);
		// home_titile1.setText("登录");
		myApplication.addActivity(this);
		if (getIntent().getStringExtra("phoneNumber") != null) {
			String phoneNumber = getIntent().getStringExtra("phoneNumber");
			mFragmentManager
					.beginTransaction()
					.replace(R.id.login_fl_continer,
							new LoginFragment(phoneNumber))
					.addToBackStack("LoginFragment").commit();
		} else {
			mFragmentManager.beginTransaction()
					.replace(R.id.login_fl_continer, new LoginFragment())
					.addToBackStack("LoginFragment").commit();
		}
		setBackType(0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
