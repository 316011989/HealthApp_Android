package com.pact.healthapp.components.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.util.ArrayList;

public class LoginOauthAdapter extends BaseAdapter {
	Context context;
	ArrayList<LoginBean> list;

	public LoginOauthAdapter(Context context, ArrayList<LoginBean> list) {
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.login_listview_item_layout, null);
			holder = new ViewHolder();
			holder.login_oauth_item = (TextView) convertView
					.findViewById(R.id.login_oauth_item);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.login_oauth_item.setText(list.get(arg0).getLeft());
		holder.login_oauth_item.setCompoundDrawablesWithIntrinsicBounds(context
				.getResources().getDrawable(list.get(arg0).getLeftDrawable()),
				null, null, null);
		return convertView;
	}

	class ViewHolder {
		TextView login_oauth_item;
	}

}
