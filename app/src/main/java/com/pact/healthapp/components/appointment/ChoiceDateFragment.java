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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.MultipleRadioGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 时间
 */
@SuppressLint("ValidFragment")
public class ChoiceDateFragment extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private View view;
    @ViewInject(R.id.chocie_date_rg)
    private RadioGroup chocie_date_rg;//年月
    @ViewInject(R.id.day)
    private MyGridView dayGridView;//日
    @ViewInject(R.id.time)
    private MyGridView timeGridView;//时分
    @ViewInject(R.id.appointment_makesure_next)
    private Button appointment_makesure_next;//
    private Calendar calendar;
    private int validDay;//可预约天数
    private String choicedDay;//选中日
    private String choicedMonth;//选中月
    private ChoiceDateBean choicedTime;

    //排期数据(包含整月的所有有排期的日及该日所有时段),可以按照日期自动排序的map
    private Map<String, List<ChoiceDateBean>> ALLtimes = new TreeMap<String, List<ChoiceDateBean>>(
            new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    try {
                        Date d1 = sdf.parse(lhs);
                        Date d2 = sdf.parse(rhs);
                        if (d1.after(d2))
                            return 1;
                        else if (d1.before(d2))
                            return -1;
                        else
                            return 0;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });

    private ChoiceDateTimesAdapter timeAdapter;
    private ChoiceDateDaysAdapter dayAdapter;

    @ViewInject(R.id.serviceorder_changedate_ll)
    private LinearLayout serviceorder_changedate_ll;
    @ViewInject(R.id.serviceorder_changedate_tv)
    private TextView serviceorder_changedate_tv;

    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(context, R.layout.appointment_choicedate_layout, null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("时间");

        if (ServiceOrderData.operation != null
                && ServiceOrderData.operation.equals("changedate")) {
            serviceorder_changedate_ll.setVisibility(View.VISIBLE);
            serviceorder_changedate_tv.setText("原预约日期" +
                    CommonUtil.convertTime1(ServiceOrderData.orderDetail.getAppointmentServiceDate(),
                            ServiceOrderData.orderDetail.getAppointmentStarthour(), ServiceOrderData.orderDetail.getAppointmentStartminute(),
                            ServiceOrderData.orderDetail.getAppointmentEndhour(), ServiceOrderData.orderDetail.getAppointmentEndminute()));
        } else {
            serviceorder_changedate_ll.setVisibility(View.GONE);
        }


        //已有保存到单例中的排期数据,并且选中月日时都有
        if (ServiceOrderData.month_days_times != null && ServiceOrderData.month_days_times.size() > 0) {
            choicedMonth = ServiceOrderData.choicedMonth;
            ALLtimes.putAll(ServiceOrderData.month_days_times);
            setMonths(ServiceOrderData.startyear, ServiceOrderData.startmonth, ServiceOrderData.endyear, ServiceOrderData.endmonth);
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
            try {
                if (ServiceOrderData.choicedDay != null) {
                    choicedDay = ServiceOrderData.choicedDay;
                    ServiceOrderData.list = ALLtimes.get(choicedDay);
                }
                if (ServiceOrderData.choicedMonth != null) {
                    Date date = sm.parse(ServiceOrderData.choicedMonth);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    setDays(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                } else {
                    setDays(ServiceOrderData.startyear, ServiceOrderData.startmonth);
                }
                setTimes();
                if (ServiceOrderData.choicedTime != null) {
                    timeAdapter.setChoicedTime(ServiceOrderData.choicedTime);
                    timeAdapter.notifyDataSetChanged();
                } else if (choicedTime != null) {
                    timeAdapter.setChoicedTime(choicedTime);
                    timeAdapter.notifyDataSetChanged();
                    appointment_makesure_next.setEnabled(true);
                    appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //单例没有,有当前fragment的变量,
            if (ALLtimes.size() > 0 &&
                    ServiceOrderData.startyear != 0 && ServiceOrderData.startmonth != 0) {
                setMonths(ServiceOrderData.startyear, ServiceOrderData.startmonth, ServiceOrderData.endyear, ServiceOrderData.endmonth);
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
                try {
                    if (ServiceOrderData.choicedDay != null) {
                        choicedDay = ServiceOrderData.choicedDay;
                        ServiceOrderData.list = ALLtimes.get(choicedDay);
                    }
                    if (choicedMonth != null) {
                        Date date = sm.parse(choicedMonth);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        setDays(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                    } else {
                        setDays(ServiceOrderData.startyear, ServiceOrderData.startmonth);
                    }
                    setTimes();
                    if (ServiceOrderData.choicedTime != null) {
                        timeAdapter.setChoicedTime(ServiceOrderData.choicedTime);
                        timeAdapter.notifyDataSetChanged();
                    } else if (choicedTime != null) {
                        timeAdapter.setChoicedTime(choicedTime);
                        timeAdapter.notifyDataSetChanged();
                        appointment_makesure_next.setEnabled(true);
                        appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                choicedMonth = null;
                ALLtimes.clear();
                //获取当前某年某月排期数据
                calendar = Calendar.getInstance();
                ServiceOrderData.startyear = calendar.get(Calendar.YEAR);
                ServiceOrderData.startmonth = calendar.get(Calendar.MONTH) + 1;
                if (ServiceOrderData.orderDetail != null) {//改期功能进入
                    getData1(ServiceOrderData.startyear, ServiceOrderData.startmonth, false);
                } else {//正常预约进入
                    getData(ServiceOrderData.startyear, ServiceOrderData.startmonth, false);//获取排期数据
                }
                appointment_makesure_next.setEnabled(false);
                appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
            }
        }

        appointment_makesure_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choicedTime != null) {
                    ServiceOrderData.month_days_times = new TreeMap<String, List<ChoiceDateBean>>();
                    ServiceOrderData.month_days_times.putAll(ALLtimes);
                    ServiceOrderData.choicedMonth = choicedMonth;
                    ServiceOrderData.choicedDay = choicedDay;
                    if (ServiceOrderData.operation != null
                            && ServiceOrderData.operation.equals("changedate")) {//服务单改期功能进入
                        String time = CommonUtil.convertTime(choicedTime.getStartDate(),
                                choicedTime.getEndDate());
                        String message = "预约改期为：" + time.substring(0, 10) + "\r\n"
                                + "时间段：" + time.substring(time.length() - 11, time.length());
                        dialog = new CommonDialog(context, 2, "改期", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ServiceOrderData.setChoicedTime(choicedTime);
                                ((AppointmentActivity) getActivity()).toStep8();
                                dialog.cancel();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        }, null, message);
                        dialog.show();
                    } else if (ServiceOrderData.operation != null
                            && ServiceOrderData.operation.equals("order")) {//服务单预约功能进入
                        ServiceOrderData.setChoicedTime(choicedTime);
                        ((AppointmentActivity) getActivity()).toStep8();
                    } else {//正常预约进入
                        ServiceOrderData.setChoicedTime(choicedTime);
                        ((AppointmentActivity) getActivity()).toStep7();
                    }
                }
            }
        });
        return view;
    }

    /**
     * 获取某年某月排期数据
     *
     * @param choicemonth 选择月份的请求,不是第一次进入
     */
    private void getData(int year, int month, final boolean choicemonth) {
        String bizId = "000";
        String serviceName = "getAppointmentDateBook";
        JSONObject jo = new JSONObject();
        jo.put("customerId", sp.getLoginInfo(context, "customerId"));
        jo.put("token", sp.getLoginInfo(context, "token"));
        jo.put("voucherId", ServiceOrderData.vb.getVoucherId());
        jo.put("packageId", ServiceOrderData.pb.getPackageId());
        jo.put("serviceBeginDate", ServiceOrderData.vb.getServiceData().get(0).getServiceBeginDate());
        jo.put("serviceEndDate", ServiceOrderData.vb.getServiceData().get(0).getServiceEndDate());
        jo.put("year", year);
        jo.put("month", month);
        jo.put("orgId", ServiceOrderData.ob.getOrgId());
        jo.put("type", "ios");
        ServiceEngin.Request(context, bizId, serviceName, jo.toJSONString(),
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0);
                        try {
                            String result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            Log.e("获取预约排期", result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {
                                if (choicemonth) {
                                    parseJson1(result);
                                } else {
                                    parseJson(result);
                                }
                            }
                        } catch (Exception e) {
                            setView();
                            if (ServiceOrderData.startyear == ServiceOrderData.endyear && ServiceOrderData.startmonth == ServiceOrderData.endmonth) {
                                dialog = new CommonDialog(context, 2, "重新选择机构", "退出预约", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        ((AppointmentActivity) getActivity()).toStep4();
                                        dialog.cancel();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                }, null, "对不起，没有可预约的时间段");
                                dialog.show();
                            }
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 解析排期数据
     * 可获取总排期天数(从今天到最后一个有排期数据的日)
     *
     * @param result
     */
    private void parseJson(String result) {
        JSONObject obj = JSON.parseObject(result);
        //可预约天数(今天到最后有排期数据的那天)
        validDay = obj.getInteger("validDay");
        ServiceOrderData.endyear = ServiceOrderData.startyear;
        ServiceOrderData.endmonth = ServiceOrderData.startmonth;
        if (validDay == 0) {//无可用排期数据
            dialog = new CommonDialog(context, 2, "重新选择机构", "退出预约", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ((AppointmentActivity) getActivity()).toStep4();
                    dialog.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            }, null, "对不起，没有可预约的时间段");
            dialog.show();
            return;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, validDay - 1);
            ServiceOrderData.endyear = calendar.get(Calendar.YEAR);
            ServiceOrderData.endmonth = calendar.get(Calendar.MONTH) + 1;
        }
        //当前月没有可选日期
        if (obj.getString("data").equals("[]")) {
            getNextData();
            return;
        }

        JSONObject days = obj.getJSONObject("data");
        //取出数据中的日期key
        Set<String> set = days.keySet();
        //当前月有日期可选
        if (set != null && set.size() > 0) {
            ALLtimes.clear();
            boolean monthUseable = false;//整月不可用
            //用key在取出某天的排期数据
            for (String key : set) {
                boolean dayUseable = false;//整天不可用
                List<ChoiceDateBean> times = new ArrayList<ChoiceDateBean>();
                ChoiceDateBean b;
                JSONArray day = days.getJSONArray(key);
                for (int i = 0; i < day.size(); i++) {
                    b = JSON.toJavaObject(day.getJSONObject(i),
                            ChoiceDateBean.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    try {
                        Date date = dateFormat.parse(b.getEndDate());
                        if (date.before(new Date()))
                            b.setPeopleAvailableNo(0);
                        times.add(b);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //循环中添加此判断,只要有一个是未排满的,就返回false
                    if (b.getPeopleAvailableNo() != 0 && (ServiceOrderData.orderDetail == null || !ServiceOrderData.orderDetail.getAppointmentServiceDate().equals(key))) {
                        monthUseable = true;//整月改为可用
                        dayUseable = true;//整天改为可用
                    }
                }
                if (dayUseable) {//!全天排满当天不可用
                    //!改期时已预约日期不可用
                    if (ServiceOrderData.orderDetail == null || !ServiceOrderData.orderDetail.getAppointmentServiceDate().equals(key)) {
                        ALLtimes.put(key, times);
                    }
                }
            }
            //取出数据中的日期key
            Set<String> alltimesset = ALLtimes.keySet();
            //排序后循环取出第一个可用日期(非今日)
            boolean isFirstUseableDate = true;
            for (String key : alltimesset) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (isFirstUseableDate && !simpleDateFormat.format(new Date()).equals(key)) {
                    choicedDay = key;
                    isFirstUseableDate = false;//取到之后不再循环
                }
            }

            //如果当前月全部排满,直接请求下个月
            if (!monthUseable) {
                getNextData();
                return;
            }
            //初始第一个可选时段的日期的数据
            if (choicedDay != null)
                ServiceOrderData.list = ALLtimes.get(choicedDay);
            if (choicedMonth == null) {
                if (ServiceOrderData.startmonth < 10)
                    choicedMonth = ServiceOrderData.startyear + "-0" + ServiceOrderData.startmonth;
                else
                    choicedMonth = ServiceOrderData.startyear + "-" + ServiceOrderData.startmonth;
            }
            setView();
        } else {//当前月没有日期可选,请求下个月的
            getNextData();
        }
    }

    /**
     * 请求下个月排期数据
     */
    private void getNextData() {
        if (ServiceOrderData.startmonth != 12) {//当前不是12月,下个月为year年month+1月
            ServiceOrderData.startmonth = ServiceOrderData.startmonth + 1;
        } else {//当前是12月,下个月为(year+1)年1月
            ServiceOrderData.startyear = ServiceOrderData.startyear + 1;
            ServiceOrderData.startmonth = 1;
        }
        //计算出可预约的最后一个月
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, validDay - 1);
        //要请求的月份在最后一个月之内
        if (calendar.get(Calendar.YEAR) > ServiceOrderData.startyear ||
                (calendar.get(Calendar.YEAR) == ServiceOrderData.startyear && calendar.get(Calendar.MONTH) + 1 >= ServiceOrderData.startmonth)) {
            if (ServiceOrderData.orderDetail != null) {
                //改期功能进入
                getData1(ServiceOrderData.startyear, ServiceOrderData.startmonth, false);
            } else {
                //正常预约进入
                getData(ServiceOrderData.startyear, ServiceOrderData.startmonth, false);//获取排期数据
            }
        } else {
            dialog = new CommonDialog(context, 2, "重新选择机构", "退出预约", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ((AppointmentActivity) getActivity()).toStep4();
                    dialog.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            }, null, "对不起，没有可预约的时间段");
            dialog.show();
            return;
        }
    }

    /**
     * 根据请求到的数据构造页面
     */
    private void setView() {
        setMonths(ServiceOrderData.startyear, ServiceOrderData.startmonth, ServiceOrderData.endyear, ServiceOrderData.endmonth);
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = sm.parse(choicedMonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            setDays(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setTimes();
    }

    /**
     * 构造可选月份
     *
     * @param startyear
     * @param startmonth
     * @param endyear
     * @param endmonth
     */
    private void setMonths(final int startyear, int startmonth, int endyear, int endmonth) {
        chocie_date_rg.removeAllViews();
        if (startyear < endyear) {//跨年
            for (int i = startmonth; i <= 12; i++) {//先把当年的年月加载出来
                final RadioButton rb = new RadioButton(context);
                rb.setId(0x19890731 + i);
                rb.setButtonDrawable(R.drawable.appointment_choicedate_selector);
                rb.setTextColor(getResources().getColorStateList(R.color.choicedate_rb_color_selector));
                rb.setTextSize(16);
                rb.setPadding(10, 10, 10, 10);
                if ((startmonth) < 10) {
                    rb.setText(startyear + "-0" + i);
                } else {
                    rb.setText(startyear + "-" + i);
                }
                final int finalI = i;
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (choicedMonth == null
                                || !choicedMonth.equals(rb.getText().toString())) {
                            choicedDay = null;
                            choicedMonth = rb.getText().toString();
                            choicedTime = null;
                            ServiceOrderData.list = null;
                            ServiceOrderData.choicedTime = null;
                            if (ServiceOrderData.orderDetail == null) {
                                getData(startyear, finalI, true);
                            } else {
                                getData1(startyear, finalI, true);
                            }
                            appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        }
                    }
                });
                if (choicedMonth != null && choicedMonth.equals(rb.getText().toString())) {
                    rb.setSelected(true);
                } else {
                    rb.setSelected(false);
                }

                if (i > startmonth) {
                    View v = new View(context);
                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(1, MultipleRadioGroup.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0, 10, 0, 10);
                    v.setLayoutParams(lp);
                    v.setBackgroundColor(getResources().getColor(R.color.gray));
                    chocie_date_rg.addView(v);
                }

                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 0, 20, 0);
                chocie_date_rg.addView(rb, layoutParams);
            }
            for (int i = 1; i <= endmonth; i++) {//再把次年的加载出来
                final RadioButton rb = new RadioButton(context);
                rb.setId(0x19890831 + i);
                rb.setButtonDrawable(R.drawable.appointment_choicedate_selector);
                rb.setTextColor(getResources().getColorStateList(R.color.choicedate_rb_color_selector));
                rb.setTextSize(16);
                rb.setPadding(10, 10, 10, 10);
                if ((endmonth) < 10) {
                    rb.setText(endyear + "-0" + i);
                } else {
                    rb.setText(endyear + "-" + i);
                }
                final int finalI = i;
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (choicedMonth == null
                                || !choicedMonth.equals(rb.getText().toString())) {
                            choicedDay = null;
                            choicedMonth = rb.getText().toString();
                            ServiceOrderData.list = null;
                            if (ServiceOrderData.orderDetail == null) {
                                getData(startyear, finalI, true);
                            } else {
                                getData1(startyear, finalI, true);
                            }
                            appointment_makesure_next.setEnabled(false);
                            appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        }
                    }
                });
                View v = new View(context);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(1, MultipleRadioGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, 10, 0, 10);
                v.setLayoutParams(lp);
                v.setBackgroundColor(getResources().getColor(R.color.gray));
                chocie_date_rg.addView(v);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 0, 20, 0);
                chocie_date_rg.addView(rb, layoutParams);
            }
        } else if (startyear == endyear) {//当年
            for (int i = startmonth; i <= endmonth; i++) {
                final RadioButton rb = new RadioButton(context);
                rb.setId(0x19890731 + i);
                rb.setButtonDrawable(R.drawable.appointment_choicedate_selector);
                rb.setTextColor(getResources().getColorStateList(R.color.choicedate_rb_color_selector));
                rb.setTextSize(16);
                rb.setPadding(10, 10, 10, 10);
                if ((startmonth) < 10) {
                    rb.setText(startyear + "-0" + i);
                } else {
                    rb.setText(startyear + "-" + i);
                }
                final int finalI = i;
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (choicedMonth == null
                                || !choicedMonth.equals(rb.getText().toString())) {
                            choicedDay = null;
                            choicedMonth = rb.getText().toString();
                            ServiceOrderData.list = null;
                            if (ServiceOrderData.orderDetail == null) {
                                getData(startyear, finalI, true);
                            } else {
                                getData1(startyear, finalI, true);
                            }
                            appointment_makesure_next.setEnabled(false);
                            appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                        }
                    }
                });
                if (choicedMonth != null && choicedMonth.equals(rb.getText().toString())) {
                    rb.setSelected(true);
                } else {
                    rb.setSelected(false);
                }
                if (i > startmonth) {
                    View v = new View(context);
                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(1, MultipleRadioGroup.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0, 10, 0, 10);
                    v.setLayoutParams(lp);
                    v.setBackgroundColor(getResources().getColor(R.color.gray));
                    chocie_date_rg.addView(v);
                }
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 0, 20, 0);
                chocie_date_rg.addView(rb, layoutParams);
            }
        }
    }

    /**
     * 构造某年某月下所有天的gridview
     * 1881174等01
     *
     * @param year  年
     * @param month 月
     */
    private void setDays(int year, int month) {
        dayAdapter = new ChoiceDateDaysAdapter(context, ALLtimes, year, month);
        dayAdapter.setchoicedDay(choicedDay);
        dayGridView.setAdapter(dayAdapter);
        dayGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dayAdapter.getItem(position) != null) {//有排期的日期才能点击
                    if (choicedDay == null
                            || !choicedDay.equals(dayAdapter.getItem(position))) {//当前点击的天与上次选中的天不同
                        ServiceOrderData.list = ALLtimes.get(dayAdapter.getItem(position));
                        choicedDay = dayAdapter.getItem(position).toString();
                        dayAdapter.setchoicedDay(choicedDay);
                        dayAdapter.notifyDataSetChanged();
                        timeAdapter = new ChoiceDateTimesAdapter(context, ServiceOrderData.list);
                        timeGridView.setAdapter(timeAdapter);
                        appointment_makesure_next.setEnabled(false);
                        appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
                    }
                }
            }
        });
    }

    /**
     * 构造某年某月某日下所有时间段的gridview
     */
    private void setTimes() {
        timeAdapter = new ChoiceDateTimesAdapter(context, ServiceOrderData.list);
        timeGridView.setAdapter(timeAdapter);
        timeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choicedTime = (ChoiceDateBean) timeAdapter.getItem(position);
                timeAdapter.setChoicedTime(choicedTime);
                timeAdapter.notifyDataSetChanged();
                appointment_makesure_next.setEnabled(true);
                appointment_makesure_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            }
        });
    }


    /**
     * 解析排期数据
     * 可获取总排期天数(从今天到最后一个有排期数据的日)
     *
     * @param result
     */
    private void parseJson1(String result) {
        JSONObject obj = JSON.parseObject(result);
        JSONObject days = obj.getJSONObject("data");
        //取出数据中的日期key
        Set<String> set = days.keySet();
        //当前月有日期可选
        if (set != null && set.size() > 0) {
            ALLtimes.clear();
            //用key在取出某天的排期数据
            for (String key : set) {
                boolean dayUseable = false;//整天不可用
                List<ChoiceDateBean> times = new ArrayList<ChoiceDateBean>();
                ChoiceDateBean b;
                JSONArray day = days.getJSONArray(key);
                for (int i = 0; i < day.size(); i++) {
                    b = JSON.toJavaObject(day.getJSONObject(i),
                            ChoiceDateBean.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    try {
                        Date date = dateFormat.parse(b.getEndDate());
                        if (date.before(new Date()))
                            b.setPeopleAvailableNo(0);
                        times.add(b);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //循环中添加此判断,只要有一个是未排满的,就返回false
                    if (b.getPeopleAvailableNo() != 0) {
                        dayUseable = true;//整天改为可用
                    }
                }

                if (dayUseable) {//!全天排满当天不可用
                    //!改期时已预约日期不可用
                    if (ServiceOrderData.orderDetail == null || !ServiceOrderData.orderDetail.getAppointmentServiceDate().equals(key)) {
                        ALLtimes.put(key, times);
                    }
                }
            }

//            //成功获取到有效数据,默认选中这个月
//            ServiceOrderData.month_days_times = ALLtimes;
            setMonths(ServiceOrderData.startyear, ServiceOrderData.startmonth, ServiceOrderData.endyear, ServiceOrderData.endmonth);
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
            try {
                if (choicedMonth != null) {
                    Date date = sm.parse(choicedMonth);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    setDays(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                } else {
                    setDays(ServiceOrderData.startyear, ServiceOrderData.startmonth);
                }
                if (choicedDay != null) {
                    ServiceOrderData.list = ALLtimes.get(choicedDay);
                }
                setTimes();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 改期获取预约排期
     */
    private void getData1(int year, int month, final boolean choicemonth) {
        String bizId = "000";
        String serviceName = "getAppointmentDate2Book";
        Map<String, String> map = new HashMap<String, String>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("serviceOrderId", ServiceOrderData.orderDetail.getServiceOrderId());
        map.put("year", year + "");
        map.put("month", month + "");
        map.put("type", "ios");
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
                                if (choicemonth) {
                                    parseJson1(result);
                                } else {
                                    parseJson(result);
                                }
//                                parseJson(result);
                                Log.e("获取预约排期", result);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }
}
