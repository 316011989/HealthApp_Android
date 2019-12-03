package com.pact.healthapp.components.changepsd;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.view.CommonToast;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 找回密码功能，发送并填写验证码界面
 * 
 * @author caotong
 * 
 */
public class PSDVerifyFragment extends BaseFragment {
	@ViewInject(R.id.changepsd_setsms)
	private EditText changepsd_setsms;// 验证码
	@ViewInject(R.id.changepsd_submit)
	private Button changepsd_submit;// 提交
	@ViewInject(R.id.changepsd_getsms)
	private Button changepsd_getsms;
	@ViewInject(R.id.changepsd_registration_account)
	private EditText changepsd_registration_account;

	private String phoneNumber;// 手机号
	private String authCode;// 验证码
	private String secretKey;// 秘钥

	@ViewInject(R.id.login_login)
	private TextView login_login;// 回归登陆

	int CYCLE = 60;
	boolean inTimer;
	private int secondNum;

	private void startTimer() {
		if (inTimer) {

		} else {
			inTimer = true;
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
			fixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					while (secondNum < CYCLE) {
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
			int num = CYCLE - msg.what;
			changepsd_getsms.setEnabled(false);
			changepsd_getsms.setText(num + "秒后可重新获取");
			if (num == 0) {
				changepsd_getsms.setEnabled(true);
				changepsd_getsms.setText("重新获取");
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.changepsd_verify_mobile_layout,
				null);
		ViewUtils.inject(this, view);
		((ChangePasswordActivity) getActivity()).setTitle("找回密码");
		((ChangePasswordActivity) getActivity()).btn_left
				.setVisibility(View.VISIBLE);
		((ChangePasswordActivity) getActivity()).btn_right
				.setVisibility(View.INVISIBLE);
		init();
		changepsd_getsms.setEnabled(true);
		return view;
	}

	private void init() {
		// 发送验证码
		changepsd_getsms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = changepsd_registration_account.getText()
						.toString();
				if (phoneNumber.equals("")) {
					CommonToast.makeText(context, "请输入手机号");
					return;
				}
				// 拼接参数， 请求验证码
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phoneNumber", changepsd_registration_account.getText()
						.toString());
				map.put("typeNo", "2");
				String para = JSON.toJSONString(map);
				// 发送请求，获取验证码
				ServiceEngin.Request(context, "", "smsSend", para,
						new EnginCallback(context) {
							@Override
							public void onSuccess(ResponseInfo arg0) {
								// TODO Auto-generated method stub
								super.onSuccess(arg0);
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
		// 回到登录
		login_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(context, LoginActivity.class)
				// .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				getActivity().finish();
				// getActivity().getSupportFragmentManager()
				// .popBackStackImmediate("LoginFragment", 0);
			}
		});
		// 点击提交,发送获取秘钥请求
		changepsd_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = changepsd_registration_account.getText()
						.toString();
				authCode = changepsd_setsms.getText().toString();
				if (phoneNumber.equals("")) {
					CommonToast.makeText(context, "请输入手机号");
					return;
				}
				if (authCode.equals("")) {
					CommonToast.makeText(context, "请输入验证码");
					return;
				}
				if (!CommonUtil.isMobileNO(phoneNumber)) {
					CommonToast.makeText(context, "手机号格式错误");
					return;
				}
				// 拼接参数
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phoneNumber", phoneNumber);
				map.put("authCode", authCode);
				String para = JSON.toJSONString(map);
				// 发送请求，获取秘钥
				ServiceEngin.Request(context, "", "findPwd", para,
						new EnginCallback(context) {
							@Override
							public void onSuccess(ResponseInfo arg0) {
								// TODO Auto-generated method stub
								super.onSuccess(arg0);
								String result = "";
								try {
									result = Des3.decode(arg0.result.toString());
									ParseJson2(result);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 解析获取验证码请求返回的Json
	 */
	private void ParseJson1(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null
				&& Integer.parseInt(obj.get("resultCode").toString()) == 0) {// 获取验证码成功
			CommonToast.makeText(context, "获取验证码成功");
			if (!inTimer) {
				secondNum = 0;
			}
			startTimer();
		}
	}

	/**
	 * 解析秘钥请求的Json
	 */
	private void ParseJson2(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null
				&& Integer.parseInt(obj.get("resultCode").toString()) == 0) {// 请求成功
			secretKey = obj.get("secretKey").toString();
			FragmentTransaction mFragmentManager = getActivity()
					.getSupportFragmentManager().beginTransaction();
			mFragmentManager
					.replace(R.id.login_fl_continer,
							new SetPasswordFragment(phoneNumber, secretKey))
					.addToBackStack("SetPasswordFragment").commit();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (secondNum > 0) {
			if ((CYCLE - secondNum) > 0) {
				changepsd_getsms.setText((CYCLE - secondNum) + "秒后可重新获取");
				changepsd_getsms.setEnabled(false);
			} else {
				changepsd_getsms.setText("重新获取");
				changepsd_getsms.setEnabled(true);
			}
		}
	}
}
