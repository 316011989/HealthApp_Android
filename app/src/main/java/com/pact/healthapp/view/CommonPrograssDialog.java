/**
 * 
 */
package com.pact.healthapp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pact.healthapp.R;

/**
 * @author zhangyl
 * 
 */
public class CommonPrograssDialog extends ProgressDialog {
	private Context context;
	private static CommonPrograssDialog dialog;

	public static CommonPrograssDialog getInstance(Context context) {
		if (dialog == null || !dialog.isShowing())
			dialog = new CommonPrograssDialog(context);
		return dialog;
	}

	private CommonPrograssDialog(Context context) {
		super(context, R.style.MyCustomDialog);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_commonprograssdialog);
		ImageView imageView = (ImageView) findViewById(R.id.view_prograssdialog_image);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.prograssdialog_rotate);
		imageView.startAnimation(hyperspaceJumpAnimation);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}
}
