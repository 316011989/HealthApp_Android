package com.pact.healthapp.components.serviceorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.AppointmentActivity;
import com.pact.healthapp.components.appointment.ChildOfItemClickCallBack;
import com.pact.healthapp.components.appointment.ServiceOrderData;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的服务单列表
 * Created by wangdong on 2016/1/11.
 */
@SuppressLint("ValidFragment")
public class ServiceOrderListFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, ChildOfItemClickCallBack {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @ViewInject(R.id.serviceorder_listview)
    private ListView listView;
    private List<ServiceOrderListBean> showList = new ArrayList<ServiceOrderListBean>();
    private ServiceOrderAdapter adapter;

    private List<ServiceOrderListBean> list = new ArrayList<ServiceOrderListBean>();
    private List<ServiceOrderListBean> appointableList = new ArrayList<ServiceOrderListBean>();
    private List<ServiceOrderListBean> appointedList = new ArrayList<ServiceOrderListBean>();
    private List<ServiceOrderListBean> completedList = new ArrayList<ServiceOrderListBean>();
    private List<ServiceOrderListBean> allList = new ArrayList<ServiceOrderListBean>();

    @ViewInject(R.id.serviceorder_appointable)
    private RadioButton appointable;//未预约
    @ViewInject(R.id.serviceorder_appointed)
    private RadioButton appointed;//已预约
    @ViewInject(R.id.serviceorder_completed)
    private RadioButton completed;//已完成
    @ViewInject(R.id.serviceorder_all)
    private RadioButton all;//全部

    @ViewInject(R.id.img_1)
    private ImageView img_1;
    @ViewInject(R.id.img_2)
    private ImageView img_2;
    @ViewInject(R.id.img_3)
    private ImageView img_3;
    @ViewInject(R.id.img_4)
    private ImageView img_4;

    private CommonDialog dialog;
    private String serviceOrderId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serviceorder_layout, null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("预约服务单");
        if (OrderData.getServiceOrderBeanList() == null || OrderData.getServiceOrderBeanList().size() == 0) {
            initeView();
            serviceOrderList();
        } else {
            list = OrderData.getServiceOrderBeanList();
            initeView();
            sortList(list);
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(backListener);
        return view;
    }

    private void initeView() {
        if (OrderData.operation != null && (
                OrderData.operation.equals("changedate") || OrderData.operation.equals("order")
                        || OrderData.operation.equals("pay") || OrderData.operation.equals("cancelOrder")
        )) {//改期或预约后 默认选中已预约
            appointed.setChecked(true);
            img_1.setVisibility(View.INVISIBLE);
            img_2.setVisibility(View.VISIBLE);
        } else {//从我的新华界面进入服务单 默认选中未预约
            appointable.setChecked(true);
            img_1.setVisibility(View.VISIBLE);
        }
        OrderData.clearData();
        adapter = new ServiceOrderAdapter(context, showList, this);
        adapter.setAllFlag(false);
        listView.setAdapter(adapter);
        appointable.setOnCheckedChangeListener(this);
        appointed.setOnCheckedChangeListener(this);
        completed.setOnCheckedChangeListener(this);
        all.setOnCheckedChangeListener(this);
        ((ServiceOrderActivity) getActivity()).btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.serviceorder_appointable://未预约
                if (isChecked) {
                    listView.smoothScrollToPosition(0);
                    showList.clear();
                    showList.addAll(appointableList);
                    adapter.setAllFlag(false);
                    adapter.notifyDataSetChanged();
                    img_1.setVisibility(View.VISIBLE);
                } else {
                    img_1.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.serviceorder_appointed://已预约
                if (isChecked) {
                    listView.smoothScrollToPosition(0);
                    showList.clear();
                    showList.addAll(appointedList);
                    adapter.setAllFlag(false);
                    adapter.notifyDataSetChanged();
                    img_2.setVisibility(View.VISIBLE);
                } else {
                    img_2.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.serviceorder_completed://已完成
                if (isChecked) {
                    listView.smoothScrollToPosition(0);
                    showList.clear();
                    showList.addAll(completedList);
                    adapter.setAllFlag(false);
                    adapter.notifyDataSetChanged();
                    img_3.setVisibility(View.VISIBLE);
                } else {
                    img_3.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.serviceorder_all://全部
                if (isChecked) {
                    listView.smoothScrollToPosition(0);
                    showList.clear();
                    showList.addAll(allList);
                    adapter.setAllFlag(true);
                    adapter.notifyDataSetChanged();
                    img_4.setVisibility(View.VISIBLE);
                } else {
                    img_4.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void mCallback(final int position, View v) {
        switch (v.getId()) {
            case R.id.serviceorder_detail://服务单详情
                if (appointable.isChecked()) {
                    serviceOrderId = appointableList.get(position).getServiceOrderId();
                } else if (appointed.isChecked()) {
                    serviceOrderId = appointedList.get(position).getServiceOrderId();
                } else if (completed.isChecked()) {
                    serviceOrderId = completedList.get(position).getServiceOrderId();
                } else if (all.isChecked()) {
                    serviceOrderId = allList.get(position).getServiceOrderId();
                }
//                serviceOrderDetail(serviceOrderId, "detail");
                queryServiceOrderStatus(serviceOrderId, position, "detail");
                break;
            case R.id.serviceorder_appointment://预约
                if (appointable.isChecked()) {
                    serviceOrderId = appointableList.get(position).getServiceOrderId();
                } else if (all.isChecked()) {
                    serviceOrderId = allList.get(position).getServiceOrderId();
                }
                queryServiceOrderStatus(serviceOrderId, position, "order");
                break;
            case R.id.serviceorder_cancel_appointment://取消预约
                if (appointed.isChecked()) {
                    serviceOrderId = appointedList.get(position).getServiceOrderId();
                } else if (all.isChecked()) {
                    serviceOrderId = allList.get(position).getServiceOrderId();
                }
                queryServiceOrderStatus(serviceOrderId, position, "cancel");
                break;
            case R.id.serviceorder_pay://去支付
                if (appointable.isChecked()) {
                    serviceOrderId = appointableList.get(position).getServiceOrderId();
                } else if (appointed.isChecked()) {
                    serviceOrderId = appointedList.get(position).getServiceOrderId();
                } else if (completed.isChecked()) {
                    serviceOrderId = completedList.get(position).getServiceOrderId();
                } else if (all.isChecked()) {
                    serviceOrderId = allList.get(position).getServiceOrderId();
                }
                queryServiceOrderStatus(serviceOrderId, position, "pay");
                break;
            case R.id.serviceorder_changedate://改期
                if (appointed.isChecked()) {
                    serviceOrderId = appointedList.get(position).getServiceOrderId();
                } else if (all.isChecked()) {
                    serviceOrderId = allList.get(position).getServiceOrderId();
                }
                queryServiceOrderStatus(serviceOrderId, position, "changedate");
                break;
        }
    }

    //获取服务单列表数据
    private void serviceOrderList() {
        String bizId = "000";
        String serviceName = "serviceOrderListBook";
        Map<String, String> map = new HashMap<String, String>();
        map.put("crmCustomerId", "");
        map.put("voucherNo", "");
        map.put("status", "'1','2','3'");//1  未预约、2 已预约、3  已到检、4 已取消
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
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
                                parseJson(result);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    //解析服务单数据
    private void parseJson(String result) {
        JSONObject obj = JSON.parseObject(result);
        JSONArray array = obj.getJSONArray("data");
        list = JSON.parseArray(array.toString(), ServiceOrderListBean.class);
//        sortName(list);
        OrderData.setServiceOrderBeanList(list);
        sortList(list);
    }

    //把服务单分成 未预约 已预约 已到检 全部不包括已取消
    private void sortList(List<ServiceOrderListBean> list) {
        appointableList.clear();
        appointedList.clear();
        completedList.clear();
        allList.clear();
        String serviceOrderStatusId;
        for (int i = 0; i < list.size(); i++) {
            serviceOrderStatusId = list.get(i).getServiceOrderStatusId();
            if (serviceOrderStatusId.equals("1")) {//未预约
                appointableList.add(list.get(i));
            } else if (serviceOrderStatusId.equals("2")) {//已预约
                appointedList.add(list.get(i));
            } else if (serviceOrderStatusId.equals("3")) {//已到检
                completedList.add(list.get(i));
            }

            if (!serviceOrderStatusId.equals("4")) {//全部不包括已取消
                allList.add(list.get(i));
            }
        }

        showList.clear();
        if (appointable.isChecked()) {
            showList.addAll(appointableList);
        } else if (appointed.isChecked()) {
            showList.addAll(appointedList);
        } else if (completed.isChecked()) {
            showList.addAll(completedList);
        } else if (all.isChecked()) {
            showList.addAll(allList);
        }

        adapter.notifyDataSetChanged();
    }


    /**
     * 获取服务单详情数据
     *
     * @param serviceOrderId
     * @param flag           true:进入详情 false:改期或者预约
     */
    private void serviceOrderDetail(String serviceOrderId, final String flag) {
        String bizId = "000";
        String serviceName = "serviceOrderDetailBook";
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
                        super.onSuccess(arg0);
                        try {
                            String result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                parseJsonDetail(result, flag);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    List<ServiceOrderDetailBean> detailList = new ArrayList<ServiceOrderDetailBean>();
    ServiceOrderDetailBean detail;

    private void parseJsonDetail(String result, String flag) {
        JSONObject obj = JSON.parseObject(result);
        JSONArray array = obj.getJSONArray("data");
        detailList = JSON.parseArray(array.toString(), ServiceOrderDetailBean.class);
        detail = detailList.get(0);
        if (flag.equals("detail")) {//进入详情页面
            OrderData.setDetailBean(detail);
            ServiceOrderDetailFragment detailFragment = new ServiceOrderDetailFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fl_continer, detailFragment)
                    .addToBackStack("ServiceOrderDetailFragment").commit();
        } else if (flag.equals("order")) {//预约
            ServiceOrderData.setOrderDetail(detail);
            Intent intent = new Intent(context, AppointmentActivity.class);
            intent.putExtra("operation", "order");
            startActivity(intent);
        } else if (flag.equals("changedate")) {//改期
            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date;
            long expirationDate = 0l;
            try {
                date = sdf.parse(detail.getExpirationDate());
                expirationDate = date.getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (expirationDate < System.currentTimeMillis()) {//已过期
                dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderData.operation = "changedateFailure";
                        serviceOrderList();
                    }
                }, null, null, "该服务单已过期");
                dialog.show();
            } else {
                ServiceOrderData.setOrderDetail(detail);
                Intent intent = new Intent(context, AppointmentActivity.class);
                intent.putExtra("operation", "changedate");
                startActivity(intent);
            }
        }

    }


    /**
     * 对服务单的体检人姓名进行排序
     * 相同姓名的服务单放在一起
     *
     * @param mList
     */
    private void sortName(List<ServiceOrderListBean> mList) {
        if (mList.size() > 2) {
            for (int i = 0; i < mList.size(); i++) {
                for (int j = 0; j < mList.size() - i - 1; j++) {
                    if (mList.get(j).getCustomerName().compareTo(
                            mList.get(j + 1).getCustomerName()) > 0) {
                        ServiceOrderListBean temp = mList.get(i);
                        mList.set(i, mList.get(j));
                        mList.set(j, temp);
                    }
                }
            }
        }
    }

    /**
     * 查询服务单状态
     *
     * @param serviceOrderId
     * @param position
     * @param operation
     */
    private void queryServiceOrderStatus(String serviceOrderId, final int position, final String operation) {
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
                                toOperation(operation, position);
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
                                    .equals("1127")) {//服务单已过期 已取消
                                String resultMsg = obj.get("resultMsg").toString();
                                if (operation.equals("detail") && resultMsg.contains("已过期")) {//已过期服务单可以进详情
                                    toOperation(operation, position);
                                } else {//不能改期,预约,取消,支付
                                    dialog = new CommonDialog(context, 1, "确定", null,
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    serviceOrderList();
                                                }
                                            }, null, null, resultMsg);
                                    dialog.show();
                                }
                            } else if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString()
                                    .equals("1128")) {
                                if (operation.equals("pay") || operation.equals("detail")) {//已到检服务单可以支付或去详情
                                    toOperation(operation, position);
                                } else {//不能改期,预约,取消
                                    String resultMsg = obj.get("resultMsg").toString();
                                    dialog = new CommonDialog(context, 1, "确定", null,
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    serviceOrderList();
                                                }
                                            }, null, null, resultMsg);
                                    dialog.show();
                                }
                            } else if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString()
                                    .equals("1")) {//无法连接CRM服务器，提示服务器繁忙
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
    private void toOperation(String operation, final int position) {
        if (operation.equals("order")) {//预约
            serviceOrderDetail(serviceOrderId, "order");
        } else if (operation.equals("cancel")) {//取消预约
            dialog = new CommonDialog(context, 2, "取消预约", "否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appointed.isChecked()) {
                        cancelServiceOrder(appointedList, position);
                    } else if (all.isChecked()) {
                        cancelServiceOrder(allList, position);
                    }
                    dialog.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            }, null, "取消预约?");
            dialog.show();
        } else if (operation.equals("pay")) {//支付
            ServiceOrderPayFragment fragment = new ServiceOrderPayFragment();
            Bundle args = new Bundle();
            double fee = 0;
            if (appointable.isChecked()) {
                if (appointableList.get(position).getUnPayAmount() != null && !appointableList.get(position).getUnPayAmount().equals("")) {
                    fee = Double.parseDouble(appointableList.get(position).getUnPayAmount());
                }
                args.putString("payment_num", appointableList.get(position).getPaymentOrderNum());
                args.putString("pay_orderfee", fee + "");
            } else if (appointed.isChecked()) {
                if (appointedList.get(position).getUnPayAmount() != null && !appointedList.get(position).getUnPayAmount().equals("")) {
                    fee = Double.parseDouble(appointedList.get(position).getUnPayAmount());
                }
                args.putString("payment_num", appointedList.get(position).getPaymentOrderNum());
                args.putString("pay_orderfee", fee + "");
            } else if (completed.isChecked()) {
                //直接取出未付金额
                if (completedList.get(position).getUnPayAmount() != null && !completedList.get(position).getUnPayAmount().equals("")) {
                    fee = Double.parseDouble(completedList.get(position).getUnPayAmount());
                }
                args.putString("payment_num", completedList.get(position).getPaymentOrderNum());
                args.putString("pay_orderfee", fee + "");
            } else if (all.isChecked()) {
                //直接取出未付金额
                if (allList.get(position).getUnPayAmount() != null && !allList.get(position).getUnPayAmount().equals("")) {
                    fee = Double.parseDouble(allList.get(position).getUnPayAmount());
                }
                args.putString("payment_num", allList.get(position).getPaymentOrderNum());
                args.putString("pay_orderfee", fee + "");
            }
            fragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fl_continer, fragment)
                    .addToBackStack("ServiceOrderPayFragment").commit();
        } else if (operation.equals("changedate")) {//改期
            serviceOrderDetail(serviceOrderId, "changedate");
        } else if (operation.equals("detail")) {
            serviceOrderDetail(serviceOrderId, "detail");
        }
    }

    /**
     * 取消预约
     */
    private void cancelServiceOrder(final List<ServiceOrderListBean> sList, final int position) {
        String bizId = "000";
        String serviceName = "cancelServiceOrderBook";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("serviceOrderId", sList.get(position).getServiceOrderId());
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
                                    .equals("0")) {//取消成功
                                Log.e("取消预约", result);
                                //服务单是“已支付”状态，提示以下信息；
                                if (sList.get(position).getPaymentStatus().equals("2")) {
                                    dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            OrderData.operation = "cancelOrder";
                                            appointed.setChecked(true);
                                            serviceOrderList();
                                        }
                                    }, null, "预约取消成功", "服务单号：" + sList.get(position).getServiceOrderNo()
                                            + "\r\n体检人：" + sList.get(position).getCustomerName()
                                            + "\r\n体检机构：" + sList.get(position).getOrgName()
                                            + "\r\n\r\n如需退款，请联系客服。"
                                            + "\r\n电话：(010)85920097"
                                    );
                                    dialog.show();
                                    TextView textView = (TextView) dialog.findViewById(R.id.common_dialog_content);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.LEFT);
                                } else {
                                    OrderData.operation = "cancelOrder";
                                    appointed.setChecked(true);
                                    serviceOrderList();
                                    adapter.notifyDataSetChanged();
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
                                                dialog.dismiss();
                                                startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                            }
                                        }, null, null, "当前账户在其他设备登录，请重新登录。");
                                dialog.show();
                            } else if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString().equals("1")
                                    ) {//取消失败
                                CommonToast.makeText(context, "取消失败，请检查服务单的状态。");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("预约服务单");
        if (OrderData.operation != null && (
                OrderData.operation.equals("changedate") || OrderData.operation.equals("order") || OrderData.operation.equals("pay"))) {//改期或预约后 刷新界面
            serviceOrderList();
            initeView();
        } else if (OrderData.operation != null
                && (OrderData.operation.equals("orderError")
                || OrderData.operation.equals("cancelOrder"))) {//操作已过期、已取消的服务单提示后 或者取消服务单 刷新界面
            serviceOrderList();
        }
        OrderData.operation = null;
    }

    private View.OnKeyListener backListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                getActivity().finish();
                return true;
            } //后退
            return false;    //已处理
        }
    };

}
