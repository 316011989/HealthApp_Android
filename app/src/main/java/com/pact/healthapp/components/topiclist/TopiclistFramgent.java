package com.pact.healthapp.components.topiclist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.community.ComTopicBean;
import com.pact.healthapp.components.community.CommunityBean;
import com.pact.healthapp.components.community.SubBoardsBean;
import com.pact.healthapp.components.communitysearch.CommunitySearchActivity;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.components.publishtopic.PublishTopicActivity;
import com.pact.healthapp.components.topicdetail.TopicdetailActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.LJListView;
import com.pact.healthapp.view.LJListView.IXListViewListener;

import java.util.ArrayList;
import java.util.HashMap;
@SuppressLint("ValidFragment")
public class TopiclistFramgent extends BaseFragment implements OnClickListener,
		OnCheckedChangeListener {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private View view;// 当前fragment的整体布局
	@ViewInject(R.id.listView)
	private LJListView listView;// 上拉加载更多下拉刷新的listview
	@ViewInject(R.id.com_child_focus)
	private Button com_child_focus;// 下方的关注按钮
	@ViewInject(R.id.com_child_publish_topic)
	private Button com_child_publish_topic;// 下方的发布话题按钮
	@ViewInject(R.id.com_child_group)
	private RadioGroup com_child_group;// 上方的tab栏
	@ViewInject(R.id.child_publish)
	private RadioButton child_publish;// 最新发布
	@ViewInject(R.id.child_reply)
	private RadioButton child_reply;// 最新回复
	@ViewInject(R.id.child_hot)
	private RadioButton child_hot;// 热点话题
	private TopiclistAdapter4ListView adapter;
	private ArrayList<ComTopicBean> list = new ArrayList<ComTopicBean>();
	private Button btn_right;
	private Button btn_filter;
	String currentOrderBy;
	ArrayList<SubBoardsBean> childTopicList;
	String boardId, favorited;
	private int page = 1;
	private ArrayList<CommunityBean> communitylist = new ArrayList<CommunityBean>();
	private ArrayList<SubBoardsBean> SubBoardslist = new ArrayList<SubBoardsBean>();

	public TopiclistFramgent(ArrayList<SubBoardsBean> list, String boardId,
			String favorited) {
		// TODO Auto-generated constructor stub
		this.childTopicList = list;
		this.boardId = boardId;
		this.favorited = favorited;
	}

	public TopiclistFramgent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.topiclist_layout, null);
		ViewUtils.inject(this, view);
		btn_filter = ((TopiclistActivity) getActivity()).btn_filter;
		btn_right = ((TopiclistActivity) getActivity()).btn_right;
		btn_filter.setVisibility(View.VISIBLE);
		btn_filter.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		com_child_focus.setOnClickListener(this);
		com_child_publish_topic.setOnClickListener(this);
		layoutListen();

		if (favorited.equals("true")) {
			com_child_focus.setCompoundDrawables(null,
					initDrawable(R.mipmap.btn_followed), null, null);
			com_child_focus.setText("取消关注");
		} else {
			com_child_focus.setCompoundDrawables(null,
					initDrawable(R.mipmap.btn_follow), null, null);
			com_child_focus.setText("关注");
		}

		return view;
	}

	private void layoutListen() {
		listView.setPullLoadEnable(false, "");
		listView.setPullRefreshEnable(true);
		listView.setIsAnimation(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, TopicdetailActivity.class);
				intent.putExtra("topicId", list.get(position - 1).getId());
				startActivity(intent);
			}
		});
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				listView.setPullLoadEnable(false, "");
				// page = 1;
				getListViewList(1, currentOrderBy, "new");
			}

			@Override
			public void onLoadMore() {
				listView.setPullRefreshEnable(false);
				page++;
				getListViewList(page, currentOrderBy, "old");
			}
		});
		com_child_group.setOnCheckedChangeListener(this);
		com_child_publish_topic.setOnClickListener(this);
		com_child_focus.setOnClickListener(this);
		adapter = new TopiclistAdapter4ListView(context, list);
		listView.setAdapter(adapter);
		currentOrderBy = "releaseTime";
		getListViewList(page, currentOrderBy, "old");
	}

	private void onLoad(int num) {
		listView.setCount(num + "");
		listView.setRefreshTime("刚刚");
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
			if (child_publish.isChecked()) {
				list.clear();
				getListViewList(page, "releaseTime", "old");
			} else if (child_reply.isChecked()) {
				list.clear();
				getListViewList(page, "commentTime", "old");
			} else if (child_hot.isChecked()) {
				list.clear();
				getListViewList(page, "hotTopic", "old");
			}
			refreshState();
		}
	}

	/*
	 * 获取帖子列表
	 */
	private void getListViewList(int page, String orderBy,
			final String queryType) {
		String bizId = "000";
		String serviceName = "queryTopic";
		// 拼接参数
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("boardId", boardId);
		map.put("orderBy", orderBy);
		map.put("page", page + "");
		map.put("pageSize", "20");
		map.put("queryType", queryType);
		map.put("now", System.currentTimeMillis() + "");
		if (orderBy.equals("releaseTime")) {
			if (queryType.equals("new")) {
				if (list.size() > 0) {
					map.put("startTime", list.get(0).getReleaseTime());
				} else {
					map.put("startTime", System.currentTimeMillis() + "");
				}
			} else if (queryType.equals("old")) {
				if (list.size() > 0) {
					map.put("startTime", list.get(list.size() - 1)
							.getReleaseTime());
				} else {
					map.put("startTime", System.currentTimeMillis() + "");
				}
			}
		} else {
			if (queryType.equals("new")) {
				if (list.size() > 0) {
					map.put("startTime", list.get(0).getLastCommentTime());
				} else {
					map.put("startTime", System.currentTimeMillis() + "");
				}
			} else if (queryType.equals("old")) {
				if (list.size() > 0) {
					map.put("startTime", list.get(list.size() - 1)
							.getLastCommentTime());
				} else {
					map.put("startTime", System.currentTimeMillis() + "");
				}
			}
		}
		String servicePara = JSON.toJSONString(map);
		// 发送请求，获取帖子列表
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
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
								String topics = obj.getString("topics");
								JSONArray jsonArray = JSON.parseArray(topics);
								ArrayList<ComTopicBean> tempList = new ArrayList<ComTopicBean>();
								for (int i = 0; i < jsonArray.size(); i++) {
									JSONObject object = jsonArray
											.getJSONObject(i);
									tempList.add(JSON.toJavaObject(object,
											ComTopicBean.class));
								}
								if (queryType.equals("new")) {
									if (tempList.size() < 20) {
										list.addAll(0, tempList);
									} else {
										list.clear();
										list.addAll(tempList);
									}
									onLoad(tempList.size());
								} else {
									list.addAll(tempList);
								}
								// 去重
								for (int i = 0; i < list.size(); i++) {// 外循环是循环的次数
									for (int j = list.size() - 1; j > i; j--) {// 内循环是外循环一次比较的次数
										if (list.get(i).equals(list.get(j))) {
											list.remove(i);
										}
									}
								}
								tempList.clear();
								adapter.notifyDataSetChanged();
								if (list.size() > 0) {
									listView.setPullLoadEnable(true, "");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							// CommonToast.makeText(context, "获取帖子列表失败");
						}
						listView.stopRefresh();
						listView.stopLoadMore();
						listView.setPullLoadEnable(true, "");
						listView.setPullRefreshEnable(true);
					}
				});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.home_btn_filter:
			if (childTopicList == null || childTopicList.size() == 0) {
				CommonToast.makeText(context, "暂无子板块");
				break;
			}
			showExitPopWindow(context, btn_filter, childTopicList);
			break;
		case R.id.home_btn_right:// 搜索
			startActivity(new Intent(context, CommunitySearchActivity.class));
			break;
		case R.id.com_child_publish_topic:// 发布话题
			intent = new Intent(context, PublishTopicActivity.class);
			intent.putExtra("boardId", boardId);
			startActivity(intent);
			break;
		case R.id.com_child_focus:// 关注
			if (!sp.getLoginState(context)) {
				intent = new Intent(context, LoginActivity.class);
				startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
				return;
			}
			formFavorite(favorited, boardId);
			break;
		}
	}

	/**
	 * 关注功能
	 * 
	 * @param state
	 *            当前状态
	 * @param boardId
	 */
	private void formFavorite(final String state, String boardId) {
		String bizId = "000";
		String serviceName = "forumFavorite";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", boardId);
		map.put("target", "board");
		map.put("cllectionTime", System.currentTimeMillis() + "");
		if (state.equals("true")) {
			map.put("action", "destroy");
		} else {
			map.put("action", "create");
		}
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		String servicePara = JSON.toJSONString(map);
		// 调用关注接口
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());
							JSONObject obj = JSON.parseObject(result);
							if (obj.get("resultCode") != null
									&& obj.get("resultCode").toString()
											.equals("0")) {// 获取验证码成功
								// com_detail_focus.set
								if (state.equals("true")) {
									com_child_focus
											.setCompoundDrawables(
													null,
													initDrawable(R.mipmap.btn_follow),
													null, null);
									com_child_focus.setText("关注");
									// CommonToast.makeText(context, "取消关注");
									favorited = "false";
								} else {
									com_child_focus
											.setCompoundDrawables(
													null,
													initDrawable(R.mipmap.btn_followed),
													null, null);
									com_child_focus.setText("取消关注");
									// CommonToast.makeText(context, "关注成功");
									favorited = "true";
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 初始化drawable
	 * 
	 * @return
	 */
	private Drawable initDrawable(int drawableId) {
		Drawable drawable = context.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		return drawable;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		listView.setPullLoadEnable(false, "");
		list.clear();
		adapter.notifyDataSetChanged();
		switch (arg1) {
		case R.id.child_publish:// 最新发布
			page = 1;
			currentOrderBy = "releaseTime";
			break;
		case R.id.child_reply:// 最新回复
			page = 1;
			currentOrderBy = "commentTime";
			break;
		case R.id.child_hot:// 热点话题
			page = 1;
			currentOrderBy = "hotTopic";
			break;
		}
		getListViewList(page, currentOrderBy, "old");
	}

	PopupWindow popWindow;

	/**
	 * 
	 * @param context
	 */
	public void showExitPopWindow(Context context, View parentView,
			final ArrayList<SubBoardsBean> topics) {
		View view = LinearLayout.inflate(context,
				R.layout.topiclist_pop_layout, null);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		ListView popListView = (ListView) view
				.findViewById(R.id.com_com_child_listview);
		TopiclistAdapter4GridView childAdapter = new TopiclistAdapter4GridView(
				context, topics);
		popListView.setAdapter(childAdapter);
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((TopiclistActivity) getActivity()).setTitle(topics.get(
						position).getBoardTitle());
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					popWindow = null;
				}
				listView.setPullLoadEnable(false, "");
				list.clear();
				boardId = topics.get(position).getId();
				currentOrderBy = "releaseTime";
				page = 1;
				if (!child_publish.isChecked()) {
					child_publish.setChecked(true);
				} else {
					getListViewList(page, currentOrderBy, "old");
				}

				favorited = topics.get(position).getFavorite() + "";
				if (favorited.equals("true")) {
					com_child_focus.setCompoundDrawables(null,
							initDrawable(R.mipmap.btn_followed), null, null);
					com_child_focus.setText("取消关注");
				} else {
					com_child_focus.setCompoundDrawables(null,
							initDrawable(R.mipmap.btn_follow), null, null);
					com_child_focus.setText("关注");
				}
			}
		});
		popWindow = new PopupWindow(view,
				CommonUtil.getScreenWide(getActivity()) * 3 / 4,
				LayoutParams.MATCH_PARENT);
		popWindow.setAnimationStyle(R.style.pop_style);
		popWindow.setFocusable(true);
		popWindow.showAsDropDown(parentView, 0, -dip2px(context, 50));
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					popWindow = null;
				}
				return false;
			}
		});
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 0) {
				refreshState();
			}
		}
	}
	
	private void refreshState(){
		String bizId = "000";
		String serviceName = "queryBoard";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());
							JSONObject obj = JSON.parseObject(result);
							if (obj.get("resultCode") != null
									&& obj.get("resultCode").toString()
											.equals("0")) {// 获取验证码成功
								String boards = obj.getString("boards");
								JSONArray jsonArray = JSON
										.parseArray(boards);
								communitylist.clear();
								for (int i = 0; i < jsonArray.size(); i++) {
									JSONObject object = (JSONObject) jsonArray.get(i);
									CommunityBean communityBean = JSON.toJavaObject(object,CommunityBean.class);
									communitylist.add(communityBean);
								}
								for (int i = 0; i < communitylist.size(); i++) {
									if (boardId.equals(communitylist.get(i).getId())) {
										if (communitylist.get(i).getFavorite()) {
											com_child_focus.setCompoundDrawables(
															null,initDrawable(R.mipmap.btn_followed),
															null, null);
											com_child_focus.setText("取消关注");
										} else {
											com_child_focus.setCompoundDrawables(
															null,initDrawable(R.mipmap.btn_follow),
															null, null);
											com_child_focus.setText("关注");
										}
									} else {
										SubBoardslist.clear();
										SubBoardslist.addAll(JSONArray.parseArray(communitylist.get(i).getSubBoards(),SubBoardsBean.class));
										for (int j = 0; j < SubBoardslist.size(); j++) {
											if (boardId.equals(SubBoardslist.get(j).getId())) {
												if (SubBoardslist.get(j).getFavorite()) {
													com_child_focus.setCompoundDrawables(
															null,initDrawable(R.mipmap.btn_followed),
															null,null);
													com_child_focus.setText("取消关注");
												} else {
													com_child_focus.setCompoundDrawables(null,
															initDrawable(R.mipmap.btn_follow),
															null,null);
													com_child_focus.setText("关注");
												}
											}
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
}
