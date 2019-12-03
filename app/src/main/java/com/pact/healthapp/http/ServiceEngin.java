package com.pact.healthapp.http;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easemob.util.NetUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.view.CommonDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 公共请求类
 *
 * @author 一龙
 */
public class ServiceEngin {

    private static HttpUtils httputil = new HttpUtils();
    private static String url = Constants.HOST + Constants.REQUST_PORT;
    private static CommonDialog commonDialog;
    public static int REQUEST_LOGIN = 100;

    /**
     * 异步请求方法,请自行在callback中处理返回结果(callbackde success中自行解析result)
     *
     * @param bizId       业务场景编码
     * @param serviceName 方法名
     * @param servicePara 参数集合
     * @param context     当前界面上下文
     * @param callback    重写的EngineCallBack回调函数
     */
    public static void Request(final Context context, String bizId,
                               String serviceName, String servicePara, EnginCallback callback) {
        if (!NetUtils.hasNetwork(context)) {// 没有网络
            if (commonDialog == null) {
                commonDialog = new CommonDialog(context, 1, "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                commonDialog.dismiss();
                                // 跳转到网络设置界面
                                Intent intent = new Intent(
                                        Settings.ACTION_DATA_ROAMING_SETTINGS);
                                context.startActivity(intent);
                            }
                        }, null, null, "请设置网络!");
            }
            commonDialog.show();
        } else {
            if (commonDialog != null) {
                commonDialog.dismiss();
            }
            // 参数拼接
            RequestParams params = new RequestParams();
            params.addBodyParameter("keyVer", "v1");
            params.addBodyParameter("appID", "1");
            EnginBean eb = new EnginBean();
            eb.setBizId(bizId);
            String deviceId = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            eb.setDeviceId(deviceId);
            eb.setClientOS("1");
            eb.setClientVer("" + android.os.Build.VERSION.SDK_INT);//
            try {
                eb.setAppVer("" + context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS).versionCode);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            eb.setServiceName(serviceName);
            eb.setServicePara(servicePara);
            String para = JSON.toJSON(eb) + "";
            try {
                para = Des3.encode(para);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            params.addBodyParameter("para", para);
            // 请求超时
            httputil.configSoTimeout(60 * 1000);
            httputil.configTimeout(1000 * 10);
            // 发送请求
            httputil.send(HttpRequest.HttpMethod.POST, url, params, callback);
        }
    }

    /**
     * 同步请求方法,自动返回解密后的json类型数据(result解密后自动解析)
     *
     * @param bizId       业务场景编码
     * @param serviceName 方法名
     * @param servicePara 参数集合
     * @param context     当前界面上下文
     * @return 已解密json类型数据
     */
    public static String Request(final Context context, String bizId,
                                 String serviceName, String servicePara) {
        if (!NetUtils.hasNetwork(context)) {// 没有网络
            if (commonDialog == null || !commonDialog.getContext().equals(context)) {
                commonDialog = new CommonDialog(context, 1, "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                commonDialog.dismiss();
                                // 跳转到网络设置界面
                                Intent intent = new Intent(
                                        Settings.ACTION_DATA_ROAMING_SETTINGS);
                                context.startActivity(intent);
                            }
                        }, null, null, "请设置网络!");
            }
            commonDialog.show();
        } else {
            if (commonDialog != null && commonDialog.isShowing()) {
                commonDialog.dismiss();
            }
            String result = "";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            // 参数拼接
            RequestParams params = new RequestParams();
            params.addBodyParameter("keyVer", "v1");
            params.addBodyParameter("appID", "1");
            EnginBean eb = new EnginBean();
            eb.setBizId(bizId);
            String deviceId = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            eb.setDeviceId(deviceId);
            eb.setClientOS("1");
            eb.setClientVer("" + android.os.Build.VERSION.SDK_INT);//
            try {
                eb.setAppVer("" + context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS).versionCode);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            eb.setServiceName(serviceName);
            eb.setServicePara(servicePara);
            String para = JSON.toJSON(eb) + "";
            try {
                para = Des3.encode(para);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            params.addBodyParameter("para", para);

            // // 请求超时
            httputil.configTimeout(1000 * 20);
            BufferedReader in = null;
            try {
                ResponseStream receiveStream = httputil.sendSync(
                        HttpRequest.HttpMethod.POST, url, params);
                in = new BufferedReader(new InputStreamReader(receiveStream));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                in.close();
                result = Des3.decode(result);
                JSONObject jo = JSONObject.parseObject(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
            // Log.i("XXXXXXXX", result);
            // 发送请求
            return result;
        }
        return null;
    }


    /**
     * 异步请求方法,请自行在callback中处理返回结果(callbackde success中自行解析result)
     *
     * @param paymentOrderNum 支付单号
     * @param requesturl      requesturl,区别于其他请求,需要url
     * @param context         当前界面上下文
     * @param callback        重写的EngineCallBack回调函数
     */
    public static void RequestWithUrl(final Context context, String requesturl, String paymentOrderNum,
                                      EnginCallback callback) {
        if (!NetUtils.hasNetwork(context)) {// 没有网络
            if (commonDialog == null || !commonDialog.getContext().equals(context)) {
                commonDialog = new CommonDialog(context, 1, "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                commonDialog.dismiss();
                                // 跳转到网络设置界面
                                Intent intent = new Intent(
                                        Settings.ACTION_DATA_ROAMING_SETTINGS);
                                context.startActivity(intent);
                            }
                        }, null, null, "请设置网络!");
            }
            commonDialog.show();
        } else {
            if (commonDialog != null && commonDialog.isShowing()) {
                commonDialog.dismiss();
            }
            // 参数拼接
            RequestParams params = new RequestParams();
            params.addBodyParameter("paymentOrderNum", paymentOrderNum);

            // 请求超时
            httputil.configSoTimeout(30 * 1000);
            httputil.configTimeout(1000 * 10);
            // 发送请求
            httputil.send(HttpRequest.HttpMethod.POST, requesturl, params, callback);
        }
    }

    /**
     * 查询支付结果专用接口
     * 异步请求方法,请自行在callback中处理返回结果(callbackde success中自行解析result)
     *
     * @param paymentOrderNum 支付单号
     * @param requesturl      requesturl,区别于其他请求,需要url
     * @param context         当前界面上下文
     * @param callback        重写的EngineCallBack回调函数
     */
    public static void RequestWithUrl(final Context context, String requesturl, String paymentOrderNum, String paymentType,
                                      EnginCallback callback) {
        if (!NetUtils.hasNetwork(context)) {// 没有网络
            if (commonDialog == null || !commonDialog.getContext().equals(context)) {
                commonDialog = new CommonDialog(context, 1, "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                commonDialog.dismiss();
                                // 跳转到网络设置界面
                                Intent intent = new Intent(
                                        Settings.ACTION_DATA_ROAMING_SETTINGS);
                                context.startActivity(intent);
                            }
                        }, null, null, "请设置网络!");
            }
            commonDialog.show();
        } else {
            if (commonDialog != null && commonDialog.isShowing()) {
                commonDialog.dismiss();
            }
            // 参数拼接
            RequestParams params = new RequestParams();
            params.addBodyParameter("paymentOrderNum", paymentOrderNum);
            params.addBodyParameter("paymentType", paymentType);
            // 请求超时
            httputil.configSoTimeout(30 * 1000);
            httputil.configTimeout(1000 * 10);
            // 发送请求
            httputil.send(HttpRequest.HttpMethod.POST, requesturl, params, callback);
        }
    }
}
