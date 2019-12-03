package com.pact.healthapp.components.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.changepsd.ChangePasswordActivity;
import com.pact.healthapp.components.im.applib.User;
import com.pact.healthapp.components.im.applib.UserDao;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@SuppressLint("ValidFragment")
public class LoginFragment extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    @ViewInject(R.id.login_login_login)
    private Button login_login_login;
    @ViewInject(R.id.login_bt_forget_password)
    private TextView login_bt_forget_password;
    @ViewInject(R.id.login_login_account)
    private EditText login_login_account;
    @ViewInject(R.id.login_login_password)
    private EditText login_login_password;
    private Button register;
    @ViewInject(R.id.login_bottom_layout)
    private ViewGroup login_bottom_layout;
    @ViewInject(R.id.login_bottom)
    private ViewGroup login_bottom;
    @ViewInject(R.id.login_gridview)
    private GridView login_gridview;
    @ViewInject(R.id.login_spread_top)
    private ImageView login_spread_top;

    private String username, password;

    // 第三方登录窗口适配器
    private LoginOauthAdapter adapter;
    // 第三方登录窗口适配器数据
    private ArrayList<LoginBean> oauthList = new ArrayList<LoginBean>();
    // 第三方登录窗口文字数据
    // private String[] oauthData = { "微信登录", "微博登录", "QQ登录" };
    private String[] oauthData = {"微博登录", "QQ登录"};
    // 第三方登录窗口资源数据
    // private int[] oauthDrawable = { R.drawable.login_weixin,
    // R.drawable.login_weibo, R.drawable.login_qq };
    private int[] oauthDrawable = {R.mipmap.login_weibo, R.mipmap.login_qq};

    public LoginFragment(String phoneNumber) {
        username = phoneNumber;

    }

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.login_login_layout, null);
        ViewUtils.inject(this, view);
        // 测试 账号
        ((LoginActivity) getActivity()).btn_left.setVisibility(View.VISIBLE);
        ((LoginActivity) getActivity()).btn_right.setVisibility(View.VISIBLE);
        getActivity().setTitle("登录");
        register = ((LoginActivity) getActivity()).btn_right;
        register.setVisibility(View.VISIBLE);
        initUM();
        initRightView(register);
        // 从修改密码返回至登录界面时，自动填写账号，输入新密码登录
        if (username != null && !username.equals("")) {
            login_login_account.setText(username);
        }
        /*
		 * 登录
		 */
        login_login_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                username = login_login_account.getText().toString();
                password = login_login_password.getText().toString();
                if (username.equals("")) {
                    CommonToast.makeText(context, "请输入登录账号");
                    return;
                }
                if (password.equals("")) {
                    CommonToast.makeText(context, "请输入密码");
                    return;
                }
                // if (!CommonUtil.isMobileNO(username)
                // && !CommonUtil.isEmail(username)
                // && !CommonUtil.isIdCard(username)) {
                // CommonToast.makeText(context, "请输入正确的账号");
                // return;
                // }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("accountName", username);
                map.put("password", password);
                String para = JSON.toJSONString(map);
                ServiceEngin.Request(context, "000", "login", para,
                        new EnginCallback(context) {
                            @Override
                            public void onSuccess(ResponseInfo arg0) {
                                super.onSuccess(arg0);
                                String result = Des3.decode(arg0.result
                                        .toString());
                                ParseJson(result);
                            }
                        });
            }
        });
        // 忘记密码按钮
        login_bt_forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(context, ChangePasswordActivity.class));
            }
        });
        return view;
    }

    /**
     * @author zhangyl
     */
    private void initUM() {
        // TODO Auto-generated method stub
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, Constants.WX_APPID,
                Constants.WX_APPSECRET);
        wxHandler.addToSocialSDK();
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
                (LoginActivity) context, Constants.QQ_APPID,
                Constants.QQ_APPKEY);
        qqSsoHandler.addToSocialSDK();
        // 设置新浪SSO handler
        ((LoginActivity) getActivity()).mController.getConfig().setSsoHandler(
                new SinaSsoHandler());
    }

    /**
     * 注册按钮
     */
    private void initRightView(Button view) {
        view.setText(R.string.regist_str);
        view.setVisibility(View.VISIBLE);
        view.setTextColor(getResources().getColor(R.color.white));
        view.setBackgroundDrawable(null);
        view.setTextSize(14);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                register.setVisibility(View.GONE);
                FragmentTransaction mFragmentManager = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                mFragmentManager
                        .replace(R.id.login_fl_continer,
                                new RegistrationFragment())
                        .addToBackStack("RegistrationFragment").commit();
            }
        });
        if (oauthList.size() <= 0) {
            oauthList.add(getLoginBean(0));
            oauthList.add(getLoginBean(1));
        }
        adapter = new LoginOauthAdapter(getActivity(), oauthList);
        login_gridview.setAdapter(adapter);
        login_gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String oauthString = oauthList.get(position).getLeft();
                if (oauthString.equals("微信登录")) {
                    doOauthVerify(SHARE_MEDIA.WEIXIN, Constants.REGIST_TYPE_WX);
                }
                if (oauthString.equals("微博登录")) {
                    doOauthVerify(SHARE_MEDIA.SINA, Constants.REGIST_TYPE_SINA);
                }
                if (oauthString.equals("QQ登录")) {
                    doOauthVerify(SHARE_MEDIA.QQ, Constants.REGIST_TYPE_QQ);
                }
            }
        });
    }

    boolean state = false;

    private LoginBean getLoginBean(int i) {
        LoginBean loginBean = new LoginBean();
        loginBean.setLeft(oauthData[i]);
        loginBean.setLeftDrawable(oauthDrawable[i]);
        return loginBean;
    }

    /**
     * 解析服务器返回Json
     */
    private void ParseJson(String result) {
        JSONObject obj = JSON.parseObject(result);
        if (obj.get("resultCode") != null
                && Integer.parseInt(obj.get("resultCode").toString()) == 0) {// 请求成功
            SharedPreferenceUtils sp = new SharedPreferenceUtils();
            sp.setFirstOpenState(context, false);
            sp.saveLoginInfo(getActivity(), "customerId",
                    obj.getString("customerId"));
            sp.saveLoginInfo(getActivity(), "token", obj.getString("token"));
            sp.saveLoginInfo(getActivity(), "birth", obj.getString("birth"));
            sp.saveLoginInfo(getActivity(), "email", obj.getString("email"));
            sp.saveLoginInfo(getActivity(), "gender", obj.getString("gender"));
            sp.saveLoginInfo(getActivity(), "headImg", obj.getString("headImg"));
            sp.saveLoginInfo(getActivity(), "idCard", obj.getString("idCard"));
            sp.saveLoginInfo(getActivity(), "marriage",
                    obj.getString("marriage"));
            sp.saveLoginInfo(getActivity(), "nickName",
                    obj.getString("nickName"));
            sp.saveLoginInfo(getActivity(), "phoneNumber",
                    obj.getString("phoneNumber"));
            sp.saveLoginInfo(getActivity(), "huanXinId",
                    obj.getString("huanXinId"));
            sp.saveLoginInfo(getActivity(), "huanXinPwd",
                    obj.getString("huanXinPwd"));
            sp.saveLoginInfo(getActivity(), "otherLogin",
                    obj.getString("otherLogin"));
            sp.saveLoginInfo(getActivity(), "account", username);
            initLoginEMChat(obj.getString("huanXinId"),
                    obj.getString("huanXinPwd"));
            // FrameworkManager.branch(getActivity(),
            // Constants.moduleList.get(0));
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
        // if (obj.get("resultCode").toString().equals("1")) {
        // CommonToast.makeText(context, obj.getString("resultMsg"));
        // }
    }

    /**
     * 登录环信
     *
     * @author zhangyl
     */
    private void initLoginEMChat(String userName, String password) {
        EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
            @Override
            public void onSuccess() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance()
                                .loadAllConversations();// 这两个方法是加载聊天记录和群组的,在splash中的ocreate也添加
                        // try {
                        // processContactsAndGroups();
                        // } catch (EaseMobException e) {
                        // // TODO Auto-generated catch block
                        // e.printStackTrace();
                        // }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });

            }
        });
    }

    private void processContactsAndGroups() throws EaseMobException {
        // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
        List<String> usernames = EMContactManager.getInstance()
                .getContactUserNames();
        EMLog.d("roster", "contacts size: " + usernames.size());
        Map<String, User> userlist = new HashMap<String, User>();
        for (String username : usernames) {
            User user = new User();
            user.setUsername(username);
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

    /**
     * 第三方授权登录
     *
     * @param platform    第三方登录所用平台
     * @param regist_type 第三方登录所用平台类型11微信,12微博,13QQ
     * @author zhangyl
     */
    private void doOauthVerify(SHARE_MEDIA platform, final int regist_type) {
        ((LoginActivity) getActivity()).mController.doOauthVerify(context,
                platform, new UMAuthListener() {
                    @Override
                    public void onError(SocializeException e,
                                        SHARE_MEDIA platform) {
                        // CommonToast.makeText(context, "授权出现错误");
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        if (value != null
                                && !TextUtils.isEmpty(value.getString("uid"))) {
                            // CommonToast.makeText(context, "授权成功.");
                            getPlatformInfo(platform, value.getString("uid"),
                                    regist_type);
                        } else {
                            // CommonToast.makeText(context, "授权失败");
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        // CommonToast.makeText(context, "取消授权");
                    }

                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        // CommonToast.makeText(context, "开始授权");
                    }
                });
    }

    /**
     * 获取平台信息
     *
     * @author zhangyl
     */
    private void getPlatformInfo(SHARE_MEDIA platform, final String openId,
                                 final int regist_type) {
        ((LoginActivity) getActivity()).mController.getPlatformInfo(context,
                platform, new UMDataListener() {
                    @Override
                    public void onStart() {
                        // CommonToast.makeText(context, "获取平台数据开始...");
                    }

                    @Override
                    public void onComplete(int arg0, Map<String, Object> arg1) {
                        if (arg0 == 200 && arg1 != null) {
                            // StringBuilder sb = new StringBuilder();
                            Set<String> keys = arg1.keySet();
                            String screen_name = "", profile_image_url = "", gender = "";
                            for (String key : keys) {
                                // sb.append(key + "=" +
                                // arg1.get(key).toString()
                                // + "\r\n");
                                if (key.equals("screen_name")) {
                                    screen_name = arg1.get(key).toString();
                                }
                                if (key.equals("profile_image_url")) {
                                    profile_image_url = arg1.get(key)
                                            .toString();
                                }
                                if (key.equals("gender")) {
                                    gender = arg1.get(key).toString();
                                }

                            }
                            otherLogin(screen_name, profile_image_url, gender,
                                    openId, regist_type);
                        } else {
                            LogUtils.e("发生错误：" + arg0);
                        }
                    }
                });
    }

    /**
     * 第三方登陆
     *
     * @param openid 第三方登录返回的唯一标识
     * @param type   第三方登录的方式(11微信,12微博,13QQ)
     * @author zhangyl
     */
    public void otherLogin(String screen_name, String profile_image_url,
                           String gender, String openid, int type) {
        String serviceName = "otherLogin";
        String bizId = "000";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("screen_name", screen_name);
        map.put("openid", openid);
        map.put("type", type + "");
        map.put("profile_image_url", profile_image_url);
        if (gender.equals("男"))
            gender = "1";
        if (gender.equals("女"))
            gender = "2";
        map.put("gender", gender); // 1：男，2：女
        String servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0);
                        String result = Des3.decode(arg0.result.toString());
                        ParseJson(result);
                    }

                });
    }

}
