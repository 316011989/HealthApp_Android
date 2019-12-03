package com.pact.healthapp.components.checkupaccount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.AppointmentActivity;
import com.pact.healthapp.components.appointment.CheckupUserBean;
import com.pact.healthapp.components.appointment.ChildOfItemClickCallBack;
import com.pact.healthapp.components.appointment.ServiceOrderData;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 亲情账户
 * Created by wangdong on 2016/1/6.
 */
@SuppressLint("ValidFragment")
public class MyCheckupAccountFragment extends BaseFragment implements View.OnClickListener, ChildOfItemClickCallBack,
        CheckupAccountAdapter4ListView.AccountCallBack, CheckupAccountAdapter4ListView.GroupCallBack {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @ViewInject(R.id.mycheckupaccount_listview)
    private ExpandableListView listView;
    private CheckupAccountAdapter4ListView adapter;
    private List<CheckupAccountBean> list = new ArrayList<CheckupAccountBean>();

    @ViewInject(R.id.add_account_btn)
    private Button add_account_btn;//新增亲情账户

    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.mycheckupaccount_layout, null);
        ViewUtils.inject(this, view);
        add_account_btn.setOnClickListener(this);

        ((CheckupAccountActivity) getActivity()).btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(backListener);
        return view;
    }

    /**
     * 查询我的亲情账户
     */
    private void showChekupAccount() {
        String bizId = "000";
        String serviceName = "showChekupAccount";
        Map<String, String> map = new HashMap<String, String>();
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
                                parseJson(result);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void parseJson(String result) {
        JSONObject object = JSONObject.parseObject(result);
        JSONArray array = object.getJSONArray("datas");
        if (array != null && array.size() > 0) {
            list = JSON.parseArray(array.toString(), CheckupAccountBean.class);
            adapter = new CheckupAccountAdapter4ListView(context, list, this, this, this);
            listView.setAdapter(adapter);
            //全部展开
            int groupCount = list.size();
            for (int i = 0; i < groupCount; i++) {
                listView.expandGroup(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_account_btn://新增账户
                mFragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                mFragmentTransaction
                        .replace(R.id.login_fl_continer,
                                new AddAccountFragment())
                        .addToBackStack("AddAccountFragment").commit();
                break;
        }
    }

    /**
     * 删除亲情账户
     */
    private void deleteChekupAccount(final int position) {
        String bizId = "000";
        String serviceName = "deleteChekupAccount";
        Map<String, String> map = new HashMap<String, String>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("checkupAccountId", list.get(position).getCheckupAccountId());//我的体检账户id
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
                                list.remove(position);
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改亲情账户名
     */
    private void saveChekupAccount(String checkupAccountId, final String accountName, final int position) {
        String bizId = "000";
        String serviceName = "saveChekupAccount";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("checkupAccountId", checkupAccountId);//我的体检账户id
        map.put("accountName", accountName.trim());
        map.put("updateDate", System.currentTimeMillis());
        String para = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, para,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
//                        super.onSuccess(arg0);
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                list.get(position).setAccountName(accountName);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart() {
//                        super.onStart();
                    }
                });

    }

    /**
     * 选择凭证预约
     */
    private void voucherAppointment(final int groupPosition, final int childPosition) {
        /**
         * 根据凭证号
         * 查询客人
         */
        String serviceName = "queryCustomerList2Book";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        if (list.get(groupPosition).getData().get(childPosition).getVoucherNo() != null
                && list.get(groupPosition).getData().get(childPosition).getVoucherNo().length() > 0) {
            jo.put("voucherNo", list.get(groupPosition).getData().get(childPosition).getVoucherNo());
        } else {
            jo.put("voucherId", list.get(groupPosition).getData().get(childPosition).getVoucherId());
        }
        jo.put("checkVoucher", false);
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
                super.onSuccess(arg0);
                try {
                    String result = Des3.decode(arg0.result.toString());
                    JSONObject obj = JSON.parseObject(result);
                    parseJsonVoucher(result, list.get(groupPosition).getData().get(childPosition).getVoucherNo());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }

    private void parseJsonVoucher(String result, String voucherNo) {
        /**
         * 解析json数据,获取体检人数据成功之后再进入选择体检人界面
         *
         * @param result 解密后的json数据
         */
        JSONObject joall = JSONObject.parseObject(result);
        if (joall.getString("resultCode") != null && joall.getString("resultCode").equals("0")) {
            if (joall.getString("data").length() == 2) {//没有查询到客人,新建一个客人并编辑个人信息
                ServiceOrderData.setCb(null);
                ServiceOrderData.certTypeId = "";
                ServiceOrderData.certNumber = voucherNo;
                ServiceOrderData.userinfoEditable = 1;
                ServiceOrderData.fromWhere = "";
                Intent intent = new Intent(context, AppointmentActivity.class);
                intent.putExtra("operation", "voucher");
                startActivity(intent);
            } else {
                List<CheckupUserBean> list = JSON.parseArray(joall.getString("data"), CheckupUserBean.class);
                if (list != null && list.size() == 1) {
                    if (list.get(0).getSexId().equals("U")
                            && list.get(0).getCustomerName().equals("")
                            && list.get(0).getCertTypeId().equals("3")
                            && !list.get(0).getCertNumber().equals("")) {//查询到一个客人,但是客人信息是空,需要编辑
                        // 启动确认体检人信息fragment
                        ServiceOrderData.setCb(list.get(0));
                        ServiceOrderData.certTypeId = "";
                        ServiceOrderData.certNumber = voucherNo;
                        ServiceOrderData.fromWhere = "";
                        ServiceOrderData.userinfoEditable = 2;//可编辑部分个人信息
                        ServiceOrderData.cb.setEdit(false);
                        //初始化部分客人信息
                        ServiceOrderData.cb.setMarryId("1");
                        ServiceOrderData.cb.setMarryName("未婚");

                        Intent intent = new Intent(context, AppointmentActivity.class);
                        intent.putExtra("operation", "voucher");
                        startActivity(intent);
                    } else {//查询到一个客人并且已有客人信息,不可编辑
                        // 启动确认体检人信息fragment
                        ServiceOrderData.setCb(list.get(0));
                        ServiceOrderData.certTypeId = list.get(0).getCertTypeId();
                        ServiceOrderData.certNumber = list.get(0).getCertNumber();
                        ServiceOrderData.fromWhere = "";
                        Intent intent = new Intent(context, AppointmentActivity.class);
                        intent.putExtra("operation", "voucher");
                        startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("亲情账户");
        showChekupAccount();
        adapter = new CheckupAccountAdapter4ListView(context, list, this, this, this);
        listView.setAdapter(adapter);
        //全部展开
        int groupCount = list.size();
        for (int i = 0; i < groupCount; i++) {
            listView.expandGroup(i);
        }
        //去掉系统默认的箭头图标
        listView.setGroupIndicator(null);
        //账户名清空，迁移焦点，显示清空前的账户名
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void mCallback(final int position, View v) {
        switch (v.getId()) {
            case R.id.account_name://修改账户名
                Log.e("accountName", AccountData.accountName);
                if (AccountData.accountName.replaceAll(" ", "").equals("")) {
                    adapter.notifyDataSetChanged();
                    AccountData.accountName = null;
                } else {
                    if (!list.get(position).getAccountName().equals(AccountData.accountName)) {
                        saveChekupAccount(list.get(position).getCheckupAccountId(), AccountData.accountName, position);
                    }
                }
                break;
        }
    }

    @Override
    public void callBack(final int groupPosition, final int childPosition) {
        dialog = new CommonDialog(context, 2, "预约", "否", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                voucherAppointment(groupPosition, childPosition);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        }, null, "选择该凭证预约?");
        dialog.show();
    }

    @Override
    public void gCallBack(final int position, boolean flag) {
        if (flag) {//长按删除亲情账户
            dialog = new CommonDialog(context, 2, "删除", "否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    deleteChekupAccount(position);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            }, null, "删除选择的亲情账户?");
            dialog.show();
        } else {//点击展开或者折叠子项
            if (!listView.isGroupExpanded(position)) {
                listView.expandGroup(position);
            } else {
                listView.collapseGroup(position);
            }
        }
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
