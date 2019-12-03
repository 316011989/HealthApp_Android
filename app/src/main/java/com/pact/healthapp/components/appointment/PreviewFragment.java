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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预览
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class PreviewFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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
    private List<AddItemBean> list = new ArrayList<AddItemBean>();
    @ViewInject(R.id.preview_additems_ll)
    private LinearLayout preview_additems_ll;//加项包
    //之前选中过的以散项形式保存的加项
    private List<AddItemBean.PackageDatasEntity> packagelist = new ArrayList<AddItemBean.PackageDatasEntity>();

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
    private TextView preview_ncipay;//新华付费
    @ViewInject(R.id.serviceorder_compay)
    private TextView preview_compay;//企业付费
    @ViewInject(R.id.serviceorder_free)
    private TextView preview_free;//免费

    @ViewInject(R.id.payment_method_ll)
    private LinearLayout payment_method_ll;//支付方式
    @ViewInject(R.id.online_pay_rb)
    private RadioButton online_pay_rb;//在线支付
    @ViewInject(R.id.proscenium_pay_rb)
    private RadioButton proscenium_pay_rb;//前台支付
    private int paymentMethod = 1;

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ServiceOrderData.payable < 0 && ServiceOrderData.paid < 0
                && ServiceOrderData.unpaid < 0 && ServiceOrderData.packagePriceSell < 0) {
            calculateAmount();
        } else {
            initView();
        }
    }

    private void initView() {
        preview_package_ll.setOnClickListener(this);

        online_pay_rb.setOnCheckedChangeListener(this);
        proscenium_pay_rb.setOnCheckedChangeListener(this);
        preview_commit.setOnClickListener(this);

        preview_tv1.setText("体检人：");
        preview_checkupuser.setText(ServiceOrderData.cb.getCustomerName());
        preview_sex.setText(ServiceOrderData.cb.getSexName());
        preview_marry.setText(ServiceOrderData.cb.getMarryName());

        preview_certTypeName.setText(ServiceOrderData.cb.getCertTypeName());
        preview_certNumber.setText(ServiceOrderData.cb.getCertNumber());
        if (ServiceOrderData.cb.getMobileNumber() != null
                && ServiceOrderData.cb.getMobileNumber().length() == 11) {//有手机号
            if (ServiceOrderData.userssize > 1) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(ServiceOrderData.cb.getMobileNumber().substring(0, 3));
                stringBuffer.append("****");
                stringBuffer.append(ServiceOrderData.cb.getMobileNumber().substring(7, 11));
                preview_phoneNumber.setText(stringBuffer);
            } else {
                preview_phoneNumber.setText(ServiceOrderData.cb.getMobileNumber());
            }
        }
        if (ServiceOrderData.cb.getEmail() != null && !ServiceOrderData.cb.getEmail().equals("")) {//有邮箱号
            preview_email.setText(ServiceOrderData.cb.getEmail());
        }

        preview_checkup_packagename.setText(ServiceOrderData.pb.getPackageName());

        if (ServiceOrderData.pb.getPayObject().equals("1")) {
            preview_package_payobject.setText("企业付费");
            preview_tv10.setVisibility(View.GONE);
            preview_priceMarket.setVisibility(View.GONE);
        } else if (ServiceOrderData.pb.getPayObject().equals("3")) {
            preview_package_payobject.setText("新华付费");
            preview_tv10.setVisibility(View.GONE);
            preview_priceMarket.setVisibility(View.GONE);
        } else if (ServiceOrderData.pb.getPayObject().equals("2")) {
            preview_package_payobject.setText("个人付费");
            if (ServiceOrderData.packagePriceSell > 0) {
                preview_priceMarket.setText("¥ " + CommonUtil.toPrice(ServiceOrderData.packagePriceSell + ""));
            } else {
                preview_priceMarket.setText("¥ 0.00");
            }
        }

        if (ServiceOrderData.pb.getPackageProductNumbers().equals("0")) {//空套餐
            preview_package_detail_img.setVisibility(View.INVISIBLE);
            preview_package_ll.setClickable(false);
        } else {//非空套餐
            preview_package_detail_img.setVisibility(View.VISIBLE);
            preview_package_ll.setClickable(true);
        }

        preview_orgName.setText(ServiceOrderData.ob.getOrgName());
        if (ServiceOrderData.ob.getAddress() != null) {
            preview_orgAddress.setText(ServiceOrderData.ob.getAddress());
        }

        if (ServiceOrderData.choicedTime != null) {
            preview_orgTime.setText(CommonUtil.convertTime(ServiceOrderData.choicedTime.getStartDate(), ServiceOrderData.choicedTime.getEndDate()));
        }

        if (ServiceOrderData.selectedAddItems == null
                || ServiceOrderData.selectedAddItems.size() == 0) {//没有加项
            View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
            TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
            packageName.setText("体检项为空");
            preview_additems_ll.addView(packageView);
        } else {
            addItemsList();
        }

        preview_payment_summary.setVisibility(View.VISIBLE);
        temp_unpaid.setText("¥" + CommonUtil.toPrice(ServiceOrderData.unpaid + ""));

        serviceorder_payment_summary.setVisibility(View.GONE);
        preview_payable.setText("¥" + CommonUtil.toPrice(ServiceOrderData.payable + ""));
        preview_paid.setText("¥" + CommonUtil.toPrice(ServiceOrderData.paid + ""));
        if (ServiceOrderData.unpaid > 0) {
            preview_unpaid.setText("¥" + CommonUtil.toPrice(ServiceOrderData.unpaid + ""));
        } else {
            preview_unpaid.setText("¥0.00");
            payment_method_ll.setVisibility(View.GONE);//未付金额为零时不显示付费方法
        }
        preview_refund.setText("¥0.00");
        preview_compay.setText("¥" + CommonUtil.toPrice(ServiceOrderData.companyPaidAmount));
        preview_ncipay.setText("¥" + CommonUtil.toPrice(ServiceOrderData.nciPaidAmount));
        preview_free.setText("¥" + CommonUtil.toPrice(ServiceOrderData.freeAmount));

    }


    /**
     * 把散项形式的加项 构造成加项包
     */
    private void addItemsList() {
        packagelist.clear();
        packagelist.addAll(ServiceOrderData.selectedAddItems);
        list.clear();
        for (int i = 0; i < packagelist.size(); i++) {
            if (packagelist.get(i).getType().equals("A")) {
                List<AddItemBean.PackageDatasEntity> packageTemp = new ArrayList<AddItemBean.PackageDatasEntity>();
                packageTemp.add(packagelist.get(i));
                for (int j = i + 1; j < packagelist.size(); j++) {
                    String packageId1 = packagelist.get(i).getPackageId();
                    String packageId2 = packagelist.get(j).getPackageId();
                    if (packageId1.equals(packageId2)) {
                        packageTemp.add(packagelist.get(j));
                        packagelist.remove(j);
                        j--;
                    }
                }
                AddItemBean bean = new AddItemBean();
                bean.setPackageName(packagelist.get(i).getPackageName());
                bean.setPriceMarket(packagelist.get(i).getPriceMarket());
                bean.setPriceSell(packagelist.get(i).getPriceSell());
                bean.setAccountType(packagelist.get(i).getAccountType());
                bean.setMust(packagelist.get(i).getMust());
                bean.setType(packagelist.get(i).getType());
                bean.setPackageDatas(packageTemp);
                list.add(bean);
            }
        }

        List<AddItemBean.PackageDatasEntity> packageTemp = new ArrayList<AddItemBean.PackageDatasEntity>();
        for (int i = 0; i < (packagelist.size()); i++) {
            if (packagelist.get(i).getType().equals("P")) {
                packageTemp.add(packagelist.get(i));
            }
        }
        if (packageTemp != null && packageTemp.size() > 0) {
            AddItemBean bean = new AddItemBean();
            bean.setPackageName("体检项");
            bean.setPriceMarket("0");
            bean.setPriceSell("0");
            bean.setAccountType("");
            bean.setMust(false);
            bean.setType("P");
            bean.setPackageDatas(packageTemp);
            list.add(bean);
        }
        if (list.size() > 0) {
            preview_additems_ll.removeAllViews();
            addItems();//加载加项UI
        }
    }

    /**
     * 以addview方式加载UI
     */
    private void addItems() {
        for (int i = 0; i < list.size(); i++) {
            //加项包
            if (list.get(i).getType().equals("A")) {//加项包
                View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
                TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
                packageName.setText(list.get(i).getPackageName());
                preview_additems_ll.addView(packageView);
                if (list.get(i).getAccountType().equals("0")) {//整包计价0
                    for (int j = 0; j < list.get(i).getPackageDatas().size(); j++) {
                        View productView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_product_layout, null);
                        TextView productname = (TextView) productView.findViewById(R.id.additem_productname);
                        LinearLayout price_ll = (LinearLayout) productView.findViewById(R.id.additem_price_ll);
                        LinearLayout package_price_ll = (LinearLayout) productView.findViewById(R.id.additem_package_price_ll);
                        TextView package_market_price = (TextView) productView.findViewById(R.id.additem_package_marketPrice);
                        TextView package_sell_price = (TextView) productView.findViewById(R.id.additem_package_sellPrice);

                        productname.setText(list.get(i).getPackageDatas().get(j).getProductName());
                        price_ll.setVisibility(View.GONE);

                        if (j == list.get(i).getPackageDatas().size() - 1) {
                            package_price_ll.setVisibility(View.VISIBLE);
                            if (list.get(i).getPackageDatas().get(0).getPayObject().contains("2")) {
                                package_sell_price.setText("¥" + CommonUtil.toPrice(list.get(i).getPriceSell()));
                                package_market_price.setVisibility(View.GONE);
                            } else {
                                if (list.get(i).getPackageDatas().get(0).getPayObject().equals("1")) {
                                    package_sell_price.setVisibility(View.GONE);
                                    package_market_price.setText("企业付费");
                                } else if (list.get(i).getPackageDatas().get(0).getPayObject().equals("3")) {
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
                } else if (list.get(i).getAccountType().equals("1")) {//累计计价1
                    for (int j = 0; j < list.get(i).getPackageDatas().size(); j++) {
                        View productView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_product_layout, null);
                        TextView productname = (TextView) productView.findViewById(R.id.additem_productname);
                        TextView market_price = (TextView) productView.findViewById(R.id.additem_marketPrice);
                        TextView sell_price = (TextView) productView.findViewById(R.id.additem_sellPrice);

                        productname.setText(list.get(i).getPackageDatas().get(j).getProductName());

                        if (list.get(i).getPackageDatas().get(j).getPayObject().contains("2")) {
                            sell_price.setText("¥" + CommonUtil.toPrice(list.get(i).getPackageDatas().get(j).getPriceSell()));
                            market_price.setVisibility(View.GONE);
                        } else {
                            if (list.get(i).getPackageDatas().get(j).getPayObject().equals("1")) {
                                sell_price.setVisibility(View.GONE);
                                market_price.setText("企业付费");
                            } else if (list.get(i).getPackageDatas().get(j).getPayObject().equals("3")) {
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
            } else if (list.get(i).getType().equals("P")) {//散项
                if (list.get(i).getPackageDatas().size() > 0) {
                    View packageView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_package_layout, null);
                    TextView packageName = (TextView) packageView.findViewById(R.id.additem_packagename);
                    packageName.setText(list.get(i).getPackageName());
                    preview_additems_ll.addView(packageView);
                    for (int l = 0; l < list.get(i).getPackageDatas().size(); l++) {
                        View productView = LayoutInflater.from(context).inflate(R.layout.serviceorder_additem_product_layout, null);
                        TextView productname = (TextView) productView.findViewById(R.id.additem_productname);
                        TextView market_price = (TextView) productView.findViewById(R.id.additem_marketPrice);
                        TextView sell_price = (TextView) productView.findViewById(R.id.additem_sellPrice);

                        productname.setText(list.get(i).getPackageDatas().get(l).getProductName());
                        if (list.get(i).getPackageDatas().get(l).getPayObject().contains("2")) {
                            sell_price.setText("¥" + CommonUtil.toPrice(list.get(i).getPackageDatas().get(l).getPriceSell()));
                            market_price.setVisibility(View.GONE);
                        } else {
                            if (list.get(i).getPackageDatas().get(l).getPayObject().equals("1")) {
                                sell_price.setVisibility(View.GONE);
                                market_price.setText("企业付费");
                            } else if (list.get(i).getPackageDatas().get(l).getPayObject().equals("3")) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_package_ll://非空套餐，进入套餐详情
                if (!ServiceOrderData.pb.getPackageProductNumbers().equals("0")) {
                    ((AppointmentActivity) getActivity()).from = "preview";
                    ServiceOrderData.isPriceSell = true;
                    ((AppointmentActivity) getActivity()).toStepm4();
                }
                break;
            case R.id.preview_commit:
                createServiceOrderBook();
                break;
        }
    }

    /**
     * 生成服务单
     */
    private void createServiceOrderBook() {
        String bizId = "000";
        String serviceName = "createServiceOrderBook";
        Map<String, String> map = new HashMap<String, String>();
        map.put("isExamination", "Y");
        map.put("voucherId", ServiceOrderData.vb.getVoucherId());
        map.put("crmCustomerId", ServiceOrderData.cb.getCustomerId());
        map.put("groupPackId", ServiceOrderData.pb.getGroupPackId());

        //套餐原子产品
        JSONArray packageProduct = new JSONArray();
        if (ServiceOrderData.underProductList != null && ServiceOrderData.underProductList.size() > 0) {//非空套餐
            for (int i = 0; i < ServiceOrderData.underProductList.size(); i++) {
                JSONObject packagePVO = new JSONObject();
                packagePVO.put("productId", ServiceOrderData.underProductList.get(i).getProductId());
                packagePVO.put("costCount", ServiceOrderData.underProductList.get(i).getCostCount());   //本次使用次数
                packageProduct.add(packagePVO);
            }
        } else {//空套餐
            JSONObject packagePVO = new JSONObject();
            packagePVO.put("productId", "");
            packagePVO.put("costCount", "");   //本次使用次数
            packageProduct.add(packagePVO);
        }

        map.put("packageProductArray", packageProduct.toString());
        map.put("orgId", ServiceOrderData.ob.getOrgId());
        map.put("packageId", ServiceOrderData.pb.getPackageId());

        //加项产品列表
        JSONArray attachProduct = new JSONArray();
        //加项产品
        if (ServiceOrderData.selectedAddItems.size() > 0) {
            for (int i = 0; i < ServiceOrderData.selectedAddItems.size(); i++) {
                JSONObject attachP1 = new JSONObject();
                attachP1.put("packageId", ServiceOrderData.selectedAddItems.get(i).getPackageId());
                attachP1.put("productId", ServiceOrderData.selectedAddItems.get(i).getProductId());
                //costProductCount购买加项产品数量
                attachP1.put("costProductCount", "1");
                //costCount本次使用数量
                attachP1.put("costCount", ServiceOrderData.selectedAddItems.get(i).getCostCount());
                attachProduct.add(attachP1);
            }
        } else {
            JSONObject attachP1 = new JSONObject();
            attachP1.put("packageId", "");
            attachP1.put("productId", "");
            //costProductCount购买加项产品数量
            attachP1.put("costProductCount", "1");
            //costCount本次使用数量
            attachP1.put("costCount", "1");
            attachProduct.add(attachP1);
        }

        map.put("attachProductArray", attachProduct.toString());
        map.put("paymentMethod", paymentMethod + "");//支付方式 1：在线支付 2：前台支付
        map.put("timeId", ServiceOrderData.choicedTime.getDaytimeId());

        map.put("certType", ServiceOrderData.cb.getCertTypeId());
        map.put("certNumber", ServiceOrderData.cb.getCertNumber());
        map.put("customerName", ServiceOrderData.cb.getCustomerName());
        map.put("fromWhere", ServiceOrderData.fromWhere);

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
                                parseJsonSuccess(result, paymentMethod);
                            } else if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("1")) {//生成服务单失败
                                parseJsonFailure(result);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
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
    private void parseJsonSuccess(String result, int paymentMethod) {
        JSONObject obj = JSONObject.parseObject(result);
        JSONObject data = obj.getJSONObject("data");
        ServiceOrderData.serviceOrderId = data.getString("serviceOrderId");//服务单Id
        ServiceOrderData.serviceOrderNo = data.getString("serviceOrderNo");//服务单号
        ServiceOrderData.selfPay = data.getString("selfPay");//支付金额
        ServiceOrderData.paymentOrderNum = data.getString("paymentOrderNum");//支付单号
        ServiceOrderData.paymentMethod = paymentMethod;
        ((AppointmentActivity) getActivity()).toStepm7();
    }

    /**
     * 解析json数据 提交失败
     *
     * @param result 解密后的json数据
     */
    private void parseJsonFailure(String result) {
        dialog = new CommonDialog(context, 1, "确定", null
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }, null, null, "提交失败，请重新预约。");
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.online_pay_rb://在线支付
                if (isChecked) {
                    paymentMethod = 1;
                }
                break;
            case R.id.proscenium_pay_rb://前台支付
                if (isChecked) {
                    paymentMethod = 2;
                }
                break;
        }
    }

    /**
     * 拼接计算金额接口所需参数
     */
    private void calculateAmount() {
        //套餐原子产品
        JSONArray packageProduct = new JSONArray();
        if (ServiceOrderData.packageitems != null && ServiceOrderData.packageitems.size() > 0) {
            for (int i = 0; i < ServiceOrderData.packageitems.size(); i++) {
                JSONObject packagePVO = new JSONObject();
                packagePVO.put("productId", ServiceOrderData.packageitems.get(i).getProductId());
                packagePVO.put("costCount", ServiceOrderData.packageitems.get(i).getCostCount());   //本次使用次数
                packageProduct.add(packagePVO);
            }
        } else {
            JSONObject packagePVO = new JSONObject();
            packagePVO.put("productId", "");
            packagePVO.put("costCount", "");   //本次使用次数
            packageProduct.add(packagePVO);
        }
        //加项产品列表
        JSONArray attachProduct = new JSONArray();
        //加项产品
        if (ServiceOrderData.selectedAddItems != null && ServiceOrderData.selectedAddItems.size() > 0) {
            for (int i = 0; i < ServiceOrderData.selectedAddItems.size(); i++) {
                JSONObject attachP1 = new JSONObject();
                attachP1.put("packageId", ServiceOrderData.selectedAddItems.get(i).getPackageId());
                attachP1.put("productId", ServiceOrderData.selectedAddItems.get(i).getProductId());
                //costProductCount购买加项产品数量
                attachP1.put("costProductCount", "1");
                //costCount本次使用数量
                attachP1.put("costCount", ServiceOrderData.selectedAddItems.get(i).getCostCount());
                attachProduct.add(attachP1);
            }
        } else {
            JSONObject attachP1 = new JSONObject();
            attachProduct.add(attachP1);
        }
        ecsVouchAccounts(packageProduct, attachProduct);
    }


    /**
     * 计算金额接口
     *
     * @param packageProduct
     * @param attachProduct
     */
    private void ecsVouchAccounts(JSONArray packageProduct, JSONArray attachProduct) {
        String bizId = "000";
        String serviceName = "ecsVouchAccountsBook";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("voucherId", ServiceOrderData.vb.getVoucherId());
        map.put("crmCustomerId", ServiceOrderData.cb.getCustomerId());
        map.put("groupPackId", ServiceOrderData.pb.getGroupPackId());
        map.put("packageProductArray", packageProduct.toString());
        map.put("orgId", ServiceOrderData.ob.getOrgId());
        map.put("attachProductArray", attachProduct.toString());
        map.put("isExamination", "Y");
        String para = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, para,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0);
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                Log.e("计算金额", result);
                                parseAccounts(result);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 解析json数据
     * 个人应付费用
     *
     * @param result
     */
    private void parseAccounts(String result) {
        JSONObject obj = JSON.parseObject(result);
        JSONObject data = obj.getJSONObject("data");
        String packagePriceSell = data.getString("packagePriceSell");//套餐销售价格
        String selfPay = data.getString("selfPay");//个人应付
        String selfPaidAmount = data.getString("selfPaidAmount");//个人已付
        String unPayAmount = data.getString("unPayAmount");//个人未付
        String companyPaidAmount = data.getString("companyPaidAmount");//企业付费
        String nciPaidAmount = data.getString("nciPaidAmount");//新华付费
        String freeAmount = data.getString("freeAmount");//免费

        ServiceOrderData.payable = Double.parseDouble(selfPay);
        ServiceOrderData.unpaid = Double.parseDouble(unPayAmount);
        ServiceOrderData.paid = Double.parseDouble(selfPaidAmount);
        ServiceOrderData.packagePriceSell = Double.parseDouble(packagePriceSell);
        ServiceOrderData.companyPaidAmount = companyPaidAmount;
        ServiceOrderData.nciPaidAmount = nciPaidAmount;
        ServiceOrderData.freeAmount = freeAmount;
        initView();
        //之前界面变更时，清空选中状态，默认选中在线支付
        online_pay_rb.setChecked(true);
        paymentMethod = 1;

        data.getJSONArray("underProductList");//承接机构承接的套餐中的原子项目列表（原子产品ID）
        parseUnderProduct(data.getJSONArray("underProductList"));
    }

    /**
     * 过滤掉承接机构没有承接的套餐中的原子项目
     *
     * @param arr
     */
    private void parseUnderProduct(JSONArray arr) {
        List<String> productList = JSON.parseArray(arr.toString(), String.class);
        List<PackageitemBean> packageitems = ServiceOrderData.packageitems;
        if (productList.size() == packageitems.size()) {//承接机构承接套餐中的全部原子项目
            ServiceOrderData.underProductList = packageitems;
        } else if (productList.size() < packageitems.size()) {//承接机构承接套餐中的部分原子项目
            ServiceOrderData.underProductList = new ArrayList<PackageitemBean>();
            for (int i = 0; i < productList.size(); i++) {
                for (int j = 0; j < packageitems.size(); j++) {
                    if (packageitems.get(j).getProductId().compareTo(productList.get(i)) == 0) {
                        ServiceOrderData.underProductList.add(packageitems.get(j));
                        break;
                    }
                }
            }
        }
    }

}
