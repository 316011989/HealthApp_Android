package com.pact.healthapp.components.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.WebviewActivity;
import com.pact.healthapp.view.CommonToast;

import java.util.HashMap;

public class RegistrationFragment extends BaseFragment {

	@ViewInject(R.id.login_registration_account)
	private EditText login_registration_account;// 账号
	@ViewInject(R.id.login_registration_next)
	private Button login_registration_next;// 下一步
	@ViewInject(R.id.login_registration_password)
	private EditText login_registration_password;// 密码
	@ViewInject(R.id.regist_1)
	private TextView regist_1;// 注册。。服务条款。。
	@ViewInject(R.id.login_login)
	private TextView login_login;// 回归登陆

	private String phoneNumber;
	private String password;
	private int retrySeconds;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.login_registration_layout, null);
		ViewUtils.inject(this, view);
		((LoginActivity) getActivity()).setTitle("注册");
		((LoginActivity) getActivity()).btn_left.setVisibility(View.VISIBLE);
		init();
		return view;
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
			public void onClick(View widget) { // 隐私声明
				Intent intent = new Intent(context, WebviewActivity.class);
				intent.putExtra("url", Constants.PRIVACY_ANNOUNCE_URL);
				intent.putExtra("title", "隐私声明");
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

	private void init() {
		// TODO Auto-generated method stub
		login_registration_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = login_registration_account.getText().toString();
				password = login_registration_password.getText().toString();
				// 账号不为空
				if (phoneNumber.equals("")) {
					CommonToast.makeText(context, "请输入账号");
					return;
				}
				// 密码不为空
				if (password.equals("")) {
					CommonToast.makeText(context, "请输入密码");
					return;
				}
				// 拼接参数
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phoneNumber", phoneNumber);
				String para = JSON.toJSONString(map);
				// 发送请求
				ServiceEngin.Request(context, "", "valiateMobile", para,
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

		login_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		regist_1.setText(getReg1ClickableSpan());
		regist_1.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 解析服务器返回Json
	 */
	private void ParseJson(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null
				&& Integer.parseInt(obj.get("resultCode").toString()) == 0) {// 请求成功
			smsSend();
		}
	}

	private void smsSend() {
		// 拼接参数， 手机号可用，发送短信
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phoneNumber", phoneNumber);
		map.put("typeNo", "1");
		String para = JSON.toJSONString(map);
		// 发送请求，获取验证码
		ServiceEngin.Request(context, "", "smsSend", para, new EnginCallback(
				context) {
			@Override
			public void onSuccess(ResponseInfo arg0) {
				// TODO Auto-generated method stub
				canclDialog();
				String result = "";
				try {
					result = Des3.decode(arg0.result.toString());
					JSONObject obj = JSON.parseObject(result);
					if (obj.get("resultCode") != null) {
						retrySeconds = obj.getIntValue("retrySeconds");
						FragmentTransaction mFragmentManager = getActivity()
								.getSupportFragmentManager().beginTransaction();
						mFragmentManager
								.replace(
										R.id.login_fl_continer,
										new VerifyFragment(phoneNumber,
												password, retrySeconds))
								.addToBackStack("VerifyFragment").commit();
					}// 请求成功

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
