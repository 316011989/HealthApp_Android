/**
 *
 */
package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.pact.healthapp.view.CommonDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 机构
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class ChoiceOrgFragment extends BaseFragment implements ChildOfItemClickCallBack {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    @ViewInject(R.id.organization_center_listview)
    private ListView listView;
    private ArrayList<OrgListBean> list = new ArrayList<OrgListBean>();
    private OrgListBean orgListBean;//当前页面已选的机构对象,点击下一步时赋值给serviceorderdata中的静态变量
    private OrgListAdapter4ListView adapter;

    @ViewInject(R.id.btn_next)
    private Button btn_next;
    private CommonDialog commonDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_choiceorg_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("机构");
        if (ServiceOrderData.orgs == null || ServiceOrderData.orgs.size() == 0) {
            getPackageOrg();
        } else {
            adapter = new OrgListAdapter4ListView(ServiceOrderData.orgs, context, this);
            listView.setAdapter(adapter);
        }

        if (ServiceOrderData.ob == null) {
            if (orgListBean != null) {
                adapter.setOrgListBean(orgListBean);
                adapter.notifyDataSetChanged();
                btn_next.setEnabled(true);
                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            } else {
                btn_next.setEnabled(false);
                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
            }
        } else {
            orgListBean = ServiceOrderData.ob;
            btn_next.setEnabled(true);
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            adapter.setOrgListBean(orgListBean);
            adapter.notifyDataSetChanged();
        }


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orgListBean != null) {
                    if (!orgListBean.equals(ServiceOrderData.ob))
                        ServiceOrderData.setOb(orgListBean);
                    ((AppointmentActivity) getActivity()).toStep5();
                    orgListBean = null;
                } else if (ServiceOrderData.ob != null) {
                    ((AppointmentActivity) getActivity()).toStep5();
                    orgListBean = null;
                }
            }
        });
        return view;
    }

    /**
     * 获取机构列表数据
     */
    private void getPackageOrg() {
        String bizId = "000";
        String serviceName = "getPackageOrgBook";
        Map<String, String> map = new HashMap<String, String>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("packageId", ServiceOrderData.pb.getPackageId());
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
                                    .equals("0")) {// 查询机构列表成功
                                Log.e("获取机构列表数据", result);
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
     *
     * @param result 解密后的json数据
     */
    private void parseJson(String result) {
        JSONObject obj = JSONObject.parseObject(result);
        JSONArray array = obj.getJSONArray("data");
        if (array != null && array.size() > 0) {
            list = (ArrayList<OrgListBean>) JSON.parseArray(array.toString(), OrgListBean.class);
            ServiceOrderData.orgs = list;
            adapter = new OrgListAdapter4ListView(list, context, this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void mCallback(final int position, View v) {
        switch (v.getId()) {
            //选择机构
            case R.id.organization_center:
            case R.id.organization_center_choice:
                btn_next.setEnabled(true);
                orgListBean = list.get(position);
                adapter.setOrgListBean(orgListBean);
                adapter.notifyDataSetChanged();
                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                break;
            //调用百度地图
            case R.id.organization_center_address:
                Intent intent = new Intent();
                intent.setClass(context, baidumapActivity.class);
                intent.putExtra("orgName", list.get(position).getOrgName());
                intent.putExtra("address", list.get(position).getAddress());
                intent.putExtra("openTime", list.get(position).getOpenTime());
                intent.putExtra("longitude", list.get(position).getLongitude());
                intent.putExtra("latitude", list.get(position).getLatitude());
                startActivity(intent);
                break;
            //拨打电话
            case R.id.organization_center_phone:
                commonDialog = new CommonDialog(context, 2, "是", "否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
//                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
//                                + list.get(position).getOrgTel()));
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:"
                                + list.get(position).getOrgTel());
                        intent.setData(data);
                        startActivity(intent);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                }, null, "是否拨打电话：" + list.get(position).getOrgTel());
                commonDialog.show();
                break;
        }
    }
}
