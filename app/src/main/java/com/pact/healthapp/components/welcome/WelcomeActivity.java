package com.pact.healthapp.components.welcome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.pact.healthapp.R;
import com.pact.healthapp.components.im.applib.User;
import com.pact.healthapp.components.im.applib.UserDao;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.universal.FrameworkManager;
import com.pact.healthapp.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends BaseFragmentActivity {
	private SharedPreferenceUtils sp = new SharedPreferenceUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApplication.isLogin = false;
		setContenierView(2);
		myApplication.addActivity(this);
		mFragmentManager.beginTransaction()
				.replace(R.id.login_fl_continer, new SplashFragment())
				.addToBackStack("LogoFragment").commit();
		startTime();
		initBaidu();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void startTime() {
		new Thread(new Runnable() {
			public void run() {
				SystemClock.sleep(1000);
				handler.sendMessage(new Message());
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (sp.getFirstOpenState(context)) {// 首次启动应用,打开引导页
				mFragmentManager.beginTransaction()
						.replace(R.id.login_fl_continer, new WelcomeFragment())
						.addToBackStack("WelcomeFragment").commitAllowingStateLoss();
				sp.setFirstOpenState(context, false);// 设置为启动过
			} else {// 非首次启动应用
				FrameworkManager.branch(WelcomeActivity.this,
						Constants.moduleList.get(0));
				WelcomeActivity.this.finish();
			}
		};
	};

	/**
	 * 登录环信
	 *
	 * @author zhangyl
	 */
//	private void initLoginEMChat(String userName, String password) {
//		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
//					@Override
//					public void onSuccess() {
//						runOnUiThread(new Runnable() {
//							public void run() {
//								EMGroupManager.getInstance().loadAllGroups();
//								EMChatManager.getInstance()
//										.loadAllConversations();// 这两个方法是加载聊天记录和群组的
//								// try {
//								// processContactsAndGroups();
//								// } catch (EaseMobException e) {
//								// e.printStackTrace();
//								// }
//							}
//						});
//					}
//
//					@Override
//					public void onProgress(int progress, String status) {
//
//					}
//
//					@Override
//					public void onError(int code, String message) {
//						runOnUiThread(new Runnable() {
//							public void run() {
//							}
//						});
//					}
//				});
//	}

	private void processContactsAndGroups() throws EaseMobException {
		// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
		List<String> usernames = EMContactManager.getInstance()
				.getContactUserNames();
		EMLog.d("roster", "contacts size: " + usernames.size());
		Map<String, User> userlist = new HashMap<String, User>();
		for (String username : usernames) {
			User user = new User();
			user.setUsername("465798");
			// user.setUsername(username);
			setUserHearder(username, user);
			userlist.put(username, user);
		}
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constants.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constants.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constants.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constants.GROUP_USERNAME, groupUser);

		// 存入内存
		myApplication.setContactList(userlist);
		System.out.println("----------------" + userlist.values().toString());
		// 存入db
		UserDao dao = new UserDao(context);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);

		// 获取黑名单列表
		List<String> blackList = EMContactManager.getInstance()
				.getBlackListUsernamesFromServer();
		// 保存黑名单
		EMContactManager.getInstance().saveBlackList(blackList);

		// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
		EMGroupManager.getInstance().getGroupsFromServer();
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 *
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constants.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

}
