package com.pact.healthapp.components.healthreport;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.io.File;
import java.util.List;

/**
 * Created by wangdong on 2015/10/10.
 */
public class HealthReportAdapter4Listview extends BaseAdapter {
	private Context context;
	private List<MyReportsBean> list;
	private HealthCallBack callBack;

	public HealthReportAdapter4Listview(Context context,
			List<MyReportsBean> list, HealthCallBack callBack) {
		this.context = context;
		this.list = list;
		this.callBack = callBack;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.health_report_listview_item_layout, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.hea_rep_img);
			holder.center_name = (TextView) convertView
					.findViewById(R.id.hea_rep_center_name);
			holder.new_tag = (ImageView) convertView
					.findViewById(R.id.hea_rep_new_tag);
			holder.time = (TextView) convertView
					.findViewById(R.id.hea_rep_time);
			holder.download_tag = (TextView) convertView
					.findViewById(R.id.hea_rep_download_tag);
			holder.download = (TextView) convertView
					.findViewById(R.id.hea_rep_download);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource(R.mipmap.health_check_default);
		holder.center_name.setText(list.get(position).getHealthCenterName());
		holder.time.setText(list.get(position).getDateregister());

		File file = new File(Environment.getExternalStorageDirectory()
				+ "/ncihealth/" + list.get(position).getHealthCheckNumber()
				+ ".zip");
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/ncihealth/" + list.get(position).getHealthCheckNumber());

		if (file.exists() || folder.exists()) {
			holder.new_tag.setVisibility(View.GONE);
			holder.download.setBackgroundResource(R.drawable.health_report_btn_shape);
		} else {
			holder.download_tag.setVisibility(View.INVISIBLE);
			holder.download.setVisibility(View.GONE);
		}

		if (holder.download.getText().equals("重新下载")) {
			holder.download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callBack.callBack(position, 0, true);
				}

			});
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img, new_tag;
		TextView center_name, time, download_tag, download;
	}

}
