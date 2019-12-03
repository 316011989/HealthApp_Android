package com.pact.healthapp.components.topicdetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.community.ComTopicBean;
import com.pact.healthapp.components.community.ComTopicCustomerBean;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.components.publishtopic.CommentsBean;
import com.pact.healthapp.components.publishtopic.PublishTopicActivity;
import com.pact.healthapp.components.publishtopic.ReCommentBean;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.imagepick.adapter.PictrueGVAdapter;
import com.pact.healthapp.view.imageshow.ImageShowActivity;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class TopicdetailFramgent extends BaseFragment {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	@ViewInject(R.id.com_detail_img)
	private ImageView com_detail_img;// 用户头像
	@ViewInject(R.id.com_detail_name)
	private TextView com_detail_name;// 用户昵称
	@ViewInject(R.id.com_detail_time)
	private TextView com_detail_time;// 发布时间
	@ViewInject(R.id.com_from_community)
	private TextView com_from_community;// 来自模块
	@ViewInject(R.id.com_detail_landlord)
	private Button com_detail_landlord;// 楼主按钮
	@ViewInject(R.id.com_detail_title)
	private TextView com_detail_title;// 标题
	@ViewInject(R.id.com_detail_content)
	private TextView com_detail_content;// 话题内容
	@ViewInject(R.id.com_detail_gridview)
	private GridView com_detail_gridview;// 用户头像集合
	@ViewInject(R.id.com_detail_replyNum)
	private TextView com_detail_replyNum;// 回复数量
	@ViewInject(R.id.com_detail_browse)
	private TextView com_detail_browse;// 浏览数量
	@ViewInject(R.id.com_detail_focus)
	private Button com_detail_focus;// 关注
	@ViewInject(R.id.com_detail_reply)
	private Button com_detail_reply;// 回复
	@ViewInject(R.id.com_detail_container)
	private LinearLayout com_detail_container;
	private ComTopicBean comTopicBean;
	// String[] pics;
	private ArrayList<String> imagePathList;
	private String filterByAuther = "0";// 0：全部
	private boolean isViewFloor = true;

	public TopicdetailFramgent() {
	}

	public TopicdetailFramgent(ComTopicBean comTopicBean) {
		this.comTopicBean = comTopicBean;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.topicdetail_layout, null);
		ViewUtils.inject(this, view);

		com_detail_name.setText(comTopicBean.getCustomer().getNickName());
		myApplication.bitmapUtils.display(com_detail_img, comTopicBean
				.getCustomer().getHeadImg());
		com_detail_time.setText(CommonUtil.getStampTimeStr(comTopicBean
				.getReleaseTime()));
		com_detail_title.setText(comTopicBean.getTitle());
		com_detail_content.setText("\t" + comTopicBean.getContent());
		com_detail_browse.setText(comTopicBean.getClickCount());
		com_detail_replyNum.setText(comTopicBean.getCommentsCount());
		com_from_community.setText("来自板块："
				+ comTopicBean.getBoard().getBoardTitle());

		int wide = CommonUtil.getScreenWide(getActivity());
		int imageWide = (wide - CommonUtil.convertDpToPixelInt(context,
				(20 + 30))) / 3;
		imagePathList = comTopicBean.getPics();
		int num;
		if (imagePathList.size() < 3 && imagePathList.size() > 0) {
			num = 1;
		} else {
			num = (int) Math.ceil((float) imagePathList.size() / (float) 3);
		}
		// 计算图片列表高度
		int gridHeight = imageWide * num
				+ CommonUtil.convertDpToPixelInt(context, 10 + (num - 1) * 10);
		com_detail_gridview.setAdapter(new PictrueGVAdapter(context,
				imagePathList));
		com_detail_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ImageShowActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("PATHS", imagePathList);
				bundle.putInt("POSITION", position);
				bundle.putInt("TYPE", 1);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		if (comTopicBean.getFavorited()) {
			com_detail_focus.setSelected(true);
			com_detail_focus.setText("取消收藏");
		} else {
			com_detail_focus.setSelected(false);
			com_detail_focus.setText("收藏");
		}

		com_detail_focus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!sp.getLoginState(context)) {
					Intent intent = new Intent(context, LoginActivity.class);
					startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
					return;
				}
				formFavorite(comTopicBean.getFavorited(), comTopicBean.getId());
			}
		});
		initHeight(LayoutParams.MATCH_PARENT, gridHeight, com_detail_gridview);
		((ScrollView) view.findViewById(R.id.scrollView))
				.smoothScrollTo(10, 10);
		getComments();
		com_detail_reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String topicId = comTopicBean.getId();
				Intent intent = new Intent(context, PublishTopicActivity.class);
				intent.putExtra("topicId", topicId);
				startActivity(intent);
			}
		});
		com_detail_landlord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isViewFloor) {
					viewFloor(); // 查看楼主评论
				} else {
					getComments(); // 查看全部评论
				}
			}
		});
		return view;
	}

	/**
	 * 收藏帖子
	 * 
	 * @author caotong
	 * @param state
	 * @param topicId
	 */
	private void formFavorite(final boolean state, String topicId) {
		// 拼接参数， 请求验证码
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", topicId);
		map.put("target", "topic");
		map.put("cllectionTime", System.currentTimeMillis() + "");
		if (state) {
			map.put("action", "destroy");
		} else {
			map.put("action", "create");
		}
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		String para = JSON.toJSONString(map);
		// 发送请求，获取验证码
		ServiceEngin.Request(context, "", "forumFavorite", para,
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
								if (state) {
									com_detail_focus.setSelected(false);
									com_detail_focus.setText("收藏");
								} else {
									com_detail_focus.setSelected(true);
									com_detail_focus.setText("取消收藏");
								}
								comTopicBean.setFavorited(!state);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取评论接口 filterByAuther 0：全部
	 * 
	 * @author zhangyl
	 */
	private void getComments() {
		// 拼接参数， 请求验证码
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("topicId", comTopicBean.getId());
		map.put("pageSize", "50");
		map.put("page", "1");
		map.put("filterByAuther", filterByAuther);
		String para = JSON.toJSONString(map);
		// 发送请求，获取验证码
		ServiceEngin.Request(context, "", "forumShowComment", para,
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
								isViewFloor = true;
								com_detail_landlord.setText("楼主");
								String comments = obj.getString("comments");
								parseComments(comments);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 查看楼主
	 */
	private void viewFloor() {
		String bizId = "000";
		String serviceName = "forumShowComment";
		// 拼接参数， 请求验证码
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("topicId", comTopicBean.getId());
		map.put("viewFloor", "viewFloor");
		map.put("page", "1");
		map.put("pageSize", "20");
		String servicePara = JSON.toJSONString(map);
		// 发送请求，获取验证码
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
								isViewFloor = false;
								com_detail_landlord.setText("全部");
								String comments = obj.getString("comments");
								parseComments(comments);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 重新设置控件宽高
	 * 
	 * @param wide
	 * @param height
	 * @param view
	 */
	private void initHeight(int wide, int height, View view) {
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = height;
		params.width = wide;
		view.setLayoutParams(params);
	}

	/**
	 * 底部循环添加view(评论)
	 */
	private void addReplyView(LinearLayout layout, final CommentsBean comment) {
		View view = LinearLayout.inflate(context,
				R.layout.topicdetail_lvitem_layout, null);
		ImageView com_detail_img = (ImageView) view
				.findViewById(R.id.com_detail_img);
		myApplication.bitmapUtils.display(com_detail_img, comment
				.getComTopicCustomerBean().getHeadImg());

		TextView com_detail_name = (TextView) view
				.findViewById(R.id.com_detail_name);
		com_detail_name
				.setText(comment.getComTopicCustomerBean().getNickName());
		TextView com_detail_time = (TextView) view
				.findViewById(R.id.com_detail_time);
		com_detail_time.setText(CommonUtil.getStampTimeStr(comment
				.getReleaseTime()));
		final Button com_detail_userful_btn = (Button) view
				.findViewById(R.id.com_detail_userful_btn);
		com_detail_userful_btn.setText("有用" + comment.getLikeCount());
		com_detail_userful_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int num = Integer.parseInt(com_detail_userful_btn.getText()
						.toString().replace("有用", ""));
				int likeCount = Integer.parseInt(comment.getLikeCount());
				if (num > likeCount) {
					CommonToast.makeText(context, "您已对该评论点赞");
					return;
				}

				// 拼接参数， 请求验证码
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("commentId", comment.getId());
				map.put("action", "create");
				map.put("customerId", sp.getLoginInfo(context, "customerId"));
				map.put("token", sp.getLoginInfo(context, "token"));
				String para = JSON.toJSONString(map);
				// 发送请求，获取验证码
				ServiceEngin.Request(context, "000", "forumLikeComment", para,
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
													.equals("0")
											&& obj.get("errorCode") != null
											&& obj.get("errorCode").toString()
													.equals("0")) {// 获取验证码成功
										com_detail_userful_btn.setText("有用"
												+ (Integer.parseInt(comment
														.getLikeCount()) + 1));
									} else if (obj.get("resultCode") != null
											&& obj.get("resultCode").toString()
													.equals("1")
											&& obj.get("errorCode") != null
											&& obj.get("errorCode").toString()
													.equals("1")) {
										CommonToast.makeText(context,
												"您已对该评论点赞");
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
			}
		});
		Button com_detail_reply_btn = (Button) view
				.findViewById(R.id.com_detail_reply_btn);
		com_detail_reply_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (!sp.getLoginState(context)) {
				// Intent intent = new Intent(context, LoginActivity.class);
				// context.startActivity(intent);
				// return;
				// }
				// String topicid = comment.getId();
				String topicid = comTopicBean.getId();
				String commentId = comment.getId();
				String nickName = comment.getComTopicCustomerBean()
						.getNickName();
				Intent intent = new Intent(context, PublishTopicActivity.class);
				intent.putExtra("topicId", topicid);
				intent.putExtra("commentId", commentId);
				intent.putExtra("nickName", nickName);
				startActivity(intent);
			}
		});
		TextView com_detail_title = (TextView) view
				.findViewById(R.id.com_detail_title);
		com_detail_title.setText(comment.getContent());
		LinearLayout com_reply_layout = (LinearLayout) view
				.findViewById(R.id.com_reply_layout);
		if (comment.getCommentBean() == null) {
			com_reply_layout.setVisibility(View.GONE);
		} else {
			TextView com_detail_reply_name = (TextView) view
					.findViewById(R.id.com_detail_reply_name);
			com_detail_reply_name.setText(comment.getCommentBean()
					.getComTopicCustomerBean().getNickName());
			TextView com_detail_reply_time = (TextView) view
					.findViewById(R.id.com_detail_reply_time);
			com_detail_reply_time.setText(CommonUtil.getStampTimeStr(comment
					.getCommentBean().getReleaseTime()));
			TextView com_detail_reply_content = (TextView) view
					.findViewById(R.id.com_detail_reply_content);
			com_detail_reply_content.setText(comment.getCommentBean()
					.getContent());
			com_reply_layout.setVisibility(View.VISIBLE);
		}
		layout.addView(view);
	}

	/**
	 * 解析评论的json并展示数据
	 * 
	 * @author zhangyl
	 * @param comments
	 */
	private void parseComments(String comments) {
		ArrayList<CommentsBean> list = new ArrayList<CommentsBean>();
		try {
			JSONArray jsonArray = JSON.parseArray(comments);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				CommentsBean comCommentsBean = JSON.toJavaObject(object,
						CommentsBean.class);
				if (object.containsKey("customer")) {
					comCommentsBean
							.setComTopicCustomerBean(parseCustomer(object
									.getString("customer")));
				}
				if (object.containsKey("refComment")) {
					comCommentsBean.setCommentBean(parseComRef(object
							.getString("refComment")));
				}
				list.add(comCommentsBean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			com_detail_container.removeAllViews();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list.size() > 0) {
			com_detail_replyNum.setText(list.size() + "");
			for (CommentsBean comCommentsBean : list) {
				addReplyView(com_detail_container, comCommentsBean);
			}
		}
	}

	/**
	 * 解析获取验证码请求返回的customer
	 */
	private ComTopicCustomerBean parseCustomer(String customer) {
		if (customer == null || customer.length() == 0) {
			return null;
		}
		JSONObject object = JSON.parseObject(customer);
		ComTopicCustomerBean comTopicCustomerBean = new ComTopicCustomerBean();
		comTopicCustomerBean.setHeadImg(object.getString("headImg"));
		comTopicCustomerBean.setId(object.getString("id"));
		comTopicCustomerBean.setNickName(object.getString("nickName"));
		return comTopicCustomerBean;
	}

	/**
	 * 解析获取验证码请求返回的ComRefCommentBean
	 */
	private ReCommentBean parseComRef(String customer) {
		if (customer == null || customer.length() == 0) {
			return null;
		}
		JSONObject object = JSON.parseObject(customer);
		ReCommentBean comCommentsBean = new ReCommentBean();
		comCommentsBean.setContent(object.getString("content"));
		comCommentsBean.setId(object.getString("id"));
		comCommentsBean.setComTopicCustomerBean(parseCustomer(object
				.getString("customer")));
		comCommentsBean.setLikeCount(object.getString("likeCount"));
		comCommentsBean.setReleaseTime(object.getString("releaseTime"));
		return comCommentsBean;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getComments();
		refreshState();
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

	private void refreshState() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token", sp.getLoginInfo(context, "token"));
		map.put("topicId", comTopicBean.getId());
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
								if (comTopicBean.getFavorited()) {
									com_detail_focus.setSelected(true);
									com_detail_focus.setText("取消收藏");
								} else {
									com_detail_focus.setSelected(false);
									com_detail_focus.setText("收藏");
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
