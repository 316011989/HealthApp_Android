package com.pact.healthapp.components.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 当一个证件号码对应多个体检账户时,启动这个页面提供用户选择账户哪一个是本人的要预约的
 * Created by zhangyl on 2015/12/29.
 */
public class ChoiceUserFragment extends BaseFragment {
    @ViewInject(R.id.appointment_choiceuser_items)
    private LinearLayout appointment_choiceuser_items;//显示证卡类型和号码的view
    @ViewInject(R.id.appointment_choiceuser_lv)
    private ListView appointment_choiceuser_lv;//体检账户列表

    private ArrayList<HashMap<String, Object>> ui_items;//头部固定信息的view
    private ArrayList<CheckupUserBean> userlist;
    private ChoiceUserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_choiceuser_layout,
                null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("选择帐户");
        userlist = (ArrayList<CheckupUserBean>) ServiceOrderData.users;
        addViews();//添加头部view(证卡类型和号码的信息)
        adapter = new ChoiceUserAdapter(context, userlist);
        appointment_choiceuser_lv.setAdapter(adapter);
        appointment_choiceuser_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userlist.get(position).getIsAble().equals("1")) {
                    // 启动确认体检人信息fragment
                    ServiceOrderData.setCb(userlist.get(position));
                    ((AppointmentActivity) getActivity()).toStepm2();
                }
            }
        });
        return view;
    }

    /**
     * 添加头部view(证卡类型和号码的信息)
     */
    private void addViews() {
        ui_items = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("left", "证件类型");
        map.put("right", ServiceOrderData.certTypeName);
        map.put("bottomLine", true);
        map.put("arrow", false);
        ui_items.add(map);
        map = new HashMap<String, Object>();
        map.put("left", "证件号码");
        map.put("right", ServiceOrderData.certNumber);
        map.put("bottomLine", false);
        map.put("arrow", false);
        ui_items.add(map);
        for (int i = 0; i < ui_items.size(); i++) {
            View infoitem = LayoutInflater.from(context).inflate(R.layout.appointment_choiceitem, null);
            TextView tvleft = (TextView) infoitem.findViewById(R.id.appointment_choiceitem_tvleft);
            TextView tvright = (TextView) infoitem.findViewById(R.id.appointment_choiceitem_tvright);
            ImageView arrow = (ImageView) infoitem.findViewById(R.id.appointment_choiceitem_arrow);
            View bottomline = infoitem.findViewById(R.id.appointment_choiceitem_bottomline);
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
}
