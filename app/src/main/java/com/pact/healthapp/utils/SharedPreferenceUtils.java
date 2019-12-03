package com.pact.healthapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {
	private static final String IS_FIRST_OPEN = "isfirstopen";
	private static final String IS_LOGIN = "islogin";

	/**
	 * 
	 * 缓存引导页面状态(是否启动过程序,如果未启动过会进入引导页,启动过会进入欢迎页)
	 */
	public void setFirstOpenState(Context context, boolean welcomeState) {
		SharedPreferences sp = context.getSharedPreferences(IS_FIRST_OPEN,
				Activity.MODE_PRIVATE);
		sp.edit().putBoolean("welcome_state", welcomeState).commit();
	}

	/**
	 * 获取打开状态(是否首次启动应用)
	 * 
	 * @param context
	 * @return
	 */
	public boolean getFirstOpenState(Context context) {
		return context.getSharedPreferences(IS_FIRST_OPEN,
				Activity.MODE_PRIVATE).getBoolean("welcome_state", true);
	}

	/**
	 * 缓存登录信息
	 * 
	 */
	public void saveLoginInfo(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(IS_LOGIN,
				Activity.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	/**
	 * 获取用户存储信息:key值：name 用户名 password 密码
	 * 
	 * @param context
	 * @return
	 */
	public String getLoginInfo(Context context, String key) {
		return context.getSharedPreferences(IS_LOGIN, Activity.MODE_PRIVATE)
				.getString(key, "");
	}

	/**
	 * 清空用户信息
	 * 
	 * @author zhangyl
	 * @param context
	 * @return
	 */
	public Boolean clearUserinfo(Context context) {
		saveLoginInfo(context, "customerId", "");
		saveLoginInfo(context, "token", "");
		saveLoginInfo(context, "birth", "");
		saveLoginInfo(context, "email", "");
		saveLoginInfo(context, "gender", "");
		saveLoginInfo(context, "headImg", "");
		saveLoginInfo(context, "idCard", "");
		saveLoginInfo(context, "marriage", "");
		saveLoginInfo(context, "nickName", "");
		saveLoginInfo(context, "phoneNumber", "");
		saveLoginInfo(context, "huanXinId", "");
		saveLoginInfo(context, "huanXinPwd", "");
		saveLoginInfo(context, "ohterLogin", "");
		saveLoginInfo(context, "account", "");
		return true;

	}

	/**
	 * 判断当前是否有登录用户信息
	 * 
	 * @author zhangyl
	 * @return
	 */
	public Boolean getLoginState(Context context) {
		SharedPreferences sp = context.getSharedPreferences(IS_LOGIN,
				Activity.MODE_PRIVATE);
		if (!sp.getString("customerId", "").equals("")
				&& !sp.getString("token", "").equals("")) {
			return true;
		}
		return false;
	}
}
