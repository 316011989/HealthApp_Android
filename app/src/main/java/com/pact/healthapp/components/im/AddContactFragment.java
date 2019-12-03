/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pact.healthapp.components.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddContactFragment extends BaseFragment {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	@ViewInject(R.id.im_addcontact_edt)
	private EditText editText;// 搜索框
	@ViewInject(R.id.im_addcontact_tv)
	private TextView textView;
	@ViewInject(R.id.im_searchresult_lv)
	private ListView im_searchresult_lv;// 搜索结果列表
	private ArrayList<UserBean> userlist;
	private ContactAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.im_addcontact_fragment, null);
		ViewUtils.inject(this, view);
		/**
		 * 使用软键盘搜索
		 */
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEND
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					// do something;
					searchContact();
					return true;
				}
				return false;
			}
		});

		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
				startActivity(new Intent(context, IMActivity.class));
			}
		});

		im_searchresult_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ChatTo("im" + userlist.get(position).getCustomerId());
			}
		});
		return view;
	}

	/**
	 * 页面加载完成后，自动弹出软键盘
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}

	/**
	 * 查找contact
	 * 
	 * @param v
	 */
	public void searchContact() {
		String name = editText.getText().toString();
		if (name.equals("")) {
			CommonToast.makeText(context, "请输入搜索条件");
			return;
		}
		SearchUserTest(name);
	}

	/**
	 * 搜索用户
	 * 
	 * @author zhangyl
	 * @param kw
	 *            关键字
	 */
	public void SearchUserTest(String kw) {
		String serviceName = "searchUser";
		String bizId = "000";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		map.put("type", "customer");
		map.put("page", "1");
		map.put("pageSize", "20");
		map.put("kw", kw);
		String para = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, para,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						super.onSuccess(arg0);
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());
							ParseJson(result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// CommonToast.makeText(context,
							// arg0.result.toString());
						}
					}

				});
	}

	private void ParseJson(String result) {
		JSONObject obj = JSON.parseObject(result);
		String resultCode = obj.getString("resultCode");
		if (resultCode != null && resultCode.equals("0")) {// 获取验证码成功
			JSONArray jaArray = obj.getJSONArray("customers");
			if (jaArray.size() > 0) {
				userlist = new ArrayList<UserBean>();
				for (int i = 0; i < jaArray.size(); i++) {
					JSONObject useritem = jaArray.getJSONObject(i);
					userlist.add(JSON.toJavaObject(useritem, UserBean.class));
				}
				adapter = new ContactAdapter(context, userlist);
				im_searchresult_lv.setAdapter(adapter);
			}
		}
	}

	/**
	 * 跳转到聊天界面
	 * 
	 * @author zhangyl
	 * @param HxID
	 */
	private void ChatTo(String HxID) {
		// 进入聊天页面
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		// it is single chat
		intent.putExtra("userId", HxID);
		startActivity(intent);
	}
}
