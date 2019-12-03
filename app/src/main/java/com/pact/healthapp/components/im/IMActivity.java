/**
 * 
 */
package com.pact.healthapp.components.im;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.easemob.chat.EMChat;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragmentActivity;

/**
 * @author zhangyl
 * 
 */
public class IMActivity extends BaseFragmentActivity {
	
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(4);
		btn_left.setVisibility(View.GONE);
		myApplication.addActivity(this);
		home_tab4_cb.setSelected(true);
		home_titile1.setText(Constants.moduleList.get(3).getModuleName());
		setBackType(2);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setContenierView(2);
				setBackType(0);
				mFragmentManager
						.beginTransaction()
						.replace(R.id.login_fl_continer,
								new AddContactFragment())
						.addToBackStack("AddContactFragment").commit();
			}
		});
		// 显示所有人消息记录的fragment
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new ChatAllHistoryFragment())
				.addToBackStack("ChatAllHistoryFragment").commit();
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

}
