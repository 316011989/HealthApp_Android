package com.pact.healthapp.components.push;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.pact.healthapp.components.event.EventDetailActivity;
import com.pact.healthapp.components.topicdetail.TopicdetailActivity;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.WebviewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下： 0 - Success 10001 - Network Problem 30600 - Internal
 * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
 * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
 * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = MyPushMessageReceiver.class
			.getSimpleName();

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + "appid="
				+ appid + "userId=" + userId + "channelId=" + channelId
				+ "requestId=" + requestId;
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			Utils.setBind(context, true);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		// updateContent(context, "绑定逻辑");
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			customJson = JSONObject.parseObject(customContentString);
			String myvalue = "";
			if (customJson.getString("custom_content") != null) {
				myvalue = customJson.getString("custom_content");
				JSONObject ja = customJson.getJSONObject("custom_content");
				String opentype = ja.getString("open_type");
				String id = ja.getString("message_id");
//				CommonToast.makeText(context, "打开方式为" + opentype + "id=" + id);
				// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
				// updateContent(context, 1, message);
			}

		}

	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		JSONObject customJson = JSONObject.parseObject(customContentString);
		if (customJson!=null&&customJson.getString("open_type") != null
				&& customJson.getString("message_id") != null) {
			String opentype = customJson.getString("open_type");
			String id = customJson.getString("message_id");
			// opentype=0是打开应用,1是topic,2是event,3是news
			if (opentype.equals("1")) {
				Intent intent = new Intent(context, TopicdetailActivity.class);
				intent.putExtra("topicId", id);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				context.startActivity(intent);
			} else if (opentype.equals("2")) {
				Intent intent = new Intent(context, EventDetailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				context.startActivity(intent);
			} else if (opentype.equals("3")) {
				Intent intent = new Intent(context, WebviewActivity.class);
				intent.putExtra("url", Constants.HOST + Constants.SHOWNEWS_PORT
						+ id);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				context.startActivity(intent);
				// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
				// updateContent(context, 1, "打开方式为" + opentype + "id=" + id);
			}
		}
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, 3, responseString);
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, 4, responseString);
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		Log.d(TAG, responseString);

		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, 5, responseString);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d(TAG, responseString);
		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			Utils.setBind(context, false);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		updateContent(context, 6, responseString);
	}

	public static List<Activity> activities = new LinkedList<Activity>();

	/**
	 * 启动更新逻辑
	 * 
	 * @param context
	 * @param content
	 */
	private void updateContent(Context context, int type, String content) {
		Log.d(TAG, "updateContent");
		String logText = null;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
		logText += sDateFormat.format(new Date()) + ": ";
		switch (type) {
		case 0:// 通知
//			CommonToast.makeText(context.getApplicationContext(), content);
			break;
		case 1:// 透传消息
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
					.getClassName();
			PushBean pushBean = new PushBean();
			if (content != null) {
				if (content.contains("文%")) {
					pushBean.setType(1);
					pushBean.setName(content.replace("文%", ""));
				} else {
					pushBean.setType(2);
					pushBean.setName(content.replace("图%", ""));
				}
			}
			if (runningActivity.contains("PushActivity")) {
				PushActivity pushActivity = (PushActivity) activities.get(0);
				pushActivity.onRefres(pushBean);
			} else {
				PushFramgent.list.add(0, pushBean);
			}
			break;
		case 2://

			break;
		case 3:// setTags() 的回调

			break;
		case 4:// delTags() 的回调

			break;
		case 5:// listTags() 的回调

			break;
		case 6:// PushManager.stopWork

			break;
		default:
			break;
		}
	}

}
