/**
 * 
 */
package com.pact.healthapp.components.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.MyApplication;

import java.util.ArrayList;

/**
 * @author zhangyl
 * 
 */
public class ContactAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<UserBean> list;

	public ContactAdapter(Context context, ArrayList<UserBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.im_contact_item, null);
			holder = new ViewHolder();
			holder.im_item_iv = (ImageView) convertView
					.findViewById(R.id.im_item_iv);
			holder.im_item_nickname = (TextView) convertView
					.findViewById(R.id.im_item_nickname);
			holder.im_item_time = (TextView) convertView
					.findViewById(R.id.im_item_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.im_item_iv.setVisibility(View.VISIBLE);
		MyApplication.bitmapUtils.display(holder.im_item_iv, list.get(position)
				.getHeadImg());
		if (list.get(position).getNickName().equals(""))
			holder.im_item_nickname.setText(list.get(position).getUserName());
		else
			holder.im_item_nickname.setText(list.get(position).getNickName());
		holder.im_item_time.setText(list.get(position).getDateCreated()
				.substring(0,19));
		return convertView;

	}

	private static class ViewHolder {
		ImageView im_item_iv;// 头像
		TextView im_item_nickname;// 昵称
		TextView im_item_time;// 时间
	}
}
