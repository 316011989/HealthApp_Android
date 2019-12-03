/**
 * 
 */
package com.pact.healthapp.components.set;

import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragmentActivity;

/**
 * @author zhangyl
 * 
 */
public class SetActivity extends BaseFragmentActivity {
	private int TYPE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myApplication.addActivity(this);
		TYPE = getIntent().getIntExtra("TYPE", 0);
		if (TYPE == 1) {
			setContenierView(1);
			btn_right.setVisibility(View.INVISIBLE);
			home_titile1.setText("个人设置");
			setBackType(0);
			mFragmentManager.beginTransaction()
					.replace(R.id.login_fl_continer, new SetUserInfoFragment())
					.addToBackStack("SetUserInfoFragment").commit();
		} else {
			setContenierView(4);
			home_tab5_cb.setSelected(true);
			btn_right.setVisibility(View.INVISIBLE);
			home_titile1.setText(Constants.moduleList.get(3).getModuleName());
			setBackType(2);
			mFragmentManager.beginTransaction()
					.replace(R.id.login_fl_continer, new SetFragment())
					.addToBackStack("SetFragment").commit();
		}
		
	}

}
