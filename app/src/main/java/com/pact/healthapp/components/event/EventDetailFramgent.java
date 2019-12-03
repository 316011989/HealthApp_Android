package com.pact.healthapp.components.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class EventDetailFramgent extends BaseFragment implements
		OnClickListener {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	View view;

	@ViewInject(R.id.event_apply_num)
	private TextView event_apply_num;// 活动已报名人数
	@ViewInject(R.id.event_remaining_time)
	private TextView event_remaining_time; // 活动剩余时间
	@ViewInject(R.id.event_title)
	private TextView event_title; // 活动标题
	@ViewInject(R.id.event_time)
	private TextView event_time; // 活动时间
	@ViewInject(R.id.event_place)
	private TextView event_place; // 活动地点
	@ViewInject(R.id.event_posterImg)
	private ImageView event_posterImg;
	@ViewInject(R.id.event_content)
	private TextView event_content; // 活动详情
	@ViewInject(R.id.event_collect)
	private Button event_collect; // 收藏
	@ViewInject(R.id.event_apply)
	private Button event_apply; // 报名

	private EventBean eventBean;

	public EventDetailFramgent(EventBean eventBean) {
		// TODO Auto-generated constructor stub
		this.eventBean = eventBean;
	}

	public EventDetailFramgent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.event_framgent_layout, null);
		ViewUtils.inject(this, view);
		setView();
		event_collect.setOnClickListener(this);
		event_apply.setOnClickListener(this);
		return view;
	}

	private void setView() {
		// TODO Auto-generated method stub
		event_apply_num.setText("已报名" + eventBean.getPersonCount() + "人");
		Long remaining_time = Long.parseLong(eventBean.getEndDatetime())
				- System.currentTimeMillis();
		if (remaining_time > 0) {
			event_remaining_time.setText("距离结束："
					+ CommonUtil.formatDuring(remaining_time));
		} else {
			event_remaining_time.setText("");
		}

		event_title.setText(eventBean.getTitle());
		event_time.setText("活动时间："
				+ CommonUtil.getStampTimeStr3(eventBean.getStartDatetime())
				+ " -- "
				+ CommonUtil.getStampTimeStr3(eventBean.getEndDatetime()));
		event_place.setText("活动地点：" + eventBean.getAddress());
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
		bitmapDisplayConfig.setLoadFailedDrawable(context.getResources()
				.getDrawable(R.mipmap.defalt_pic));
		bitmapDisplayConfig.setLoadingDrawable(context.getResources()
				.getDrawable(R.mipmap.defalt_pic));
		bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
		bitmapUtils.display(event_posterImg, eventBean.getPosterImg());
		event_content.setText("			" + eventBean.getTicketDescription());

		if (eventBean.getCollection()) {
			event_collect.setSelected(true);
			event_collect.setText("取消收藏");
		} else {
			event_collect.setSelected(false);
			event_collect.setText("收藏");
		}

		if (remaining_time < 0) {
			event_apply.setText("报名结束");
			event_apply.setBackgroundResource(R.drawable.not_click_btn_shape);
		} else {
			if (eventBean.isRegist()) {
				event_apply.setText("已报名");
				event_apply
						.setBackgroundResource(R.drawable.not_click_btn_shape);
			} else {
				if (eventBean.getMaxInstance() <= eventBean.getPersonCount()) {
					event_apply.setText("人数已满");
					event_apply
							.setBackgroundResource(R.drawable.not_click_btn_shape);
				} else {
					if (!eventBean.isRegist()) {
						event_apply.setText("马上报名");
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.event_collect: // 收藏
			if (!sp.getLoginState(context)) {
				Intent intent = new Intent(context, LoginActivity.class);
				startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
				return;
			}
			getCollect(eventBean.getCollection());
			break;
		case R.id.event_apply: // 报名
			if (event_apply.getText().equals("马上报名")) {
				if (!sp.getLoginState(context)) {
					Intent intent = new Intent(context, LoginActivity.class);
					startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
					return;
				}
				getResist(eventBean.getId());
			}
			break;
		}

	}

	// 收藏活动
	private void getCollect(final boolean action) {
		String bizId = "000";
		String serviceName = "favoriteEvent";
		Map<String, String> map = new HashMap<String, String>();

		map.put("target", "event");
		if (action) {
			map.put("action", "destroy");
		} else {
			map.put("action", "create");
		}
		map.put("id", eventBean.getId());
		map.put("cllectionTime", System.currentTimeMillis() + "");
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));

		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = "";
						result = Des3.decode(arg0.result.toString());
						JSONObject obj = JSON.parseObject(result);
						if (obj.get("resultCode").toString().equals("0")) {
							if (action) {
								event_collect.setSelected(false);
								event_collect.setText("收藏");
							} else {
								event_collect.setSelected(true);
								event_collect.setText("取消收藏");
							}
							eventBean.setCollection(!action);
						}

					}
				});
	}

	// 活动报名
	private void getResist(String eventId) {
		String bizId = "000";
		String serviceName = "eventResist";
		Map<String, String> map = new HashMap<String, String>();

		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		map.put("eventId", eventId);

		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = Des3.decode(arg0.result.toString());
						JSONObject obj = JSON.parseObject(result);
						if (obj.get("resultCode").toString().equals("0")) {
							// CommonToast.makeText(context, "报名成功");
							event_apply.setText("已报名");
							event_apply
									.setBackgroundResource(R.drawable.not_click_btn_shape);
							int count = eventBean.getPersonCount();
							count += 1;
							event_apply_num.setText("已报名" + count + "人");
						}

					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 0) {
				String bizId, serviceName, servicePara;
				bizId = "000";
				serviceName = "queryEventDetail";
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("customerId", sp.getLoginInfo(context, "customerId"));
				map.put("eventId", eventBean.getId());
				map.put("token", sp.getLoginInfo(context, "token"));
				servicePara = JSON.toJSONString(map);
				ServiceEngin.Request(context, bizId, serviceName, servicePara,
						new EnginCallback(context) {
							@Override
							public void onSuccess(ResponseInfo arg0) {
								super.onSuccess(arg0);
								String result = "";
								result = Des3.decode(arg0.result.toString());
								JSONObject obj = JSON.parseObject(result);
								if (obj.getString("resultCode").equals("0")) {
									JSONObject eventobj = obj
											.getJSONObject("event");
									eventBean = JSON.toJavaObject(eventobj,
											EventBean.class);
									setView();
								}
							}
						});
			}
		}
	}

}
