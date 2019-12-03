package com.pact.healthapp.components.communitysearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;

public class CommunitySearchAdapter4GridView extends BaseAdapter {
	private String[] itemName;
	private LayoutInflater inflater;

	public CommunitySearchAdapter4GridView(Context context,String[] itemName) {
		this.itemName = itemName;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return itemName.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.communitysearch_gditem_layout, null);
			holder = new ViewHolder();
			holder.com_search_text = (TextView) convertView
					.findViewById(R.id.com_search_text);
			holder.com_search_hot = (ImageView) convertView
					.findViewById(R.id.com_search_hot);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.com_search_text.setText(itemName[position]);
		holder.com_search_hot.setVisibility(View.GONE);

		return convertView;
	}

	class ViewHolder {
		TextView com_search_text;
		ImageView com_search_hot;
	}

}
