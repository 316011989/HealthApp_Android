/**
 *
 */

package com.pact.healthapp.components.topicdetail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.view.CommonToast;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 *
 */
public class UMCustomShareBoard extends PopupWindow implements OnClickListener {

    // private UMSocialService mController = UMServiceFactory
    // .getUMSocialService("com.umeng.share");
    private Activity mActivity;
    private String url;
    private String title = "";

    public UMCustomShareBoard(Activity activity, String url, String title) {
        super(activity);
        this.mActivity = activity;
        this.url = url;
        this.title = title;
        initView(activity);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.um_custom_board, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        rootView.findViewById(R.id.sina).setOnClickListener(this);
        rootView.findViewById(R.id.copyurl).setOnClickListener(this);
        rootView.findViewById(R.id.cancle).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
        setTouchable(true);
    }

    /**
     * platform:0微信,1朋友圈,2QQ,3QZone,4微博 配置分享平台参数</br>
     */
    private void configPlatforms(int platform) {
        if (platform == 0 || platform == 1) {
            // 添加微信、微信朋友圈平台
            addWXPlatform();
        } else if (platform == 4) {
            // 添加新浪SSO授权
            ((BaseFragmentActivity) mActivity).mController.getConfig()
                    .setSsoHandler(new SinaSsoHandler());
        } else if (platform == 2 || platform == 3) {
            // 添加QQ、QZone平台
            addQQQZonePlatform();
        }
        // 所有平台都关闭toast提示
        ((BaseFragmentActivity) mActivity).mController.getConfig().closeToast();
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {
        String appId = Constants.QZONE_APPID;
        String appKey = Constants.QZONE_APPKEY;
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appId,
                appKey);
        qqSsoHandler.setTargetUrl(url);
        qqSsoHandler.addToSocialSDK();
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId,
                appKey);
        qZoneSsoHandler.addToSocialSDK();
        ((BaseFragmentActivity) mActivity).mController.getConfig()
                .setSsoHandler(qqSsoHandler);
        ((BaseFragmentActivity) mActivity).mController.getConfig()
                .registerListener(new SnsPostListener() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int arg1,
                                           SocializeEntity arg2) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = Constants.WX_APPID;
        String appSecret = Constants.WX_APPSECRET;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxHandler.addToSocialSDK();
        wxHandler.showCompressToast(false);
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId,
                appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        ((BaseFragmentActivity) mActivity).mController.getConfig()
                .setSsoHandler(wxHandler);

    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent(int platform) {
        if (platform == 0) {
            // 微信
            UMImage umImage = new UMImage(mActivity, R.mipmap.ic_launcher);
            WeiXinShareContent weixinContent = new WeiXinShareContent();
            weixinContent.setShareImage(umImage);
            weixinContent.setTitle("新华健康");
            weixinContent.setShareContent(title + "//" + url);
            weixinContent.setTargetUrl(url);
            ((BaseFragmentActivity) mActivity).mController
                    .setShareMedia(weixinContent);
        } else if (platform == 1) {
            // 设置朋友圈分享的内容
            UMImage umImage = new UMImage(mActivity, R.mipmap.ic_launcher);
            CircleShareContent circleMedia = new CircleShareContent();
            circleMedia.setTargetUrl(url);
            circleMedia.setShareImage(umImage);
            circleMedia.setTitle("新华健康");
            circleMedia.setShareContent(title + "//" + url);
            ((BaseFragmentActivity) mActivity).mController
                    .setShareMedia(circleMedia);
        } else if (platform == 2) {
            // QQ
            UMImage umImage = new UMImage(mActivity, R.mipmap.ic_launcher);
            QQShareContent qqShareContent = new QQShareContent();
            qqShareContent.setShareImage(umImage);
            qqShareContent.setTitle("新华健康");
            qqShareContent.setTargetUrl(url);
            qqShareContent.setShareContent(title + "//" + url);
            ((BaseFragmentActivity) mActivity).mController
                    .setShareMedia(qqShareContent);
        } else if (platform == 3) {
            // 设置QQ空间分享内容
            UMImage umImage = new UMImage(mActivity, R.mipmap.ic_launcher);
            QZoneShareContent qzone = new QZoneShareContent();
            qzone.setShareImage(umImage);
            qzone.setTargetUrl(url);
            qzone.setTitle("新华健康");
            qzone.setShareContent(title + "//" + url);
            ((BaseFragmentActivity) mActivity).mController.setShareMedia(qzone);
        } else if (platform == 4) {
            // 设置sina微博分享内容
            SinaShareContent sinaShareContent = new SinaShareContent();
            sinaShareContent.setTitle("新华健康");
            sinaShareContent.setTargetUrl(url);
            sinaShareContent.setShareContent(title + "//" + url);
            UMImage i = new UMImage(mActivity, R.mipmap.ic_launcher);
            i.setTargetUrl(url);
            i.setTitle("新华健康");
            sinaShareContent.setShareImage(i);
            ((BaseFragmentActivity) mActivity).mController
                    .setShareMedia(sinaShareContent);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.wechat:
                configPlatforms(0);
                // 设置分享的内容
                setShareContent(0);
                performShare(SHARE_MEDIA.WEIXIN);
                dismiss();
                break;
            case R.id.wechat_circle:
                configPlatforms(1);
                // 设置分享的内容
                setShareContent(1);
                performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                dismiss();
                break;
            case R.id.qq:
                configPlatforms(2);
                // 设置分享的内容
                setShareContent(2);
                performShare(SHARE_MEDIA.QQ);
                dismiss();
                break;
            case R.id.qzone:
                configPlatforms(3);
                // 设置分享的内容
                setShareContent(3);
                performShare(SHARE_MEDIA.QZONE);
                dismiss();
                break;
            case R.id.sina:
                configPlatforms(4);
                // 设置分享的内容
                setShareContent(4);
                performShare(SHARE_MEDIA.SINA);
                dismiss();
                break;
            case R.id.copyurl:
                ClipboardManager clip = (ClipboardManager) mActivity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setPrimaryClip(ClipData.newPlainText(null, url));// 复制
                CommonToast.makeText(mActivity, "已经复制到粘贴板");
                dismiss();
                break;
            case R.id.cancle:
                dismiss();
            default:
                break;
        }
    }

    private void performShare(SHARE_MEDIA platform) {
        ((BaseFragmentActivity) mActivity).mController.postShare(mActivity,
                platform, snsPostListener);
    }

    SnsPostListener snsPostListener = new SnsPostListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode,
                               SocializeEntity entity) {
            String showText = platform.toString();
            if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                showText += "平台分享成功";
            } else {
                showText += "平台分享失败";
            }
            Log.e("分享结果", showText);
            // Toast.makeText(mActivity, showText,
            // Toast.LENGTH_SHORT).show();

        }

    };
}
