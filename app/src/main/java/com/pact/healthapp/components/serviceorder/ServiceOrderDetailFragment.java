package com.pact.healthapp.components.serviceorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.AppointmentActivity;
import com.pact.healthapp.components.appointment.PackageDetailFragment;
import com.pact.healthapp.components.appointment.ServiceOrderData;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务单详情
 * Created by wangdong on 2016/1/14.
 */
public class ServiceOrderDetailFragment extends BaseFragment implements View.OnClickListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @ViewInject(R.id.serviceorder_rl)
    private LinearLayout serviceorder_rl;
    @ViewInject(R.id.serviceorder_num)
    private TextView serviceorder_num;//服务单号
    @ViewInject(R.id.serviceorder_status)
    private TextView serviceorder_status;//服务单状态
    @ViewInject(R.id.serviceorder_payment_status)
    private TextView serviceorder_payment_status;//支付状态
    @ViewInject(R.id.serviceorder_tv3)
    private TextView serviceorder_tv3;//
    @ViewInject(R.id.serviceorder_time_status)
    private TextView serviceorder_time_status;//是否过期

    @ViewInject(R.id.preview_tv1)
    private TextView preview_tv1;
    @ViewInject(R.id.preview_checkupuser)
    private TextView preview_checkupuser;//体检人
    @ViewInject(R.id.preview_sex)
    private TextView preview_sex;//性别
    @ViewInject(R.id.preview_marry)
    private TextView preview_marry;//婚否

    @ViewInject(R.id.preview_cert_type_name)
    private TextView preview_certTypeName;//证件类型
    @ViewInject(R.id.preview_cert_number)
    private TextView preview_certNumber;//证件号码
    @ViewInject(R.id.preview_phone_number)
    private TextView preview_phoneNumber;//手机
    @ViewInject(R.id.preview_email)
    private TextView preview_email;//邮箱

    @ViewInject(R.id.preview_package_ll)
    private RelativeLayout preview_package_ll;//套餐
    @ViewInject(R.id.preview_checkup_packagename)
    private TextView preview_checkup_packagename;//体检套餐名称
    @ViewInject(R.id.preview_package_payobject)
    private TextView preview_package_payobject;//付费方式
    @ViewInject(R.id.preview_tv10)
    private TextView preview_tv10;
    @ViewInject(R.id.preview_priceMarket)
    private TextView preview_priceMarket;//市场价格
    @ViewInject(R.id.preview_package_detail_img)
    private ImageView preview_package_detail_img;//非空套餐时，显示向右箭头

    @ViewInject(R.id.preview_org_name)
    private TextView preview_orgName;//体检机构
    @ViewInject(R.id.preview_org_address)
    private TextView preview_orgAddress;//体检地址
    @ViewInject(R.id.preview_org_time_ll)
    private LinearLayout preview_org_time_ll;
    @ViewInject(R.id.preview_org_time)
    private TextView preview_orgTime;//体检时间

    @ViewInject(R.id.preview_additems_ll)
    private LinearLayout preview_additems_ll;//体检项包
    private List<ServiceOrderDetailBean.AttachPackageDataEntity> packageData = new ArrayList<ServiceOrderDetailBean.AttachPackageDataEntity>();
    private List<ServiceOrderDetailBean.AttachDataEntity> attachData = new ArrayList<ServiceOrderDetailBean.AttachDataEntity>();

    @ViewInject(R.id.serviceorder_payment_tv)
    private TextView serviceorder_payment_tv;
    //由于之前的crm系统缺陷,暂时只显示未付金额
    @ViewInject(R.id.preview_payment_summary)
    private LinearLayout preview_payment_summary;//预览界面的合计信息
    @ViewInject(R.id.preview_unpaid)
    private TextView temp_unpaid;//未付金额

    @ViewInject(R.id.serviceorder_payment_summary)
    private RelativeLayout serviceorder_payment_summary;//服务单明细界面的合计信息
    @ViewInject(R.id.serviceorder_payable)
    private TextView serviceorder_detail_payable;//个人应付
    @ViewInject(R.id.serviceorder_paid)
    private TextView serviceorder_detail_paid;//个人已付
    @ViewInject(R.id.serviceorder_unpaid)
    private TextView serviceorder_detail_unpaid;//未付金额
    @ViewInject(R.id.serviceorder_refund)
    private TextView serviceorder_detail_refund;//退款金额
    @ViewInject(R.id.serviceorder_ncipay)
    private TextView serviceorder_ncipay;//新华付费
    @ViewInject(R.id.serviceorder_compay)
    private TextView serviceorder_compay;//企业付费
    @ViewInject(R.id.serviceorder_free)
    private TextView serviceorder_free;//免费

    @ViewInject(R.id.preview_commit)
    private Button preview_commit;//预约

    @ViewInject(R.id.payment_method_ll)
    private LinearLayout payment_method_ll;//支付方式
    @ViewInject(R.id.online_pay_rb)
    private RadioButton online_pay_rb;//在线支付
    @ViewInject(R.id.proscenium_pay_rb)
    private RadioButton proscenium_pay_rb;//前台支付

    @ViewInject(R.id.preview_additems_tv)
    private TextView preview_additems_tv;
    @ViewInject(R.id.serviceorder_operation_ll)
    private LinearLayout serviceorder_operation_ll;
    @ViewInject(R.id.btn_appointment)
    private Button btn_appointment;//预约
    @ViewInject(R.id.btn_cancel_appointment)
    private Button btn_cancel_appointment;//取消预约
    @ViewInject(R.id.btn_pay)
    private Button btn_pay;//支付
    @ViewInject(R.id.btn_changedate)
    private Button btn_changedate;//改期

    private ServiceOrderDetailBean detail;
    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_preview_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("服务单明细");
        if (OrderData.getDetailBean() != null) {
            detail = OrderData.getDetailBean();
            initView();
        }
        ((ServiceOrderActivity) getActivity()).btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(backListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("服务单明细");
        if (OrderData.operation != null) {//改期或预约成功后 进入服务单列表界面
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getActivity().getSupportFragmentManager().popBackStack("ServiceOrderListFragment", 0);
            }
        }
    }

    private void initView() {
        preview_package_ll.setOnClickListener(this);
        btn_appointment.setOnClickListener(this);
        btn_cancel_appointment.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_changedate.setOnClickListener(this);

        preview_tv1.setText("体检人：");
        preview_checkupuser.setText(detail.getExamineeName());
        preview_sex.setText(detail.getSexName());
        preview_marry.setText(detail.getMarryName());

        serviceorder_rl.setVisibility(View.VISIBLE);
        serviceorder_num.setText(detail.getServiceOrderNo());
        serviceorder_status.setText(detail.getStatusName());
        if (detail.getPaymentStatus().equals("1") || detail.getPaymentStatus().equals("3")) {
            serviceorder_payment_status.setText("未支付");
            serviceorder_payment_status.setTextColor(getResources().getColor(R.color.green_common));
            //	服务单“未支付”状态：只显示“未付金额：xx.xx(元) ”, 去除其他支付明细项
            serviceorder_payment_tv.setVisibility(View.VISIBLE);
            preview_payment_summary.setVisibility(View.VISIBLE);
            temp_unpaid.setText("¥" + CommonUtil.toPrice(detail.getUnPayAmount()));
        } else if (detail.getPaymentStatus().equals("2")) {
            serviceorder_payment_status.setText("已支付");
            serviceorder_payment_status.setTextColor(getResources().getColor(R.color.gray));
            //	服务单“已支付”状态：不显示支付明细，只显示支付状态为“已支付”, 去除其他支付明细项
            serviceorder_payment_tv.setVisibility(View.GONE);
            preview_payment_summary.setVisibility(View.GONE);
        }

        if (CommonUtil.getStampTime(detail.getServiceEndDate())
                < System.currentTimeMillis()) {//已过期
            serviceorder_tv3.setVisibility(View.VISIBLE);
            serviceorder_time_status.setVisibility(View.VISIBLE);
            serviceorder_operation_ll.setVisibility(View.GONE);
        } else {
            serviceorder_tv3.setVisibility(View.GONE);
            serviceorder_time_status.setVisibility(View.GONE);
            serviceorder_operation_ll.setVisibility(View.VISIBLE);
        }

        preview_certTypeName.setText(detail.getCertTypeName());
        preview_certNumber.setText(detail.getCertNumber());
        preview_phoneNumber.setText(detail.getMobileNumber());
        preview_email.setText(detail.getEmail());

        preview_checkup_packagename.setText(detail.getPackageName());

        if (detail.getPayObjectId().equals("1")) {
            preview_package_payobject.setText("企业付费");
            preview_tv10.setVisibility(View.GONE);
            preview_priceMarket.setVisibility(View.GONE);
        } else if (detail.getPayObjectId().equals("3")) {
            preview_package_payobject.setText("新华付费");
            preview_tv10.setVisibility(View.GONE);
            preview_priceMarket.setVisibility(View.GONE);
        } else if (detail.getPayObjectId().equals("2")) {
            preview_package_payobject.setText("个人付费");
            preview_priceMarket.setText("¥" + CommonUtil.toPrice(detail.getPackageSellPrice()));
        }

        if (detail.getPackageDatas() != null && detail.getPackageDatas().size() > 0) {
            preview_package_ll.setClickable(true);
            preview_package_detail_img.setVisibility(View.VISIBLE);
        } else {
            preview_package_ll.setClickable(false);
            preview_package_detail_img.setVisibility(View.INVISIBLE);
        }

        preview_orgName.setText(detail.getUnderOrgName());
        preview_orgAddress.setText(detail.getUnderOrgAddress());

        if (detail.getStatusId().equals("1")) {//未预约不显示时间
            preview_org_time_ll.setVisibility(View.GONE);
        } else if (detail.getStatusId().equals("2")) {//已预约 显示预约时间 yyyy年MM月dd日 HH:mm-HH:mm
            preview_orgTime.setText(CommonUtil.convertTime1(detail.getAppointmentServiceDate(),
                    detail.getAppointmentStarthour(), detail.getAppointmentStartminute(),
                    detail.getAppointmentEndhour(), detail.getAppointmentEndminute()));
        } else if (detail.getStatusId().equals("3")) {//已到检显示到检时间
            preview_orgTime.setText(detail.getActualServiceTime().substring(0, 16));
        }

        if (detail.getAttachPackageData().size()
                == 0 && detail.getAttachData().size() == 0) {//没有体检项
            View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
            TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
            packageName.setText("体检项为空");
            preview_additems_ll.addView(packageView);
        } else {
            addItems();
        }


        serviceorder_payment_summary.setVisibility(View.GONE);
        serviceorder_detail_payable.setText("¥" + CommonUtil.toPrice(detail.getPayableAmount()));
        serviceorder_detail_paid.setText("¥" + CommonUtil.toPrice(detail.getPaidAmount()));
        if (detail.getUnPayAmount() != null && Double.parseDouble(detail.getUnPayAmount()) > 0) {
            serviceorder_detail_unpaid.setText("¥" + CommonUtil.toPrice(detail.getUnPayAmount()));
        } else {
            serviceorder_detail_unpaid.setText("¥0.00");
//            payment_method_ll.setVisibility(View.GONE);//未付金额为零时不显示付费方法
        }

        serviceorder_detail_refund.setText("¥" + CommonUtil.toPrice(detail.getRefundAmount()));
        serviceorder_compay.setText("¥" + CommonUtil.toPrice(detail.getCompanyPaidAmount()));
        serviceorder_ncipay.setText("¥" + CommonUtil.toPrice(detail.getNciPaidAmount()));
        serviceorder_free.setText("¥" + CommonUtil.toPrice(detail.getFreeAmount()));

        preview_commit.setVisibility(View.GONE);

        if (detail.getStatusId().equals("1")) {//未预约
            btn_appointment.setVisibility(View.VISIBLE);
            btn_cancel_appointment.setVisibility(View.GONE);
            btn_changedate.setVisibility(View.GONE);
            if (detail.getPaymentStatus().equals("1") || detail.getPaymentStatus().equals("3")) {//1未支付 3部分支付
                if (detail.getPaymentMethodId().equals("1")) {//在线支付
                    btn_pay.setVisibility(View.VISIBLE);
                } else if (detail.getPaymentMethodId().equals("2")) {//前台支付
                    btn_pay.setVisibility(View.GONE);
                }
            } else if (detail.getPaymentStatus().equals("2")) {//已支付
                btn_pay.setVisibility(View.GONE);
            }
        } else if (detail.getStatusId().equals("2")) {//已预约
            btn_appointment.setVisibility(View.GONE);
            btn_cancel_appointment.setVisibility(View.VISIBLE);
            btn_changedate.setVisibility(View.VISIBLE);
            if (detail.getPaymentStatus().equals("1") || detail.getPaymentStatus().equals("3")) {//1未支付 3部分支付
                if (detail.getPaymentMethodId().equals("1")) {//在线支付
                    btn_pay.setVisibility(View.VISIBLE);
                } else if (detail.getPaymentMethodId().equals("2")) {//前台支付
                    btn_pay.setVisibility(View.GONE);
                }
            } else if (detail.getPaymentStatus().equals("2")) {//已支付
                btn_pay.setVisibility(View.GONE);
            }
        } else if (detail.getStatusId().equals("3")) {//已到检
            btn_appointment.setVisibility(View.GONE);
            btn_cancel_appointment.setVisibility(View.GONE);
            btn_changedate.setVisibility(View.GONE);
            if (detail.getPaymentStatus().equals("1") || detail.getPaymentStatus().equals("3")) {//1未支付 3部分支付
                if (detail.getPaymentMethodId().equals("1")) {//在线支付
                    btn_pay.setVisibility(View.VISIBLE);
                } else if (detail.getPaymentMethodId().equals("2")) {//前台支付
                    btn_pay.setVisibility(View.GONE);
                }
            } else if (detail.getPaymentStatus().equals("2")) {//已支付
                btn_pay.setVisibility(View.GONE);
            }
        } else if (detail.getStatusId().equals("4")) {//已取消
            btn_appointment.setVisibility(View.GONE);
            btn_cancel_appointment.setVisibility(View.GONE);
            btn_changedate.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
        }

        if (detail.getPaymentMethodId().equals("1")) {//在线支付
            online_pay_rb.setChecked(true);
            proscenium_pay_rb.setClickable(false);
            online_pay_rb.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_button_normal));
            proscenium_pay_rb.setVisibility(View.GONE);
        } else if (detail.getPaymentMethodId().equals("2")) {//前台支付
            proscenium_pay_rb.setChecked(true);
            online_pay_rb.setClickable(false);
            online_pay_rb.setVisibility(View.GONE);
            proscenium_pay_rb.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_button_normal));
        }
    }

    /**
     * 体检项
     */
    private void addItems() {
        if (detail.getAttachPackageData() != null
                && detail.getAttachPackageData().size() > 0) {
            packageData = detail.getAttachPackageData();
            for (int i = 0; i < packageData.size(); i++) {
                //体检项包
                View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
                TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
                packageName.setText(packageData.get(i).getPackageName());
                preview_additems_ll.addView(packageView);

                if (packageData.get(i).getAccountMethod().equals("0")) {//整包计价0
                    for (int j = 0; j < packageData.get(i).getPackageDatas().size(); j++) {
                        View productView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_product_layout, null);
                        TextView productname = (TextView) productView.findViewById(R.id.additem_productname);
                        LinearLayout price_ll = (LinearLayout) productView.findViewById(R.id.additem_price_ll);
                        LinearLayout package_price_ll = (LinearLayout) productView.findViewById(R.id.additem_package_price_ll);
                        TextView package_market_price = (TextView) productView.findViewById(R.id.additem_package_marketPrice);
                        TextView package_sell_price = (TextView) productView.findViewById(R.id.additem_package_sellPrice);

                        productname.setText(packageData.get(i).getPackageDatas().get(j).getProductName());
                        price_ll.setVisibility(View.GONE);
                        if (j == packageData.get(i).getPackageDatas().size() - 1) {
                            package_price_ll.setVisibility(View.VISIBLE);
                            if (packageData.get(i).getPackageDatas().get(0).getPayObject().contains("2")) {
                                package_sell_price.setText("¥" + CommonUtil.toPrice(packageData.get(i).getPackageDatas().get(0).getSellPrice()));
                                package_market_price.setVisibility(View.GONE);
                            } else {
                                if (packageData.get(i).getPackageDatas().get(0).getPayObject().equals("1")) {//企业付费
                                    package_sell_price.setVisibility(View.GONE);
                                    package_market_price.setText("企业付费");
                                } else if (packageData.get(i).getPackageDatas().get(0).getPayObject().equals("3")) {//新华付费
                                    package_sell_price.setVisibility(View.GONE);
                                    package_market_price.setText("新华付费");
                                } else {
                                    package_sell_price.setVisibility(View.GONE);
                                    package_market_price.setText("企业付费+新华付费");
                                }
                            }
                        }
                        preview_additems_ll.addView(productView);
                    }

                } else if (packageData.get(i).getAccountMethod().equals("1")) {//累计计价1
                    for (int j = 0; j < packageData.get(i).getPackageDatas().size(); j++) {
                        View productView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_product_layout, null);
                        TextView productname = (TextView) productView.findViewById(R.id.additem_productname);
                        TextView market_price = (TextView) productView.findViewById(R.id.additem_marketPrice);
                        TextView sell_price = (TextView) productView.findViewById(R.id.additem_sellPrice);

                        productname.setText(packageData.get(i).getPackageDatas().get(j).getProductName());
                        if (packageData.get(i).getPackageDatas().get(j).getPayObject().contains("2")) {
                            sell_price.setText("¥" + CommonUtil.toPrice(packageData.get(i).getPackageDatas().get(j).getSellPrice()));
                            market_price.setVisibility(View.GONE);
                        } else {
                            if (packageData.get(i).getPackageDatas().get(j).getPayObject().equals("1")) {
                                sell_price.setVisibility(View.GONE);
                                market_price.setText("企业付费");
                            } else if (packageData.get(i).getPackageDatas().get(j).getPayObject().equals("3")) {
                                sell_price.setVisibility(View.GONE);
                                market_price.setText("新华付费");
                            } else {
                                sell_price.setVisibility(View.GONE);
                                market_price.setText("企业付费+新华付费");
                            }
                        }
                        preview_additems_ll.addView(productView);
                    }
                }
            }
        }

        //单一体检项
        if (detail.getAttachData() != null
                && detail.getAttachData().size() > 0) {
            attachData = detail.getAttachData();
            View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
            TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
            packageName.setText("体检项");
            preview_additems_ll.addView(packageView);
            for (int l = 0; l < attachData.size(); l++) {
                View productView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_product_layout, null);
                TextView productname = (TextView) productView.findViewById(R.id.additem_productname);
                TextView market_price = (TextView) productView.findViewById(R.id.additem_marketPrice);
                TextView sell_price = (TextView) productView.findViewById(R.id.additem_sellPrice);

                productname.setText(attachData.get(l).getProductName());
                if (attachData.get(l).getPayObject().contains("2")) {
                    sell_price.setText("¥" + CommonUtil.toPrice(attachData.get(l).getSellPrice()));
                    market_price.setVisibility(View.GONE);
                } else {
                    if (attachData.get(l).getPayObject().equals("1")) {
                        sell_price.setVisibility(View.GONE);
                        market_price.setText("企业付费");
                    } else if (attachData.get(l).getPayObject().equals("3")) {
                        sell_price.setVisibility(View.GONE);
                        market_price.setText("新华付费");
                    } else {
                        sell_price.setVisibility(View.GONE);
                        market_price.setText("企业付费+新华付费");
                    }
                }
                preview_additems_ll.addView(productView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_package_ll://套餐详情
                ServiceOrderData.isPriceSell = true;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_fl_continer, new PackageDetailFragment())
                        .addToBackStack("PackageDetailFragment").commit();
                break;
            case R.id.btn_appointment://预约
                queryServiceOrderStatus(detail.getServiceOrderId(), "appointment");
                break;
            case R.id.btn_cancel_appointment://取消预约
                queryServiceOrderStatus(detail.getServiceOrderId(), "cancel");
                break;
            case R.id.btn_pay://去支付
                queryServiceOrderStatus(detail.getServiceOrderId(), "pay");
                break;
            case R.id.btn_changedate://改期
                queryServiceOrderStatus(detail.getServiceOrderId(), "changedate");
                break;
        }
    }


    /**
     * 查询服务单状态
     *
     * @param serviceOrderId
     * @return
     */
    private void queryServiceOrderStatus(String serviceOrderId, final String operation) {
        String bizId = "000";
        String serviceName = "queryServiceOrderStatusBook";
        Map<String, String> map = new HashMap<String, String>();
        map.put("serviceOrderId", serviceOrderId);
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        String para = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, para,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        canclDialog();
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                toOperation(operation);
                            } else if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString()
                                    .equals("1120")) {
                                dialog = new CommonDialog(context, 1, "确定", "",
                                        new View.OnClickListener() {
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
                                                startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                            }
                                        }, null, null, "当前账户在其他设备登录，请重新登录。");
                                dialog.show();
                            } else if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString()
                                    .equals("1127")) {
                                String resultMsg = obj.get("resultMsg").toString();
                                dialog = new CommonDialog(context, 1, "确定", null,
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                OrderData.operation = "orderError";
                                                getFragmentManager().popBackStack();
                                            }
                                        }, null, null, resultMsg);
                                dialog.show();
                            } else if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString()
                                    .equals("1128")) {
                                if (operation.equals("pay")) {
                                    toOperation(operation);
                                } else {
                                    String resultMsg = obj.get("resultMsg").toString();
                                    dialog = new CommonDialog(context, 1, "确定", null,
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    OrderData.operation = "orderError";
                                                    getFragmentManager().popBackStack();
                                                }
                                            }, null, null, resultMsg);
                                    dialog.show();
                                }
                            } else if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString()
                                    .equals("1")) {
                                CommonToast.makeText(context, obj.getString("resultMsg"));
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 检查服务单状态后
     * 预约 取消预约 改期 支付
     *
     * @param operation
     */
    private void toOperation(String operation) {
        if (operation.equals("appointment")) {//预约
            toAppointment();
        } else if (operation.equals("cancel")) {//取消预约
            dialog = new CommonDialog(context, 2, "取消预约", "否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelServiceOrder(detail.getServiceOrderId());
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            }, null, "取消预约?");
            dialog.show();
        } else if (operation.equals("pay")) {//支付
            ServiceOrderPayFragment fragment = new ServiceOrderPayFragment();
            Bundle args = new Bundle();
            double fee = 0;
            //用应付金额减去已付金额得出未付金额
//            if (detail.getPayableAmount() != null && !detail.getPayableAmount().equals("")) {
//                if (detail.getPaidAmount() != null && !detail.getPaidAmount().equals("")) {
//                    fee = Double.parseDouble(detail.getPayableAmount()) - Double.parseDouble(detail.getPaidAmount());
//                }
//            }

            //直接取出未付金额
            if (detail.getUnPayAmount() != null && !detail.getUnPayAmount().equals("")) {
                fee = Double.parseDouble(detail.getUnPayAmount());
            }
            args.putString("payment_num", detail.getPaymentOrderNum());
            args.putString("pay_orderfee", fee + "");
            fragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fl_continer, fragment)
                    .addToBackStack("ServiceOrderPayFragment").commit();
        } else if (operation.equals("changedate")) {//改期
            changedate();
        }
    }

    /**
     * 预约
     */
    private void toAppointment() {
        ServiceOrderData.setOrderDetail(detail);
        ServiceOrderData.orderDetail = detail;
        Intent intent = new Intent(context, AppointmentActivity.class);
        intent.putExtra("operation", "order");
        startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
    }

    /**
     * 取消预约
     */
    private void cancelServiceOrder(String serviceOrderId) {
        String bizId = "000";
        String serviceName = "cancelServiceOrderBook";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("serviceOrderId", serviceOrderId);
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        String para = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, para,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
//                        super.onSuccess(arg0);
                        canclDialog();
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {//取消成功
                                Log.e("取消预约", result);
                                dialog.dismiss();
                                //服务单是“已支付”状态，提示以下信息；
                                if (detail.getPaymentStatus().equals("2")) {
                                    dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            OrderData.operation = "cancelOrder";
                                            getFragmentManager().popBackStack();
                                        }
                                    }, null, "预约取消成功", "服务单号：" + detail.getServiceOrderNo()
                                            + "\r\n体检人：" + detail.getExamineeName()
                                            + "\r\n体检机构：" + detail.getUnderOrgName()
                                            + "\r\n\r\n如需退款，请联系客服。"
                                            + "\r\n电话：(010)85920097"
                                    );
                                    dialog.show();
                                    TextView textView = (TextView) dialog.findViewById(R.id.common_dialog_content);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.LEFT);
                                } else {
                                    OrderData.operation = "cancelOrder";
                                    getFragmentManager().popBackStack();
                                }
                            } else if (obj.get("errorCode").toString().equals("1120")) {
                                dialog = new CommonDialog(context, 1, "确定", "",
                                        new View.OnClickListener() {
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
                                                context.startActivity(intent);
                                                dialog.dismiss();
                                                ((Activity) context).finish();
                                            }
                                        }, null, null, "当前账户在其他设备登录，请重新登录。");
                                dialog.show();
                            } else if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("1")) {//取消失败
                                dialog.dismiss();
                                CommonToast.makeText(context, "取消失败，请检查服务单的状态。");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 改期
     */
    private void changedate() {
        ServiceOrderData.setOrderDetail(detail);
        Intent intent = new Intent(context, AppointmentActivity.class);
        intent.putExtra("operation", "changedate");
        startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
    }

    private View.OnKeyListener backListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                getFragmentManager().popBackStack();
                return true;
            } //后退
            return false;    //已处理
        }
    };

}
