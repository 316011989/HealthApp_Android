package com.pact.healthapp.components.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.components.topicdetail.UMCustomShareBoard;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.HashMap;
import java.util.Set;

public class EventDetailActivity extends BaseFragmentActivity {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private String eventId;
	private EventBean eventBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(1);
		myApplication.addActivity(this);
		setTitle("活动");
		setBackType(1);
		btn_right.setVisibility(View.VISIBLE);
		btn_left.setVisibility(View.VISIBLE);
		eventId = getIntent().getStringExtra("eventId");
		getEventDetail();
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setBackgroundResource(R.mipmap.btn_more);
		btn_right.setOnClickListener(new OnClickListener() {// 分享
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						postShare();
					}
				});
	}

	/**
	 * 加载活动详情数据
	 */
	private void getEventDetail() {
		String bizId, serviceName, servicePara;
		bizId = "000";
		serviceName = "queryEventDetail";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("eventId", eventId);
		map.put("token", sp.getLoginInfo(context, "token"));
		servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						super.onSuccess(arg0);
						String result = "";
						result = Des3.decode(arg0.result.toString());
						parseJSON(result);
					}
				});
	}

	private void parseJSON(String result) {
		// TODO Auto-generated method stub
		JSONObject obj = JSON.parseObject(result);
		if (obj.getString("resultCode").equals("0")) {
			JSONObject eventobj = obj.getJSONObject("event");
			eventBean = JSON.toJavaObject(eventobj, EventBean.class);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.login_fl_continer,
							new EventDetailFramgent(eventBean))
					.addToBackStack("EventFramgent").commit();
		}
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		String custmerId = "";
		if (!sp.getLoginInfo(context, "customerId").equals("")) {
			custmerId = sp.getLoginInfo(context, "customerId");
		}
		UMCustomShareBoard shareBoard = new UMCustomShareBoard(this,
				Constants.HOST + Constants.SHOWEVENT_PORT + eventId
						+ Constants.CUSTOMERID + custmerId,
				eventBean.getTitle());
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		shareBoard.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
				0, 0);
		shareBoard.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String result = "null";
		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if (keySet.size() > 0)
				result = "result size:" + keySet.size();
			for (String key : keySet) {
				Object object = b.get(key);
			}
		} catch (Exception e) {

		}

		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
