package com.pact.healthapp.components.topicdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.MyApplication;

public class TopicdetailAdapter4GridView extends BaseAdapter {

	String pics[];
	private LayoutInflater inflater;
	private int wide;
	private Context context;

	public TopicdetailAdapter4GridView(Context context, int wide, String pics[]) {
		inflater = LayoutInflater.from(context);
		this.wide = wide;
		this.pics = pics;
		this.context = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return pics.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pics[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.topicdetail_gridview_adapter_layout, null);
			holder = new ViewHolder();
			holder.home_item_image = (ImageView) convertView
					.findViewById(R.id.com_detail_adapter_image);
			ViewGroup.LayoutParams params = holder.home_item_image
					.getLayoutParams();
			params.width = wide;
			params.height = wide;
			holder.home_item_image.setLayoutParams(params);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// LoadingImgUtil.displayAnimate(Constants.HOST + pics[position],
		// holder.home_item_image);
		// ImageLoader.getInstance().displayImage(Constants.HOST +
		// pics[position],
		// holder.home_item_image);
		MyApplication.bitmapUtils.display(holder.home_item_image, pics[position]);
		return convertView;
	}

	class ViewHolder {
		ImageView home_item_image;
	}

}
