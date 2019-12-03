package com.pact.healthapp.components.community;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.components.event.EventBean;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventListAdapter4ListView extends BaseAdapter {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private Context context;
	private ArrayList<EventBean> list = new ArrayList<EventBean>();

	public EventListAdapter4ListView(Context context, ArrayList<EventBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null && list.size() >= 0) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LinearLayout.inflate(context,
					R.layout.community_active_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.active_people = (TextView) convertView
					.findViewById(R.id.active_people);
			viewHolder.active_time = (TextView) convertView
					.findViewById(R.id.active_time);
			viewHolder.active_content = (TextView) convertView
					.findViewById(R.id.active_content);
			viewHolder.active_title = (TextView) convertView
					.findViewById(R.id.active_title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.active_title.setText(list.get(position).getTitle());
		viewHolder.active_content.setText(list.get(position).getAddress());

		String StartTime = CommonUtil.getStampTimeStr2(list.get(position)
				.getStartDatetime());
		String EndTime = CommonUtil.getStampTimeStr2(list.get(position)
				.getEndDatetime());

		viewHolder.active_time.setText(StartTime + "——" + EndTime);

		if (Long.parseLong(list.get(position).getEndDatetime()) < System
				.currentTimeMillis()) {
			viewHolder.active_people.setText("报名结束");
			viewHolder.active_people
					.setBackgroundResource(R.drawable.not_click_btn_shape);
			viewHolder.active_people.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
		} else {
			if (list.get(position).isRegist()) {
				viewHolder.active_people.setText("已报名");
				viewHolder.active_people
						.setBackgroundResource(R.drawable.not_click_btn_shape);
				viewHolder.active_people
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						});
			} else {
				if (list.get(position).getMaxInstance() <= list.get(position)
						.getPersonCount()) {
					viewHolder.active_people.setText("人数已满");
					viewHolder.active_people
							.setBackgroundResource(R.drawable.not_click_btn_shape);
					viewHolder.active_people
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
								}
							});
				} else {
					if (!list.get(position).isRegist()) {
						viewHolder.active_people.setText("马上报名");
						viewHolder.active_people
								.setBackgroundResource(R.drawable.login_button_selector);
						viewHolder.active_people
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (!sp.getLoginState(context)) {
											Intent intent = new Intent(context,
													LoginActivity.class);
											context.startActivity(intent);
											return;
										}
										eventResist(position, list
												.get(position).getId());
									}
								});
					}
				}
			}
		}

		return convertView;
	}

	/**
	 * 活动报名
	 * 
	 * @param eventId
	 */
	private void eventResist(final int position, final String eventId) {
		// TODO Auto-generated method stub
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
							// CommonToast.makeText(context,"报名成功");
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).getId().equals(eventId)) {
									list.get(i).regist = true;
								}
							}
							notifyDataSetChanged();
						}
					}
				});
	}

	class ViewHolder {
		TextView active_people;
		TextView active_time;
		TextView active_content;
		TextView active_title;
	}

}
