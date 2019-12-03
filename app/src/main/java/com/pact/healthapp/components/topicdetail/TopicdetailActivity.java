package com.pact.healthapp.components.topicdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.components.community.ComTopicBean;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.Set;

public class TopicdetailActivity extends BaseFragmentActivity {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private String topicId;
	private ComTopicBean comTopicBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(1);
		myApplication.addActivity(this);
		setBackType(1);
		btn_left.setVisibility(View.VISIBLE);
		home_titile1.setText("话题详情");
		topicId = getIntent().getStringExtra("topicId");
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setBackgroundResource(R.mipmap.btn_more);
		btn_right.setOnClickListener(new OnClickListener() {// 分享
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						postShare();
					}
				});
		forumShowTopic(topicId);
	}

	/**
	 * 查询帖子详情数据,传递给帖子详情界面
	 * 
	 * @author zhangyl
	 */
	private void forumShowTopic(String topicId) {
		// 拼接参数
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		map.put("topicId", topicId);
		String para = JSON.toJSONString(map);
		// 发送请求，获取帖子详情
		ServiceEngin.Request(context, "000", "forumShowTopic", para,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());
							JSONObject obj = JSON.parseObject(result);
							if (obj.get("resultCode").toString().equals("0")) {// 获取详情成功
								JSONObject jo = obj.getJSONObject("topic");
								comTopicBean = JSON.toJavaObject(jo,
										ComTopicBean.class);
								mFragmentManager
										.beginTransaction()
										.replace(
												R.id.login_fl_continer,
												new TopicdetailFramgent(
														comTopicBean))
										.addToBackStack("ComDetailFramgent")
										.commit();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// CommonToast.makeText(context, "获取帖子信息失败");
						}
					}
				});

	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		UMCustomShareBoard shareBoard = new UMCustomShareBoard(this,
				Constants.HOST + "/topic/detail/" + topicId,
				comTopicBean.getTitle());
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		shareBoard.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
				0, 0);
		shareBoard.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("", "#### ssoHandler.authorizeCallBack11111");
		String result = "null";
		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if (keySet.size() > 0)
				result = "result size:" + keySet.size();
			for (String key : keySet) {
				Object object = b.get(key);
				Log.d("TestData", "Result:" + key + "   " + object.toString());
			}
		} catch (Exception e) {

		}
		Log.d("TestData", "onActivityResult   " + requestCode + "   "
				+ resultCode + "   " + result);

		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}
}
