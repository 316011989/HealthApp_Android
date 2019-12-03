package com.pact.healthapp.components.communitysearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.community.ComTopicBean;
import com.pact.healthapp.components.community.ComTopicBoardBean;
import com.pact.healthapp.components.community.ComTopicCustomerBean;
import com.pact.healthapp.components.community.CommunityAdapter4ListView;
import com.pact.healthapp.components.community.CommunityBean;
import com.pact.healthapp.components.topicdetail.TopicdetailActivity;
import com.pact.healthapp.components.topiclist.TopiclistActivity;
import com.pact.healthapp.components.topiclist.TopiclistAdapter4ListView;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.LJListView;
import com.pact.healthapp.view.LJListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunitySearchFramgent extends BaseFragment implements
		OnClickListener, OnCheckedChangeListener {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private View view;
	@ViewInject(R.id.listView)
	private LJListView listView;
	@ViewInject(R.id.com_search)
	private EditText com_search;// 搜索内容框
	@ViewInject(R.id.com_search_cancle)
	private Button com_search_cancle; // 取消
	@ViewInject(R.id.com_search_gridview)
	private GridView com_search_gridview;// 热门搜索 内容
	@ViewInject(R.id.search_result_layout)
	private ViewGroup search_result_layout;
	@ViewInject(R.id.search_title_layout)
	private ViewGroup search_title_layout;
	@ViewInject(R.id.com_search_group)
	private RadioGroup com_search_group;
	private ArrayList<CommunityBean> boardLists = new ArrayList<CommunityBean>(); // 版块列表数据
	private CommunityAdapter4ListView groupAdapter;
	private ArrayList<ComTopicBean> topicLists = new ArrayList<ComTopicBean>(); // 话题列表数据
	private TopiclistAdapter4ListView topicAdapter;
	private CommunitySearchAdapter4GridView gridAdapter;
	private RadioButton com_topic, com_group;
	private String currentKD; // 搜索的内容
	private int page = 1;
	private boolean addCountNum = true;

	// 默认 热门搜索数据
	private String[] hotSearch = { "健康", "美体" };

	public CommunitySearchFramgent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.communitysearch_layout, null);
		ViewUtils.inject(this, view);

		Button btn_right = ((CommunitySearchActivity) getActivity()).btn_right;
		btn_right.setBackgroundResource(R.mipmap.btn_filter);
		btn_right.setOnClickListener(this);
		layoutListen(view);
		return view;
	}

	private void layoutListen(View view) {
		com_topic = (RadioButton) view.findViewById(R.id.com_topic);
		com_group = (RadioButton) view.findViewById(R.id.com_group);
		groupAdapter = new CommunityAdapter4ListView(context, boardLists);
		topicAdapter = new TopiclistAdapter4ListView(context, topicLists);
		com_search_group.setOnCheckedChangeListener(this);
		com_search_cancle.setOnClickListener(this);
		com_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					currentKD = com_search.getText().toString();
					if (currentKD.equals("")) {
						CommonToast.makeText(context, "请输入搜索关键词");
					} else {
						HideKeyBoard();
						search_title_layout.setVisibility(View.GONE);
						search_result_layout.setVisibility(View.VISIBLE);
						page = 1;
						addCountNum = true;
						if (com_group.isChecked()) {
							listView.setAdapter(groupAdapter);
							boardLists.clear();
							forumSearch(page, "board");// 版块
						} else if (com_topic.isChecked()) {
							listView.setAdapter(topicAdapter);
							topicLists.clear();
							forumSearch(page, "topic");// 话题
						}
					}
				}
				return false;
			}
		});
		gridAdapter = new CommunitySearchAdapter4GridView(context, hotSearch);
		com_search_gridview.setAdapter(gridAdapter);
		com_search_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				com_search.setText(hotSearch[position]);
				currentKD = hotSearch[position];
				search_title_layout.setVisibility(View.GONE);
				search_result_layout.setVisibility(View.VISIBLE);
				listView.setAdapter(groupAdapter);
				boardLists.clear();
				addCountNum = true;
				forumSearch(page, "board");// 默认搜索版块
				HideKeyBoard();
			}
		});

		listView.setPullLoadEnable(true, "");
		listView.setPullRefreshEnable(true);
		listView.setIsAnimation(true);
		listView.setXListViewListener(new IXListViewListener() {
			// 向下滑动刷新
			@Override
			public void onRefresh() {
				listView.setPullLoadEnable(false, "");
				if (com_group.isChecked()) {
					forumSearch(0, "board");
				} else if (com_topic.isChecked()) {
					forumSearch(0, "topic");
				}
			}

			// 向上滑动加载更多
			@Override
			public void onLoadMore() {
				listView.setPullRefreshEnable(false);
				page++;
				if (com_group.isChecked()) {
					forumSearch(page, "board");
				} else if (com_topic.isChecked()) {
					forumSearch(page, "topic");
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (com_topic.isChecked()) { // 话题
					Intent topic = new Intent();
					topic.setClass(context, TopicdetailActivity.class);
					topic.putExtra("topicId", topicLists.get(position - 1)
							.getId());
					startActivity(topic);
				} else if (com_group.isChecked()) { // 版块
					Intent board = new Intent();
					board.setClass(context, TopiclistActivity.class);
					board.putExtra("TITLE", boardLists.get(position - 1)
							.getBoardTitle());
					board.putExtra("subBoards", boardLists.get(position - 1)
							.getSubBoards());
					board.putExtra("BOARDID", boardLists.get(position - 1)
							.getId());
					board.putExtra("FAVORITED", boardLists.get(position - 1)
							.getFavorite() + "");
					startActivity(board);
				}
			}
		});
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/**
	 * 隐藏软键盘
	 */
	private void HideKeyBoard() {
		try {
			((InputMethodManager) com_search.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.home_btn_right:
			break;
		case R.id.com_search_cancle:
			getActivity().finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.com_group:// 圈子
			listView.setAdapter(groupAdapter);
			boardLists.clear();
			page = 1;
			addCountNum = true;
			forumSearch(page, "board");
			break;
		case R.id.com_topic:// 话题
			listView.setAdapter(topicAdapter);
			topicLists.clear();
			page = 1;
			addCountNum = true;
			forumSearch(page, "topic");
			break;
		default:
			break;
		}
	}

	private void forumSearch(int page, final String type) {
		if (currentKD.equals("")) {
			CommonToast.makeText(context, "请输入搜索关键词");
		}
		// 拼接参数， 请求验证码
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("page", page + "");
		map.put("pageSize", "20");
		map.put("kw", currentKD);
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		String para = JSON.toJSONString(map);
		// 发送请求，获取验证码
		ServiceEngin.Request(context, "", "forumSearch", para,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						super.onSuccess(arg0);
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());
							JSONObject obj = JSON.parseObject(result);
							if (obj.get("resultCode") != null
									&& obj.get("resultCode").toString()
											.equals("0")) {// 获取验证码成功
								if (type.equals("topic")) {
									if (addCountNum) {
										addCountNum = false;
										String num = obj
												.getString("topicCount");
										com_topic.setText("话题（" + num + "）");
										com_group.setText("圈子");
									}
									ArrayList<ComTopicBean> list = new ArrayList<ComTopicBean>();
									String topics = obj.getString("topics");
									JSONArray array = new JSONArray(topics);
									for (int i = 0; i < array.length(); i++) {
										org.json.JSONObject object = array
												.getJSONObject(i);
										list.add(parseJsonTopic(object));
									}
									topicLists.addAll(list);
									topicAdapter.notifyDataSetChanged();
								} else {
									if (addCountNum) {
										addCountNum = false;
										String num = obj
												.getString("broadCount");
										com_group.setText("圈子（" + num + "）");
										com_topic.setText("话题");
									}
									String boards = obj.getString("boards");
									ArrayList<CommunityBean> list = new ArrayList<CommunityBean>();
									JSONArray array = new JSONArray(boards);
									for (int i = 0; i < array.length(); i++) {
										org.json.JSONObject object = array
												.getJSONObject(i);
										list.add(parseJsonBoard(object));
									}
									boardLists.addAll(list);
									groupAdapter.notifyDataSetChanged();
								}
							}
							listView.stopLoadMore();
							listView.stopRefresh();
							listView.setPullLoadEnable(true, "");
							listView.setPullRefreshEnable(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 解析获取验证码请求返回的Json
	 */
	private ComTopicBean parseJsonTopic(org.json.JSONObject object) {
		ComTopicBean comTopicBean = new ComTopicBean();
		try {
			comTopicBean.setBoard(parseBoard(object.getString("board")));
			comTopicBean.setCommentsCount(object.getString("commentsCount"));
			comTopicBean
					.setCustomer(parseCustomer(object.getString("customer")));
			comTopicBean.setId(object.getString("id"));
			comTopicBean.setReleaseTime(object.getString("releaseTime"));
			comTopicBean.setTitle(object.getString("title"));
			comTopicBean.setHavePic(object.getBoolean("havePic"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comTopicBean;

	}

	/**
	 * 解析获取验证码请求返回的board
	 */
	private ComTopicBoardBean parseBoard(String board) {
		JSONObject object = JSON.parseObject(board);
		ComTopicBoardBean comTopicBoardBean = new ComTopicBoardBean();
		String boardTitle = object.getString("boardTitle");
		String id = object.getString("id");
		comTopicBoardBean.setBoardTitle(boardTitle);
		comTopicBoardBean.setId(id);
		return comTopicBoardBean;
	}

	/**
	 * 解析获取验证码请求返回的customer
	 */
	private ComTopicCustomerBean parseCustomer(String customer) {
		JSONObject object = JSON.parseObject(customer);
		ComTopicCustomerBean comTopicCustomerBean = new ComTopicCustomerBean();
		comTopicCustomerBean.setId(object.getString("id"));
		comTopicCustomerBean.setNickName(object.getString("nickName"));
		return comTopicCustomerBean;
	}

	/**
	 * 解析获取验证码请求返回的Json
	 */
	private CommunityBean parseJsonBoard(org.json.JSONObject object) {
		CommunityBean communityBean = new CommunityBean();
		try {
			communityBean.setBoardTitle(object.getString("boardTitle"));
			communityBean.setFavoriteCount(object.getInt("favoriteCount"));
			communityBean.setId(object.getString("id"));
			communityBean.setTopicsCount(object.getInt("topicsCount"));
			communityBean.setSubBoards(object.getString("subBoards"));
			communityBean.setFavorite(object.getBoolean("favorite"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return communityBean;
	}

	boolean resume = true;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (resume) {
			resume = false;
		} else {
			page = 1;
			addCountNum = true;
			if (com_group.isChecked()) {
				boardLists.clear();
				forumSearch(page, "board");// 版块
			} else if (com_topic.isChecked()) {
				topicLists.clear();
				forumSearch(page, "topic");// 话题
			}
		}
	}

}
