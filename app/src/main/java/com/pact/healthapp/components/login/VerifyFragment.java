package com.pact.healthapp.components.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.FrameworkManager;
import com.pact.healthapp.universal.WebviewActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注册功能，获取验证码界面
 * 
 * @author caotong
 * 
 */
@SuppressLint("ValidFragment")
public class VerifyFragment extends BaseFragment {
	@ViewInject(R.id.login_setsms)
	private EditText login_setsms;// 验证码输入框
	@ViewInject(R.id.login_submit)
	private Button login_submit;// 提交
	@ViewInject(R.id.login_getsms)
	private Button login_getsms;// 获取验证码
	@ViewInject(R.id.regist_1)
	private TextView regist_1;// 注册。。服务条款。。
	@ViewInject(R.id.login_login)
	private TextView login_login;// 回归登陆

	public String phoneNumber;
	private String password;
	private String authCode;
	private int mretrySeconds;

	public VerifyFragment() {

	}

	public VerifyFragment(String cellphone, String password, int retrySeconds) {
		this.phoneNumber = cellphone;
		this.password = password;
		this.mretrySeconds = retrySeconds;
	}

	boolean inTimer;
	private  int secondNum;

	private void startTimer() {
		if (inTimer) {

		} else {
			inTimer = true;
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
			fixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					while (secondNum < mretrySeconds) {
						secondNum++;
						timerHandler.sendEmptyMessage(secondNum);
						SystemClock.sleep(1000);
					}
					inTimer = false;
				}
			});
		}
	}

	Handler timerHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int num = mretrySeconds - msg.what;
			login_getsms.setText(num + "秒后可重新获取");
			login_getsms.setEnabled(false);
			if (num == 0) {
				login_getsms.setEnabled(true);
				login_getsms.setText("重新获取");
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login_verify_mobile_layout, null);
		ViewUtils.inject(this, view);
		init();
		((LoginActivity) getActivity()).setTitle("验证手机号");
		((LoginActivity) getActivity()).btn_left.setVisibility(View.VISIBLE);
		startTimer();
		login_getsms.setEnabled(false);
		return view;
	}


	private void init() {
		// 提交
		login_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (login_setsms.getText() == null
						|| login_setsms.getText().toString().equals("")) {
					CommonToast.makeText(context, "请输入验证码");
					return;
				}
				authCode = login_setsms.getText().toString();
				// 拼接参数
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phoneNumber", phoneNumber);
				map.put("password", password);
				map.put("authCode", authCode);
				String para = JSON.toJSONString(map);
				ServiceEngin.Request(context, "", "register", para,
						new EnginCallback(context) {
							@Override
							public void onSuccess(ResponseInfo arg0) {
								// TODO Auto-generated method stub
								super.onSuccess(arg0);
								String result = "";
								try {
									result = Des3.decode(arg0.result.toString());
									ParseJson(result);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});
			}
		});
		// 重新发送验证码
		login_getsms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!inTimer) {
					secondNum = 0;
					mretrySeconds = 60;
				}
				// 拼接参数， 手机号可用，发送短信
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phoneNumber", phoneNumber);
				map.put("typeNo", "1");
				String para = JSON.toJSONString(map);
				// 发送请求，获取验证码
				ServiceEngin.Request(context, "", "smsSend", para,
						new EnginCallback(context) {
							@Override
							public void onSuccess(ResponseInfo arg0) {
								// TODO Auto-generated method stub
								canclDialog();
								String result = "";
								try {
									result = Des3.decode(arg0.result.toString());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								ParseJson1(result);
							}
						});

			}
		});
		login_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().popBackStackImmediate("LoginFragment", 0);
//				FragmentTransaction mFragmentManager = getActivity()
//						.getSupportFragmentManager().beginTransaction();
//				mFragmentManager
//						.replace(R.id.login_fl_continer, new LoginFragment())
//						.addToBackStack("LoginFragment").commit();
			}
		});
		regist_1.setText(getReg1ClickableSpan());
		regist_1.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 解析获取验证码请求返回的Json
	 */
	private void ParseJson1(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null) {
			mretrySeconds = obj.getIntValue("retrySeconds");
			secondNum = 0;
			startTimer();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	// 设置超链接文字
	private SpannableString getReg1ClickableSpan() {
		SpannableString spanStr = new SpannableString(getResources().getString(
				R.string.regist_desc1_str));
		// 设置文字的单击事件
		spanStr.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) { // 服务条款
				Intent intent = new Intent(context, WebviewActivity.class);
				intent.putExtra("url", Constants.SERVICE_TERMS_URL);
				intent.putExtra("title", "服务条款");
				startActivity(intent);
			}
		}, 12, 16, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		// 设置文字的前景色
		spanStr.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.login_next_color)), 12, 16,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置文字的单击事件
		spanStr.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) { // 隐私条款
				Intent intent = new Intent(context, WebviewActivity.class);
				intent.putExtra("url", Constants.PRIVACY_ANNOUNCE_URL);
				intent.putExtra("title", "服务条款");
				startActivity(intent);
			}
		}, 17, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置文字的前景色
		spanStr.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.login_next_color)), 17, 21,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanStr;
	}

	/**
	 * 解析服务器返回Json
	 */
	private void ParseJson(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null
				&& Integer.parseInt(obj.get("resultCode").toString()) == 0) {// 请求成功
			CommonToast.makeText(getActivity(), "注册成功");
			SharedPreferenceUtils sp = new SharedPreferenceUtils();
			sp.saveLoginInfo(getActivity(), "customerId",
					obj.getString("customerId"));
			sp.saveLoginInfo(getActivity(), "token", obj.getString("token"));
			sp.saveLoginInfo(getActivity(), "huanxinId",
					obj.getString("huanxinId"));
			sp.saveLoginInfo(getActivity(), "huanxinPwd",
					obj.getString("huanxinPwd"));
			sp.saveLoginInfo(getActivity(), "birth", "");
			sp.saveLoginInfo(getActivity(), "email", "");
			sp.saveLoginInfo(getActivity(), "gender", "");
			sp.saveLoginInfo(getActivity(), "headImg", "");
			sp.saveLoginInfo(getActivity(), "idCard", "");
			sp.saveLoginInfo(getActivity(), "marriage", "");
			sp.saveLoginInfo(getActivity(), "nickName", "");
			sp.saveLoginInfo(getActivity(), "phoneNumber", phoneNumber);
			sp.saveLoginInfo(getActivity(), "otherLogin", "false");
			// LoginUser.getInstance().setToken(obj.getString("token"));
			// LoginUser.getInstance().setCustomerId(obj.getString("customerId"));
			// LoginUser.getInstance().setHuanXinId(obj.getString("huanXinId"));
			// LoginUser.getInstance().setHuanXinPwd(obj.getString("huanXinPwd"));
			// LoginUser.getInstance().setPhoneNumber(phoneNumber);
			// LoginUser.getInstance().setOtherLogin("false");
			// LoginUser.getInstance().setBirth("");
			// LoginUser.getInstance().setEmail("");
			// LoginUser.getInstance().setGender("");
			// LoginUser.getInstance().setHeadImg("");
			// LoginUser.getInstance().setIdCard("");
			// LoginUser.getInstance().setMarriage(false);
			// LoginUser.getInstance().setNickName("");
			initLoginEMChat(obj.getString("huanXinId"),
					obj.getString("huanXinPwd"));
			FrameworkManager.branch(getActivity(), Constants.moduleList.get(0));
			getActivity().finish();
		}
	}

	/**
	 * 登录环信
	 * 
	 * @author zhangyl
	 */
	private void initLoginEMChat(String userName, String password) {
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						((Activity) context).runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance()
										.loadAllConversations();// 这两个方法是加载聊天记录和群组的,在splash中的ocreate也添加
								// try {
								// processContactsAndGroups();
								// } catch (EaseMobException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// }
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						((Activity) context).runOnUiThread(new Runnable() {
							public void run() {
							}
						});

					}
				});
	}

}
