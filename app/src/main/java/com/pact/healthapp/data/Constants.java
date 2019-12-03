package com.pact.healthapp.data;

import com.pact.healthapp.universal.Framework;

import java.util.List;

public class Constants {
    // 生产服务器环境(域名)
//    public static String HOST = "http://api.ncihealth.cn";
    // 外网测试服务器环境(域名)
    public static String HOST = "http://t.app.ncihealth.cn";
    // 外网测试服务器环境
//     public static String HOST = "http://219.143.202.184:8080";
    public static String SHOWNEWS_PORT = "/shownews?newsId=";
    public static String SHOWEVENT_PORT = "/showevent?eventId=";
    public static String CUSTOMERID = "&customerId=";
    public static String REQUST_PORT = "/mobileservice/ServiceEngin.do";
    public static String UPLOAD_PORT = "/mobileservice/newupload.do";
    // 上传crash日志地址
    public static String UPLOADLOG_PORT = "/mobileservice/uploadLog.do";
    // 体检报告下载地址
    public static String DOWNLOAD_PORT = "/mobileservice/download/report";
    // 健康报告下载地址
    public static String DOWNLOADPG_PORT = "/mobileservice/download/reportpg";
    // 底栏要显示的模块
    public static List<Framework> moduleList;
    // QQ 登录需要的appid和appkey
    public static String QQ_APPID = "1104998382";
    public static String QQ_APPKEY = "AN6TO1zSrPRxCq0d";
    public static String QQ_SCHEME = "tencent1104998382";// manifest中填写
    // QQ 空间分享需要的appid和appkey
    public static String QZONE_APPID = "1104998382";
    public static String QZONE_APPKEY = "AN6TO1zSrPRxCq0d";
    // 微信&朋友圈分享需要的appid和appkey
    public static String WX_APPID = "wx13dc958ab05ebc13";
    public static String WX_APPSECRET = "6e00c01a63094afe39ed69cdb660d627";
    public static String WX_partnerId = "1307602901";
    // 友盟的UMENG_APPKEY(manifest中需填写)
    public static String UMENG_APPKEY = "565e444167e58eab62004343";
    // 环信的EASEMOB_APPKEY(manifest中需填写)
    public static String EASEMOB_APPKEY = "ncl2015#nclhelth001";
    // 百度的api_key和com.baidu.lbsapi.API_KEY(manifest中两处地方需填写,BaseFragmentActivity中需填写)
    public static String BAIDU_APIKEY = "dwjBCufzHypA9zgzb0PMTv1c";
    // 环信用到的常量
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    public static final String ACCOUNT_REMOVED = "account_removed";
    // 第三方登录在app后台注册时的type
    public static final int REGIST_TYPE_WX = 11;// 微信
    public static final int REGIST_TYPE_SINA = 12;// 微博
    public static final int REGIST_TYPE_QQ = 13;// QQ
    // 后台推送来的type
    public static final int PUSH_TYPE_TOPIC = 1;// 帖子
    public static final int PUSH_TYPE_EVENT = 2;// 活动
    public static final int PUSH_TYPE_NEWS = 3;// 资讯

    // 我的设置 关于我们
    public static final String SET_ABOUTUS_URL = "http://img.ncihealth.cn/jk_p_app/company_intro.html";
    // 注册 服务条款
    public static final String SERVICE_TERMS_URL = "http://img.ncihealth.cn/jk_p_app/right_list.html";
    // 注册 隐私条款
    public static final String PRIVACY_ANNOUNCE_URL = "http://img.ncihealth.cn/jk_p_app/privacy-policy.html";

    //签名文件生成的sha1码
    public static final String KEYSTORE_SHA1 = "0a4d8459a6cdc7cc44df26e0817ac2a4";

    //预约成功后的检前提醒
    public static final String APPOINTMENT_PRE_TIP = "http://img.ncihealth.cn/jk_p_app/jqtx.html";
}
