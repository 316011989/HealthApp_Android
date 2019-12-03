package com.pact.healthapp.components.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangepwdFragment extends BaseFragment implements OnClickListener {
	private View view;
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	@ViewInject(R.id.changepwd_old)
	private EditText changepwd_old; // 旧密码
	@ViewInject(R.id.changepwd_new)
	private EditText changepwd_new; // 新密码
	@ViewInject(R.id.changepwd_new_again)
	private EditText changepwd_new_again; // 再次新密码
	@ViewInject(R.id.changepwd_submit)
	private Button changepwd_submit;

	public ChangepwdFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.changepwd_layout, null);
		ViewUtils.inject(this, view);
		( getActivity()).setTitle("修改密码");
		((SetActivity) getActivity()).btn_right.setVisibility(View.INVISIBLE);
		((SetActivity) getActivity()).btn_left
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getActivity().finish();
						startActivity(new Intent(context, SetActivity.class));
					}

				});
		changepwd_submit.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		String old_pwd = changepwd_old.getText().toString();
		String new_pwd = changepwd_new.getText().toString();
		String new_again_pwd = changepwd_new_again.getText().toString();

		if (old_pwd.equals("")) {
			CommonToast.makeText(context, "请输入原密码");
			return;
		}

		if (new_pwd.equals("")) {
			CommonToast.makeText(context, "请输入新密码");
			return;
		}

		if (new_again_pwd.equals("")) {
			CommonToast.makeText(context, "请再次输入新密码");
			return;
		}

		if (!new_pwd.equals(new_again_pwd)) {
			CommonToast.makeText(context, "两次输入的密码不一致");
			return;
		}
		if (old_pwd.equals(new_pwd)) {
			CommonToast.makeText(context, "请输入与原密码不同的新密码");
			return;
		}

		String bizId = "000";
		String serviceName = "modifyPwd";
		Map<String, String> map = new HashMap<String, String>();

		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		map.put("password", old_pwd);
		map.put("newPassword", new_pwd);

		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = Des3.decode(arg0.result.toString());

						try {
							JSONObject obj = new JSONObject(result);
							String resultMsg = obj.getString("resultMsg");
							if (obj.get("resultCode") != null
									&& obj.get("resultCode").toString()
											.equals("0")) {
								// CommonToast.makeText(context, resultMsg
								// + ",请重新登陆");
								SharedPreferenceUtils sp = new SharedPreferenceUtils();
								String phoneNumber = sp.getLoginInfo(
										getActivity(), "phoneNumber")
										.toString();
								Intent intent = new Intent(context,
										LoginActivity.class);
								intent.putExtra("phoneNumber", phoneNumber);
								sp.saveLoginInfo(getActivity(), "customerId",
										"");
								sp.saveLoginInfo(getActivity(), "token", "");
								sp.saveLoginInfo(getActivity(), "birth", "");
								sp.saveLoginInfo(getActivity(), "email", "");
								sp.saveLoginInfo(getActivity(), "gender", "");
								sp.saveLoginInfo(getActivity(), "headImg", "");
								sp.saveLoginInfo(getActivity(), "idCard", "");
								sp.saveLoginInfo(getActivity(), "marriage", "");
								sp.saveLoginInfo(getActivity(), "nickName", "");
								sp.saveLoginInfo(getActivity(), "phoneNumber",
										"");
								sp.saveLoginInfo(getActivity(), "huanXinId", "");
								sp.saveLoginInfo(getActivity(), "huanXinPwd",
										"");
								startActivity(intent);
								getActivity().getSupportFragmentManager().popBackStack();
							} 

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}
}
