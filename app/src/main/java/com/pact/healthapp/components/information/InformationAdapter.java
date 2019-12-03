package com.pact.healthapp.components.information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.util.ArrayList;

public class InformationAdapter extends BaseAdapter {
	Context context;
	ArrayList<InformationBean> list;

	public InformationAdapter(Context context, ArrayList<InformationBean> list) {
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.information_listview_item_layout, null);
			holder = new ViewHolder();
			holder.information_item_image = (ImageView) convertView
					.findViewById(R.id.information_item_image);
			holder.information_item_title = (TextView) convertView
					.findViewById(R.id.information_item_title);
			holder.information_item_content = (TextView) convertView
					.findViewById(R.id.information_item_content);
			holder.information_item_unreadNum = (TextView) convertView
					.findViewById(R.id.information_item_unreadNum);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.information_item_image.setImageResource(list.get(arg0)
				.getImageId());
		holder.information_item_title.setText(list.get(arg0).getTitle());
		holder.information_item_content.setText(list.get(arg0).getContent());
		holder.information_item_unreadNum.setText(list.get(arg0).getNum());
		return convertView;
	}

	class ViewHolder {
		ImageView information_item_image;
		TextView information_item_title;
		TextView information_item_content;
		TextView information_item_unreadNum;
	}

}
