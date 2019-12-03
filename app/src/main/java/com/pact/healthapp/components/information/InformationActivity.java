package com.pact.healthapp.components.information;

import android.os.Bundle;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;

public class InformationActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(0);
		setBackType(2);
		myApplication.addActivity(this);
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new InformationFragment())
				.addToBackStack("InformationFragment").commit();
	}
}
