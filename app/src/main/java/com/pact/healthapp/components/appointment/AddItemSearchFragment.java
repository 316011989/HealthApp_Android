package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 体检项筛选
 * Created by wangdong on 2016/1/4.
 */
@SuppressLint("ValidFragment")
public class AddItemSearchFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @ViewInject(R.id.keyword_et)
    private EditText keyword_et;
    //    private String keyword = "";//搜索关键字
    @ViewInject(R.id.cancle_search)
    private TextView cancel;//取消搜索
    @ViewInject(R.id.confirm_search)
    private Button confirm;//确定搜索

    @ViewInject(R.id.individual_pay_rl)
    private RelativeLayout individual_pay_rl;
    @ViewInject(R.id.individual_pay_tv)
    private TextView individual_pay_tv;//个人付费
    @ViewInject(R.id.individual_pay_cb)
    private CheckBox individual_pay_cb;

    @ViewInject(R.id.individual_pay_rll)
    private RelativeLayout individual_pay_rll;//个人付费
//    private boolean isPersonalPay = true;

    @ViewInject(R.id.whole_price)
    private RadioButton whole_price;//全部
    @ViewInject(R.id.zero_price)
    private RadioButton zero_price;//0-50
    @ViewInject(R.id.fifty_price)
    private RadioButton fifty_price;//50-100
    @ViewInject(R.id.one_hundred_price)
    private RadioButton one_hundred_price;//100-150
    @ViewInject(R.id.onepointfive_hundred_price)
    private RadioButton onepointfive_hundred_price;//150-200
    @ViewInject(R.id.two_hundred_price)
    private RadioButton two_hundred_price;//200以上

//    private int startPrice = 0;
//    private int endPrice = 0;

    @ViewInject(R.id.nci_pay_rl)
    private RelativeLayout nci_pay_rl;
    @ViewInject(R.id.nci_pay_tv)
    private TextView nci_pay_tv;//新华付费
    @ViewInject(R.id.nci_pay_cb)
    private CheckBox nci_pay_cb;
//    private boolean isXinhuaPay = true;

    @ViewInject(R.id.company_pay_rl)
    private RelativeLayout company_pay_rl;
    @ViewInject(R.id.company_pay_tv)
    private TextView company_pay_tv;//公司付费
    @ViewInject(R.id.company_pay_cb)
    private CheckBox company_pay_cb;
//    private boolean isCompanyPay = true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_additem_search_layout, null);
        ViewUtils.inject(this, view);

        return view;
    }


    private void initView() {
        keyword_et.setText(ServiceOrderData.keyword);

        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        if (ServiceOrderData.isPersonalPay) {
            individual_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
        } else {
            individual_pay_tv.setTextColor(getResources().getColor(R.color.black));
        }
        individual_pay_cb.setChecked(ServiceOrderData.isPersonalPay);
        individual_pay_rl.setOnClickListener(this);
        individual_pay_cb.setOnCheckedChangeListener(this);
        if (ServiceOrderData.startPrice == 0 &&
                ServiceOrderData.endPrice == 0) {
            whole_price.setChecked(true);
        } else if (ServiceOrderData.startPrice == 50 &&
                ServiceOrderData.endPrice == 0) {
            zero_price.setChecked(true);
        } else if (ServiceOrderData.startPrice == 50 &&
                ServiceOrderData.endPrice == 100) {
            fifty_price.setChecked(true);
        } else if (ServiceOrderData.startPrice == 100 &&
                ServiceOrderData.endPrice == 150) {
            one_hundred_price.setChecked(true);
        } else if (ServiceOrderData.startPrice == 150 &&
                ServiceOrderData.endPrice == 200) {
            onepointfive_hundred_price.setChecked(true);
        } else if (ServiceOrderData.startPrice == 0 &&
                ServiceOrderData.endPrice == 200) {
            two_hundred_price.setChecked(true);
        }
        whole_price.setOnCheckedChangeListener(this);
        zero_price.setOnCheckedChangeListener(this);
        fifty_price.setOnCheckedChangeListener(this);
        one_hundred_price.setOnCheckedChangeListener(this);
        onepointfive_hundred_price.setOnCheckedChangeListener(this);
        two_hundred_price.setOnCheckedChangeListener(this);

        if (ServiceOrderData.isXinhuaPay) {
            nci_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
        } else {
            nci_pay_tv.setTextColor(getResources().getColor(R.color.black));
        }
        nci_pay_cb.setChecked(ServiceOrderData.isXinhuaPay);
        nci_pay_rl.setOnClickListener(this);
        nci_pay_cb.setOnCheckedChangeListener(this);

        if (ServiceOrderData.isCompanyPay) {
            company_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
        } else {
            company_pay_tv.setTextColor(getResources().getColor(R.color.black));
        }
        company_pay_rl.setOnClickListener(this);
        company_pay_cb.setChecked(ServiceOrderData.isCompanyPay);
        company_pay_cb.setOnCheckedChangeListener(this);

        if (ServiceOrderData.keyword.trim().equals("") && !individual_pay_cb.isChecked() && !nci_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
            confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
            confirm.setClickable(false);
        } else {
            confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            confirm.setClickable(true);
        }

        keyword_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ServiceOrderData.keyword = s.toString().trim();
                if (ServiceOrderData.keyword.equals("") &&
                        !individual_pay_cb.isChecked() && !nci_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                    confirm.setClickable(false);
                } else {
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                }
            }
        });

        keyword_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    ServiceOrderData.keyword = keyword_et.getText().toString().trim();
                    if (ServiceOrderData.keyword.equals("") &&
                            !individual_pay_cb.isChecked() && !nci_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
                    } else {
                        getAttachPackageList();
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        ServiceOrderData.keyword = keyword_et.getText().toString().trim();
        switch (v.getId()) {
            case R.id.cancle_search://取消
                //判断隐藏软键盘是否弹出
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive(keyword_et)) {
                    try {
                        getView().requestFocus();//强制获取焦点，不然getActivity().getCurrentFocus().getWindowToken()会报错
                        //隐藏软键盘
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        imm.restartInput(keyword_et);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                ((AppointmentActivity) getActivity()).toStep5();
                break;
            case R.id.confirm_search://确定搜索
                getAttachPackageList();
                break;
            case R.id.individual_pay_rl://个人付费
                if (individual_pay_cb.isChecked()) {
                    individual_pay_cb.setChecked(false);
                    if (ServiceOrderData.keyword.equals("") && !nci_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
                        confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        confirm.setClickable(false);
                    }
                } else {
                    individual_pay_cb.setChecked(true);
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                }
                break;
            case R.id.nci_pay_rl://新华付费
                if (nci_pay_cb.isChecked()) {
                    nci_pay_tv.setTextColor(getResources().getColor(R.color.titleColor));
                    nci_pay_cb.setChecked(false);
                    if (ServiceOrderData.keyword.equals("") && !individual_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
                        confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        confirm.setClickable(false);
                    }
                } else {
                    nci_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
                    nci_pay_cb.setChecked(true);
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                }
                break;
            case R.id.company_pay_rl://企业付费
                if (company_pay_cb.isChecked()) {
                    company_pay_tv.setTextColor(getResources().getColor(R.color.titleColor));
                    company_pay_cb.setChecked(false);
                    if (ServiceOrderData.keyword.equals("") && !individual_pay_cb.isChecked() && !nci_pay_cb.isChecked()) {
                        confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        confirm.setClickable(false);
                    }
                } else {
                    company_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
                    company_pay_cb.setChecked(true);
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ServiceOrderData.keyword = keyword_et.getText().toString().trim();
        switch (buttonView.getId()) {
            case R.id.individual_pay_cb://个人付费
                if (isChecked) {
                    individual_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
                    individual_pay_rll.setVisibility(View.VISIBLE);
                    ServiceOrderData.isPersonalPay = true;
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                } else {
                    individual_pay_tv.setTextColor(getResources().getColor(R.color.titleColor));
                    individual_pay_rll.setVisibility(View.GONE);
                    ServiceOrderData.isPersonalPay = false;
                    ServiceOrderData.startPrice = 0;
                    ServiceOrderData.endPrice = 0;
                    if (ServiceOrderData.keyword.equals("") && !company_pay_cb.isChecked() && !nci_pay_cb.isChecked()) {
                        confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        confirm.setClickable(false);
                    }
                }
                break;
            case R.id.whole_price://全部
                if (isChecked) {
                    ServiceOrderData.startPrice = 0;
                    ServiceOrderData.endPrice = 0;
                }
                break;
            case R.id.zero_price://0-50
                if (isChecked) {
                    ServiceOrderData.startPrice = 50;
                    ServiceOrderData.endPrice = 0;
                }
                break;
            case R.id.fifty_price://50-100
                if (isChecked) {
                    ServiceOrderData.startPrice = 50;
                    ServiceOrderData.endPrice = 100;
                }
                break;
            case R.id.one_hundred_price://100-150
                if (isChecked) {
                    ServiceOrderData.startPrice = 100;
                    ServiceOrderData.endPrice = 150;
                }
                break;
            case R.id.onepointfive_hundred_price://150-200
                if (isChecked) {
                    ServiceOrderData.startPrice = 150;
                    ServiceOrderData.endPrice = 200;
                }
                break;
            case R.id.two_hundred_price://200以上
                if (isChecked) {
                    ServiceOrderData.startPrice = 0;
                    ServiceOrderData.endPrice = 200;
                }
                break;
            case R.id.nci_pay_cb://新华付费
                if (isChecked) {
                    nci_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
                    ServiceOrderData.isXinhuaPay = true;
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                } else {
                    nci_pay_tv.setTextColor(getResources().getColor(R.color.titleColor));
                    ServiceOrderData.isXinhuaPay = false;
                    if (ServiceOrderData.keyword.equals("") && !individual_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
                        confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        confirm.setClickable(false);
                    }
                }
                break;
            case R.id.company_pay_cb://企业付费
                if (isChecked) {
                    company_pay_tv.setTextColor(getResources().getColor(R.color.green_common));
                    ServiceOrderData.isCompanyPay = true;
                    confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    confirm.setClickable(true);
                } else {
                    company_pay_tv.setTextColor(getResources().getColor(R.color.titleColor));
                    ServiceOrderData.isCompanyPay = false;
                    if (ServiceOrderData.keyword.equals("") && !nci_pay_cb.isChecked() && !individual_pay_cb.isChecked()) {
                        confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        confirm.setClickable(false);
                    }
                }
                break;
        }
    }

    /**
     * 筛选体检项列表
     */
    private void getAttachPackageList() {
        String bizId = "000";
        String serviceName = "getAttachPackageListBook";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("voucherId", ServiceOrderData.vb.getVoucherId());
        map.put("groupPackId", ServiceOrderData.pb.getGroupPackId());
        map.put("orgId", ServiceOrderData.ob.getOrgId());
        map.put("orgCode", ServiceOrderData.ob.getOrgCode());
        map.put("sexId", ServiceOrderData.cb.getSexId());
        map.put("marryId", ServiceOrderData.cb.getMarryId());
        map.put("isExamination", "Y");
        //获取体检项列表  新加参数
        map.put("isSearch", "Y");      //Y | N
        map.put("keyWord", ServiceOrderData.keyword);
        /**
         * 是否企业付费、个人付费、新华付费
         * boolean 类型   true|false
         */
        map.put("isCompanyPay", ServiceOrderData.isCompanyPay);
        map.put("isXinhuaPay", ServiceOrderData.isXinhuaPay);
        map.put("isPersonalPay", ServiceOrderData.isPersonalPay);
        /**
         * 价格 int型
         * 开始价格不为0，结束价格为0：表示筛选小于开始价格的产品
         * 开始价格为0，结束价格不为0：表示筛选大于结束价格的产品
         * 开始价格不为0，结束价格不为0：表示筛选大于开始价格且小于结束价格的产品
         */
        map.put("startPrice", ServiceOrderData.startPrice);
        map.put("endPrice", ServiceOrderData.endPrice);
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
                                Log.e("筛选体检项列表", result);
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
        JSONObject obj = JSON.parseObject(result);
        AddItemBean bean = new AddItemBean();
        List<AddItemBean> parents = new ArrayList<AddItemBean>();
        JSONArray array = obj.getJSONArray("products");//单项
        if (array != null && array.size() > 0) {
            List<AddItemBean.PackageDatasEntity> list = JSON.parseArray(array.toString(), AddItemBean.PackageDatasEntity.class);
            bean.setPackageName("体检项");
            bean.setPriceMarket("0");
            bean.setPriceSell("0");
            bean.setAccountType("3");
            bean.setMust(false);
            bean.setType("P");
            bean.setPackageDatas(list);
            parents.add(bean);
        }

        JSONArray datas = obj.getJSONArray("datas");//体检项包
        if (datas != null && datas.size() > 0) {
            List<AddItemBean> packages = JSON.parseArray(datas.toString(), AddItemBean.class);
            parents.addAll(packages);
        }
        ServiceOrderData.allAddItems = parents;
        ((AppointmentActivity) getActivity()).toStep5();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
//        ServiceOrderData.keyword = keyword_et.getText().toString().trim();
//        if (ServiceOrderData.keyword.equals("") && !individual_pay_cb.isChecked() && !nci_pay_cb.isChecked() && !company_pay_cb.isChecked()) {
//            confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
//            confirm.setClickable(false);
//        } else {
//            confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
//            confirm.setClickable(true);
//        }

    }
}
