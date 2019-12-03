/**
 * 
 */
package com.pact.healthapp.components.push;

import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragmentActivity;

/**
 * @author zhangyl
 * 
 */
public class PushActivity extends BaseFragmentActivity implements HCallBack {
	PushFramgent pushFramgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(4);
		myApplication.addActivity(this);
		home_titile1.setText(Constants.moduleList.get(0).getModuleName());
		btn_right.setVisibility(View.GONE);
		home_tab1_cb.setSelected(true);
		setBackType(2);
		if (pushFramgent == null) {
			pushFramgent = new PushFramgent();
		}
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, pushFramgent)
				.addToBackStack("pushFramgent").commit();
	}

	@Override
	public void onRefres(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return;
		}
		pushFramgent.notifyListView((PushBean) obj);
	}

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// MyPushMessageReceiver.activities.add(this);
	// }
	//
	// @Override
	// protected void onPause() {
	// // TODO Auto-generated method stub
	// super.onPause();
	// MyPushMessageReceiver.activities.remove(this);
	// }
}
