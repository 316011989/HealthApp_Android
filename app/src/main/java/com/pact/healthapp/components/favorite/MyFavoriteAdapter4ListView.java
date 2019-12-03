package com.pact.healthapp.components.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.utils.CommonUtil;

import java.util.List;

/**
 * Created by wangdong on 2015/8/14.
 */
public class MyFavoriteAdapter4ListView extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<CollectionsBean> list;

	public static final int TYPE_COUNT = 3; //
	public static final int BOARD_TYPE = 0; // 圈子
	public static final int TOPIC_TYPE = 1; // 帖子
	public static final int EVENT_TYPE = 2; // 活动

	public MyFavoriteAdapter4ListView(Context context,
			List<CollectionsBean> list, LayoutInflater inflater) {
		this.context = context;
		this.list = list;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return list.isEmpty() ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (list.get(position).getTarget().equals("board")) {
			return BOARD_TYPE;
		} else if (list.get(position).getTarget().equals("topic")) {
			return TOPIC_TYPE;
		} else if (list.get(position).getTarget().equals("event")) {
			return EVENT_TYPE;
		}
		return super.getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BroadViewHolder broadViewHolder = null;
		TopicViewHolder topicViewHolder = null;
		EventViewHolder eventViewHolder = null;

		int type = getItemViewType(position);

		// 无convertView，需要new出各个控件
		if (convertView == null) {
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case BOARD_TYPE:
				convertView = inflater.inflate(
						R.layout.community_listview_item_layout, parent, false);
				broadViewHolder = new BroadViewHolder();

				broadViewHolder.community_listView_item_icon = (ImageView) convertView
						.findViewById(R.id.community_listView_item_icon);
				broadViewHolder.community_listView_item_title = (TextView) convertView
						.findViewById(R.id.community_listView_item_title);
				broadViewHolder.community_listView_item_focus = (TextView) convertView
						.findViewById(R.id.community_listView_item_topic);
				broadViewHolder.community_listView_item_topics = (TextView) convertView
						.findViewById(R.id.community_listView_item_browse);
				broadViewHolder.community_listView_item_concern = (Button) convertView
						.findViewById(R.id.community_listView_item_concern);

				convertView.setTag(broadViewHolder);
				break;
			case TOPIC_TYPE:
				convertView = inflater.inflate(
						R.layout.topiclist_lvitem_layout, parent, false);
				topicViewHolder = new TopicViewHolder();

				topicViewHolder.com_item_icon = (ImageView) convertView
						.findViewById(R.id.com_item_icon);
				topicViewHolder.com_item_title = (TextView) convertView
						.findViewById(R.id.com_item_title);
				topicViewHolder.com_item_time = (TextView) convertView
						.findViewById(R.id.com_item_time);
				topicViewHolder.com_item_board_title = (TextView) convertView
						.findViewById(R.id.com_item_board_title);
				topicViewHolder.com_item_publish_nickname = (TextView) convertView
						.findViewById(R.id.com_item_publish_nickname);
				topicViewHolder.com_listView_item_browse = (TextView) convertView
						.findViewById(R.id.com_listView_item_browse);

				convertView.setTag(topicViewHolder);
				break;
			case EVENT_TYPE:
				convertView = inflater.inflate(
						R.layout.community_active_item_layout, parent, false);
				eventViewHolder = new EventViewHolder();

				eventViewHolder.active_people = (TextView) convertView
						.findViewById(R.id.active_people);
				eventViewHolder.active_time = (TextView) convertView
						.findViewById(R.id.active_time);
				eventViewHolder.active_content = (TextView) convertView
						.findViewById(R.id.active_content);
				eventViewHolder.active_title = (TextView) convertView
						.findViewById(R.id.active_title);

				convertView.setTag(eventViewHolder);
				break;
			}
		} else {
			// 有convertView，按样式，取得不同的布局
			switch (type) {
			case BOARD_TYPE:
				broadViewHolder = (BroadViewHolder) convertView.getTag();
				break;
			case TOPIC_TYPE:
				topicViewHolder = (TopicViewHolder) convertView.getTag();
				break;
			case EVENT_TYPE:
				eventViewHolder = (EventViewHolder) convertView.getTag();
				break;
			}
		}

		// 设置资源
		switch (type) {
		case BOARD_TYPE:
			broadViewHolder.community_listView_item_icon
					.setVisibility(View.GONE);
			broadViewHolder.community_listView_item_title.setText(list.get(
					position).getTitle());
			broadViewHolder.community_listView_item_focus.setText(list.get(
					position).getFavoriteCount());
			broadViewHolder.community_listView_item_topics.setText(list.get(
					position).getTopicsCount());
			broadViewHolder.community_listView_item_concern
					.setVisibility(View.GONE);
			break;
		case TOPIC_TYPE:
			topicViewHolder.com_item_time.setText(CommonUtil
					.getStampTimeStr(list.get(position).getReleaseTime()));
			topicViewHolder.com_item_board_title.setText(list.get(position)
					.getBoardTitle());
			topicViewHolder.com_item_publish_nickname.setText(list
					.get(position).getCustomer().getNickName());
			topicViewHolder.com_listView_item_browse.setText(list.get(position)
					.getCommentsCount());
			if (list.get(position).isHavePic()) {
				if (list.get(position).getTitle().length() <= 15) {
					topicViewHolder.com_item_title.setText(list.get(position)
							.getTitle());
					topicViewHolder.com_item_icon.setVisibility(View.VISIBLE);
				} else {
					String title = list.get(position).getTitle();
					topicViewHolder.com_item_title.setText(title.substring(0,
							15) + "...");
					topicViewHolder.com_item_icon.setVisibility(View.VISIBLE);
				}
			} else {
				topicViewHolder.com_item_title.setText(list.get(position)
						.getTitle());
				topicViewHolder.com_item_icon.setVisibility(View.GONE);
			}
			break;
		case EVENT_TYPE:
			eventViewHolder.active_title.setText(list.get(position).getTitle());
			eventViewHolder.active_content.setText(list.get(position)
					.getEvent().getAddress());
			eventViewHolder.active_time.setText(CommonUtil
					.getStampTimeStr2(list.get(position).getEvent()
							.getStartDatetime())
					+ "--"
					+ CommonUtil.getStampTimeStr2(list.get(position).getEvent()
							.getEndDatetime()));
			eventViewHolder.active_people.setVisibility(View.GONE);
			break;
		}
		return convertView;
	}

	// 各个布局的控件资源

	private static class BroadViewHolder { // 圈子
		ImageView community_listView_item_icon;// 是否为热点
		TextView community_listView_item_title;// title
		TextView community_listView_item_focus;// 话题关注数量
		TextView community_listView_item_topics;// 话题数量
		Button community_listView_item_concern;// 关注按钮
	}

	private static class TopicViewHolder { // 帖子
		ImageView com_item_icon;// 是否有图片
		TextView com_item_title;// title
		TextView com_item_time;// 时间
		TextView com_item_board_title;// 版块名称
		TextView com_item_publish_nickname;// 发布者昵称
		TextView com_listView_item_browse;// 浏览数量
	}

	private static class EventViewHolder { // 活动
		TextView active_people; // 关注
		TextView active_time; // 时间
		TextView active_content; // 地点
		TextView active_title; // 标题
	}
}
