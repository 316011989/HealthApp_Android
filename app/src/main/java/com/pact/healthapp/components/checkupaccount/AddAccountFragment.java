package com.pact.healthapp.components.checkupaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.CheckupUserBean;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.EditTextDialog;
import com.pact.healthapp.view.MultipleRadioGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存亲情账户
 * Created by wangdong on 2016/1/7.
 */
@SuppressLint("ValidFragment")
public class AddAccountFragment extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @ViewInject(R.id.appointment_checkupuser_tip)
    private TextView appointment_checkupuser_tip;
    @ViewInject(R.id.appointment_checkupuser_tip2)
    private LinearLayout appointment_checkupuser_tip2;

    @ViewInject(R.id.add_accountr_rg)
    private MultipleRadioGroup add_accountr_rg;

    private EditTextDialog dialog;
    private CommonDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_checkupuser_layout, null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("新增账户");
        appointment_checkupuser_tip.setText("请选择以下任意一种证件新增账户");
        appointment_checkupuser_tip2.setVisibility(View.GONE);
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
     * 点击选择证卡类型
     *
     * @param v
     */
    @OnClick({R.id.appointment_checkupuser_rb1, R.id.appointment_checkupuser_rb2,
            R.id.appointment_checkupuser_rb3, R.id.appointment_checkupuser_rb4,
            R.id.appointment_checkupuser_rb5, R.id.appointment_checkupuser_rb6,
            R.id.appointment_checkupuser_rb7, R.id.appointment_checkupuser_rb8, R.id.appointment_checkupuser_rb9})
    private void showDialog(View v) {
        switch (v.getId()) {
            case R.id.appointment_checkupuser_rb1:
                AccountData.certTypeId = "1";
                AccountData.certTypeName = "身份证";
                break;
            case R.id.appointment_checkupuser_rb2:
                AccountData.certTypeId = "";
                AccountData.certTypeName = "凭证号";
                break;
            case R.id.appointment_checkupuser_rb3:
                AccountData.certTypeId = "3";
                AccountData.certTypeName = "新华体检卡";
                break;
            case R.id.appointment_checkupuser_rb4:
                AccountData.certTypeId = "4";
                AccountData.certTypeName = "员工号";
                break;
            case R.id.appointment_checkupuser_rb5:
                AccountData.certTypeId = "5";
                AccountData.certTypeName = "医保卡号";
                break;
            case R.id.appointment_checkupuser_rb6:
                AccountData.certTypeId = "6";
                AccountData.certTypeName = "护照";
                break;
            case R.id.appointment_checkupuser_rb7:
                AccountData.certTypeId = "7";
                AccountData.certTypeName = "军官证";
                break;
            case R.id.appointment_checkupuser_rb8:
                AccountData.certTypeId = "9";
                AccountData.certTypeName = "新华保险保单号";
                break;
            case R.id.appointment_checkupuser_rb9:
                AccountData.certTypeId = "10";
                AccountData.certTypeName = "唯一号";
                break;
        }
        dialog = new EditTextDialog(context, 2, "取消", "新增", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("");
                String tempNumber = ((EditText) dialog.findViewById(R.id.view_editdialog_edittext)).getText().toString().trim();
                if (AccountData.certTypeId == null) {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("请选择证件类型");
                    return;
                }
                if (tempNumber.equals("")) {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("请输入号码");
                    return;
                }
                if (AccountData.certTypeId.equals("1") && !CommonUtil.isIdCard(tempNumber)) {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("身份证号不正确，请重新输入");
                    return;
                }
                AccountData.certNumber = tempNumber;
                if (AccountData.certTypeId.equals("")) {
                    queryCustomerList1(true);
                } else {
                    queryCustomerList(false);
                }
            }
        }, "请输入号码");
        dialog.show();
        dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(false);
        if (AccountData.certTypeName.contains("号")) {
            ((EditText) dialog.findViewById(R.id.view_editdialog_edittext)).setHint("请输入" + AccountData.certTypeName);
        } else {
            ((EditText) dialog.findViewById(R.id.view_editdialog_edittext)).setHint("请输入" + AccountData.certTypeName + "号码");
        }
        ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.gray));
        ((EditText) dialog.findViewById(R.id.view_editdialog_edittext)).addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.toString().trim().length() > 0) {
                    ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.black));
                    dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(true);
                } else {
                    ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.gray));
                    dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(false);
                }
            }
        });
        if (AccountData.certTypeId.equals("1")) {
            final EditText et_content = ((EditText) dialog.findViewById(R.id.view_editdialog_edittext));
            et_content.addTextChangedListener(new TextWatcher() {
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
                    selectionStart = et_content.getSelectionStart();
                    selectionEnd = et_content.getSelectionEnd();
                    if (temp.toString().trim().length() > 18) {
                        s.delete(selectionStart - 1, selectionEnd);
                        int tempSelection = selectionStart;
                        et_content.setText(s);
                        et_content.setSelection(tempSelection);//设置光标在最后
                    }

                }
            });
        }
    }

    /**
     * 根据证件类型和证件号
     * 查询客人
     */
    private void queryCustomerList(final boolean isVoucher) {
        String serviceName = "queryCustomerListBooks";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("certType", AccountData.certTypeId);
        jo.put("certNumber", AccountData.certNumber);
        jo.put("isAdd", "add");
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
//                super.onSuccess(arg0);
                canclDialog();
                try {
                    String result = Des3.decode(arg0.result.toString());
                    JSONObject obj = JSON.parseObject(result);
                    if (obj.get("errorCode").toString().equals("1120")) {
                        dialog.dismiss();
                        mDialog = new CommonDialog(context, 1, "确定", "",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        mDialog.dismiss();
                                        SharedPreferenceUtils sp = new SharedPreferenceUtils();
                                        String phoneNumber = sp.getLoginInfo(
                                                context, "phoneNumber");
                                        Intent intent = new Intent(context,
                                                LoginActivity.class);
                                        intent.putExtra("phoneNumber", phoneNumber);
                                        sp.clearUserinfo(context);
                                        startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                    }
                                }, null, null, "当前账户在其他设备登录，请重新登录。");
                        mDialog.show();
                        return;
                    } else if (obj.get("errorCode").toString().equals("1")) {
                        ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setVisibility(View.VISIBLE);
                        ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText(obj.getString("resultMsg"));
                    } else {
                        parseJson(result, isVoucher);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                canclDialog();
                dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("服务器繁忙");
            }
        });
    }

    /**
     * 根据凭证号
     * 查询客人
     */
    private void queryCustomerList1(final boolean isVoucher) {
        String serviceName = "queryCustomerList2Book";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("voucherNo", AccountData.certNumber);
        jo.put("checkVoucher", false);
        jo.put("isAdd", "add");
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
//                super.onSuccess(arg0);
                canclDialog();
                try {
                    String result = Des3.decode(arg0.result.toString());
                    JSONObject obj = JSON.parseObject(result);
                    if (obj.get("errorCode").toString().equals("1120")) {
                        dialog.dismiss();
                        mDialog = new CommonDialog(context, 1, "确定", "",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        mDialog.dismiss();
                                        SharedPreferenceUtils sp = new SharedPreferenceUtils();
                                        String phoneNumber = sp.getLoginInfo(
                                                context, "phoneNumber");
                                        Intent intent = new Intent(context,
                                                LoginActivity.class);
                                        intent.putExtra("phoneNumber", phoneNumber);
                                        sp.clearUserinfo(context);
                                        ((Activity) context).startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                    }
                                }, null, null, "当前账户在其他设备登录，请重新登录。");
                        mDialog.show();
                        return;
                    } else if (obj.get("errorCode").toString().equals("1126")) {
                        dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                        seconds = obj.getIntValue("seconds");
                        handler.postDelayed(runnable, 0);
                        dialog.findViewById(R.id.view_editdialog_edittext).setEnabled(false);
                        ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.gray));
                        dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(false);
                    } else if (obj.get("errorCode").toString().equals("1")) {
                        ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setVisibility(View.VISIBLE);
                        ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText(obj.getString("resultMsg"));
                    } else {
                        parseJson(result, isVoucher);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                canclDialog();
                dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("服务器繁忙");
            }

        });
    }

    /**
     * 解析json数据,获取体检人数据成功之后再进入选择体检人界面
     * 保存亲情账户
     *
     * @param result 解密后的json数据
     */
    private void parseJson(String result, boolean isVoucher) {
        JSONObject joall = JSONObject.parseObject(result);
        if (joall.getString("resultCode").equals("0")) {
            List<CheckupUserBean> list = JSON.parseArray(joall.getString("data"), CheckupUserBean.class);
            if (list != null && list.size() > 1) {
                // 启动选择体检人fragment
                AccountData.users = list;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_fl_continer, new ChoiceAccountFragment())
                        .addToBackStack("ChoiceAccountFragment").commit();
                dialog.dismiss();
            } else if (list != null && list.size() == 1) {
                // 保存亲情账户
                AccountData.setCb(list.get(0));
                saveChekupAccount(isVoucher);
            } else {
                CheckupUserBean bean = new CheckupUserBean();
                bean.setCustomerName("");
                bean.setCertTypeId(AccountData.certTypeId);
                bean.setCertTypeName(AccountData.certTypeName);
                bean.setCertNumber(AccountData.certNumber);
                bean.setCustomerId("");
                AccountData.setCb(bean);
                saveChekupAccount(isVoucher);
            }
        }

    }

    private int seconds = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds--;
            if (seconds >= 0) {
                String delay = "5分00秒";
                if (seconds / 60 != 0) {
                    delay = (seconds / 60) + "分" + (seconds % 60) + "秒";
                } else {
                    delay = (seconds % 60) + "秒";
                }
                ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("您已连续3次输入无效的凭证号，请在" + delay + "后重新输入！");
                handler.postDelayed(this, 1000);
            } else {
                if (AddAccountFragment.this.isAdded()) {
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("");
                    dialog.findViewById(R.id.view_editdialog_edittext).setEnabled(true);
                    ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.black));
                    dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(true);
                    handler.removeCallbacks(runnable);
                }
            }
        }
    };

    /**
     * 保存亲情账户
     */
    private void saveChekupAccount(boolean isVoucher) {
        String bizId = "000";
        String serviceName = "saveChekupAccount";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("accountName", AccountData.cb.getCustomerName());
        if (isVoucher) {
            map.put("cardType", AccountData.certTypeId);
            map.put("cardTypeName", AccountData.certTypeName);
            map.put("cardNo", AccountData.certNumber);
        } else {
            map.put("cardType", AccountData.cb.getCertTypeId());
            map.put("cardTypeName", AccountData.cb.getCertTypeName());
            map.put("cardNo", AccountData.cb.getCertNumber());
        }
        map.put("crmCustomerId", AccountData.cb.getCustomerId());
        map.put("updateDate", System.currentTimeMillis());  //long类型
        String para = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, para,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        canclDialog();
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                getFragmentManager().popBackStack();
                                dialog.dismiss();
                            }

                            if ((obj.get("errorCode") != null
                                    && obj.get("errorCode").toString().equals("1120"))) {
                                dialog.dismiss();
                                mDialog = new CommonDialog(context, 1, "确定", "",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method stub
                                                mDialog.dismiss();
                                                SharedPreferenceUtils sp = new SharedPreferenceUtils();
                                                String phoneNumber = sp.getLoginInfo(
                                                        context, "phoneNumber");
                                                Intent intent = new Intent(context,
                                                        LoginActivity.class);
                                                intent.putExtra("phoneNumber", phoneNumber);
                                                sp.clearUserinfo(context);
                                                ((Activity) context).startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                            }
                                        }, null, null, "当前账户在其他设备登录，请重新登录。");
                                mDialog.show();
                            } else if ((obj.get("errorCode") != null
                                    && obj.get("errorCode").toString().equals("1"))) {
                                dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                                ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText(obj.getString("resultMsg"));
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
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
