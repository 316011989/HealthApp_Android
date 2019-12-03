package com.pact.healthapp.universal;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.frontia.FrontiaApplication;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.pact.healthapp.R;
import com.pact.healthapp.components.im.NciHXSDKHelper;
import com.pact.healthapp.components.im.applib.User;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.data.CrashHandler;
import com.pact.healthapp.db.DatabaseTools;
import com.pact.healthapp.db.DbOpenHelper;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyApplication extends FrontiaApplication {
    // 常用事件监听，用来处理日常的网络错误，授权验证错误等
    public static MyApplication myApplication;
    public List<Activity> activityList = new LinkedList<Activity>();
    public static SQLiteDatabase db;
    public static boolean isLogin;// 当前登录状态
    public static BitmapUtils bitmapUtils;
    public static NciHXSDKHelper hxSDKHelper = new NciHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO Auto-generated method stub
        myApplication = this;
        // 初始化数据库文件
        db = new DbOpenHelper(getApplicationContext()).openDatabase();
        // 查看登录状态
        getLoginStatus();
        // 初始化底栏
        initHomeTabs();
        // 初始化环信
        initHuanXin();
        initImageLoader(getApplicationContext());
//        initWeixinPay();
        CrashHandler.getInstance().init(getApplicationContext());
    }

    /**
     * 将该app注册到微信
     */
    private void initWeixinPay() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(myApplication, null);
        msgApi.registerApp(Constants.WX_APPID);
    }


    // public static ImageLoader imageLoader=ImageLoader.getInstance();
    private void initImageLoader(Context context) {
        // ImageLoaderConfiguration config = new
        // ImageLoaderConfiguration.Builder(
        // getApplicationContext())
        // .threadPriority(Thread.NORM_PRIORITY - 2)
        // .denyCacheImageMultipleSizesInMemory()
        // .discCacheFileNameGenerator(new Md5FileNameGenerator())
        // .tasksProcessingOrder(QueueProcessingType.LIFO)
        // .build();
        // ImageLoader.getInstance().init(config);
        bitmapUtils = new BitmapUtils(context);
        BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
        bitmapDisplayConfig.setLoadFailedDrawable(getResources().getDrawable(
                R.mipmap.defalt_pic));
        bitmapDisplayConfig.setLoadingDrawable(getResources().getDrawable(
                R.mipmap.defalt_pic));
        bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
    }

    /**
     * 初始化环信
     *
     * @author zhangyl
     */
    private void initHuanXin() {
        // int pid = android.os.Process.myPid();
        // String processAppName = getAppName(pid);
        // // 如果app启用了远程的service，此application:onCreate会被调用2次
        // // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
        // // name就立即返回
        // if (processAppName == null
        // || !processAppName.equalsIgnoreCase("com.pact.healthapp")) {
        // // 则此application::onCreate 是被service 调用的，直接返回
        // return;
        // }
        // // TODO Auto-generated method stub
        // EMChat.getInstance().init(myApplication);
        // /**
        // * debugMode == true 时为打开，sdk 会在log里输入调试信息
        // *
        // * @param debugMode
        // * 在做代码混淆的时候需要设置成false
        // */
        // EMChat.getInstance().setDebugMode(true);//
        // 在做打包混淆时，要关闭debug模式，如果未被关闭，则会出现程序无法运行问题
        hxSDKHelper.onInit(this);
    }

    /**
     * 添加Activity 到容器中
     *
     * @param activity
     * @author zhangyl
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 遍历所有Activity 并finish
     *
     * @author zhangyl
     */
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(1);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 检查登录状态
     */
    private void getLoginStatus() {
        // TODO Auto-generated method stub
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin", false);
    }

    /**
     * 初始化底栏应显示的界面集合
     *
     * @author zhangyl
     */
    private void initHomeTabs() {
        // TODO Auto-generated method stub
        Constants.moduleList = DatabaseTools.findHomeTabs();
    }

    /**
     * 根据pid查询应用包名
     *
     * @param pID
     * @return 包名
     * @author zhangyl
     */
    private String getAppName(int pID) {
        String processName = "";
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);

    }
}
