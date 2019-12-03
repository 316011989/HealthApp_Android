package com.pact.healthapp.components.favorite;

import android.content.Intent;
import android.os.Bundle;

import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;

/**
 * Created by wangdong on 2015/8/12.
 */
public class MyFavoriteActivity extends BaseFragmentActivity {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(1);
		myApplication.addActivity(this);
		setTitle("我的收藏");
		setBackType(1);
		mFragmentManager
				.beginTransaction()
				.replace(R.id.login_fl_continer, new FavoriteCategoryFragment())
				.addToBackStack("FavoriteCategoryFragment").commit();
		if (!sp.getLoginState(context)) {
			Intent intent = new Intent(context, LoginActivity.class);
			startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
			return;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_CANCELED) {
			finish();
		}
	}

}
