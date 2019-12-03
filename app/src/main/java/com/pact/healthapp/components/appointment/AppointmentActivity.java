/**
 *
 */
package com.pact.healthapp.components.appointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.serviceorder.OrderData;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.view.CommonDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyl
 */
public class AppointmentActivity extends BaseFragmentActivity {

    private CheckupUserFragment f1;
    private VoucherFragment f2;
    private PackageListFragment f3;
    private ChoiceOrgFragment f4;
    private AddItemFragment f5;
    private ChoiceDateFragment f6;
    private PreviewFragment f7;
    private Preview2Fragment f8;
    private ChoiceUserFragment m1;
    private MakeSureUserInfo m2;
    private MakeSureUserInfo2 m3;
    private PackageDetailFragment m4;
    private AddItemSearchFragment m5;
    private AddItemChoicedFragment m6;
    private AppointmentSuccessFragment m7;
    private PayFragment m8;

    private int step_now = 0;
    private GestureDetector gestureDetector = null;
    private List<BaseFragment> fragments;//所有页面集合,用于循环隐藏所有fragment(之后展示需要的fragment)
    public static int editType;//编辑个人信息fragment时判断编辑哪一类,1性别,2证件类型,3婚否
    private CommonDialog dialog;

    public HorizontalScrollView steps_scrollview;//横向滚动进度条
    private View stepsview;//进度条view
    private Button appointment_rb_step1;//进度条中按钮
    private Button appointment_rb_step2;
    private Button appointment_rb_step3;
    private Button appointment_rb_step4;
    private Button appointment_rb_step5;
    private Button appointment_rb_step6;
    private Button appointment_rb_step7;
    private ImageView appointment_mark_step1;//进度条当前标识
    private ImageView appointment_mark_step2;
    private ImageView appointment_mark_step3;
    private ImageView appointment_mark_step4;
    private ImageView appointment_mark_step5;
    private ImageView appointment_mark_step6;
    private ImageView appointment_mark_step7;
    private List<Button> steps_selected = new ArrayList<Button>();
    private List<ImageView> steps_markd = new ArrayList<ImageView>();

    public String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContenierView(1);
        setBackType(0);
        setTitle("预约");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setBackgroundResource(R.mipmap.appointment_btn_exit);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new CommonDialog(context, 2, "退出", "否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceOrderData.clearData();
                        dialog.dismiss();
                        finish();
                    }
                }, null, null, "退出预约？");
                dialog.show();
            }
        });

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ServiceOrderData.orderDetail != null) {//改期中的返回
//                    ServiceOrderData.clearData();
//                    finish();
//                } else {//预约流程中的返回
                back();
//                }
            }
        });
        myApplication.addActivity(this);
        initStepsview();
        steps_scrollview = (HorizontalScrollView) findViewById(R.id.appointment_steps);
        steps_scrollview.addView(stepsview);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int moveDistanceX = (int) (e1.getX() - e2.getX());
                int moveDistanceY = (int) (e1.getY() - e2.getY());
                //横向移动比纵向的绝对值大,说明是横向滑动
                if (Math.abs(moveDistanceX) > Math.abs(moveDistanceY)) {
                    //横向移动距离大于屏幕1/4,以防误操作
                    if (Math.abs(moveDistanceX) > dm.widthPixels / 4) {
                        //从屏幕边缘1/10以内距离滑动进入屏幕
                        if (e1.getX() > dm.widthPixels * 0.9 || e1.getX() < dm.widthPixels * 0.1) {
                            Log.e("onFling", "有效滑动,横向距离为" + moveDistanceX);
                            if (moveDistanceX < 0) {//向右滑
                                toStep(true);
                            } else if (moveDistanceX > 0) {//向左滑
                                toStep(false);
                            }
                        }
                    }
                }
                return false;
            }
        });
        fragments = new ArrayList<BaseFragment>();

        Intent intent = getIntent();
        String operation = intent.getStringExtra("operation");
        ServiceOrderData.operation = operation;

        if (operation == null) {
            ServiceOrderData.clearData();
            toStep1();
        } else {
            if (operation.equals("voucher")) {//我的体检账户选择凭证进行预约
                toStepm2();
            } else if (operation.equals("changedate")) {//我的服务单 改期
                toStep6();
            } else if (operation.equals("order")) {//我的服务单 预约
                toStep6();
            }
        }

    }

    /**
     * 初始化进度控制view
     */
    private void initStepsview() {
        stepsview = LayoutInflater.from(context).inflate(
                R.layout.appointment_stepsview, null);
        appointment_rb_step1 = (Button) stepsview.findViewById(R.id.appointment_rb_step1);
        appointment_rb_step2 = (Button) stepsview.findViewById(R.id.appointment_rb_step2);
        appointment_rb_step3 = (Button) stepsview.findViewById(R.id.appointment_rb_step3);
        appointment_rb_step4 = (Button) stepsview.findViewById(R.id.appointment_rb_step4);
        appointment_rb_step5 = (Button) stepsview.findViewById(R.id.appointment_rb_step5);
        appointment_rb_step6 = (Button) stepsview.findViewById(R.id.appointment_rb_step6);
        appointment_rb_step7 = (Button) stepsview.findViewById(R.id.appointment_rb_step7);
        appointment_mark_step1 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step1);
        appointment_mark_step2 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step2);
        appointment_mark_step3 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step3);
        appointment_mark_step4 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step4);
        appointment_mark_step5 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step5);
        appointment_mark_step6 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step6);
        appointment_mark_step7 = (ImageView) stepsview.findViewById(R.id.appointment_mark_step7);
        steps_selected.add(appointment_rb_step1);
        steps_selected.add(appointment_rb_step2);
        steps_selected.add(appointment_rb_step3);
        steps_selected.add(appointment_rb_step4);
        steps_selected.add(appointment_rb_step5);
        steps_selected.add(appointment_rb_step6);
        steps_selected.add(appointment_rb_step7);
        steps_markd.add(appointment_mark_step1);
        steps_markd.add(appointment_mark_step2);
        steps_markd.add(appointment_mark_step3);
        steps_markd.add(appointment_mark_step4);
        steps_markd.add(appointment_mark_step5);
        steps_markd.add(appointment_mark_step6);
        steps_markd.add(appointment_mark_step7);
        appointment_rb_step1.setOnClickListener(rbclickListener);
        appointment_rb_step2.setOnClickListener(rbclickListener);
        appointment_rb_step3.setOnClickListener(rbclickListener);
        appointment_rb_step4.setOnClickListener(rbclickListener);
        appointment_rb_step5.setOnClickListener(rbclickListener);
        appointment_rb_step6.setOnClickListener(rbclickListener);
        appointment_rb_step7.setOnClickListener(rbclickListener);
    }

    View.OnClickListener rbclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.appointment_rb_step1:
                    if (ServiceOrderData.certTypeId != null && ServiceOrderData.certNumber != null) {
                        ServiceOrderData.cb.setEdit(false);
                        toStepm2();//确认体检人信息页面
                    }
                    break;
                case R.id.appointment_rb_step2:
                    if (ServiceOrderData.vouchers != null) {//进入凭证列表页面,需判断是否有凭证列表数据
                        toStep2();
                    }
                    break;
                case R.id.appointment_rb_step3:
                    if (ServiceOrderData.vb != null) {//进入套餐列表页面,需判断是否已选凭证对象进行预约
                        toStep3();
                    }
                    break;
                case R.id.appointment_rb_step4:
                    if (ServiceOrderData.pb != null) {//进入机构列表页面,需判断是否已选套餐进行预约
                        toStep4();
                    }
                    break;
                case R.id.appointment_rb_step5:
                    if (ServiceOrderData.ob != null) {//进入体检项列表页面,需判断是否已选机构进行预约
                        toStep5();
                    }
                    break;
                case R.id.appointment_rb_step6:
                    if (ServiceOrderData.selectedAddItems != null) {//预约流程 进入排期选择
                        toStep6();
                    } else {
                    }
                    if (ServiceOrderData.orderDetail != null) {//服务单改期或预约 进入排期选择
                        toStep6();
                    }
                    break;
                case R.id.appointment_rb_step7:
                    if (ServiceOrderData.choicedTime != null) {//选择过排期的可以进入预览
                        if (ServiceOrderData.orderDetail == null) {//预约流程 进入预览
                            toStep7();
                        } else {//服务单改期或预约 进入预览
                            toStep8();
                        }
                    }
                    break;
            }
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //TouchEvent dispatcher.
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                //If the gestureDetector handles the event, a swipe has been executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 手势滑动页面
     *
     * @param left 向左滑动,向右滑动
     */
    private void toStep(boolean left) {
        if (left) {
            switch (step_now) {
                case 1:
                    if (ServiceOrderData.certTypeId != null && ServiceOrderData.certNumber != null) {
                        ServiceOrderData.cb.setEdit(false);
                        toStepm2();//确认体检人信息页面
                    }
                    break;
                case 2:
                    if (ServiceOrderData.vouchers != null) {//进入凭证列表页面,需判断是否有凭证列表数据
                        toStep2();
                    }
                    break;
                case 3:
                    if (ServiceOrderData.vb != null) {//进入套餐列表页面,需判断是否已选凭证对象进行预约
                        toStep3();
                    }
                    break;
                case 4:
                    if (ServiceOrderData.pb != null) {//进入机构列表页面,需判断是否已选套餐进行预约
                        toStep4();
                    }
                    break;
                case 5:
                    if (ServiceOrderData.ob != null) {//进入体检项列表页面,需判断是否已选机构进行预约
                        toStep5();
                    }
                    break;
                case 6://预览
                    if (ServiceOrderData.selectedAddItems != null) {//进入排期选择
                        toStep6();
                    }
                    break;
                case 7://预览2(改期)
                    if (ServiceOrderData.orderDetail != null) {//进入排期选择
                        toStep6();
                    }
                    break;
                case 12:
                    dialog = new CommonDialog(context, 2, "是", "否", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ServiceOrderData.operation != null && ServiceOrderData.operation.equals("voucher")) {
                                finish();
                            } else {
                                if (ServiceOrderData.users != null && ServiceOrderData.users.size() > 1) {
                                    toStepm1();
                                } else {
                                    ServiceOrderData.clearData();
                                    toStep1();
                                }
                            }
                            dialog.dismiss();
                        }
                    }, null, null, "选择其他体检人？");
                    dialog.show();
                    break;
            }
        } else {
            switch (step_now) {
                case 12:
                    if (ServiceOrderData.vouchers != null) {//进入凭证列表页面,需判断是否有凭证列表数据
                        toStep2();
                    }
                    break;
                case 1:
                    if (ServiceOrderData.vb != null) {//进入套餐列表页面,需判断是否已选凭证对象进行预约
                        toStep3();
                    }
                    break;
                case 2:
                    if (ServiceOrderData.pb != null) {//进入机构列表页面,需判断是否已选套餐进行预约
                        toStep4();
                    }
                    break;
                case 3:
                    if (ServiceOrderData.ob != null) {//进入体检项列表页面,需判断是否已选机构进行预约
                        toStep5();
                    }
                    break;
                case 4:
                    if (ServiceOrderData.selectedAddItems != null) {//进入排期
                        toStep6();
                    }
                    break;
                case 5:
                    if (ServiceOrderData.choicedTime != null) {//选择过排期才可进入预览
                        if (ServiceOrderData.orderDetail == null) {//预约流程 进入预览
                            toStep7();
                        } else {//服务单改期或预约 进入预览
                            toStep8();
                        }
                    }
                    break;
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    /**
     * 进入证件类型选择
     */
    public void toStep1() {
        step_now = 0;
        if (f1 == null) {
            f1 = new CheckupUserFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f1)
                .addToBackStack("CheckupUserFragment").commitAllowingStateLoss();
        setSelectedAndMark(1);
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        btn_left.setVisibility(View.VISIBLE);
        //清空上次选中但没有确定的套餐对象
        if (f3 != null)
            f3.packageBean = null;
        if (f2 != null)
            f2.voucherBean = null;
    }

    /**
     * 进入凭证选择列表
     */
    public void toStep2() {
        step_now = 1;
        if (f2 == null) {
            f2 = new VoucherFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f2)
                .addToBackStack("VoucherFragment").commit();
        setSelectedAndMark(2);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
    }

    /**
     * 进入选择套餐列表
     */
    public void toStep3() {
        step_now = 2;
        if (f3 == null) {
            f3 = new PackageListFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f3)
                .addToBackStack("PackageListFragment").commit();
        setSelectedAndMark(3);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
    }

    /**
     * 进入选择机构列表
     */
    public void toStep4() {
        step_now = 3;
        if (f4 == null) {
            f4 = new ChoiceOrgFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f4)
                .addToBackStack("ChoiceOrgFragment").commit();
        setSelectedAndMark(4);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
    }

    /**
     * 进入体检项选择列表
     */
    public void toStep5() {
        step_now = 4;
        if (f5 == null) {
            f5 = new AddItemFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f5)
                .addToBackStack("AddItemFragment").commit();
        setSelectedAndMark(5);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
        home_titlebar.setVisibility(View.VISIBLE);
    }

    /**
     * 进入选择预约时间
     */
    public void toStep6() {
        step_now = 5;
        if (f6 == null) {
            f6 = new ChoiceDateFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f6)
                .addToBackStack("ChoiceDateFragment").commit();
        setSelectedAndMark(6);
        steps_scrollview.setVisibility(View.VISIBLE);
        home_titlebar.setVisibility(View.VISIBLE);
        if (ServiceOrderData.operation != null && (ServiceOrderData.operation.equals("order")
                || ServiceOrderData.operation.equals("changedate"))) {
            btn_right.setVisibility(View.GONE);
        } else {
            btn_right.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 进入预览
     */
    public void toStep7() {
        step_now = 6;
        if (f7 == null) {
            f7 = new PreviewFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f7)
                .addToBackStack("PreviewFragment").commit();
        setSelectedAndMark(7);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
    }

    /**
     * 我的服务单 预约或改期 选择排期后进入预览
     */
    public void toStep8() {
        step_now = 7;
        if (f8 == null) {
            f8 = new Preview2Fragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        f8)
                .addToBackStack("Preview2Fragment").commit();
        setSelectedAndMark(7);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.GONE);
    }

    /**
     * 进入选择体检人
     */
    public void toStepm1() {
        step_now = 11;
        if (m1 == null) {
            m1 = new ChoiceUserFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m1).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        //清空上次选中但没有确定的套餐对象
        if (f3 != null) {
            f3.packageBean = null;
        }
    }

    /**
     * 进入确认体检人信息
     */
    public void toStepm2() {
        step_now = 12;
        if (m2 == null) {
            m2 = new MakeSureUserInfo();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m2).addToBackStack("MakeSureUserInfo").commit();
        setSelectedAndMark(1);
        steps_scrollview.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
    }

    /**
     * 进入编辑体检人信息
     */
    public void toStepm3() {
        step_now = 13;
        if (m3 == null) {
            m3 = new MakeSureUserInfo2();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m3).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
    }

    /**
     * 进入套餐详情
     */
    public void toStepm4() {
        step_now = 14;
        if (m4 == null) {
            m4 = new PackageDetailFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m4).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
    }

    /**
     * 体检项筛选搜索
     */
    public void toStepm5() {
        step_now = 15;
        if (m5 == null) {
            m5 = new AddItemSearchFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m5).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        home_titlebar.setVisibility(View.GONE);
    }

    /**
     * 已选体检项
     */
    public void toStepm6() {
        step_now = 16;
        if (m6 == null) {
            m6 = new AddItemChoicedFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m6).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        home_titlebar.setVisibility(View.VISIBLE);
    }


    /**
     * 生成服务单成功
     */
    public void toStepm7() {
        step_now = 17;
        if (m7 == null) {
            m7 = new AppointmentSuccessFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m7).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        btn_left.setVisibility(View.GONE);
    }


    /**
     * 支付页面
     */
    public void toStepm8() {
        step_now = 18;
        if (m8 == null) {
            m8 = new PayFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.login_fl_continer,
                        m8).commit();
        steps_scrollview.setVisibility(View.GONE);
        btn_right.setVisibility(View.GONE);
        btn_left.setVisibility(View.VISIBLE);
    }

    /**
     * 使当前的step下边加横线
     */
    private void setSelectedAndMark(int step) {
        steps_selected.get(0).setSelected(true);
        if (ServiceOrderData.vouchers != null)
            steps_selected.get(1).setSelected(true);
        else
            steps_selected.get(1).setSelected(false);
        if (ServiceOrderData.vb != null)
            steps_selected.get(2).setSelected(true);
        else
            steps_selected.get(2).setSelected(false);
        if (ServiceOrderData.pb != null)
            steps_selected.get(3).setSelected(true);
        else
            steps_selected.get(3).setSelected(false);
        if (ServiceOrderData.ob != null)
            steps_selected.get(4).setSelected(true);
        else
            steps_selected.get(4).setSelected(false);
        if (ServiceOrderData.selectedAddItems != null
                || ServiceOrderData.orderDetail != null)
            steps_selected.get(5).setSelected(true);
        else
            steps_selected.get(5).setSelected(false);

        if (ServiceOrderData.choicedTime != null)
            steps_selected.get(6).setSelected(true);
        else
            steps_selected.get(6).setSelected(false);


        for (int i = 0; i < steps_markd.size(); i++) {
            if (i == step - 1) {
                steps_markd.get(i).setVisibility(View.VISIBLE);
            } else {
                steps_markd.get(i).setVisibility(View.INVISIBLE);
            }
        }

        if (ServiceOrderData.orderDetail != null) {
            appointment_rb_step1.setVisibility(View.GONE);
            appointment_rb_step2.setVisibility(View.GONE);
            appointment_rb_step3.setVisibility(View.GONE);
            appointment_rb_step4.setVisibility(View.GONE);
            appointment_rb_step5.setVisibility(View.GONE);
            appointment_mark_step1.setVisibility(View.GONE);
            appointment_mark_step2.setVisibility(View.GONE);
            appointment_mark_step3.setVisibility(View.GONE);
            appointment_mark_step4.setVisibility(View.GONE);
            appointment_mark_step5.setVisibility(View.GONE);
        } else {
            appointment_rb_step1.setVisibility(View.VISIBLE);
            appointment_rb_step2.setVisibility(View.VISIBLE);
            appointment_rb_step3.setVisibility(View.VISIBLE);
            appointment_rb_step4.setVisibility(View.VISIBLE);
            appointment_rb_step5.setVisibility(View.VISIBLE);
        }

        if (step == 3)
            steps_scrollview.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        if (step == 5)
            steps_scrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return false;
    }

    /**
     * 返回键执行方法
     */
    private void back() {
        switch (step_now) {
            case 0:
                finish();
                break;
            case 1:
                ServiceOrderData.cb.setEdit(false);
                toStepm2();
                break;
            case 2:
                toStep2();
                break;
            case 3:
                toStep3();
                break;
            case 4:
                toStep4();
                break;
            case 5:
                if (ServiceOrderData.operation != null
                        && (ServiceOrderData.operation.equals("changedate") || ServiceOrderData.operation.equals("order"))) {
                    ServiceOrderData.orderDetail = null;
                    ServiceOrderData.clearData();
                    finish();
                } else {
                    toStep5();
                }
                break;
            case 6:
                toStep6();
                break;
            case 7:
                toStep6();
                break;
            case 11:
                ServiceOrderData.clearData();
                toStep1();
                break;
            case 12:
                dialog = new CommonDialog(context, 2, "是", "否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ServiceOrderData.operation != null && ServiceOrderData.operation.equals("voucher")) {
                            finish();
                        } else {
                            if (ServiceOrderData.users != null && ServiceOrderData.users.size() > 1) {
                                toStepm1();
                            } else {
                                ServiceOrderData.clearData();
                                toStep1();
                            }
                        }
                        dialog.dismiss();
                    }
                }, null, null, "选择其他体检人？");
                dialog.show();
                break;
            case 13:
                ServiceOrderData.cb.setEdit(false);
                toStepm2();
                break;
            case 14:
                if (from.equals("preview")) {
                    toStep7();
                } else if (from.equals("preview2")) {
                    toStep8();
                } else {
                    toStep3();
                }
                break;
            case 15:
            case 16:
                toStep5();
                break;
            case 17://预约成功提示信息页面,没有返回键
                break;
            case 18:
                toStepm7();
                break;
        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == ServiceEngin.REQUEST_LOGIN && arg1 == Activity.RESULT_CANCELED) {
            ServiceOrderData.clearData();
            finish();
        } else {
            if (ServiceOrderData.operation != null &&
                    (ServiceOrderData.operation.equals("changedate") || ServiceOrderData.operation.equals("order"))) {
                OrderData.operation = "overtime";
                ServiceOrderData.clearData();
                finish();
            } else if (ServiceOrderData.operation != null && ServiceOrderData.operation.equals("voucher")) {
                ServiceOrderData.clearData();
                finish();
            } else {
                ServiceOrderData.clearData();
                toStep1();
            }
        }

    }

    @Override
    protected void onResume() {
        if (ServiceOrderData.cb == null && ServiceOrderData.operation == null) {
            ServiceOrderData.clearData();
            toStep1();
        }
        super.onResume();
    }
}
