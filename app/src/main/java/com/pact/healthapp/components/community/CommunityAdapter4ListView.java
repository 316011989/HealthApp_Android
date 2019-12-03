package com.pact.healthapp.components.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityAdapter4ListView extends BaseAdapter {
	Context context;
	ArrayList<CommunityBean> list;
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();

	public CommunityAdapter4ListView(Context context,
			ArrayList<CommunityBean> list) {
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.community_listview_item_layout, null);
			holder = new ViewHolder();
			holder.community_listView_item_icon = (ImageView) convertView
					.findViewById(R.id.community_listView_item_icon);
			holder.community_listView_item_title = (TextView) convertView
					.findViewById(R.id.community_listView_item_title);
			holder.community_listView_item_focus = (TextView) convertView
					.findViewById(R.id.community_listView_item_topic);
			holder.community_listView_item_topics = (TextView) convertView
					.findViewById(R.id.community_listView_item_browse);
			holder.community_listView_item_concern = (Button) convertView
					.findViewById(R.id.community_listView_item_concern);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.community_listView_item_icon.setVisibility(View.GONE);
		holder.community_listView_item_title.setText(list.get(position)
				.getBoardTitle());
		holder.community_listView_item_focus.setText(""
				+ list.get(position).getFavoriteCount());
		holder.community_listView_item_topics.setText(""
				+ list.get(position).getTopicsCount());
		if (list.get(position).favorite) {
			holder.community_listView_item_concern.setCompoundDrawables(null,
					initDrawable(R.mipmap.btn_followed), null, null);
			holder.community_listView_item_concern.setText("取消关注");
		} else {
			holder.community_listView_item_concern.setCompoundDrawables(null,
					initDrawable(R.mipmap.btn_follow), null, null);
			holder.community_listView_item_concern.setText("关注");
		}
		holder.community_listView_item_concern
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (!sp.getLoginState(context)) {
							Intent intent = new Intent(context,
									LoginActivity.class);
							context.startActivity(intent);
							return;
						}
						forumFavorite(list.get(position));
					}
				});
		return convertView;
	}

	/**
	 * 关注
	 * 
	 * @param cb
	 */
	private void forumFavorite(final CommunityBean cb) {
		// 拼接参数，关注,取消关注
		String bizId = "000";
		String serviceName = "forumFavorite";
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", cb.getId());
		map.put("target", "board");
		map.put("cllectionTime", System.currentTimeMillis() + "");
		if (cb.getFavorite()) {
			map.put("action", "destroy");
		} else {
			map.put("action", "create");
		}
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		String servicePara = JSON.toJSONString(map);
		// 发送请求，获取验证码
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						try {
							String result = Des3.decode(arg0.result.toString());
							JSONObject obj = JSON.parseObject(result);
							String resultCode = obj.getString("resultCode");
							if (resultCode != null && resultCode.equals("0")) {
								// if (cb.isFavorite()) {
								// CommonToast.makeText(context, "取消关注");
								// } else {
								// CommonToast.makeText(context, "关注成功");
								// }
								if (map.get("action").equals("create")) {
									cb.favoriteCount += 1;
								} else if (map.get("action").equals("destroy")) {
									cb.favoriteCount -= 1;
								}
								cb.favorite = !cb.favorite;
								notifyDataSetChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
							// CommonToast.makeText(context, "请求成功,出现异常");
						}
					}
				});
	}

	/**
	 * 初始化drawble
	 * 
	 * @return
	 */
	private Drawable initDrawable(int drawableId) {
		Drawable drawable = context.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		return drawable;
	}

	class ViewHolder {
		ImageView community_listView_item_icon;// 是否为热点
		TextView community_listView_item_title;// title
		TextView community_listView_item_focus;// 话题关注数量
		TextView community_listView_item_topics;// 话题数量
		Button community_listView_item_concern;// 关注按钮
	}

}
