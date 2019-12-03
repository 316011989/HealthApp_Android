package com.pact.healthapp.components.topiclist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.community.SubBoardsBean;
import com.pact.healthapp.universal.BaseFragmentActivity;

import java.util.ArrayList;

public class TopiclistActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContenierView(1);
		myApplication.addActivity(this);
		setBackType(1);
		Intent intent = getIntent();
		String title = intent.getStringExtra("TITLE");
		String boardId = intent.getStringExtra("BOARDID");
		String subBoards = intent.getStringExtra("subBoards");
		String favorited = intent.getStringExtra("FAVORITED");
		ArrayList<SubBoardsBean> list = new ArrayList<SubBoardsBean>();
		JSONArray jsonArray = JSON.parseArray(subBoards);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			list.add(JSON.toJavaObject(obj, SubBoardsBean.class));
		}
		home_titile1.setText(title);
		// + "\n (" + num + ")"
		btn_left.setVisibility(View.VISIBLE);
		mFragmentManager
				.beginTransaction()
				.replace(R.id.login_fl_continer,
						new TopiclistFramgent(list, boardId, favorited))
				.addToBackStack("CommunityFramgent").commit();

	}

}
