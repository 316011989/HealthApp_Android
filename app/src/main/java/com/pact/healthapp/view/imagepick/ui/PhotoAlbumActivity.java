package com.pact.healthapp.view.imagepick.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.imagepick.utils.Utility;

/**
 * 分相册查看SD卡所有图片。 Created by hanj on 14-10-14.
 */
public class PhotoAlbumActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContenierView(1);
		btn_left.setText("");
		btn_left.setBackgroundResource(R.mipmap.btn_back);
		btn_right.setText("");
		btn_right.setBackgroundDrawable(null);
		if (!Utility.isSDcardOK()) {
			CommonToast.makeText(context, "SD卡不可用。");
			return;
		}
	}


	/**
	 * 点击返回时，回到相册页面
	 */
	private void backAction() {
		// Intent intent = new Intent(this, MainActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backAction();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// 动画
		// overridePendingTransition(R.anim.in_from_left,
		// R.anim.out_from_right);
	}


}
