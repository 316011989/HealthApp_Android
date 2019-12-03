package com.pact.healthapp.components.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.pact.healthapp.R;
import com.pact.healthapp.components.communitysearch.CommunitySearchActivity;
import com.pact.healthapp.universal.BaseFragmentActivity;

public class CommunityActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(4);
		myApplication.addActivity(this);
		btn_left.setVisibility(View.GONE);
		btn_right.setVisibility(View.VISIBLE);
		home_tab3_cb.setSelected(true);
		home_titile1.setText("社区");
		btn_right.setBackgroundResource(R.mipmap.btn_search);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context, CommunitySearchActivity.class));
			}
		});
		setBackType(2);
		// 启动版块列表fragment
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new CommunityListFramgent())
				.addToBackStack("CommunityListFramgent").commit();

	}
}
