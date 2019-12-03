package com.pact.healthapp.components.changepsd;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;

import java.util.HashMap;

@SuppressLint("ValidFragment")
public class SetPasswordFragment extends BaseFragment {
	@ViewInject(R.id.changepsd_login)
	private TextView changepsd_login;
	@ViewInject(R.id.changepsd_password)
	private EditText changepsd_password;
	@ViewInject(R.id.changepsd_submit)
	private Button changepsd_submit;

	private String phoneNumber;// 手机号
	private String secretKey;// 秘钥

	public SetPasswordFragment() {

	}

	public SetPasswordFragment(String phoneNumber, String secretKey) {
		this.phoneNumber = phoneNumber;
		this.secretKey = secretKey;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.changepsd_setpsd_layout, null);
		ViewUtils.inject(this, view);
		((ChangePasswordActivity) getActivity()).setTitle("重置密码");
		((ChangePasswordActivity) getActivity()).btn_left
				.setVisibility(View.VISIBLE);
		((ChangePasswordActivity) getActivity()).btn_right
		.setVisibility(View.INVISIBLE);
		/**
		 * 登录
		 */
		changepsd_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivity(new Intent(context, LoginActivity.class)
				// .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				getActivity().finish();
			}
		});
		changepsd_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String password = changepsd_password.getText().toString();
				// 密码不为空
				if (password.equals("")) {
					CommonToast.makeText(context, "请输入密码");
					return;
				}
				// 拼接参数，发送请求修改密码
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phoneNumber", phoneNumber);
				map.put("secretKey", secretKey);
				map.put("password", password);
				String para = JSON.toJSONString(map);
				// 发送请求，修改密码
				ServiceEngin.Request(context, "", "findAndSetNewPwd", para,
						new EnginCallback(context) {
							@Override
							public void onSuccess(ResponseInfo arg0) {
								super.onSuccess(arg0);
								String result = "";
								try {
									result = Des3.decode(arg0.result.toString());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								ParseJson(result);
							}
						});
			}
		});
		return view;
	}

	/**
	 * 解
	 */
	private void ParseJson(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null
				&& Integer.parseInt(obj.get("resultCode").toString()) == 0) {// 请求成功
			new CommonDialog(context, 1, "确定", null, new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			}, null, null, "密码修改成功,跳转至登录界面").show();

		}
	}
}
