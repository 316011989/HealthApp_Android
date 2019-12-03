package com.pact.healthapp.components.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangyl on 2016/1/15.
 */
public class MakeSureUserInfo2 extends BaseFragment {
    private View view;
    private int choiceType;
    @ViewInject(R.id.appointment_makesure_items)
    private LinearLayout appointment_makesure_items;
    private List<HashMap<String, Object>> ui_items;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_makesureuserinfo_layout2,
                null);
        ViewUtils.inject(this, view);
        choiceType = AppointmentActivity.editType;
        addUIDatas();
        return view;
    }

    private void addUIDatas() {
        HashMap<String, Object> map;
        if (choiceType == 1) {//选择性别
            getActivity().setTitle("性别");
            ui_items = new ArrayList<HashMap<String, Object>>();
            map = new HashMap<String, Object>();
            map.put("left", "男");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getSexName().equals("男")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "女");
            map.put("right", "");
            map.put("bottomLine", false);
            if (ServiceOrderData.cb.getSexName().equals("女")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            addViews(false);//加载选择性别页面
        } else if (choiceType == 2) {//选择证件类型
            getActivity().setTitle("证件类型");
            ui_items = new ArrayList<HashMap<String, Object>>();
            map = new HashMap<String, Object>();
            map.put("left", "身份证");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("身份证")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "新华体检卡");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("新华体检卡")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "员工号");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("员工号")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "医保卡号");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("医保卡号")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "护照");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("护照")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "军官证");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("军官证")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "新华保险保单号");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("新华保险保单号")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "唯一号");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getCertTypeName().equals("唯一号")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            addViews(false);//加载选择证卡类型页面
        } else if (choiceType == 3) {//选择婚否状态
            ui_items = new ArrayList<HashMap<String, Object>>();
            map = new HashMap<String, Object>();
            map.put("left", "已婚");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getMarryName().equals("已婚")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "未婚");
            map.put("right", "");
            map.put("bottomLine", true);
            if (ServiceOrderData.cb.getMarryName().equals("未婚")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            map = new HashMap<String, Object>();
            map.put("left", "不限");
            map.put("right", "");
            map.put("bottomLine", false);
            if (ServiceOrderData.cb.getMarryName().equals("不限")) {
                map.put("arrow", true);
            } else {
                map.put("arrow", false);
            }
            ui_items.add(map);
            addViews(false);//加载选择婚否状态页面
            getActivity().setTitle("婚姻状况");
        }
    }

    /**
     * 加载页面
     *
     * @param nexttype 区分打开编辑选择或是退出编辑选择的页面
     */
    private void addViews(final boolean nexttype) {
        for (int i = 0; i < ui_items.size(); i++) {
            View infoitem = LayoutInflater.from(context).inflate(R.layout.appointment_choiceitem, null);
            TextView tvleft = (TextView) infoitem.findViewById(R.id.appointment_choiceitem_tvleft);
            TextView tvright = (TextView) infoitem.findViewById(R.id.appointment_choiceitem_tvright);
            ImageView arrow = (ImageView) infoitem.findViewById(R.id.appointment_choiceitem_arrow);
            View bottomline = infoitem.findViewById(R.id.appointment_choiceitem_bottomline);
            arrow.setBackgroundResource(R.mipmap.tick);
            tvleft.setText(ui_items.get(i).get("left").toString());
            tvright.setText(ui_items.get(i).get("right").toString());
            if ((Boolean) ui_items.get(i).get("arrow")) {
                arrow.setVisibility(View.VISIBLE);
            } else {
                arrow.setVisibility(View.INVISIBLE);
            }
            if ((Boolean) ui_items.get(i).get("bottomLine")) {
                bottomline.setVisibility(View.VISIBLE);
            } else {
                bottomline.setVisibility(View.GONE);
            }
            final int clickitem = i;
            infoitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choiceType == 1) {//性别选择
                        if (clickitem == 0) {//选择男
                            if (!ServiceOrderData.cb.getSexId().equals("M")) {//和现有对象有变化进行赋值
                                ServiceOrderData.cb.setSexId("M");
                                ServiceOrderData.cb.setSexName("男");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else {//选择女
                            if (!ServiceOrderData.cb.getSexId().equals("F")) {//和现有对象有变化进行赋值
                                ServiceOrderData.cb.setSexId("F");
                                ServiceOrderData.cb.setSexName("女");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        }
                    }
                    if (choiceType == 2) {//选择证件类型
                        if (clickitem == 0) {//选择身份证
                            if (!ServiceOrderData.cb.getCertTypeId().equals("1")) {
                                ServiceOrderData.cb.setCertTypeId("1");
                                ServiceOrderData.cb.setCertTypeName("身份证");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 1) {//选择新华体检卡
                            if (!ServiceOrderData.cb.getCertTypeId().equals("3")) {
                                ServiceOrderData.cb.setCertTypeId("3");
                                ServiceOrderData.cb.setCertTypeName("新华体检卡");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 2) {//选择员工卡
                            if (!ServiceOrderData.cb.getCertTypeId().equals("4")) {
                                ServiceOrderData.cb.setCertTypeId("4");
                                ServiceOrderData.cb.setCertTypeName("员工号");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 3) {//选择医保卡
                            if (!ServiceOrderData.cb.getCertTypeId().equals("5")) {
                                ServiceOrderData.cb.setCertTypeId("5");
                                ServiceOrderData.cb.setCertTypeName("医保卡号");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 4) {//选择护照
                            if (!ServiceOrderData.cb.getCertTypeId().equals("6")) {
                                ServiceOrderData.cb.setCertTypeId("6");
                                ServiceOrderData.cb.setCertTypeName("护照");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 5) {//选择军官证
                            if (!ServiceOrderData.cb.getCertTypeId().equals("7")) {
                                ServiceOrderData.cb.setCertTypeId("7");
                                ServiceOrderData.cb.setCertTypeName("军官证");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 6) {//选择新华保险单
                            if (!ServiceOrderData.cb.getCertTypeId().equals("9")) {
                                ServiceOrderData.cb.setCertTypeId("9");
                                ServiceOrderData.cb.setCertTypeName("新华保险保单号");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 7) {//选择唯一号
                            if (!ServiceOrderData.cb.getCertTypeId().equals("10")) {
                                ServiceOrderData.cb.setCertTypeId("10");
                                ServiceOrderData.cb.setCertTypeName("唯一号");
                                ServiceOrderData.cb.setCertNumber("");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        }
                    }
                    if (choiceType == 3) {//选择婚否状态
                        if (clickitem == 0) {//选择已婚
                            if (!ServiceOrderData.cb.getMarryId().equals("2")) {
                                ServiceOrderData.cb.setMarryId("2");
                                ServiceOrderData.cb.setMarryName("已婚");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 1) {//选择未婚
                            if (!ServiceOrderData.cb.getMarryId().equals("1")) {
                                ServiceOrderData.cb.setMarryId("1");
                                ServiceOrderData.cb.setMarryName("未婚");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        } else if (clickitem == 2) {//选择不限
                            if (!ServiceOrderData.cb.getMarryId().equals("6")) {
                                ServiceOrderData.cb.setMarryId("6");
                                ServiceOrderData.cb.setMarryName("不限");
                                ServiceOrderData.cb.setEdit(true);
                            }
                        }
                    }
                    ((AppointmentActivity) getActivity()).toStepm2();
                }
            });
            appointment_makesure_items.addView(infoitem);
        }
    }
}
