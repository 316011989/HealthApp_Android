/**
 *
 */
package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.pact.healthapp.components.serviceorder.ServiceOrderDetailBean;
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
 * 我的服务单改期或者预约后
 * 选择排期预览
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class Preview2Fragment extends BaseFragment implements View.OnClickListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
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
    @ViewInject(R.id.preview_org_time)
    private TextView preview_orgTime;//体检时间

    @ViewInject(R.id.preview_additems_tv)
    private TextView preview_additems_tv;
    @ViewInject(R.id.preview_additems_ll)
    private LinearLayout preview_additems_ll;//体检项包
    private List<ServiceOrderDetailBean.AttachPackageDataEntity> packageData = new ArrayList<ServiceOrderDetailBean.AttachPackageDataEntity>();
    private List<ServiceOrderDetailBean.AttachDataEntity> attachData = new ArrayList<ServiceOrderDetailBean.AttachDataEntity>();

    //由于之前的crm系统缺陷,暂时只显示未付金额
    @ViewInject(R.id.preview_payment_summary)
    private LinearLayout preview_payment_summary;//预览界面的合计信息
    @ViewInject(R.id.preview_unpaid)
    private TextView temp_unpaid;//未付金额

    @ViewInject(R.id.serviceorder_payment_summary)
    private RelativeLayout serviceorder_payment_summary;//服务单明细界面的合计信息
    @ViewInject(R.id.serviceorder_payable)
    private TextView preview_payable;//个人应付
    @ViewInject(R.id.serviceorder_paid)
    private TextView preview_paid;//个人已付
    @ViewInject(R.id.serviceorder_unpaid)
    private TextView preview_unpaid;//未付金额
    @ViewInject(R.id.serviceorder_refund)
    private TextView preview_refund;//退款金额
    @ViewInject(R.id.serviceorder_ncipay)
    private TextView serviceorder_ncipay;//新华付费
    @ViewInject(R.id.serviceorder_compay)
    private TextView serviceorder_compay;//企业付费
    @ViewInject(R.id.serviceorder_free)
    private TextView serviceorder_free;//免费

    @ViewInject(R.id.payment_method_ll)
    private LinearLayout payment_method_ll;//支付方式
    @ViewInject(R.id.online_pay_rb)
    private RadioButton online_pay_rb;//在线支付
    @ViewInject(R.id.proscenium_pay_rb)
    private RadioButton proscenium_pay_rb;//前台支付
    private int paymentMethod = -1;

    @ViewInject(R.id.preview_commit)
    private Button preview_commit;//提交

    //服务单生成 失败的提示框
    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_preview_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("预览");
        initView();
        return view;
    }

    private void initView() {
        preview_package_ll.setOnClickListener(this);

        if (ServiceOrderData.operation != null
                && ServiceOrderData.operation.equals("changedate")) {
            preview_commit.setBackgroundDrawable(getResources().getDrawable(R.drawable.appointment_btn_selector));
            preview_commit.setText("改期");
        } else if (ServiceOrderData.operation != null
                && ServiceOrderData.operation.equals("order")) {
            preview_commit.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            preview_commit.setText("预约");
        }

        preview_commit.setOnClickListener(this);

        preview_tv1.setText("姓名：");
        preview_checkupuser.setText(ServiceOrderData.orderDetail.getExamineeName());
        preview_sex.setText(ServiceOrderData.orderDetail.getSexName());
        preview_marry.setText(ServiceOrderData.orderDetail.getMarryName());

        preview_certTypeName.setText(ServiceOrderData.orderDetail.getCertTypeName());
        preview_certNumber.setText(ServiceOrderData.orderDetail.getCertNumber());
        if (ServiceOrderData.orderDetail.getMobileNumber() != null) {//有手机号
            preview_phoneNumber.setText(ServiceOrderData.orderDetail.getMobileNumber());
        }
        if (ServiceOrderData.orderDetail.getEmail() != null) {//有邮箱号
            preview_email.setText(ServiceOrderData.orderDetail.getEmail());
        }

        preview_checkup_packagename.setText(ServiceOrderData.orderDetail.getPackageName());

        if (ServiceOrderData.orderDetail.getPayObjectId().equals("1")) {
            preview_package_payobject.setText("企业付费");
            preview_tv10.setVisibility(View.GONE);
            preview_priceMarket.setVisibility(View.GONE);
        } else if (ServiceOrderData.orderDetail.getPayObjectId().equals("3")) {
            preview_package_payobject.setText("新华付费");
            preview_tv10.setVisibility(View.GONE);
            preview_priceMarket.setVisibility(View.GONE);
        } else if (ServiceOrderData.orderDetail.getPayObjectId().equals("2")) {
            preview_package_payobject.setText("个人付费");
            if (ServiceOrderData.orderDetail.getPackageSellPrice() != null
                    && Double.parseDouble(CommonUtil.toPrice(ServiceOrderData.orderDetail.getPackageSellPrice())) > 0) {
                preview_priceMarket.setText("¥ " + CommonUtil.toPrice(ServiceOrderData.orderDetail.getPackageSellPrice()));
            } else {
                preview_priceMarket.setText("¥ 0.00");
            }
        }

        if (ServiceOrderData.orderDetail.getPackageDatas() != null
                && ServiceOrderData.orderDetail.getPackageDatas().size() > 0) {
            preview_package_ll.setClickable(true);
            preview_package_detail_img.setVisibility(View.VISIBLE);
        } else {
            preview_package_ll.setClickable(false);
            preview_package_detail_img.setVisibility(View.INVISIBLE);
        }

        preview_orgName.setText(ServiceOrderData.orderDetail.getUnderOrgName());
        if (ServiceOrderData.orderDetail.getUnderOrgAddress() != null) {
            preview_orgAddress.setText(ServiceOrderData.orderDetail.getUnderOrgAddress());
        }

        if (ServiceOrderData.choicedTime.getStartDate() != null && ServiceOrderData.choicedTime.getEndDate() != null) {
            preview_orgTime.setText(CommonUtil.convertTime(ServiceOrderData.choicedTime.getStartDate(), ServiceOrderData.choicedTime.getEndDate()));
        }

        if ((ServiceOrderData.orderDetail.getAttachData() == null
                || ServiceOrderData.orderDetail.getAttachData().size() == 0)
                && (ServiceOrderData.orderDetail.getAttachPackageData() == null
                || ServiceOrderData.orderDetail.getAttachPackageData().size() == 0)) {//没有体检项
            View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
            TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
            packageName.setText("体检项为空");
            preview_additems_ll.addView(packageView);
        } else {
            preview_additems_ll.removeAllViews();
            addItems();
        }

        preview_payment_summary.setVisibility(View.VISIBLE);
        temp_unpaid.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getUnPayAmount()));

        serviceorder_payment_summary.setVisibility(View.GONE);
        preview_payable.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getPayableAmount()));
        preview_paid.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getPaidAmount()));

        if (ServiceOrderData.orderDetail.getUnpaidAmount() != null
                && Double.parseDouble(ServiceOrderData.orderDetail.getUnPayAmount()) > 0) {
            preview_unpaid.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getUnPayAmount()));
        } else {
            preview_unpaid.setText("¥0.00");
//            payment_method_ll.setVisibility(View.GONE);//未付金额为零时不显示付费方法
        }

        preview_refund.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getRefundAmount()));
        serviceorder_compay.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getCompanyPaidAmount()));
        serviceorder_ncipay.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getNciPaidAmount()));
        serviceorder_free.setText("¥" + CommonUtil.toPrice(ServiceOrderData.orderDetail.getFreeAmount()));

        if (ServiceOrderData.orderDetail.getPaymentMethodId().equals("1")) {
            paymentMethod = 1;
            online_pay_rb.setChecked(true);
            proscenium_pay_rb.setChecked(false);
            online_pay_rb.setClickable(false);
            proscenium_pay_rb.setClickable(false);

            online_pay_rb.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_button_normal));
            proscenium_pay_rb.setVisibility(View.GONE);
        } else if (ServiceOrderData.orderDetail.getPaymentMethodId().equals("2")) {
            paymentMethod = 2;
            online_pay_rb.setChecked(false);
            proscenium_pay_rb.setChecked(true);
            online_pay_rb.setClickable(false);
            proscenium_pay_rb.setClickable(false);

            online_pay_rb.setVisibility(View.GONE);
            proscenium_pay_rb.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_button_normal));
        }

    }

    private void addItems() {
        if (ServiceOrderData.orderDetail.getAttachPackageData() != null
                && ServiceOrderData.orderDetail.getAttachPackageData().size() > 0) {
            packageData = ServiceOrderData.orderDetail.getAttachPackageData();
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
        if (ServiceOrderData.orderDetail.getAttachData() != null
                && ServiceOrderData.orderDetail.getAttachData().size() > 0) {
            attachData = ServiceOrderData.orderDetail.getAttachData();
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
            case R.id.preview_package_ll://进入套餐详情
                ((AppointmentActivity) getActivity()).from = "preview2";
                ServiceOrderData.isPriceSell = true;
                ((AppointmentActivity) getActivity()).toStepm4();
                break;
            case R.id.preview_commit:
                reBookDateBook();
                break;
        }
    }

    /**
     * 重新预约
     */
    private void reBookDateBook() {
        String bizId = "000";
        String serviceName = "reBookDateBook";
        Map<String, String> map = new HashMap<String, String>();
        map.put("serviceOrderId", ServiceOrderData.orderDetail.getServiceOrderId());
        map.put("timeId", ServiceOrderData.choicedTime.getDaytimeId());
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
                                    .equals("0")) {// 生成服务单成功
                                Log.e("生成服务单", result);
                                parseJsonSuccess(result);
                            } else if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("1")) {//生成服务单失败
                                parseJsonFailure(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 解析json数据 提交成功
     *
     * @param result 解密后的json数据
     */
    private void parseJsonSuccess(String result) {
        ServiceOrderData.serviceOrderId = ServiceOrderData.orderDetail.getServiceOrderId();//服务单Id
        ServiceOrderData.serviceOrderNo = ServiceOrderData.orderDetail.getServiceOrderNo();//服务单号
        ServiceOrderData.selfPay = ServiceOrderData.orderDetail.getUnPayAmount();//支付金额
        ServiceOrderData.paymentOrderNum = ServiceOrderData.orderDetail.getPaymentOrderNum();//支付单号
        ServiceOrderData.paymentMethod = paymentMethod;//支付方式

        ((AppointmentActivity) getActivity()).toStepm7();
    }

    /**
     * 解析json数据 提交失败
     *
     * @param result 解密后的json数据
     */
    private void parseJsonFailure(String result) {
        if (ServiceOrderData.operation != null
                && ServiceOrderData.operation.equals("changedate")) {//改期
//            dialog = new CommonDialog(context, 1, "确定", null
//                    , new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            }, null, null, "提交失败，请重新改期。");
//            dialog.show();
            CommonToast.makeText(context, "提交失败，请重新改期。");
        } else if (ServiceOrderData.operation != null
                && ServiceOrderData.operation.equals("order")) {//预约
//            dialog = new CommonDialog(context, 1, "确定", null
//                    , new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            }, null, null, "提交失败，请重新预约。");
//            dialog.show();
            CommonToast.makeText(context, "提交失败，请重新预约。");
        }
    }

}
