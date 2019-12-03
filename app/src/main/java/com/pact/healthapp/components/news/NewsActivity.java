/**
 * 
 */
package com.pact.healthapp.components.news;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;

import java.util.ArrayList;

/**
 * @author zhangyl
 * 
 */
public class NewsActivity extends BaseFragmentActivity {

	private ArrayList<CategoryBean> categories = new ArrayList<CategoryBean>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(4);
		myApplication.addActivity(this);
		setBackType(2);
		home_tab2_cb.setSelected(true);
		home_titile1.setText(Constants.moduleList.get(1).getModuleName());
		btn_left.setVisibility(View.INVISIBLE);
		btn_right.setVisibility(View.INVISIBLE);
		// 请求资讯分类数据
		getCategoryList();
	}

	/**
	 * 请求资讯分类数据
	 * 
	 * @author zhangyl
	 */
	private void getCategoryList() {
		// TODO Auto-generated method stub
		String result;
		String bizId, serviceName, servicePara;
		bizId = "000";
		serviceName = "queryNewCategories";
		servicePara = "";
		result = ServiceEngin.Request(context, bizId, serviceName, servicePara);
		if (result != null) {
			JSONObject obj = JSON.parseObject(result);
			if (obj != null && obj.get("resultCode") != null
					&& obj.get("resultCode").toString().equals("0")) {// 请求成功
				JSONArray jsonArray = obj.getJSONArray("categories");
				categories.clear();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					CategoryBean bean = JSON.toJavaObject(object,
							CategoryBean.class);
					categories.add(bean);
				}
				mFragmentManager
						.beginTransaction()
						.replace(R.id.login_fl_continer,
								new NewsFragment(categories))
						.addToBackStack("NewsFragment").commit();
			}
		}
	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		// 请求资讯分类数据
//		getCategoryList();
//	}

}
