package com.pact.healthapp.components.topiclist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.community.ComTopicBean;
import com.pact.healthapp.utils.CommonUtil;

import java.util.ArrayList;

public class TopiclistAdapter4ListView extends BaseAdapter {
	Context context;
	ArrayList<ComTopicBean> list;

	public TopiclistAdapter4ListView(Context context,
			ArrayList<ComTopicBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
					R.layout.topiclist_lvitem_layout, null);
			holder = new ViewHolder();
			holder.com_item_icon = (ImageView) convertView
					.findViewById(R.id.com_item_icon);
			holder.com_item_title = (TextView) convertView
					.findViewById(R.id.com_item_title);
			holder.com_item_time = (TextView) convertView
					.findViewById(R.id.com_item_time);
			holder.com_item_board_title = (TextView) convertView
					.findViewById(R.id.com_item_board_title);
			holder.com_item_publish_nickname = (TextView) convertView
					.findViewById(R.id.com_item_publish_nickname);
			holder.com_listView_item_browse = (TextView) convertView
					.findViewById(R.id.com_listView_item_browse);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).isHavePic()) {
			if (list.get(position).getTitle().length() <= 15) {
				holder.com_item_title.setText(list.get(position).getTitle());
				holder.com_item_icon.setVisibility(View.VISIBLE);
			} else {
				String title = list.get(position).getTitle();
				holder.com_item_title.setText(title.substring(0, 15) + "...");
				holder.com_item_icon.setVisibility(View.VISIBLE);
			}
		} else {
			holder.com_item_title.setText(list.get(position).getTitle());
			holder.com_item_icon.setVisibility(View.GONE);
		}

		holder.com_item_time.setText(CommonUtil.getStampTimeStr(list.get(
				position).getReleaseTime()));
		holder.com_item_board_title.setText(list.get(position).getBoard()
				.getBoardTitle());
		holder.com_item_publish_nickname.setText(list.get(position)
				.getCustomer().getNickName());
		holder.com_listView_item_browse.setText(list.get(position)
				.getCommentsCount());
		return convertView;
	}

	class ViewHolder {
		ImageView com_item_icon;// 是否有图片
		TextView com_item_title;// title
		TextView com_item_time;// 时间
		TextView com_item_board_title;// 板块名称
		TextView com_item_publish_nickname;// 发布者昵称
		TextView com_listView_item_browse;// 浏览数量
	}

}
