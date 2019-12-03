package com.pact.healthapp.components.homepage;

import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;

public class HomepageActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(4);
		myApplication.addActivity(this);
		btn_right.setVisibility(View.GONE);
		home_tab1_cb.setSelected(true);
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new HomeFragment())
				.addToBackStack("HomeFragment").commit();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackType(2);
	}
}
