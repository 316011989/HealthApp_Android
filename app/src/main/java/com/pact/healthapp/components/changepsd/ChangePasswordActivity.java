package com.pact.healthapp.components.changepsd;

import android.os.Bundle;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;

/**
 * 修改密码，重置密码，找回密码
 * 
 * @author Administrator
 * 
 */
public class ChangePasswordActivity extends BaseFragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(3);
		setBackType(0);
		myApplication.addActivity(this);
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new PSDVerifyFragment())
				.addToBackStack("PSDVerifyFragment").commit();
	}
	
}
