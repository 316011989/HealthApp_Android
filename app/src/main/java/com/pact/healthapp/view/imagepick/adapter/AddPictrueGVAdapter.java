package com.pact.healthapp.view.imagepick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.MyApplication;

import java.util.ArrayList;

/**
 * 主页面中GridView的适配器
 * 
 * @author hanj
 */

public class AddPictrueGVAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> imagePathList = null;

	// private SDCardImageLoader loader;

	public AddPictrueGVAdapter(Context context, ArrayList<String> imagePathList) {
		this.context = context;
		this.imagePathList = imagePathList;

		// loader = new SDCardImageLoader(ScreenUtils.getScreenW(),
		// ScreenUtils.getScreenH());
	}

	@Override
	public int getCount() {
		if (imagePathList == null) {
			return 1;
		} else if (imagePathList.size() >= 5) {
			return 5;
		} else {
			return imagePathList.size() + 1;
		}
		// return imagePathList == null ? 1 : imagePathList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position >= imagePathList.size()) {
			return position;
		} else {
			return imagePathList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (position < imagePathList.size()) {
			String filePath = (String) getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.view_imagepick_gridview_item, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.view_imagepick_item_photo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView.setTag(filePath);
			MyApplication.bitmapUtils.display(holder.imageView, filePath);
		} else {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.view_imagepick_gridview_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.view_imagepick_item_photo);
			holder.imageView.setBackgroundResource(R.mipmap.add_pic);
			convertView.setTag(holder);
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
	}

}
