package com.pact.healthapp.universal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.pact.healthapp.components.login.LoginActivity;

/**
 * 跳转管理类
 * 
 * @author haix
 * 
 */
public class FrameworkManager extends BaseFragment {

	/**
	 * 
	 * @param context
	 * @param framework
	 */
	public static void branch(FragmentActivity context, Framework framework) {

		FragmentManager thisManager = context.getSupportFragmentManager();

		// isLogin
		if ("1".equals(framework.getIsLogin()) && !MyApplication.isLogin) {
			Intent intent = new Intent(context, LoginActivity.class);
			intent.putExtra("framework", framework);
			intent.putExtra("isExit", "false");
			context.startActivity(intent);

		} else {
			// type
			if ("1".equals(framework.getModuleType())) {
				// Intent intent = new Intent(context,
				// ListModulesActivity.class);
				// intent.putExtra("framework", framework);
				// context.startActivity(intent);
			} else if ("2".equals(framework.getModuleType())) {
				try {
					Intent intent = new Intent(context, toActivity(context,
							framework));
					intent.putExtra("framework", framework);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					context.overridePendingTransition(0, 0);
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("Manager", e.toString());
				}

			} else if ("3".equals(framework.getModuleType())) {
				if (framework.getClickUrl() != null) {
					context.startActivity(new Intent(context,
							WebviewActivity.class).putExtra("framework",
							framework));
				}

			} else if ("4".equals(framework.getModuleType())) {

				PackageManager packageManager = context.getPackageManager();
				Intent intent = new Intent();
				intent = packageManager.getLaunchIntentForPackage(framework
						.getPackageName());
				if (intent == null) {
					Toast.makeText(context, "应用未下载", Toast.LENGTH_LONG).show();
					return;
				}
				context.startActivity(intent);

			} else if ("0".equals(framework.getModuleType())) {
				try {
					Intent intent = new Intent(context, toActivity(context,
							framework));
					intent.putExtra("framework", framework);
					context.startActivity(intent);
				} catch (Exception e) {
					Log.e("Manager", e.toString());
				}
			}

		}

	}

	/**
	 * activity
	 */
	private static Class toActivity(FragmentActivity context,
			Framework framework) throws Exception {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
		Class activity = Class.forName(info.packageName + "."
				+ framework.getPackageName());
		return activity;

	}

}
