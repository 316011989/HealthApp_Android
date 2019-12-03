/**
 * 
 */
package com.pact.healthapp.components.publishtopic;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;

import java.util.HashMap;

/**
 * 回复评论功能
 * 
 * @author zhangyl
 * 
 */
@SuppressLint("ValidFragment")
public class PublishCommentFramgent extends BaseFragment implements
		OnClickListener {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();
	private View view;
	private TextView btn_left;//
	private Button btn_right;
	private LinearLayout btn_right_;
	private EditText publishcomment_edit_content;// 帖子内容输入框
	private TextView publishcomment_content_num;// 内容字数

	private PopupWindow pop_cancelpublish;
	private View cancelpublishView; // pop_cancelpublish窗口
	private Button publishcomment_popup_save;// pop中选择保存
	private Button publishcomment_popup_delete;// pop中选择不保存
	private Button publishcomment_popup_cancel;// pop中取消

	private String commentContent;// 帖子内容

	private String topicId;
	private String commentId;
	private String nickName;

	public PublishCommentFramgent() {
	}

	/**
	 * 回复帖子
	 * 
	 * @param topicId
	 */
	public PublishCommentFramgent(String topicId) {
		this.topicId = topicId;
	}

	/**
	 * 回复评论
	 * 
	 * @param topicId
	 * @param commentId
	 */
	public PublishCommentFramgent(String topicId, String commentId,
			String nickName) {
		this.topicId = topicId;
		this.commentId = commentId;
		this.nickName = nickName;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.publishcomment_layout, null);
		ViewUtils.inject(this, view);
		if (nickName == null || nickName.equals("")) {
			((PublishTopicActivity) getActivity()).setTitle("回复");
		} else {
			((PublishTopicActivity) getActivity()).setTitle("回复--" + nickName);
		}

		publishcomment_edit_content = (EditText) view
				.findViewById(R.id.publishcomment_edit_content);
		publishcomment_content_num = (TextView) view
				.findViewById(R.id.publishcomment_content_num);
		btn_left = ((PublishTopicActivity) getActivity()).leftView;
		btn_right = ((PublishTopicActivity) getActivity()).btn_right;
		btn_left.setVisibility(View.VISIBLE);
		btn_left.setText("取消");
		btn_right.setText("发布");
		btn_right.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		btn_right.setTextColor(getResources().getColor(
				android.R.color.darker_gray));
		btn_right.setOnClickListener(this);
		btn_right.setClickable(false);
		btn_left.setOnClickListener(this);
		publishcomment_edit_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				publishcomment_content_num.setText(s.length() + "/5000");
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (publishcomment_edit_content.getText().toString().length() > 0) {
					btn_right.setTextColor(getResources().getColor(
							android.R.color.white));
					btn_right.setClickable(true);
				} else {
					btn_right.setTextColor(getResources().getColor(
							android.R.color.darker_gray));
					btn_right.setClickable(false);
				}
			}

		});
		initpop_cancelpublish();// 创建pop_cancelpublish
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.home_btn_right:
			forumPostcomment();// 发布评论
			break;
		case R.id.home_btn_left_view:
			getActivity().finish();
			break;
		case R.id.publishtopic_popup_save:// pop中点击保存
			pop_cancelpublish.dismiss();
			// CommonToast.makeText(context, "保存草稿");
			getActivity().finish();
			break;
		case R.id.publishtopic_popup_delete:// pop中不保存
			pop_cancelpublish.dismiss();
			// CommonToast.makeText(context, "不保存草稿");
			getActivity().finish();
			break;
		case R.id.publishtopic_popup_cancel:// pop中取消
			pop_cancelpublish.dismiss();
			break;
		}
	}

	/**
	 * @author zhangyl
	 */
	private void forumPostcomment() {
		commentContent = publishcomment_edit_content.getText().toString();
		if (commentContent.equals("")) {
			CommonToast.makeText(context, "请输入评论内容");
			return;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", sp.getLoginInfo(context, "customerId"));
		map.put("token",sp.getLoginInfo(context, "token"));
		map.put("topicId", topicId);
		map.put("content", commentContent);
		map.put("commentTime", System.currentTimeMillis() + "");
		if (commentId != null && !commentId.equals("")) {
			map.put("commentId", commentId);
		}
		String servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, "000", "forumPostComment", servicePara,
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
		if (obj.get("resultCode") != null
				&& obj.get("resultCode").toString().equals("0")) {// 请求成功
			// CommonToast.makeText(context, "评论发布成功");
			getActivity().finish();
		}

	}

	/**
	 * 创建pop_takephoto方法
	 */
	private void initpop_cancelpublish() {
		// 加载PopupWindow的布局文件
		cancelpublishView = LayoutInflater.from(context).inflate(
				R.layout.publishtopic_popup_cancelpublish, null);
		pop_cancelpublish = new PopupWindow(cancelpublishView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pop_cancelpublish.setFocusable(true);// 获取popup中控件焦点 ,设置PopupWindow可触摸
		pop_cancelpublish.setBackgroundDrawable(new ColorDrawable(0xb0000000));
		pop_cancelpublish.setOutsideTouchable(true);
		pop_cancelpublish.setAnimationStyle(R.style.popupwindow_anim);
		publishcomment_popup_save = (Button) cancelpublishView
				.findViewById(R.id.publishtopic_popup_save);
		publishcomment_popup_delete = (Button) cancelpublishView
				.findViewById(R.id.publishtopic_popup_delete);
		publishcomment_popup_cancel = (Button) cancelpublishView
				.findViewById(R.id.publishtopic_popup_cancel);
		publishcomment_popup_save.setOnClickListener(this);
		publishcomment_popup_delete.setOnClickListener(this);
		publishcomment_popup_cancel.setOnClickListener(this);
	}

}
