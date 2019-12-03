package com.pact.healthapp.components.push;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.util.ArrayList;

public class PushAdapter extends BaseAdapter {
	Context context;
	ArrayList<PushBean> list;

	public PushAdapter(Context context, ArrayList<PushBean> list) {
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
					R.layout.push_listview_item_layout, null);
			holder = new ViewHolder();
			holder.push_item_text = (TextView) convertView
					.findViewById(R.id.push_item_text);
			holder.push_item_image = (ImageView) convertView
					.findViewById(R.id.push_item_image);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(arg0).getType() == 1) {
			holder.push_item_text.setText(list.get(arg0).getName());
			holder.push_item_text.setVisibility(View.VISIBLE);
			holder.push_item_image.setVisibility(View.GONE);
		} else {
			holder.push_item_image.setImageResource(context.getResources()
					.getIdentifier(list.get(arg0).getName(), "drawable",
							context.getPackageName()));
			holder.push_item_text.setVisibility(View.GONE);
			holder.push_item_image.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView push_item_text;
		ImageView push_item_image;
	}

}
