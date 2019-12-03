package com.pact.healthapp.view.imageshow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.pact.healthapp.R;
import com.pact.healthapp.components.healthreport.HealthReportActivity;
import com.pact.healthapp.http.DownloadFileEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;

import java.io.File;
import java.util.ArrayList;

public class ImageShowActivity extends BaseFragmentActivity {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private ArrayList<String> paths;
	private int position;
	private int type;// 1为URL 2为本地图片3为本地绝对路径
	private String healthCheckNumber;
	private String date;
	private String reportname;
	private String fileUrl;
	private String customerId;
	private boolean isReLoad;
	private CommonDialog commonDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContenierView(1);
		myApplication.addActivity(this);
		home_titile1.setText("图片展示");
		setBackType(1);
		btn_left.setVisibility(View.VISIBLE);
		btn_right.setVisibility(View.GONE);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getStringArrayList("PATHS") != null
				&& bundle.getStringArrayList("PATHS").size() > 0) {
			paths = bundle.getStringArrayList("PATHS");
			position = bundle.getInt("POSITION", 0);
			type = bundle.getInt("TYPE", -1);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.login_fl_continer,
							new ImageFramgent(paths, type, position))
					.addToBackStack("ImageFramgent").commit();
		} else if (bundle != null
				&& bundle.getString("healthCheckNumber") != null) {
			home_titile1.setText("体检报告");
			// 需要下载
			healthCheckNumber = bundle.getString("healthCheckNumber")
					.toString();
			date = bundle.getString("date").toString();
			reportname = bundle.getString("reportname").toString();
			type = bundle.getInt("TYPE", -1);
			isReLoad = bundle.getBoolean("isReLoad", false);
			mFragmentManager
					.beginTransaction()
					.replace(
							R.id.login_fl_continer,
							new ImageFramgent(healthCheckNumber, date,
									reportname, type, isReLoad))
					.addToBackStack("ImageFramgent").commit();
		} else if (bundle != null && bundle.getString("fileUrl") != null) {
			home_titile1.setText("健康报告");
			// 需要下载
			fileUrl = bundle.getString("fileUrl").toString();
			customerId = sp.getLoginInfo(context, "customerId");
			date = bundle.getString("date").toString();
			reportname = bundle.getString("reportname").toString();
			type = bundle.getInt("TYPE", -1);
			isReLoad = bundle.getBoolean("isReLoad", false);
			mFragmentManager
					.beginTransaction()
					.replace(
							R.id.login_fl_continer,
							new ImageFramgent(fileUrl, customerId, date,
									reportname, type, isReLoad))
					.addToBackStack("ImageFramgent").commit();
		} else {
			new CommonDialog(context, 1, "确定", "", new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			}, null, null, "没有图片").show();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (healthCheckNumber != null && !healthCheckNumber.equals("")) {// 体检报告
				if (!new File(Environment.getExternalStorageDirectory()
						+ "/ncihealth/" + healthCheckNumber).exists()) {// 文件夹不存在
					commonDialog = new CommonDialog(context, 2, "停止下载", "继续下载",
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									commonDialog.dismiss();
									// TODO Auto-generated method stub
									if (new File(Environment
											.getExternalStorageDirectory()
											+ "/ncihealth/" + healthCheckNumber)
											.exists()) {
									} else {// 文件夹不存在，取消下载
										DownloadFileEngin.cancel();
									}
									// 返回报告列表
									Intent intent = new Intent(context,
											HealthReportActivity.class);
									intent.putExtra("TYPE", "0");
									startActivity(intent);
									finish();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									commonDialog.dismiss();
								}
							}, null, "是否取消下载;");
					commonDialog.show();
				} else {// 文件夹存在，直接返回报告列表
					Intent intent = new Intent(context,
							HealthReportActivity.class);
					intent.putExtra("TYPE", "0");
					startActivity(intent);
					finish();
				}
			} else if (fileUrl != null && !fileUrl.equals("")) {// 健康报告
				final String fileName = fileUrl.substring(
						fileUrl.length() - 19, fileUrl.length() - 4);
				if (!new File(Environment.getExternalStorageDirectory()
						+ "/ncihealth/" + fileName).exists()) {// 文件夹不存在
					commonDialog = new CommonDialog(context, 2, "停止下载", "继续下载",
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									commonDialog.dismiss();
									// TODO Auto-generated method stub
									if (new File(Environment
											.getExternalStorageDirectory()
											+ "/ncihealth/" + fileName)
											.exists()) {
									} else {
										DownloadFileEngin.cancel();
									}
									// 返回报告列表
									Intent intent = new Intent(context,
											HealthReportActivity.class);
									intent.putExtra("TYPE", "1");
									startActivity(intent);
									finish();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									commonDialog.dismiss();
								}
							}, null, "是否取消下载;");
					commonDialog.show();
				} else {// 文件夹存在，直接返回报告列表
					Intent intent = new Intent(context,
							HealthReportActivity.class);
					intent.putExtra("TYPE", "1");
					startActivity(intent);
					finish();
				}
			} else {
				finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
