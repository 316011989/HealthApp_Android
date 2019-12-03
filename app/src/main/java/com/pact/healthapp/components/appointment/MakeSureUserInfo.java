package com.pact.healthapp.components.appointment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pact.healthapp.R;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;

/**
 * 体检人
 * Created by zhangyl on 2015/12/25.
 */
public class MakeSureUserInfo extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private View view;
    @ViewInject(R.id.appointment_choiceitem1)
    private LinearLayout appointment_choiceitem1;
    @ViewInject(R.id.appointment_choiceitem4)
    private LinearLayout appointment_choiceitem4;
    @ViewInject(R.id.appointment_choiceitem_tvright1)
    private TextView appointment_choiceitem_tvright1;
    @ViewInject(R.id.appointment_choiceitem_tvright2)
    private EditText appointment_choiceitem_tvright2;
    @ViewInject(R.id.appointment_choiceitem_tvright3)
    private TextView appointment_choiceitem_tvright3;
    @ViewInject(R.id.appointment_choiceitem_tvright4)
    private TextView appointment_choiceitem_tvright4;
    @ViewInject(R.id.appointment_choiceitem_tvright5)
    private EditText appointment_choiceitem_tvright5;
    @ViewInject(R.id.appointment_choiceitem_tvright6)
    private TextView appointment_choiceitem_tvright6;
    @ViewInject(R.id.appointment_choiceitem_tvright7)
    private EditText appointment_choiceitem_tvright7;
    @ViewInject(R.id.appointment_choiceitem_tvright8)
    private EditText appointment_choiceitem_tvright8;
    @ViewInject(R.id.appointment_choiceitem_must5)
    private TextView appointment_choiceitem_must5;
    @ViewInject(R.id.appointment_choiceitem_must2)
    private TextView appointment_choiceitem_must2;
    @ViewInject(R.id.appointment_choiceitem_arrow3)
    private View appointment_choiceitem_arrow3;
    @ViewInject(R.id.appointment_choiceitem_arrow4)
    private View appointment_choiceitem_arrow4;
    @ViewInject(R.id.appointment_choiceitem_arrow6)
    private View appointment_choiceitem_arrow6;
    @ViewInject(R.id.appointment_makesure_next)
    private Button appointment_makesure_next;
    @ViewInject(R.id.appointment_makesureuserinfo_tip)
    private TextView appointment_makesureuserinfo_tip;

    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_makesureuserinfo_layout,
                null);
        ViewUtils.inject(this, view);
        appointment_makesure_next.requestFocus();
        getActivity().setTitle("体检人");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setView();
    }

    private void setView() {
        appointment_makesureuserinfo_tip.setVisibility(View.VISIBLE);
        if (ServiceOrderData.certTypeId.equals("")) {//凭证号预约时,证卡类型为""
            appointment_choiceitem_tvright1.setText(ServiceOrderData.certNumber);//第一行凭证号赋值
            //凭证号预约时可能没有对应客人信息
            if (ServiceOrderData.userinfoEditable == 1) {
                appointment_makesureuserinfo_tip.setVisibility(View.INVISIBLE);
                //为个人信息赋值
                if (ServiceOrderData.cb == null) {
                    //初始化部分客人信息
                    ServiceOrderData.setCb(new CheckupUserBean());
                    ServiceOrderData.cb.setCustomerName("");
                    ServiceOrderData.cb.setSexId("M");
                    ServiceOrderData.cb.setSexName("男");
                    ServiceOrderData.cb.setCertTypeId("1");
                    ServiceOrderData.cb.setCertTypeName("身份证");
                    ServiceOrderData.cb.setCertNumber("");
                    ServiceOrderData.cb.setMarryId("1");
                    ServiceOrderData.cb.setMarryName("未婚");
                    ServiceOrderData.cb.setMobileNumber("");
                    ServiceOrderData.cb.setEmail("");
                    ServiceOrderData.cb.setEdit(false);
                }
                //凭证号查询并且没有对应客人信息,需要新建客人,所有信息都可以编辑
                appointment_choiceitem_tvright2.setEnabled(true);
                appointment_choiceitem_tvright3.setEnabled(true);
                appointment_choiceitem_tvright4.setEnabled(true);
                appointment_choiceitem_tvright5.setEnabled(true);
                appointment_choiceitem_tvright6.setEnabled(true);
                appointment_choiceitem_tvright7.setEnabled(true);
                appointment_choiceitem_tvright8.setEnabled(true);
                appointment_choiceitem_must2.setVisibility(View.VISIBLE);
                appointment_choiceitem_must5.setVisibility(View.VISIBLE);
                appointment_choiceitem_arrow3.setVisibility(View.VISIBLE);
                appointment_choiceitem_arrow4.setVisibility(View.VISIBLE);
                appointment_choiceitem_arrow6.setVisibility(View.VISIBLE);
            } else if (ServiceOrderData.userinfoEditable == 2) {
                //查询到一个空信息客人
                appointment_makesureuserinfo_tip.setVisibility(View.INVISIBLE);
                //凭证号查询并且没有对应客人信息,需要新建客人,所有信息都可以编辑
                appointment_choiceitem_tvright2.setEnabled(true);
                appointment_choiceitem_tvright3.setEnabled(true);
                appointment_choiceitem_tvright4.setEnabled(false);
                appointment_choiceitem_tvright5.setEnabled(false);
                appointment_choiceitem_tvright6.setEnabled(true);
                appointment_choiceitem_tvright7.setEnabled(true);
                appointment_choiceitem_tvright8.setEnabled(true);
                appointment_choiceitem_must2.setVisibility(View.VISIBLE);
                appointment_choiceitem_must5.setVisibility(View.GONE);
                appointment_choiceitem_arrow3.setVisibility(View.VISIBLE);
                appointment_choiceitem_arrow4.setVisibility(View.GONE);
                appointment_choiceitem_arrow6.setVisibility(View.VISIBLE);
            } else {
                //查询到对应客人
                appointment_choiceitem_tvright2.setEnabled(false);
                appointment_choiceitem_tvright3.setEnabled(false);
                appointment_choiceitem_tvright4.setEnabled(false);
                appointment_choiceitem_tvright5.setEnabled(false);
                appointment_choiceitem_tvright6.setEnabled(false);
                appointment_choiceitem_tvright7.setEnabled(false);
                appointment_choiceitem_tvright8.setEnabled(false);
                appointment_choiceitem_must2.setVisibility(View.GONE);
                appointment_choiceitem_must5.setVisibility(View.GONE);
                appointment_choiceitem_arrow3.setVisibility(View.GONE);
                appointment_choiceitem_arrow4.setVisibility(View.GONE);
                appointment_choiceitem_arrow6.setVisibility(View.GONE);
                appointment_choiceitem_tvright2.setHint("");
                appointment_choiceitem_tvright7.setHint("");
                appointment_choiceitem_tvright8.setHint("");
            }
        } else {//证卡类型预约
            //隐藏凭证号的第一行和分割线
            appointment_choiceitem1.setVisibility(View.GONE);
            view.findViewById(R.id.appointment_choiceitem_bottomline).setVisibility(View.GONE);
            //证卡预约只有查询到客人才可进入,而有客人的情况下个人信息全部不可编辑
            appointment_choiceitem_tvright2.setEnabled(false);
            appointment_choiceitem_tvright3.setEnabled(false);
            appointment_choiceitem_tvright4.setEnabled(false);
            appointment_choiceitem_tvright5.setEnabled(false);
            appointment_choiceitem_tvright6.setEnabled(false);
            appointment_choiceitem_tvright7.setEnabled(false);
            appointment_choiceitem_tvright8.setEnabled(false);
            appointment_choiceitem_tvright2.setHint("");
            appointment_choiceitem_tvright7.setHint("");
            appointment_choiceitem_tvright8.setHint("");
            appointment_choiceitem_must2.setVisibility(View.GONE);
            appointment_choiceitem_must5.setVisibility(View.GONE);
            appointment_choiceitem_arrow3.setVisibility(View.GONE);
            appointment_choiceitem_arrow4.setVisibility(View.GONE);
            appointment_choiceitem_arrow6.setVisibility(View.GONE);
        }
        //为个人信息赋值
        appointment_choiceitem_tvright2.setText(ServiceOrderData.cb.getCustomerName());
        appointment_choiceitem_tvright3.setText(ServiceOrderData.cb.getSexName());
        appointment_choiceitem_tvright4.setText(ServiceOrderData.cb.getCertTypeName());
        appointment_choiceitem_tvright5.setText(ServiceOrderData.cb.getCertNumber());
        appointment_choiceitem_tvright6.setText(ServiceOrderData.cb.getMarryName());
        if (ServiceOrderData.userssize > 1 && !ServiceOrderData.cb.getMobileNumber().equals("")) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ServiceOrderData.cb.getMobileNumber().substring(0, 3));
            stringBuffer.append("****");
            stringBuffer.append(ServiceOrderData.cb.getMobileNumber().substring(7, ServiceOrderData.cb.getMobileNumber().length()));
            appointment_choiceitem_tvright7.setText(stringBuffer);
        } else {
            appointment_choiceitem_tvright7.setText(ServiceOrderData.cb.getMobileNumber());
        }
        appointment_choiceitem_tvright8.setText(ServiceOrderData.cb.getEmail());
        //设置监听
        appointment_makesure_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ServiceOrderData.userinfoEditable == 1 || ServiceOrderData.userinfoEditable == 2) {
                    if (checkUserinfoUseable())
                        saveCustermerInfo();
                } else {
                    if (checkUserinfoUseable())
                        if (!ServiceOrderData.cb.isEdit()) {//如果未修改用户信息,就不用调保存体检人信息
                            //获取过凭证直接展示
                            if (ServiceOrderData.vouchers != null) {
                                if (ServiceOrderData.vouchers.size() == 0) {
                                    CommonToast.makeText(context, "该证件所有者无可用凭证");
                                } else if (ServiceOrderData.vouchers.size() == 1) {
                                    //没选择过凭证或上次选择与本次不同
                                    if (ServiceOrderData.vb == null || !ServiceOrderData.vouchers.get(0).equals(ServiceOrderData.vb)) {
                                        ServiceOrderData.setVb(ServiceOrderData.vouchers.get(0));
                                    }
                                    ((AppointmentActivity) getActivity()).toStep3();
                                } else if (ServiceOrderData.vouchers.size() > 1) {
                                    ((AppointmentActivity) getActivity()).toStep2();
                                }
                            } else {
                                //未获取过凭证
                                queryVoucherList();
                            }
                        } else {
                            //未获取过凭证
                            queryVoucherList();
                        }

                }
            }
        });
        appointment_choiceitem_tvright2.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = appointment_choiceitem_tvright2.getSelectionStart();
                selectionEnd = appointment_choiceitem_tvright2.getSelectionEnd();
                if (temp.length() > 30) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    appointment_choiceitem_tvright2.setText(s);
                    appointment_choiceitem_tvright2.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        appointment_choiceitem_tvright5.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = appointment_choiceitem_tvright5.getSelectionStart();
                selectionEnd = appointment_choiceitem_tvright5.getSelectionEnd();
                if (ServiceOrderData.cb.getCertTypeId().equals("1") && temp.length() > 18) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    appointment_choiceitem_tvright5.setText(s);
                    appointment_choiceitem_tvright5.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }


    @OnClick({R.id.appointment_choiceitem3, R.id.appointment_choiceitem4, R.id.appointment_choiceitem6})
    public void editClick(View v) {
        if (!appointment_choiceitem_tvright2.getText().toString().trim().equals("")) {//前后有空格的过滤掉
            //修改后的执行赋值操作,未修改不用赋值
            if (!appointment_choiceitem_tvright2.getText().toString().trim().replaceAll(" ", "").equals(ServiceOrderData.cb.getCustomerName())) {
                ServiceOrderData.cb.setCustomerName(appointment_choiceitem_tvright2.getText().toString().trim().replaceAll(" ", ""));
            }
        } else {//全是空格的不过滤
            //修改后的执行赋值操作,未修改不用赋值
            if (!appointment_choiceitem_tvright2.getText().toString().replaceAll(" ", "").equals(ServiceOrderData.cb.getCustomerName())) {
                ServiceOrderData.cb.setCustomerName(appointment_choiceitem_tvright2.getText().toString().replaceAll(" ", ""));
            }
        }
        ServiceOrderData.cb.setMobileNumber(appointment_choiceitem_tvright7.getText().toString());
        ServiceOrderData.cb.setEmail(appointment_choiceitem_tvright8.getText().toString());
        ServiceOrderData.cb.setCertNumber(appointment_choiceitem_tvright5.getText().toString());

        if (ServiceOrderData.certTypeId.equals("") && ServiceOrderData.userinfoEditable == 1) {
            switch (v.getId()) {
                case R.id.appointment_choiceitem3:
                    // 启动性别选择fragment
                    AppointmentActivity.editType = 1;
                    ((AppointmentActivity) getActivity()).toStepm3();
                    break;
                case R.id.appointment_choiceitem4:
                    // 启动证卡类型选择fragment
                    AppointmentActivity.editType = 2;
                    ((AppointmentActivity) getActivity()).toStepm3();
                    break;
                case R.id.appointment_choiceitem6:
                    // 启动婚姻状况fragment
                    AppointmentActivity.editType = 3;
                    ((AppointmentActivity) getActivity()).toStepm3();
                    break;
            }
        } else if (ServiceOrderData.certTypeId.equals("") && ServiceOrderData.userinfoEditable == 2) {
            switch (v.getId()) {
                case R.id.appointment_choiceitem3:
                    // 启动性别选择fragment
                    AppointmentActivity.editType = 1;
                    ((AppointmentActivity) getActivity()).toStepm3();
                    break;
                case R.id.appointment_choiceitem6:
                    // 启动婚姻状况fragment
                    AppointmentActivity.editType = 3;
                    ((AppointmentActivity) getActivity()).toStepm3();
                    break;
            }
        }

    }

    /**
     * 保存个人信息
     */
    private void saveCustermerInfo() {
        if (!ServiceOrderData.cb.isEdit()) {//如果未修改用户信息,就不用调保存体检人信息
            //获取过凭证直接展示
            if (ServiceOrderData.vouchers != null) {
                if (ServiceOrderData.vouchers.size() == 0) {
                    CommonToast.makeText(context, "该证件所有者无可用凭证");
                } else if (ServiceOrderData.vouchers.size() == 1) {
                    //没选择过凭证或上次选择与本次不同
                    if (ServiceOrderData.vb == null || !ServiceOrderData.vouchers.get(0).equals(ServiceOrderData.vb)) {
                        ServiceOrderData.setVb(ServiceOrderData.vouchers.get(0));
                    }
                    ((AppointmentActivity) getActivity()).toStep3();
                } else if (ServiceOrderData.vouchers.size() > 1) {
                    ((AppointmentActivity) getActivity()).toStep2();
                }
            } else {
                //未获取过凭证
                queryVoucherList();
            }
            return;
        }
        String serviceName = "saveCustomerBook";
        JSONObject perJo = new JSONObject();
        perJo.put("crmCustomerId", ServiceOrderData.cb.getCustomerId());
        perJo.put("certType", ServiceOrderData.cb.getCertTypeId());
        perJo.put("certNumber", ServiceOrderData.cb.getCertNumber());
        perJo.put("sexId", ServiceOrderData.cb.getSexId()); // U, F, M
        perJo.put("customerName", ServiceOrderData.cb.getCustomerName());
        perJo.put("nciSocialClass", ServiceOrderData.cb.getSocialClass());
        perJo.put("mobileNumber", ServiceOrderData.cb.getMobileNumber());
        perJo.put("phoneNumber", ServiceOrderData.cb.getPhoneNumber());
        perJo.put("email", ServiceOrderData.cb.getEmail());
        perJo.put("educationLevel", ServiceOrderData.cb.getEducationLevelId());
        perJo.put("nCIDictionaryIncome", ServiceOrderData.cb.getNCIDictionaryIncomeId());
        perJo.put("enterpriseName", ServiceOrderData.cb.getEnterpriseName());
        perJo.put("industry", ServiceOrderData.cb.getIndustryId());
        perJo.put("department1", ServiceOrderData.cb.getDepartment1());
        perJo.put("department2", ServiceOrderData.cb.getDepartment2());
        perJo.put("vocation", ServiceOrderData.cb.getVocation());
        perJo.put("homeAddress", ServiceOrderData.cb.getHomeAddress());
        perJo.put("postalAddress", ServiceOrderData.cb.getPostalAddress());
        perJo.put("postalCode", ServiceOrderData.cb.getPostalCode());
        perJo.put("marryId", ServiceOrderData.cb.getMarryId()); // [2, 1, 6]
        perJo.put("nationality", ServiceOrderData.cb.getNationality());
        perJo.put("birthday", ServiceOrderData.cb.getBirthday());
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        if (ServiceOrderData.operation != null && ServiceOrderData.operation.equals("voucher")) {
            jo.put("isAddByVoucher", "");
        } else {
            if (ServiceOrderData.userinfoEditable == 1 && ServiceOrderData.cb.getCustomerId() == null) {
                jo.put("isAddByVoucher", "addByVoucher");
            } else {
                jo.put("isAddByVoucher", "");
            }
        }
        jo.put("examineeInfo", perJo);
        ServiceEngin.Request(context, "000", serviceName, jo.toJSONString(), new

                EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        super.onSuccess(arg0);
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            parseJson(result);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

        );
    }


    /**
     * 检查体检人信息是否可正常使用
     */
    private boolean checkUserinfoUseable() {
        String str = appointment_choiceitem_tvright2.getText().toString();
        if (str.trim().equals("")) {//全是空格
            CommonToast.makeText(context, "请输入姓名！");
            return false;
        } else {
            if (str.getBytes().length == str.length()) {//不带汉字
                str = str.trim();//仅去掉两头的空格
            } else {//带汉字
                str = str.trim().replaceAll(" ", "");//去掉所有空格
            }
            //修改后的执行赋值操作,未修改不用赋值
            if (!str.equals(ServiceOrderData.cb.getCustomerName())) {
                ServiceOrderData.cb.setCustomerName(str);
            }
        }

        //手机号校验
        if (appointment_choiceitem_tvright7.getText().toString().equals("")) {//手机号填写空
            if (!appointment_choiceitem_tvright7.getText().toString().equals(ServiceOrderData.cb.getMobileNumber())) {//之前不是空
                ServiceOrderData.cb.setMobileNumber("");//修改单例中用户的手机号
            }
        } else if ((ServiceOrderData.userinfoEditable == 1 || ServiceOrderData.userinfoEditable == 2) && CommonUtil.isMobileNO(appointment_choiceitem_tvright7.getText().toString())) {//手机号可编辑并且编辑后符合手机号标准
            if (!appointment_choiceitem_tvright7.getText().toString().equals(ServiceOrderData.cb.getMobileNumber())) {//与之前不同
                ServiceOrderData.cb.setMobileNumber(appointment_choiceitem_tvright7.getText().toString());//修改单例中用户的手机号
            }
        } else if ((ServiceOrderData.userinfoEditable == 1 || ServiceOrderData.userinfoEditable == 2) && !CommonUtil.isMobileNO(appointment_choiceitem_tvright7.getText().toString())) {//手机号可编辑并且编辑后不符合手机号标准
            CommonToast.makeText(context, "手机号格式错误");
            return false;
        }

        //邮箱格式校验
        if (appointment_choiceitem_tvright8.getText().toString().equals("")) {//邮箱空
            if (!appointment_choiceitem_tvright8.getText().toString().equals(ServiceOrderData.cb.getEmail())) {//之前不是空
                ServiceOrderData.cb.setEmail("");//修改单例中用户的邮箱
            }
        } else if ((ServiceOrderData.userinfoEditable == 1 || ServiceOrderData.userinfoEditable == 2) && CommonUtil.isEmail(appointment_choiceitem_tvright8.getText().toString())) {//邮箱可编辑并且编辑后符合邮箱标准
            if (!appointment_choiceitem_tvright8.getText().toString().equals(ServiceOrderData.cb.getEmail())) {//与之前不同
                ServiceOrderData.cb.setEmail(appointment_choiceitem_tvright8.getText().toString());//修改单例中用户的邮箱
            }
        } else if ((ServiceOrderData.userinfoEditable == 1 || ServiceOrderData.userinfoEditable == 2) && !CommonUtil.isEmail(appointment_choiceitem_tvright8.getText().toString())) {//邮箱可编辑并且编辑后符合不邮箱标准
            CommonToast.makeText(context, "邮箱格式错误");
            return false;
        }


        if (appointment_choiceitem_tvright5.getText().toString().equals("")) {
            CommonToast.makeText(context, "请输入证件号码！");
            return false;
        } else if (ServiceOrderData.cb.getCertTypeId().equals("1") && !CommonUtil.isIdCard(appointment_choiceitem_tvright5.getText().toString())) {
            CommonToast.makeText(context, "身份证号不正确，请重新输入");
            return false;
        } else {
            if (!appointment_choiceitem_tvright5.getText().toString().equals(ServiceOrderData.cb.getCertNumber())) {//与之前不同
                ServiceOrderData.cb.setCertNumber(appointment_choiceitem_tvright5.getText().toString());//修改单例中用户的正卡号
            }
        }
        if (ServiceOrderData.userinfoEditable == 0 && !ServiceOrderData.cb.getSexId().equals("M") && !ServiceOrderData.cb.getSexId().equals("F")) {
            dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            }, null, null, "性别信息不确定，请联系客服（95567-6）确认性别后重新预约。");
            dialog.show();
            return false;
        } else if ((ServiceOrderData.userinfoEditable == 2 || ServiceOrderData.userinfoEditable == 1)
                && !ServiceOrderData.cb.getSexId().equals("M") && !ServiceOrderData.cb.getSexId().equals("F")) {
            CommonToast.makeText(context, "请确认性别");
            return false;
        }
        if (ServiceOrderData.userinfoEditable == 1 && ServiceOrderData.cb.getSexId().equals("F") && ServiceOrderData.cb.getMarryId().equals("6")) {
            CommonToast.makeText(context, "请确认婚姻状况");
            return false;
        } else if (ServiceOrderData.userinfoEditable == 0 && ServiceOrderData.cb.getSexId().equals("F") && ServiceOrderData.cb.getMarryId().equals("6")) {
            dialog = new CommonDialog(context, 1, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            }, null, null, "婚姻状况信息不确定，请联系客服（95567-6）确认婚姻状况后重新预约。");
            dialog.show();
            return false;
        }
        return true;
    }


    /**
     * 解析json数据,保存客人信息成功
     *
     * @param result 解密后的json数据
     */
    private void parseJson(String result) {
        JSONObject joall = JSONObject.parseObject(result);
        if (joall.getString("resultCode").equals("0")) {//保存个人信息成功后进入凭证选择页面
            JSONObject data = joall.getJSONObject("data");
            if (data.getString("customerId") != null)
                ServiceOrderData.cb.setCustomerId(data.getString("customerId"));
            queryVoucherList();
        }
    }


    /**
     * 查询凭证列表
     */
    private void queryVoucherList() {
        String serviceName = "queryVoucherListBook";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("crmCustomerId", ServiceOrderData.cb.getCustomerId());
        jo.put("isExamination", "Y");
        if (ServiceOrderData.certTypeId.equals(""))
            jo.put("voucherNo", ServiceOrderData.certNumber);
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
                        Log.e("查询凭证列表", result);
                        parseJson1(result);
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
    private void parseJson1(String result) {
        JSONObject joall = JSONObject.parseObject(result);
        JSONArray javouchers = joall.getJSONArray("data");
        if (javouchers != null && javouchers.size() > 0) {
            ServiceOrderData.vouchers = JSON.parseArray(javouchers.toJSONString(), VoucherBean.class);
            if (ServiceOrderData.vouchers.size() == 0) {
                CommonToast.makeText(context, "该证件所有者无可用凭证");
            } else if (ServiceOrderData.vouchers.size() == 1) {
                ServiceOrderData.setVb(ServiceOrderData.vouchers.get(0));
                ((AppointmentActivity) getActivity()).toStep3();
            } else if (ServiceOrderData.vouchers.size() > 1) {
                ((AppointmentActivity) getActivity()).toStep2();
            }
        } else {
            CommonToast.makeText(context, "该证件所有者无可用凭证");
            ServiceOrderData.vouchers = null;
            ServiceOrderData.setVb(null);
            ((AppointmentActivity) getActivity()).toStepm2();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftInput();
    }

    //隐藏软键盘
    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        // 找到当前获得焦点的 view，从而可以获得正确的窗口 token
        View view = getActivity().getCurrentFocus();
        // 如果没有获得焦点的 view，创建一个新的，从而得到一个窗口的 token
        if (view == null) {
            view = new View(getActivity());
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
