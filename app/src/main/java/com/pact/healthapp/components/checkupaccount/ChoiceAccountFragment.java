package com.pact.healthapp.components.checkupaccount;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.CheckupUserBean;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个证卡对应对个客人
 * 选择一个客人
 * Created by wangdong on 2016/1/25.
 */
public class ChoiceAccountFragment extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    @ViewInject(R.id.appointment_choiceuser_items)
    private LinearLayout appointment_choiceuser_items;//显示证卡类型和号码的view
    @ViewInject(R.id.appointment_choiceuser_lv)
    private ListView appointment_choiceuser_lv;//体检账户列表

    private ArrayList<HashMap<String, Object>> ui_items;//头部固定信息的view
    private ArrayList<CheckupUserBean> userlist;
    private ChoiceAccountAdapter4ListView adapter;

    @ViewInject(R.id.add_checkup_account_btn)
    private Button add_checkup_account_btn;//新增
    private CommonDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_choiceuser_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("确认帐户");
        userlist = (ArrayList<CheckupUserBean>) AccountData.users;
        addViews();//添加头部view(证卡类型和号码的信息)
        adapter = new ChoiceAccountAdapter4ListView(context, userlist);
        appointment_choiceuser_lv.setAdapter(adapter);

        add_checkup_account_btn.setVisibility(View.VISIBLE);
        add_checkup_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChekupAccount();
            }
        });

        ((CheckupAccountActivity) getActivity()).btn_left.setOnClickListener(new View.OnClickListener() {
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

    /**
     * 添加头部view(证卡类型和号码的信息)
     */
    private void addViews() {
        ui_items = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("left", "证件类型");
        map.put("right", AccountData.certTypeName);
        map.put("bottomLine", true);
        map.put("arrow", false);
        ui_items.add(map);
        map = new HashMap<String, Object>();
        map.put("left", "证件号码");
        map.put("right", AccountData.certNumber);
        map.put("bottomLine", false);
        map.put("arrow", false);
        ui_items.add(map);
        for (int i = 0; i < ui_items.size(); i++) {
            View infoitem = LayoutInflater.from(context).inflate(R.layout.appointment_choiceitem, null);
            TextView tvleft = (TextView) infoitem.findViewById(R.id.appointment_choiceitem_tvleft);
            TextView tvright = (TextView) infoitem.findViewById(R.id.appointment_choiceitem_tvright);
            ImageView arrow = (ImageView) infoitem.findViewById(R.id.appointment_choiceitem_arrow);
            View bottomline = (View) infoitem.findViewById(R.id.appointment_choiceitem_bottomline);
            tvleft.setText(ui_items.get(i).get("left").toString());
            tvright.setText(ui_items.get(i).get("right").toString());
            if ((Boolean) ui_items.get(i).get("arrow")) {
                arrow.setVisibility(View.VISIBLE);
            } else {
                arrow.setVisibility(View.GONE);
            }
            if ((Boolean) ui_items.get(i).get("bottomLine")) {
                bottomline.setVisibility(View.VISIBLE);
            } else {
                bottomline.setVisibility(View.GONE);
            }
            appointment_choiceuser_items.addView(infoitem);
        }
    }

    /**
     * 保存我的体检账户
     */
    private void saveChekupAccount() {
        if (AccountData.cb == null) {
            CommonToast.makeText(context, "请选择客人！");
            return;
        }
        String bizId = "000";
        String serviceName = "saveChekupAccount";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("accountName", AccountData.cb.getCustomerName());
        map.put("cardType", AccountData.cb.getCertTypeId());
        map.put("cardTypeName", AccountData.cb.getCertTypeName());
        map.put("cardNo", AccountData.cb.getCertNumber());
        map.put("crmCustomerId", AccountData.cb.getCustomerId());
        map.put("updateDate", System.currentTimeMillis());  //long类型
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
//                                CommonToast.makeText(context, "添加成功");
                                getFragmentManager().popBackStack("MyCheckupAccountFragment", 0);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AccountData.cb = null;
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
