package com.pact.healthapp.view.imagepick.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.view.imagepick.adapter.PictrueGVAdapter;
import com.pact.healthapp.view.imagepick.utils.ScreenUtils;
import com.pact.healthapp.view.imagepick.utils.Utility;

import java.util.ArrayList;

/**
 * 主页面，可跳转至相册选择照片，并在此页面显示所选择的照片。 Created by hanj on 14-10-13.
 */
public class MainActivity extends BaseFragmentActivity {
	private PictrueGVAdapter adapter;
	private ArrayList<String> imagePathList;
	private final int PIC_FROM_CAMERA = 1;// 选择拍照
	private final int PIC_FROM＿LOCALPHOTO = 0;// 选择本地相册
	private final int SAVE_IMAGE = 2;// 裁剪后保存图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_imagepick_layout);
		// 获取屏幕像素
		ScreenUtils.initScreen(this);

		Button selectImgBtn = (Button) findViewById(R.id.main_select_image);
		GridView mainGV = (GridView) findViewById(R.id.view_imagepick_gridView);

		imagePathList = new ArrayList<String>();
		adapter = new PictrueGVAdapter(this, imagePathList);
		mainGV.setAdapter(adapter);

		selectImgBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转至最终的选择图片页面
				Intent intent = new Intent(MainActivity.this,
						PhotoWallActivity.class);
				// startActivity(intent);
				startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		int code = intent.getIntExtra("code", -1);
		if (code != 100) {
			return;
		}
		ArrayList<String> paths = intent.getStringArrayListExtra("paths");
		// 添加，去重
		boolean hasUpdate = false;
		for (String path : paths) {
			if (!imagePathList.contains(path)) {
				// 最多9张
				if (imagePathList.size() == 9) {
					Utility.showToast(this, "最多可添加9张图片。");
					break;
				}
				imagePathList.add(path);
				hasUpdate = true;
			}
		}

		if (hasUpdate) {
			adapter.notifyDataSetChanged();
		}
	
	}

}
