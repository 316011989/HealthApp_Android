package com.pact.healthapp.components.topiclist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.community.SubBoardsBean;

import java.util.ArrayList;

public class TopiclistAdapter4GridView extends BaseAdapter {

	ArrayList<SubBoardsBean> list = new ArrayList<SubBoardsBean>();
	private LayoutInflater inflater;

	public TopiclistAdapter4GridView(Context context,
			ArrayList<SubBoardsBean> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.topiclist_gditem_layout,
					null);
			holder = new ViewHolder();
			holder.com_child_adapter_text = (TextView) convertView
					.findViewById(R.id.com_child_adapter_text);
			holder.com_child_adapter_num = (TextView) convertView
					.findViewById(R.id.com_child_adapter_num);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.com_child_adapter_text.setText(list.get(position)
				.getBoardTitle());
		holder.com_child_adapter_num.setText(list.get(position)
				.getTopicsCount() + "");
		return convertView;
	}

	class ViewHolder {
		TextView com_child_adapter_text;
		TextView com_child_adapter_num;
	}

}
