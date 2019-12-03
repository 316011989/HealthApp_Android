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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;

import java.util.ArrayList;

/**
 * 套餐列表
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class PackageListFragment extends BaseFragment implements ChildOfItemClickCallBack {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    @ViewInject(R.id.appointment_checkupset_listview)
    private ListView appointment_checkupset_listview;
    private PackageListAdapter adapter;
    private ArrayList<PackageBean> packagelist = new ArrayList<PackageBean>();
    public PackageBean packageBean;//当前页面选中的套餐,下一步时赋值给serviceorderdata的静态变量

    @ViewInject(R.id.appointment_makesure_next)
    private Button appointment_makesure_next;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_checkupset_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("套餐");
        if (ServiceOrderData.packages == null || ServiceOrderData.packages.size() == 0) {
            getPackageList();
        } else {
            packagelist = (ArrayList<PackageBean>) ServiceOrderData.packages;
            adapter = new PackageListAdapter(context, packagelist, this);
            appointment_checkupset_listview.setAdapter(adapter);
        }
        if (ServiceOrderData.pb == null) {
            appointment_makesure_next.setEnabled(false);
            appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
            if (packageBean != null) {
                appointment_makesure_next.setEnabled(true);
                appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                adapter.setPackageBean(packageBean);
                adapter.notifyDataSetChanged();
            }
        } else {
            packageBean = ServiceOrderData.pb;
            appointment_makesure_next.setEnabled(true);
            appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            adapter.setPackageBean(packageBean);
            adapter.notifyDataSetChanged();
        }
        appointment_makesure_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packageBean != null) {
                    if (!packageBean.equals(ServiceOrderData.pb)) {
                        ServiceOrderData.setPb(packageBean);
                    }
                    if (!packageBean.equals(ServiceOrderData.pbdetail))
                        getPackageitem(packageBean, false);
                    else {
                        ((AppointmentActivity) getActivity()).toStep4();
                        packageBean = null;
                    }
                } else if (ServiceOrderData.pb != null) {
                    ((AppointmentActivity) getActivity()).toStep4();
                }
            }
        });
        appointment_checkupset_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!packagelist.get(position).equals(ServiceOrderData.pbdetail)) {
                    getPackageitem(packagelist.get(position), true);
                } else {
                    ((AppointmentActivity) getActivity()).from = "package";
                    ServiceOrderData.isPriceSell = false;
                    ((AppointmentActivity) getActivity()).toStepm4();
                }

            }
        });
        return view;
    }

    /**
     * 查询套餐列表
     */
    private void getPackageList() {
        String serviceName = "getPackageListBooks";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("voucherId", ServiceOrderData.vb.getVoucherId());
        jo.put("serviceId", ServiceOrderData.vb.getServiceData().get(0).getServiceId());
        jo.put("packageCategoryId", ServiceOrderData.vb.getServiceData().get(0).getPackageCategoryId());
        jo.put("sexId", ServiceOrderData.cb.getSexId());
        jo.put("marryId", ServiceOrderData.cb.getMarryId());
        jo.put("isExamination", "Y");
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
                super.onSuccess(arg0);
                String result = "";
                try {
                    result = Des3.decode(arg0.result.toString());
                    JSONObject obj = JSON.parseObject(result);
                    if (obj.get("resultCode") != null
                            && obj.get("resultCode").toString()
                            .equals("0")) {// 查询凭证列表成功
                        Log.e("查询套餐列表", result);
                        parseJson(result);
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
     * 套餐列表数据
     *
     * @param result 解密后的json数据
     */
    private void parseJson(String result) {
        JSONObject joall = JSONObject.parseObject(result);
        JSONArray japackages = joall.getJSONArray("data");
        if (japackages != null && japackages.size() > 0) {
            packagelist = (ArrayList<PackageBean>) JSON.parseArray(japackages.toJSONString(), PackageBean.class);
            ServiceOrderData.packages = packagelist;
            adapter = new PackageListAdapter(context, packagelist, this);
            appointment_checkupset_listview.setAdapter(adapter);
        }
    }


    /**
     * 查询套餐原子项
     */
    private void getPackageitem(final PackageBean pb, final boolean todetail) {
        JSONObject jo = new JSONObject();
        String serviceName = "getProductListBook";
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("voucherId", ServiceOrderData.vb.getVoucherId());
        jo.put("groupPackId", pb.getGroupPackId());
        jo.put("productCategoryId", "");
        jo.put("isExamination", "Y");
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
                super.onSuccess(arg0);
                try {
                    String result = Des3.decode(arg0.result.toString());
                    parseJson1(result, pb, todetail);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析json数据
     *
     * @param result 解密后的json数据
     */
    private void parseJson1(String result, PackageBean pb, boolean todetail) {
        JSONObject joall = JSONObject.parseObject(result);
        if (joall.getString("resultCode") != null && joall.getString("resultCode").equals("0")) {
            JSONArray jaitems = joall.getJSONArray("data");
            if (jaitems != null && jaitems.size() > 0) {
                ServiceOrderData.pbdetail = pb;
                ServiceOrderData.packageitems = JSON.parseArray(jaitems.toJSONString(), PackageitemBean.class);
                if (todetail) {
                    ((AppointmentActivity) getActivity()).from = "package";
                    ServiceOrderData.isPriceSell = false;
                    ((AppointmentActivity) getActivity()).toStepm4();
                } else {
                    ((AppointmentActivity) getActivity()).toStep4();
                    packageBean = null;
                }
            } else {
                if (!todetail) {
                    ((AppointmentActivity) getActivity()).toStep4();
                    packageBean = null;
                }
            }
        }
    }

    @Override
    public void mCallback(int position, View v) {
        switch (v.getId()) {
            case R.id.checkupset_item_btn1:
                appointment_makesure_next.setEnabled(true);
                packageBean = packagelist.get(position);
                adapter.setPackageBean(packageBean);
                adapter.notifyDataSetChanged();
                appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                break;
        }
    }
}
