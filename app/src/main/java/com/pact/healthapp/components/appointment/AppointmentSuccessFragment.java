package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.serviceorder.OrderData;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;

/**
 * 预约成功
 * Created by wangdong on 2016/2/29.
 */
@SuppressLint("ValidFragment")
public class AppointmentSuccessFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.appointment_serviceorder_num)
    private TextView serviceorder_num;
    @ViewInject(R.id.appointment_remind_message)
    private TextView remind_message;
    @ViewInject(R.id.btn_pay)
    private Button btn_pay;
    @ViewInject(R.id.btn_completed)
    private Button btn_completed;
    @ViewInject(R.id.appointment_attention_matters)
    private WebView appointment_attention_matters;

    String serviceOrderId;//服务单Id
    String serviceOrderNo;//服务单号
    String paymentOrderNum;//服务单对应支付单号
    String selfPay;//支付金额
    int paymentMethod;//支付方式

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_success_fragment_layout,
                null);
        ViewUtils.inject(this, view);
        if (ServiceOrderData.operation != null
                && ServiceOrderData.operation.equals("changedate")) {
            getActivity().setTitle("改期成功");
        } else {
            getActivity().setTitle("预约成功");
        }
        initView();
        appointment_attention_matters.loadUrl(Constants.APPOINTMENT_PRE_TIP);
        return view;
    }

    private void initView() {
        serviceOrderId = ServiceOrderData.serviceOrderId;//服务单Id
        serviceOrderNo = ServiceOrderData.serviceOrderNo;//服务单号
        selfPay = ServiceOrderData.selfPay;//支付金额
        paymentMethod = ServiceOrderData.paymentMethod;//支付方式
        paymentOrderNum = ServiceOrderData.paymentOrderNum;//服务单对应支付单号

        serviceorder_num.setText(serviceOrderNo);
        //支付金额大于0，并且是在线支付
        if (Double.parseDouble(selfPay) > 0 && paymentMethod == 1) {
            String message = "提醒：您还有¥" + CommonUtil.toPrice(selfPay) + "未付，超过1小时未支付，预约将被取消";
            remind_message.setText(message);
            remind_message.setTextColor(getResources().getColor(R.color.red));
        }
        //支付金额为0或者前台支付
        if (Double.parseDouble(selfPay) == 0 || paymentMethod == 2) {
            String message = "提醒：您可以随时在“预约服务单”中查看预约";
            remind_message.setText(message);
            remind_message.setTextColor(getResources().getColor(R.color.gray_dark));
            btn_pay.setVisibility(View.GONE);
            btn_completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            btn_completed.setTextColor(getResources().getColor(R.color.white));
        }
        btn_pay.setOnClickListener(this);
        btn_completed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                toPay();
                break;
            case R.id.btn_completed://完成
                if (ServiceOrderData.operation != null
                        && ServiceOrderData.operation.equals("changedate")) {
                    OrderData.clearData();
                    OrderData.operation = "changedate";
                    ServiceOrderData.clearData();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else if (ServiceOrderData.operation != null
                        && ServiceOrderData.operation.equals("order")) {
                    OrderData.clearData();
                    OrderData.operation = "order";
                    ServiceOrderData.clearData();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else if (ServiceOrderData.operation != null
                        && ServiceOrderData.operation.equals("voucher")) {
                    ServiceOrderData.clearData();
                    getActivity().finish();
                } else {
                    ((AppointmentActivity) getActivity()).toStep1();
                    ServiceOrderData.clearData();
                }
                break;
        }
    }

    /**
     * 去支付
     */
    private void toPay() {
        ((AppointmentActivity) getActivity()).toStepm8();
    }


}
