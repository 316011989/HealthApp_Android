/**
 *
 */
package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.EditTextDialog;

import java.util.List;

/**
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class CheckupUserFragment extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private EditTextDialog dialog;
    private CommonDialog exitdialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_checkupuser_layout,
                null);
        ViewUtils.inject(this, view);
        ((BaseFragmentActivity) getActivity()).btn_filter.setVisibility(View.GONE);
        getActivity().setTitle("预约");
        ServiceOrderData.fromWhere = "Y";
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
                ServiceOrderData.setCertTypeId("1");
                ServiceOrderData.setCertTypeName("身份证");
                break;
            case R.id.appointment_checkupuser_rb2:
                ServiceOrderData.setCertTypeId("");
                ServiceOrderData.setCertTypeName("凭证号");
                break;
            case R.id.appointment_checkupuser_rb3:
                ServiceOrderData.setCertTypeId("3");
                ServiceOrderData.setCertTypeName("新华体检卡");
                break;
            case R.id.appointment_checkupuser_rb4:
                ServiceOrderData.setCertTypeId("4");
                ServiceOrderData.setCertTypeName("员工号");
                break;
            case R.id.appointment_checkupuser_rb5:
                ServiceOrderData.setCertTypeId("5");
                ServiceOrderData.setCertTypeName("医保卡号");
                break;
            case R.id.appointment_checkupuser_rb6:
                ServiceOrderData.setCertTypeId("6");
                ServiceOrderData.setCertTypeName("护照");
                break;
            case R.id.appointment_checkupuser_rb7:
                ServiceOrderData.setCertTypeId("7");
                ServiceOrderData.setCertTypeName("军官证");
                break;
            case R.id.appointment_checkupuser_rb8:
                ServiceOrderData.setCertTypeId("9");
                ServiceOrderData.setCertTypeName("新华保险保单号");
                break;
            case R.id.appointment_checkupuser_rb9:
                ServiceOrderData.setCertTypeId("10");
                ServiceOrderData.setCertTypeName("唯一号");
                break;
        }
        dialog = new EditTextDialog(context, 2, "取消", "预约", new View.OnClickListener() {
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
                if (ServiceOrderData.certTypeId == null) {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("请选择证件类型");
                    return;
                }
                if (tempNumber.equals("")) {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("请输入号码");
                    return;
                }
                if (ServiceOrderData.certTypeId.equals("1") && !CommonUtil.isIdCard(tempNumber)) {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("身份证号不正确，请重新输入");
                    return;
                }
                ServiceOrderData.setCertNumber(tempNumber);
                if (ServiceOrderData.certTypeId.equals("")) {
                    queryCustomerList1();
                } else {
                    queryCustomerList();
                }
            }
        }, "请输入号码");
        dialog.show();
        dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(false);
        if (ServiceOrderData.certTypeName.contains("号")) {
            ((EditText) dialog.findViewById(R.id.view_editdialog_edittext)).setHint("请输入" + ServiceOrderData.certTypeName);
        } else {
            ((EditText) dialog.findViewById(R.id.view_editdialog_edittext)).setHint("请输入" + ServiceOrderData.certTypeName + "号码");
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
                    ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.gray_light));
                    dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(false);
                }
            }
        });
        if (ServiceOrderData.certTypeId.equals("1")) {
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
    private void queryCustomerList() {
        String serviceName = "queryCustomerListBooks";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("certType", ServiceOrderData.certTypeId);
        jo.put("certNumber", ServiceOrderData.certNumber);
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
                canclDialog();
                try {
                    String result = Des3.decode(arg0.result.toString());
                    JSONObject obj = JSON.parseObject(result);
                    if (obj.get("errorCode").toString().equals("1")) {
                        dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                        ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText(obj.getString("resultMsg"));
                    } else if (obj.get("errorCode").toString().equals("1120")) {
                        dialog.dismiss();
                        exitdialog = new CommonDialog(context, 1, "确定", "",
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
                                        exitdialog.dismiss();
                                        startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
//                                        ((Activity) context).finish();
                                    }
                                }, null, null, "当前账户在其他设备登录，请重新登录。");
                        exitdialog.show();
                        return;
                    } else {
                        parseJson(result);
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
    private void queryCustomerList1() {
        String serviceName = "queryCustomerList2Book";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("voucherNo", ServiceOrderData.certNumber);
        jo.put("checkVoucher", false);
        String para = jo.toJSONString();
        ServiceEngin.Request(context, "000", serviceName, para, new EnginCallback(context) {
            @Override
            public void onSuccess(ResponseInfo arg0) {
                canclDialog();
                try {
                    String result = Des3.decode(arg0.result.toString());
                    JSONObject obj = JSON.parseObject(result);
                    if (obj.get("errorCode").toString().equals("1126")) {
                        dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                        seconds = obj.getIntValue("seconds");
                        handler.postDelayed(runnable, 1000);
                        dialog.findViewById(R.id.view_editdialog_edittext).setEnabled(false);
                        ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.gray));
                        dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(false);
                    } else if (obj.get("errorCode").toString().equals("1120")) {
                        dialog.dismiss();
                        exitdialog = new CommonDialog(context, 1, "确定", "",
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
                                        exitdialog.dismiss();
                                        startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
//                                        ((Activity) context).finish();
                                    }
                                }, null, null, "当前账户在其他设备登录，请重新登录。");
                        exitdialog.show();
                        return;
                    } else {
                        parseJson(result);
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

    private int seconds = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds--;
            if (seconds > 0) {
                String delay = "5分00秒";
                if (seconds / 60 != 0) {
                    delay = (seconds / 60) + "分" + (seconds % 60) + "秒";
                } else {
                    delay = (seconds % 60) + "秒";
                }
                ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("您已连续3次输入无效的凭证号，请在" + delay + "后重新输入！");
                handler.postDelayed(this, 1000);
            } else {
                if (CheckupUserFragment.this.isAdded()) {
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("");
                    dialog.findViewById(R.id.view_editdialog_edittext).setEnabled(true);
                    ((Button) dialog.findViewById(R.id.view_editdialog_btn_cancel)).setTextColor(getResources().getColor(R.color.black));
                    dialog.findViewById(R.id.view_editdialog_btn_cancel).setClickable(true);
                    handler.removeCallbacks(runnable);
                }
            }
            Log.i("seconds---->", seconds + "");
        }
    };

    /**
     * 解析json数据,获取体检人数据成功之后再进入选择体检人界面
     *
     * @param result 解密后的json数据
     */
    private void parseJson(String result) {
        JSONObject joall = JSONObject.parseObject(result);
        if (joall.getString("resultCode") != null && joall.getString("resultCode").equals("0")) {
            List<CheckupUserBean> list = JSON.parseArray(joall.getString("data"), CheckupUserBean.class);
            ServiceOrderData.userssize = list.size();
            //不可用的客人 不显示 isAble为2时表示不可用
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIsAble().equals("2")) {
                        list.remove(i);
                        i--;
                    }
                }
            }
            if (list != null && list.size() > 1) {
                // 启动选择体检人fragment
                ServiceOrderData.users = list;
                ServiceOrderData.userinfoEditable = 0;//仅可以选择客人,并且查看个人信息,不可编辑
                ((AppointmentActivity) getActivity()).toStepm1();
                dialog.dismiss();
            } else if (list != null && list.size() == 1) {
                if (list.get(0).getSexId().equals("U")
                        && list.get(0).getCustomerName().equals("")
                        && list.get(0).getCertTypeId().equals("3")
                        && !list.get(0).getCertNumber().equals("")) {
                    // 启动确认体检人信息fragment
                    ServiceOrderData.setCb(list.get(0));
                    ServiceOrderData.cb.setEdit(false);
                    //初始化部分客人信息
                    ServiceOrderData.cb.setMarryId("1");
                    ServiceOrderData.cb.setMarryName("未婚");
                    ServiceOrderData.userinfoEditable = 2;//可编辑部分个人信息
                } else {
                    // 启动确认体检人信息fragment
                    ServiceOrderData.setCb(list.get(0));
                    ServiceOrderData.cb.setEdit(false);
                    ServiceOrderData.userinfoEditable = 0;//仅可以查看个人信息,不可编辑
                }
                ((AppointmentActivity) getActivity()).toStepm2();
                dialog.dismiss();
            } else {
                if (ServiceOrderData.certTypeId.equals("")) {
                    ServiceOrderData.setCb(null);
                    ServiceOrderData.userinfoEditable = 1;//凭证号查询没有对应客人信息,需要新建一个,进入下个页面需要编辑客人信息
                    dialog.dismiss();
                    ((AppointmentActivity) getActivity()).toStepm2();
                } else {
                    dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText("该证件客人不存在");
                }
            }

        } else if (joall.get("errorCode") != null && joall.get("errorCode").toString().equals("1")) {
            dialog.findViewById(R.id.view_editdialog_errormsg).setVisibility(View.VISIBLE);
            ((TextView) dialog.findViewById(R.id.view_editdialog_errormsg)).setText(joall.getString("resultMsg"));
        }
    }

}
