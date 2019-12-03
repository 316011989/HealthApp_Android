package com.pact.healthapp.components.favorite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.event.EventDetailActivity;
import com.pact.healthapp.components.topicdetail.TopicdetailActivity;
import com.pact.healthapp.components.topiclist.TopiclistActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.LJListView;
import com.pact.healthapp.view.LJListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdong on 2015/8/12.
 */
@SuppressLint("ValidFragment")
public class MyFavoriteFragment extends BaseFragment {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private View view;

	@ViewInject(R.id.homepage_home_listView)
	private LJListView favorite_listView;
	private List<CollectionsBean> collectionsList = new ArrayList<CollectionsBean>();
	private MyFavoriteAdapter4ListView adapter;

	private String target;
	private int curPage = 1;

	public MyFavoriteFragment() {

	}

	public MyFavoriteFragment(String target) {
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.homepage_home_layout, null);
		((MyFavoriteActivity) getActivity()).btn_right.setVisibility(View.GONE);
		ViewUtils.inject(this, view);
		if (target.equals("event")) {
			((MyFavoriteActivity) getActivity()).setTitle("我收藏的活动");
		} else if (target.equals("board")) {
			((MyFavoriteActivity) getActivity()).setTitle("我收藏的板块");
		} else if (target.equals("topic")) {
			((MyFavoriteActivity) getActivity()).setTitle("我收藏的话题");
		}
		collectionsList.clear();
		adapter = new MyFavoriteAdapter4ListView(context, collectionsList,
				inflater);

		favorite_listView.setAdapter(adapter);
		favorite_listView.setPullLoadEnable(true, "");
		favorite_listView.setPullRefreshEnable(true);
		favorite_listView.setIsAnimation(true);
		favorite_listView.setXListViewListener(new IXListViewListener() {
			// 向下滑动刷新
			@Override
			public void onRefresh() {
				favorite_listView.setPullLoadEnable(false, "");
				getFavorite(0, "new");

			}

			// 向上滑动加载更多
			@Override
			public void onLoadMore() {
				favorite_listView.setPullRefreshEnable(false);
				curPage++;
				getFavorite(curPage, "old");
			}
		});

		favorite_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (collectionsList.get(position - 1).getTarget()
						.equals("board")) { // 跳转圈子详情界面
					Intent board = new Intent();
					board.setClass(context, TopiclistActivity.class);
					board.putExtra("TITLE", collectionsList.get(position - 1)
							.getTitle());
					board.putExtra("subBoards",
							collectionsList.get(position - 1).getSubBoards());
					board.putExtra("BOARDID", collectionsList.get(position - 1)
							.getId());
					board.putExtra("FAVORITED", "true");
					startActivity(board);

				} else if (collectionsList.get(position - 1).getTarget()
						.equals("topic")) { // 跳转帖子详情界面
					Intent topic = new Intent();
					topic.setClass(context, TopicdetailActivity.class);
					topic.putExtra("topicId", collectionsList.get(position - 1)
							.getId());
					startActivity(topic);

				} else if (collectionsList.get(position - 1).getTarget()
						.equals("event")) { // 跳转活动详情界面
					Intent event = new Intent();
					event.setClass(context, EventDetailActivity.class);
					event.putExtra("eventId", collectionsList.get(position - 1)
							.getId());
					startActivity(event);

				}

			}
		});
		getFavorite(curPage, "old");
		return view;
	}

	private void onLoad(int num) {
		favorite_listView.setCount(num + "");
		favorite_listView.setRefreshTime("刚刚");
	}

	// 加载我的收藏数据
	private void getFavorite(int curPage, final String queryType) {
		String bizId = "000";
		String serviceName = "myFavorite";
		Map<String, String> map = new HashMap<String, String>();
		map.put("target", target);
		map.put("startTime", System.currentTimeMillis() + "");
		map.put("queryType", queryType);
		map.put("curPage", curPage + "");
		map.put("pageSize", "20");
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						super.onSuccess(arg0);
						String result = "";
						result = Des3.decode(arg0.result.toString());
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray array = obj.getJSONArray("collections");

							if (queryType.equals("new")) {
								collectionsList.addAll(0,
										JSON.parseArray(array.toString(),
												CollectionsBean.class));
								onLoad(JSON.parseArray(array.toString(),
										CollectionsBean.class).size());
							} else if (queryType.equals("old")) {
								collectionsList.addAll(JSON.parseArray(
										array.toString(), CollectionsBean.class));
							}

							adapter.notifyDataSetChanged();
							favorite_listView.stopLoadMore();
							favorite_listView.stopRefresh();
							favorite_listView.setPullLoadEnable(true, "");
							favorite_listView.setPullRefreshEnable(true);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	boolean resume = true;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (resume) {
			resume = false;
		} else {
			curPage = 1;
			collectionsList.clear();
			getFavorite(curPage, "old");
		}

	}

}
