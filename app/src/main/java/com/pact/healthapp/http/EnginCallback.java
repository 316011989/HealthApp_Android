package com.pact.healthapp.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonPrograssDialog;
import com.pact.healthapp.view.CommonToast;

public class EnginCallback extends RequestCallBack {

    private CommonPrograssDialog pd;
    private Context context;

    private static CommonDialog dialog;
    private static boolean isUpdate = true;//是否更新
    private String downloadAddress;//APP更新包下载地址

    /**
     * 构造函数,为公共进度条重写此回调
     */
    public EnginCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onFailure(HttpException arg0, String arg1) {
        // TODO Auto-generated method stub
        canclDialog();
        Log.e("失败异常", "HttpException:" + arg0 + "返回值" + arg1);
        CommonToast.makeText(context, "服务器繁忙");
    }

    @Override
    public void onSuccess(ResponseInfo arg0) {
        canclDialog();
        String result = "";
        try {
            result = Des3.decode(arg0.result.toString());
            JSONObject obj = JSON.parseObject(result);
            if (obj.get("errorCode") != null) {
                if (obj.get("errorCode").toString().equals("1101")) {
                    CommonToast.makeText(context, "手机号已注册");
                } else if (obj.get("errorCode").toString().equals("1102")) {
                    CommonToast.makeText(context, "手机号未注册");
                } else if (obj.get("errorCode").toString().equals("1103")) {
                    CommonToast.makeText(context, "手机号格式错误");
                } else if (obj.get("errorCode").toString().equals("1104")) {
                    CommonToast.makeText(context, obj.getString("resultMsg"));
                } else if (obj.get("errorCode").toString().equals("1105")) {
                    CommonToast.makeText(context, "发送短信出错");
                } else if (obj.get("errorCode").toString().equals("1106")) {
                    CommonToast.makeText(context, "注册失败");
                } else if (obj.get("errorCode").toString().equals("1107")) {
                    CommonToast.makeText(context, "用户名未注册");
                } else if (obj.get("errorCode").toString().equals("1108")) {
                    CommonToast.makeText(context, "邮箱未激活");
                } else if (obj.get("errorCode").toString().equals("1109")) {
                    CommonToast.makeText(context, "手机号未激活");
                } else if (obj.get("errorCode").toString().equals("1110")) {
                    CommonToast.makeText(context, "验证码错误");
                } else if (obj.get("errorCode").toString().equals("1111")) {
                    CommonToast.makeText(context, "密码错误");
                } else if (obj.get("errorCode").toString().equals("1112")) {
                    if (isUpdate) {
                        if (obj.getString("downloadAddress") != null)
                            downloadAddress = obj.getString("downloadAddress");
                        int btnCounts = 2;
                        if (obj.getBoolean("isMustUpdate") != null && obj.getBoolean("isMustUpdate"))
                            btnCounts = 1;
                        if (dialog == null || !dialog.isShowing()) {
                            dialog = new CommonDialog(context, btnCounts, "立即升级", "暂不升级",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(downloadAddress));
                                            context.startActivity(intent);
                                            dialog.cancel();
                                        }
                                    },
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isUpdate = false;
                                            dialog.cancel();
                                        }
                                    }, obj.get("resultMsg") + "", obj.get("versionDesc") + "");
                            dialog.show();
                            ((TextView) dialog.findViewById(R.id.common_dialog_title)).setTextSize(19);
                            ((TextView) dialog.findViewById(R.id.common_dialog_content)).setTextSize(15);
                            ((TextView) dialog.findViewById(R.id.common_dialog_content)).setGravity(Gravity.LEFT);
                        }
                    }
                } else if (obj.get("errorCode").toString().equals("1120")) {
                    dialog = new CommonDialog(context, 1, "确定", "",
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    SharedPreferenceUtils sp = new SharedPreferenceUtils();
                                    String phoneNumber = sp.getLoginInfo(
                                            context, "phoneNumber");
                                    Intent intent = new Intent(context,
                                            LoginActivity.class);
                                    intent.putExtra("phoneNumber", phoneNumber);
                                    sp.clearUserinfo(context);
                                    dialog.dismiss();
                                    ((Activity) context).startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
//                                    ((Activity) context).finish();
                                }
                            }, null, null, "当前账户在其他设备登录，请重新登录。");
                    dialog.show();
                    return;
                } else if (obj.get("errorCode").toString().equals("1121")) {
                    CommonToast.makeText(context, "该邮箱已存在");
                } else if (obj.get("errorCode").toString().equals("1122")) {
                    CommonToast.makeText(context, "邮箱格式不正确");
                } else if (obj.get("errorCode").toString().equals("1124")) {
                    CommonToast.makeText(context, "身份证号格式错误");
                } else if (obj.get("errorCode").toString().equals("1")) {
                    CommonToast.makeText(context, obj.getString("resultMsg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 取消请求对话框
     */
    public void canclDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (pd == null && context != null) {
            pd = CommonPrograssDialog.getInstance(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("正在请求服务器...");
            pd.setCancelable(false);
            pd.show();
        }
    }

}
