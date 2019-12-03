package com.pact.healthapp.components.communitysearch;

import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;

/**
 * 社区中搜索
 * 
 * @author Administrator
 * 
 */
public class CommunitySearchActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(2);
		myApplication.addActivity(this);
		setBackType(1);
		btn_right.setVisibility(View.VISIBLE);
		btn_left.setVisibility(View.VISIBLE);
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new CommunitySearchFramgent())
				.addToBackStack("CommunitySearchFramgent").commit();
	}
}
