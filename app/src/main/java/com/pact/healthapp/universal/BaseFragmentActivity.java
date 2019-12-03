package com.pact.healthapp.universal;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.NetUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.UploadCallback;
import com.pact.healthapp.http.UploadFileEngin;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import java.io.File;

/**
 * 基类Activity,可自动继承各变量和界面框架,title栏底栏等
 *
 * @author zhangyl
 */
public class BaseFragmentActivity extends FragmentActivity implements
        OnClickListener {
    public CommonDialog dialog;
    protected FragmentManager mFragmentManager;
    public MyApplication myApplication;
    protected Context context;
    protected SQLiteDatabase db;
    // tab标签栏
    public LinearLayout home_tabs;
    // tab标签按钮
    public Button home_tab1_cb, home_tab2_cb, home_tab3_cb, home_tab4_cb,
            home_tab5_cb;
    // title栏
    public RelativeLayout home_titlebar;
    // title栏左按钮
    public Button btn_left;
    // title标题文字
    public TextView home_titile1, leftView;
    // title栏右按钮
    public Button btn_right;
    // title栏右侧按钮(圈子父版块点击选择子版块)
    public Button btn_filter;
    public int backtype;// 返回方式
    public final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    public NewMessageBroadcastReceiver msgReceiver;

    @Override
    /**
     * 供子类直接使用myApplication，db，mFragmentManager
     */
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.myApplication;
        db = myApplication.db;
        mFragmentManager = this.getSupportFragmentManager();
        initHXreceiver();
        uploadLog();
    }

    public void initBaidu() {
        // if (PushManager.isConnected(context)) {
        // if (PushManager.isPushEnabled(context)) {
        // return;
        // }
        // PushManager.resumeWork(context);
        // }
        /********** 初始化百度 ********/
        String pkgName = getPackageName();
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY, Constants.BAIDU_APIKEY);
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());
        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                getApplicationContext(), getResources().getIdentifier(
                "notification_custom_builder", "layout", pkgName),
                getResources()
                        .getIdentifier("notification_icon", "id", pkgName),
                getResources().getIdentifier("notification_title", "id",
                        pkgName), getResources().getIdentifier(
                "notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(R.mipmap.ic_launcher);
        cBuilder.setLayoutDrawable(R.mipmap.ic_launcher);
        PushManager.setNotificationBuilder(this, 1, cBuilder);
        /******* 推送结束 ********/
    }

    /**
     * @author zhangyl
     */
    private void initHXreceiver() {
        // TODO Auto-generated method stub
        // 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
    }

    /**
     * 接收新消息的监听广播
     *
     * @author zhangyl
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();
            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            // 发送方
            String username = intent.getStringExtra("from");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(username);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(username)) {
                // 消息不是发给当前会话，return
                return;
            }
        }
    }

    /**
     * @param showtabs 1.显示上不显示下;2.上下都不显示;3.只显示上但没有按钮;4.上下都显示但没有返回按钮;默认都显示
     */
    @TargetApi(19)
    public void setContenierView(int showtabs) {
        View view = LayoutInflater.from(this).inflate(
                R.layout.universal_base_layout, null);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			getWindow().addFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////			getWindow().addFlags(
////					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}
        setContentView(view);
        context = this;
        myApplication.addActivity(this);
        home_tabs = (LinearLayout) view.findViewById(R.id.home_tabs);
        home_titlebar = (RelativeLayout) view.findViewById(R.id.home_titlebar);
        btn_left = (Button) view.findViewById(R.id.home_btn_left);
        leftView = (TextView) view.findViewById(R.id.home_btn_left_view);
        home_titile1 = (TextView) view.findViewById(R.id.home_titile1);
        btn_right = (Button) view.findViewById(R.id.home_btn_right);
        btn_filter = (Button) view.findViewById(R.id.home_btn_filter);
        home_tab1_cb = (Button) view.findViewById(R.id.home_tab1_cb);
        home_tab2_cb = (Button) view.findViewById(R.id.home_tab2_cb);
        home_tab3_cb = (Button) view.findViewById(R.id.home_tab3_cb);
        home_tab4_cb = (Button) view.findViewById(R.id.home_tab4_cb);
        home_tab5_cb = (Button) view.findViewById(R.id.home_tab5_cb);
        if (showtabs == 1) {
            home_titlebar.setVisibility(View.VISIBLE);
            home_tabs.setVisibility(View.GONE);
        } else if (showtabs == 2) {
            home_tabs.setVisibility(View.GONE);
            home_titlebar.setVisibility(View.GONE);
        } else if (showtabs == 3) {
            home_tabs.setVisibility(View.GONE);
            home_titlebar.setVisibility(View.VISIBLE);
            btn_left.setVisibility(View.GONE);
        } else if (showtabs == 4) {
            home_tabs.setVisibility(View.VISIBLE);
            home_titlebar.setVisibility(View.VISIBLE);
            btn_left.setVisibility(View.GONE);
        } else {
            home_tabs.setVisibility(View.VISIBLE);
            home_titlebar.setVisibility(View.VISIBLE);
        }
        home_tab1_cb.setText(Constants.moduleList.get(0).getModuleName());
        home_tab2_cb.setText(Constants.moduleList.get(1).getModuleName());
        home_tab3_cb.setText(Constants.moduleList.get(2).getModuleName());
        home_tab4_cb.setText(Constants.moduleList.get(3).getModuleName());
        home_tab5_cb.setText(Constants.moduleList.get(4).getModuleName());
        home_tab1_cb.setOnClickListener(this);
        home_tab2_cb.setOnClickListener(this);
        home_tab3_cb.setOnClickListener(this);
        home_tab4_cb.setOnClickListener(this);
        home_tab5_cb.setOnClickListener(this);
        btn_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.home_tab1_cb:
                if (v.isSelected()) {
                    break;
                }
                FrameworkManager.branch((FragmentActivity) context,
                        Constants.moduleList.get(0));
                break;
            case R.id.home_tab2_cb:
                if (v.isSelected()) {
                    break;
                }
                FrameworkManager.branch((FragmentActivity) context,
                        Constants.moduleList.get(1));
                break;
            case R.id.home_tab3_cb:
                if (v.isSelected()) {
                    break;
                }
                FrameworkManager.branch((FragmentActivity) context,
                        Constants.moduleList.get(2));
                break;
            case R.id.home_tab4_cb:
                if (v.isSelected()) {
                    break;
                }
                FrameworkManager.branch((FragmentActivity) context,
                        Constants.moduleList.get(3));
                break;
            case R.id.home_tab5_cb:
                if (v.isSelected()) {
                    break;
                }
                FrameworkManager.branch((FragmentActivity) context,
                        Constants.moduleList.get(4));
                break;
            case R.id.home_btn_left:
                int num = getSupportFragmentManager().getBackStackEntryCount();
                if (num <= 1) {
                    this.finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }

    }

    /**
     * @param type 实体键返回键的实现方式; 0,默认fragment一层一层返回 ;1,关闭当前activity;2,提示对话框退出程序
     */
    public void setBackType(int type) {
        backtype = type;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (backtype == 0) {
                if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                    this.finish();
                } else {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } else if (backtype == 1) {
                this.finish();
            } else if (backtype == 2) {
                dialog = new CommonDialog(context, 2, "确认", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                Intent home = new Intent(Intent.ACTION_MAIN);
                                home.addCategory(Intent.CATEGORY_HOME);
                                startActivity(home);
                                dialog.dismiss();
                                myApplication.exit();
                            }
                        }, null, null, "确认退出程序?");
                dialog.show();
                // Intent home = new Intent(Intent.ACTION_MAIN);
                // home.addCategory(Intent.CATEGORY_HOME);
                // startActivity(home);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*
     * 继承于Activity的设置标题
     */
    @Override
    public void setTitle(CharSequence title) {
        // TODO Auto-generated method stub
        home_titile1.setText(title);
        super.setTitle(title);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        PushManager.stopWork(getApplicationContext());
        initBaidu();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }

    /**
     * 在有网络连接状态下，上传crash日志
     */
    private void uploadLog() {
        if (NetUtils.hasDataConnection(this)) {
            final String filePath = Environment.getExternalStorageDirectory()
                    + "/ncihealth/crash/";
            File[] file = new File(filePath).listFiles();// 设定扫描路径
            for (int i = 0; file != null && i < file.length; i++) {
                // 判读是否文件以及文件后缀名
                if (file[i].isFile() && file[i].getName().endsWith(".log")) {
                    final String fileName = file[i].getName();
                    UploadFileEngin.uploadLog(fileName, this,
                            new UploadCallback(this) {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(ResponseInfo arg0) {
                                    // TODO Auto-generated method stub
                                    super.onSuccess(arg0);
                                    String logName = arg0.result.toString();
                                    Log.e("crash日志上传成功", "crash日志上传成功");
                                    File file = new File(filePath + fileName);
                                    file.delete();
                                }

                                @Override
                                public void onFailure(HttpException arg0,
                                                      String arg1) {
                                    // TODO Auto-generated method stub
                                    super.onFailure(arg0, arg1);
                                }
                            });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(arg0);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(arg0, arg1, arg2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (CommonToast.getInstance() != null)
            CommonToast.getInstance().cancel();
    }
}
