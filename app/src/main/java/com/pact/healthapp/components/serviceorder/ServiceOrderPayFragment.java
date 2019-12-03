package com.pact.healthapp.components.serviceorder;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.components.pay.AliPayReslut;
import com.pact.healthapp.components.pay.AliSignUtils;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.view.CommonDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ServiceOrderPayFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private String paynum;
    private String pay_orderfee;
    private TextView payorderfee;
    private Button appayBtn;
    private RelativeLayout pay_orderpayway_wx;
    private RelativeLayout pay_orderpayway_zfb;
    private ImageView pay_way1_cb;
    private ImageView pay_way2_cb;
    private CommonDialog dialog;

    private int payway = 1;//支付方式,1微信,2支付宝

    private IWXAPI api;//微信支付接口类
    //支付宝支付
    public static final String PARTNER = "2088811008053589";// 商户PID
    public static final String SELLER = "ncihealth@ncihealth.cn";// 商户收款账号
    public static final String RSA_PRIVATE = "MIICWgIBAAKBgQCn+2aDoQRuADl+BHCrks1nB7tfDI7GrooYDGG/s0fOLCfYtm9r" +
            "Ck/b6BS6IwZLkAOqhLv2vQywa3Y5kkoGgZfi3mumHUs37N50p44Qs66hzGenZpzI" +
            "kri5cNCyt70bX0w5EznE3WSUII/AJWQjphiqmgY0DdcSBRT64oGuuBrjOwIDAQAB" +
            "An8+Vgy0zAvYxjyPhxZIlTGkCjp+Cyrwj9SvF+kuOllrKPegdBLkpe78rCME7/pm" +
            "EAtMO/WEmqMa1MYKlz7hKzYxYCzHgPIwiQPPGZpNYE6rOB3iYouqVX4svGG2mKev" +
            "Iqa3K+Kr/Ux6VmytTNOxfdM9TIXTkBNaGV1rY6yfnJ6JAkEA29LSfxAce5RqwGuL" +
            "nKU5hRhBCUB6Jz5nti7bnpeteNevd0eNCz9WQL2vF6HQB6Jq01o34sTh/L6r6XpT" +
            "ABfT/wJBAMOgfkCnuSKXQi7IgnO1v8k8QfsxDhIgBaj23IwHCBCq1YooiQAJCPWk" +
            "N4WqF3oIfWuxCWlQZXTju2ymUCs7QMUCQQCE9qNeOgeQdHiRxTtgV8xQBhiomPiK" +
            "LkQbPYKYDhTeqE+5LW1R5VCtvk5fU//57jMBfmuZXg93inl/sv7ran5PAkB8UlWJ" +
            "300/l2Le+lw1Ds74I0xHR3CKwkD2lROqBZnxha+5YnNWS2efPCJw1pye2CBkvRif" +
            "DhG8DDIDJVqTbs+FAkBEA81hwwUeUxStAZ2xkDnirHd948PxjTcBadnZhedrMwcx" +
            "k5rZqGN+P6ibn6ILh2dbZunKDNMUjcM4AFytHIR7";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    AliPayReslut payResult = new AliPayReslut((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证
                     * （验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1)
                     * 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        queryOrderState();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            queryOrderState();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            dialog = new CommonDialog(getActivity(), 1, "确定", null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            }, null, null, "支付失败");
                            dialog.show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wx_payresult = intent.getIntExtra("wx_payresult", 0);
            if (wx_payresult != 1) {
                if (wx_payresult == -2) {
                    dialog = new CommonDialog(getActivity(), 1, "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }, null, null, "支付失败");
                    dialog.show();
                } else if (wx_payresult == -1) {
                    dialog = new CommonDialog(getActivity(), 1, "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }, null, null, "支付失败");
                    dialog.show();
                } else if (wx_payresult == 0) {
                    queryOrderState();
                }
            }
            unregisterReceiver();
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_onlinepay_fragment_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("去支付");
        //向微信注册本App
        api = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APPID);
        api.registerApp(Constants.WX_APPID);
        initView();
        payway = 1;
        //获取待支付信息
        Bundle args = getArguments();
        paynum = args.getString("payment_num");
        pay_orderfee = args.getString("pay_orderfee");
        payorderfee.setText("¥ " + CommonUtil.toPrice(pay_orderfee));
        ((ServiceOrderActivity) getActivity()).btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }


    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("wx_payresult");
        getActivity().registerReceiver(receiver, intentFilter);
    }

    private void unregisterReceiver() {
        getActivity().unregisterReceiver(receiver);
    }


    private void initView() {
        //展示待支付信息
        payorderfee = (TextView) view.findViewById(R.id.pay_orderfee);
        pay_orderpayway_wx = (RelativeLayout) view.findViewById(R.id.pay_orderpayway_wx);
        pay_orderpayway_zfb = (RelativeLayout) view.findViewById(R.id.pay_orderpayway_zfb);
        pay_orderpayway_wx.setOnClickListener(this);
        pay_orderpayway_zfb.setOnClickListener(this);
        pay_way1_cb = (ImageView) view.findViewById(R.id.pay_way1_cb);
        pay_way2_cb = (ImageView) view.findViewById(R.id.pay_way2_cb);

        appayBtn = (Button) view.findViewById(R.id.btn_topay);
        appayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_orderpayway_wx:
                payway = 1;
                pay_way1_cb.setBackgroundResource(R.mipmap.checked);
                pay_way2_cb.setBackgroundResource(R.mipmap.not_checked);
                break;
            case R.id.pay_orderpayway_zfb:
                payway = 2;
                pay_way2_cb.setBackgroundResource(R.mipmap.checked);
                pay_way1_cb.setBackgroundResource(R.mipmap.not_checked);
                break;
            case R.id.btn_topay:
                if (payway == 1) {
                    createWXUnifiedOrder();
                } else if (payway == 2) {
                    createAliUnifiedOrder();
                }
                break;
            case R.id.home_btn_left:
                getActivity().finish();
                break;
        }

    }


    /**
     * 调用微信统一下单接口
     */
    private void createWXUnifiedOrder() {
        try {
            ServiceEngin.RequestWithUrl(context,
                    Constants.HOST + "/unifiedOrder", Des3.encode(paynum),
                    new EnginCallback(context) {
                        @Override
                        public void onSuccess(ResponseInfo arg0) {
                            super.onSuccess(arg0);
                            String result = Des3.decode(arg0.result
                                    .toString());
                            Log.e("get server pay params:", result);
                            JSONObject obj = JSONObject.parseObject(result);
                            if (obj.getString("resultCode").equals("0")) {
                                PayReq req = new PayReq();
                                // 无影响
                                req.appId = Constants.WX_APPID;
                                req.partnerId = Constants.WX_partnerId;
                                req.packageValue = "Sign=WXPay";
                                req.extData = paynum; // optional
                                // 有影响
                                req.sign = obj.getString("sign");
                                req.prepayId = obj.getString("prepayid");
                                req.nonceStr = obj.getString("noncestr");
                                req.timeStamp = obj.getString("timestamp");
                                System.out.println("调用结果" + api.sendReq(req));
                                registerReceiver();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用支付宝下单接口
     */
    private void createAliUnifiedOrder() {
        try {
            ServiceEngin.RequestWithUrl(context,
                    Constants.HOST + "/getAlipaySign", Des3.encode(paynum),
                    new EnginCallback(context) {
                        @Override
                        public void onSuccess(ResponseInfo arg0) {
                            super.onSuccess(arg0);
                            String result = Des3.decode(arg0.result
                                    .toString());
                            Log.e("get server pay params:", result);
                            JSONObject obj = JSONObject.parseObject(result);
                            if (obj.getString("resultCode").equals("0")) {
                                String orderInfo = obj.getString("orderInfo");
                                /**
                                 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
                                 */
                                String sign = AliSignUtils.sign(orderInfo, RSA_PRIVATE);
                                try {
                                    /**
                                     * 仅需对sign 做URL编码
                                     */
                                    sign = URLEncoder.encode(sign, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                /**
                                 * 完整的符合支付宝参数规范的订单信息
                                 */
                                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                                        + "sign_type=\"RSA\"";
                                Runnable payRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(getActivity());
                                        // 调用支付接口，获取支付结果
                                        String result = alipay.pay(payInfo, true);
                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };
                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询支付状态
     */
    private void queryOrderState() {
        try {
            String paymentType = "";
            if (payway == 1) paymentType = "wx";
            if (payway == 2) paymentType = "zfb";
            ServiceEngin.RequestWithUrl(context,
                    Constants.HOST + "/queryPayMentOrderStatus", Des3.encode(paynum), Des3.encode(paymentType),
                    new EnginCallback(context) {
                        @Override
                        public void onSuccess(ResponseInfo arg0) {
                            super.onSuccess(arg0);
                            String result = Des3.decode(arg0.result
                                    .toString());
                            Log.e("get server pay params:", result);
                            JSONObject obj = JSONObject.parseObject(result);
                            if (obj.getString("resultCode").equals("0")) {//接口调用成功
                                OrderData.operation = "pay";
                                if (obj.getString("trade_state").equals("SUCCESS")) {
                                    //支付成功
                                    dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            getFragmentManager().popBackStack("ServiceOrderListFragment", 0);
                                        }
                                    }, null, null, "支付成功");
                                    dialog.show();
                                } else {
                                    //支付失败
                                    dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    }, null, null, "支付失败");
                                    dialog.show();
                                }
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
