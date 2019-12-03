package com.pact.healthapp.components.healthreport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.set.SetActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.imageshow.ImageShowActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressLint("ValidFragment")
public class HealthReportListFragment extends BaseFragment implements
        OnItemClickListener, HealthCallBack {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    @ViewInject(R.id.physical_examination_listview)
    private ListView listView;
    private List<MyReportsBean> list = new ArrayList<MyReportsBean>();
    private HealthReportAdapter4Listview adapter;

    private List<MyReportsPgBean> pgList = new ArrayList<MyReportsPgBean>();
    private HealthReportPgAdapter4Listview pgAdapter;

    private String TYPE;// 0：体检报告 1：健康报告

    private CommonDialog dialog;

    public HealthReportListFragment() {

    }

    public HealthReportListFragment(String TYPE) {
        // TODO Auto-generated constructor stub
        this.TYPE = TYPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.health_report_layout, null);
        ViewUtils.inject(this, view);

        if (TYPE.equals("0")) { // 体检报告
            adapter = new HealthReportAdapter4Listview(context, list, this);
            listView.setAdapter(adapter);
        } else if (TYPE.equals("1")) { // 健康报告
            pgAdapter = new HealthReportPgAdapter4Listview(context, pgList,
                    this);
            listView.setAdapter(pgAdapter);
        }
        // 先查看账户信息中有没有身份证号
        isHaveIDCard();
        listView.setOnItemClickListener(this);
        return view;
    }

    /**
     * @author zhangyl
     */
    private void isHaveIDCard() {
        // TODO Auto-generated method stub
        if (!sp.getLoginInfo(context, "idCard").equals("")) {
            // 获取体检报告
            getReportListData();
        } else {
            dialog = new CommonDialog(context, 2, "填写", "否",
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO 去填写身份证号
                            Intent intent = new Intent(context,
                                    SetActivity.class);
                            intent.putExtra("TYPE", 1);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            }, null, "账户信息中没有“身份证”信息\n• 填写“身份证”信息\n• 否：退出查看报告");
            dialog.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        getActivity().finish();
        if (TYPE.equals("0")) { // 体检报告
            Intent intent = new Intent(context, ImageShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("healthCheckNumber", list.get(position)
                    .getHealthCheckNumber());
            bundle.putString("date", list.get(position).getDateregister());
            bundle.putString("reportname", list.get(position)
                    .getHealthCenterName());
            bundle.putInt("TYPE", 3);
            bundle.putBoolean("isReLoad", false);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (TYPE.equals("1")) { // 健康报告
            Intent intent = new Intent(context, ImageShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fileUrl", pgList.get(position).getFileUrl());
            bundle.putString("date", pgList.get(position).getDate());
            bundle.putString("reportname", pgList.get(position)
                    .getHealthCenterName());
            bundle.putInt("TYPE", 3);
            bundle.putBoolean("isReLoad", false);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 获取体检报告列表数据
     */
    private void getReportListData() {
        // TODO Auto-generated method stub
        String bizId = "000";
        String serviceName = "viewHealthReportList";
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", null);
        map.put("pagePg", null);
        map.put("cardId", sp.getLoginInfo(context, "idCard"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        String servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        super.onSuccess(arg0);
                        String result = "";
                        result = Des3.decode(arg0.result.toString());
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                if (TYPE.equals("0")) { // 体检报告
                                    JSONArray array = obj
                                            .getJSONArray("myreports");
                                    list.clear();
                                    list.addAll(JSON.parseArray(
                                            array.toString(),
                                            MyReportsBean.class));
                                    adapter.notifyDataSetChanged();
                                    handler.sendEmptyMessage(0);
                                } else if (TYPE.equals("1")) { // 健康报告
                                    JSONArray array = obj
                                            .getJSONArray("myreportsPg");
                                    pgList.clear();
                                    pgList.addAll(JSON.parseArray(
                                            array.toString(),
                                            MyReportsPgBean.class));
                                    pgAdapter.notifyDataSetChanged();
                                    handler.sendEmptyMessage(1);
                                }

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 没有体检报告时弹框
                    if (list.isEmpty()) {
                        dialog = new CommonDialog(context, 1, "确定", null,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().finish();
                                    }
                                }, null, null, "您目前没有体检报告可供查看");
                        dialog.show();
                    }
                    break;
                case 1: // 没有健康报告时弹框
                    if (pgList.isEmpty()) {
                        dialog = new CommonDialog(context, 1, "确定", null,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().finish();
                                    }
                                }, null, null, "您目前没有评估报告可供查看");
                        dialog.show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    boolean resume = true;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (resume) { // 第一次不执行
            resume = false;
        } else {
            isHaveIDCard();
        }
    }

    @Override
    public void callBack(int position, int type, boolean isReLoad) {
        // TODO Auto-generated method stub
        getActivity().finish();
        if (type == 0) {// 体检报告
            Intent intent = new Intent(context, ImageShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("healthCheckNumber", list.get(position)
                    .getHealthCheckNumber());
            bundle.putString("date", list.get(position).getDateregister());
            bundle.putString("reportname", list.get(position)
                    .getHealthCenterName());
            bundle.putInt("TYPE", 3);
            bundle.putBoolean("isReLoad", true);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (type == 1) {// 健康报告
            Intent intent = new Intent(context, ImageShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fileUrl", pgList.get(position).getFileUrl());
            bundle.putString("date", pgList.get(position).getDate());
            bundle.putString("reportname", pgList.get(position)
                    .getHealthCenterName());
            bundle.putInt("TYPE", 3);
            bundle.putBoolean("isReLoad", true);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}
