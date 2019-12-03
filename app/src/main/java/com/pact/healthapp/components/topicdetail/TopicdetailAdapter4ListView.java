package com.pact.healthapp.components.topicdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;

public class TopicdetailAdapter4ListView extends BaseAdapter {

	private int[] imageId = { R.mipmap.defalt_pic,
			R.mipmap.defalt_pic, R.mipmap.defalt_pic,
			R.mipmap.defalt_pic, R.mipmap.defalt_pic,
			R.mipmap.defalt_pic };

	private LayoutInflater inflater;

	public TopicdetailAdapter4ListView(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return imageId.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageId[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.topicdetail_lvitem_layout, null);
			holder = new ViewHolder();
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		ImageView home_item_image;
		TextView home_item_text;
	}

}
