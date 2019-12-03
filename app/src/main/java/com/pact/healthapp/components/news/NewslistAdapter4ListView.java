package com.pact.healthapp.components.news;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.MyApplication;
import com.pact.healthapp.utils.CommonUtil;

import java.util.ArrayList;

public class NewslistAdapter4ListView extends BaseAdapter {
	private Context context;
	private ArrayList<NewsBean> newslist;
	private ArrayList<NewsBean> bannerlist;

	public NewslistAdapter4ListView(Context context, ArrayList<NewsBean> list) {
		this.context = context;
		this.newslist = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newslist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return newslist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.newslist_lvitem_layout, null);
			holder = new ViewHolder();
			holder.news_item_iv = (ImageView) convertView
					.findViewById(R.id.news_item_iv);
			holder.news_item_title = (TextView) convertView
					.findViewById(R.id.news_item_title);
			holder.news_item_time = (TextView) convertView
					.findViewById(R.id.news_item_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (newslist.get(position).getImage() != null
				&& !newslist.get(position).getImage().equals("")) {
			holder.news_item_iv.setVisibility(View.VISIBLE);
			MyApplication.bitmapUtils.display(holder.news_item_iv, newslist
					.get(position).getImage());
		} else {
			holder.news_item_iv.setVisibility(View.GONE);
		}
		holder.news_item_title.setText(newslist.get(position).getTitle());
		holder.news_item_time.setText(CommonUtil.getStampTimeStr(newslist.get(
				position).getReleaseTime()));

		return convertView;
	}

	class ViewHolder {
		ImageView news_item_iv;// 是否为热点
		TextView news_item_title;// title
		TextView news_item_time;// content
	}

	class BannerHolder {
		ViewPager news_banner_viewpager;
	}
}
