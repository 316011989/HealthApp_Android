/**
 *
 */
package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 体检项
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class AddItemFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private View view;

    @ViewInject(R.id.search_tv)
    private TextView search_tv;//搜索
    @ViewInject(R.id.pay_sort)
    private CheckBox pay_sort;//个人付费排序
    @ViewInject(R.id.add_items_elistview)
    private ExpandableListView eListView;//体检项包
    @ViewInject(R.id.add_items_next)
    private TextView add_items_next;//下一步
    @ViewInject(R.id.add_items_showchoose)
    private ImageView add_items_showchoose;//进入已选体检项页面
    @ViewInject(R.id.add_items_choosecount)
    private TextView add_items_choosecount;//已选数量

    private AddItemAdapter adapter;

    private int count = 0;//点击次数
    private Long firClick = 0l;//第一次点击时间
    private Long secClick = 0l;//第二次点击时间

    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_additem_layout, null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("体检项");
        initView();
        if (ServiceOrderData.allAddItems == null) {
            resetChoose = false;
            getAttachPackageList();
        } else {
            positiveSequence();
            adapter = new AddItemAdapter(context, ServiceOrderData.allAddItems, new ChildOfItemClickCallBack() {
                @Override
                public void mCallback(int position, View v) {
                    setAdd_items_choosecount();
                }
            });
            eListView.setAdapter(adapter);
            setAdd_items_choosecount();
            //首次加载全部展开
            int groupCount = ServiceOrderData.allAddItems.size();
            for (int i = 0; i < groupCount; i++) {
//                eListView.expandGroup(i);
                eListView.collapseGroup(i);
            }
        }
        //去掉系统默认的箭头图标
        eListView.setGroupIndicator(null);

        //双击标题栏 页面滑动到顶部
        ((AppointmentActivity) getActivity()).home_titlebar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    count++;
                    if (count == 1) {
                        firClick = System.currentTimeMillis();
                    } else if (count == 2) {
                        secClick = System.currentTimeMillis();
                        if (secClick - firClick < 1000) {
                            eListView.setSelectedGroup(0);
                        }
                        count = 0;
                        firClick = 0l;
                        secClick = 0l;
                    }
                }
                return true;
            }
        });

        return view;
    }

    private void initView() {
        search_tv.setOnClickListener(this);
        pay_sort.setOnCheckedChangeListener(this);
        add_items_next.setOnClickListener(this);
        add_items_showchoose.setOnClickListener(this);
    }


    /**
     * 获取体检项列表
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
        map.put("isSearch", "N");
        map.put("keyWord", "");
        map.put("isCompanyPay", false);
        map.put("isXinhuaPay", false);
        map.put("isPersonalPay", false);
        map.put("startPrice", 0);
        map.put("endPrice", 0);
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
                                Log.e("获取体检项列表", result);
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
        positiveSequence();
        adapter = new AddItemAdapter(context, ServiceOrderData.allAddItems, new ChildOfItemClickCallBack() {
            @Override
            public void mCallback(int position, View v) {
                setAdd_items_choosecount();
            }
        });
        eListView.setAdapter(adapter);
        setAdd_items_choosecount();
        //首次加载全部展开
        int groupCount = ServiceOrderData.allAddItems.size();
        for (int i = 0; i < groupCount; i++) {
            //                eListView.expandGroup(i);
            eListView.collapseGroup(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_tv://筛选搜索
                resetChoose = false;
                ((AppointmentActivity) getActivity()).toStepm5();
                break;
            case R.id.add_items_next://下一步
                if (!ServiceOrderData.pb.getPackageProductNumbers().equals("0")) {//不是空套餐
                    if (checkMust(ServiceOrderData.checkedChildren)) {//必选包已选
                        if (checkRepeat(ServiceOrderData.checkedChildren)) {//没有重复项
                            nextOperation();
                        }
                    }
                } else {//选择的是空套餐
                    if (ServiceOrderData.checkedChildren == null
                            || ServiceOrderData.checkedChildren.size() == 0) {//没有选择体检项
                        CommonToast.makeText(context, "请选择体检项！");
                    } else {
                        if (checkMust(ServiceOrderData.checkedChildren)) {//必选包已选
                            if (checkRepeat(ServiceOrderData.checkedChildren)) {//没有重复项
                                nextOperation();
                            }
                        }
                    }
                }
                break;
            case R.id.add_items_showchoose://查看已选
                resetChoose = false;
                ((AppointmentActivity) getActivity()).toStepm6();
                break;
        }
    }

    /**
     * 下一步时的互斥检查
     */
    private void nextOperation() {
        //如果之前未选择过体检项,或者之前选择的体检项与现在选择的不同则重新为选中体检项集合赋值,并且检查互斥
        if (ServiceOrderData.selectedAddItems == null || !ServiceOrderData.selectedAddItems.equals(ServiceOrderData.checkedChildren)) {
            ServiceOrderData.setSelectedAddItems(ServiceOrderData.checkedChildren);
        }
        //套餐原子产品
        JSONArray packageProductA = new JSONArray();
        if (ServiceOrderData.packageitems != null && ServiceOrderData.packageitems.size() > 0) {
            for (int i = 0; i < ServiceOrderData.packageitems.size(); i++) {
                packageProductA.add(ServiceOrderData.packageitems.get(i).getProductId());
            }
        }
        //体检项产品列表
        JSONArray attachProductA = new JSONArray();
        if (ServiceOrderData.selectedAddItems.size() > 0) {
            for (int i = 0; i < ServiceOrderData.selectedAddItems.size(); i++) {
                attachProductA.add(ServiceOrderData.selectedAddItems.get(i).getProductId());
            }
        }
        checkPackageAndAttach(packageProductA, attachProductA);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (ServiceOrderData.allAddItems != null && ServiceOrderData.allAddItems.size() > 0) {
            if (isChecked) {//从小到大排序
                positiveSequence();
                adapter = new AddItemAdapter(context, ServiceOrderData.allAddItems, new ChildOfItemClickCallBack() {
                    @Override
                    public void mCallback(int position, View v) {
                        setAdd_items_choosecount();
                    }
                });
                eListView.setAdapter(adapter);
            } else {//从大到小排序
                negativeSequence();
                adapter = new AddItemAdapter(context, ServiceOrderData.allAddItems, new ChildOfItemClickCallBack() {
                    @Override
                    public void mCallback(int position, View v) {
                        setAdd_items_choosecount();
                    }
                });
                eListView.setAdapter(adapter);
            }
            for (int i = 0; i < ServiceOrderData.allAddItems.size(); i++) {
                System.out.println(ServiceOrderData.allAddItems.get(i).getPackageName() + "--------"
                        + ServiceOrderData.allAddItems.get(i).getPriceSell() + "---------"
                        + ServiceOrderData.allAddItems.get(i).isMust());
            }
            setAdd_items_choosecount();
            //首次加载全部展开
            int groupCount = ServiceOrderData.allAddItems.size();
            for (int i = 0; i < groupCount; i++) {
                //                eListView.expandGroup(i);
                eListView.collapseGroup(i);
            }
        }
    }

    /**
     * 套餐、体检项重复互斥检查
     */
    private void checkPackageAndAttach(JSONArray packageProductA, JSONArray attachProductA) {
        String bizId = "000";
        String serviceName = "checkPackageAndAttachBook";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("packageProductArray", packageProductA.toString());
        map.put("attachProductArray", attachProductA.toString());
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        String para = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, para,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
//                        super.onSuccess(arg0);
                        canclDialog();
                        String result = "";
                        try {
                            result = Des3.decode(arg0.result.toString());
                            JSONObject obj = JSON.parseObject(result);
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("0")) {//无互斥
                                parseSuccess(result);
                            }
                            if (obj.get("resultCode") != null
                                    && obj.get("resultCode").toString()
                                    .equals("1")) {//有互斥
                                String resultMsg = obj.get("resultMsg").toString();
                                parseFailure(resultMsg);
                            }
                            if (obj.get("errorCode") != null
                                    && obj.get("errorCode").toString().equals("1120")) {
                                dialog = new CommonDialog(context, 1, "确定", "",
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
                                                dialog.dismiss();
                                                startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                            }
                                        }, null, null, "当前账户在其他设备登录，请重新登录。");
                                dialog.show();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 解析json数据 没有互斥
     *
     * @param result 解密后的json数据
     */
    private void parseSuccess(String result) {
        JSONObject obj = JSON.parseObject(result);
        JSONObject data = obj.getJSONObject("data");
        boolean success = data.getBoolean("success");
        if (success) {
            ((AppointmentActivity) getActivity()).toStep6();
        }
    }

    /**
     * 解析json数据 存在互斥 依赖关系
     *
     * @param resultMsg
     */
    private void parseFailure(String resultMsg) {
        if (resultMsg.contains("互斥")) {
            dialog = new CommonDialog(context, 2, "重新选择", "继续", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    ((AppointmentActivity) getActivity()).toStep6();
                }
            }, null, resultMsg);
            dialog.show();
        } else if (resultMsg.contains("依赖")) {
            dialog = new CommonDialog(context, 1, "重新选择", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            }, null, null, resultMsg);
            dialog.show();
        }
    }

    /**
     * 从小到大排序
     */
    private void positiveSequence() {
        // 包中原子项价格排序
        for (int i = 0; i < ServiceOrderData.allAddItems.size(); i++) {
            itemSort(ServiceOrderData.allAddItems.get(i).getPackageDatas(), 0, ServiceOrderData.allAddItems
                    .get(i).getPackageDatas().size() - 1);
        }
        // 包之间价格排序
        packageSort(ServiceOrderData.allAddItems, 0, ServiceOrderData.allAddItems.size() - 1);
        sortPackage();
    }


    /**
     * 从大到小排序
     */
    private void negativeSequence() {
        // 包中原子项价格排序
        for (int i = 0; i < ServiceOrderData.allAddItems.size(); i++) {
            itemSortDESC(ServiceOrderData.allAddItems.get(i).getPackageDatas(), 0, ServiceOrderData.allAddItems
                    .get(i).getPackageDatas().size() - 1);
        }
        // 包之间价格排序
        packageSortDESC(ServiceOrderData.allAddItems, 0, ServiceOrderData.allAddItems.size() - 1);
        sortPackage();
    }

    /**
     * 将体检项包按照必选,整包,累计,散项排序
     */
    private void sortPackage() {
        // 按整包,累计,散项排序
        List<AddItemBean> p1 = new ArrayList<AddItemBean>();// 整包
        List<AddItemBean> p2 = new ArrayList<AddItemBean>();// 累计
        List<AddItemBean> p3 = new ArrayList<AddItemBean>();// 散项
        for (int i = 0; i < ServiceOrderData.allAddItems.size(); i++) {
            if (ServiceOrderData.allAddItems.get(i).getAccountType().equals("0")) {
                p1.add(ServiceOrderData.allAddItems.get(i));
            } else if (ServiceOrderData.allAddItems.get(i).getAccountType().equals("1")) {
                p2.add(ServiceOrderData.allAddItems.get(i));
            } else {
                p3.add(ServiceOrderData.allAddItems.get(i));
            }
        }
        ServiceOrderData.allAddItems.clear();
        ServiceOrderData.allAddItems.addAll(p1);
        ServiceOrderData.allAddItems.addAll(p2);
        ServiceOrderData.allAddItems.addAll(p3);
        // 必选,非必选排序
        List<AddItemBean> p11 = new ArrayList<AddItemBean>();// 必选
        List<AddItemBean> p12 = new ArrayList<AddItemBean>();// 非必选
        for (int i = 0; i < ServiceOrderData.allAddItems.size(); i++) {
            if (ServiceOrderData.allAddItems.get(i).isMust()) {
                p11.add(ServiceOrderData.allAddItems.get(i));
            } else {
                p12.add(ServiceOrderData.allAddItems.get(i));
            }
        }
        ServiceOrderData.allAddItems.clear();
        ServiceOrderData.allAddItems.addAll(p11);
        ServiceOrderData.allAddItems.addAll(p12);
    }

    /**
     * 体检项包内排序
     *
     * @param items
     * @param l
     * @param r
     */
    public void itemSort(List<AddItemBean.PackageDatasEntity> items, int l,
                         int r) {
        if (l < r) {
            int i = l, j = r;
            AddItemBean.PackageDatasEntity x = items.get(l);
            while (i < j) {
                while (i < j && Double.parseDouble(items.get(j).getPriceSell()) >= Double.parseDouble(x.getPriceSell()))
                    // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    items.set(i++, items.get(j));

                while (i < j && Double.parseDouble(items.get(i).getPriceSell()) < Double.parseDouble(x.getPriceSell()))
                    // 从左向右找第一个大于等于x的数
                    i++;
                if (i < j)
                    items.set(j--, items.get(i));
            }
            items.set(i, x);
            itemSort(items, l, i - 1); // 递归调用
            itemSort(items, i + 1, r);
        }
    }

    /**
     * 包之间按价格快速排序
     */
    private void packageSort(List<AddItemBean> packages, int l, int r) {
        if (l < r) {
            int i = l, j = r;
            AddItemBean x = packages.get(l);
            while (i < j) {
                while (i < j
                        && Double.parseDouble(packages.get(j).getPriceSell()) >= Double.parseDouble(x.getPriceSell()))
                    // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    packages.set(i++, packages.get(j));

                while (i < j
                        && Double.parseDouble(packages.get(i).getPriceSell()) < Double.parseDouble(x.getPriceSell()))
                    // 从左向右找第一个大于等于x的数
                    i++;
                if (i < j)
                    packages.set(j--, packages.get(i));
            }
            packages.set(i, x);
            packageSort(packages, l, i - 1); // 递归调用
            packageSort(packages, i + 1, r);
        }
    }


    /**
     * 体检项包内排序
     *
     * @param items
     * @param l
     * @param r
     */
    public void itemSortDESC(List<AddItemBean.PackageDatasEntity> items, int l,
                             int r) {
        if (l < r) {
            int i = l, j = r;
            AddItemBean.PackageDatasEntity x = items.get(l);
            while (i < j) {
                while (i < j && Double.parseDouble(items.get(j).getPriceSell()) < Double.parseDouble(x.getPriceSell()))
                    // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    items.set(i++, items.get(j));

                while (i < j && Double.parseDouble(items.get(i).getPriceSell()) >= Double.parseDouble(x.getPriceSell()))
                    // 从左向右找第一个大于等于x的数
                    i++;
                if (i < j)
                    items.set(j--, items.get(i));
            }
            items.set(i, x);
            itemSortDESC(items, l, i - 1); // 递归调用
            itemSortDESC(items, i + 1, r);
        }
    }

    /**
     * 包之间按价格快速排序
     */
    private void packageSortDESC(List<AddItemBean> packages, int l, int r) {
        if (l < r) {
            int i = l, j = r;
            AddItemBean x = packages.get(l);
            while (i < j) {
                while (i < j
                        && Double.parseDouble(packages.get(j).getPriceSell()) < Double.parseDouble(x.getPriceSell()))
                    // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    packages.set(i++, packages.get(j));

                while (i < j
                        && Double.parseDouble(packages.get(i).getPriceSell()) >= Double.parseDouble(x.getPriceSell()))
                    // 从左向右找第一个大于等于x的数
                    i++;
                if (i < j)
                    packages.set(j--, packages.get(i));
            }
            packages.set(i, x);
            packageSortDESC(packages, l, i - 1); // 递归调用
            packageSortDESC(packages, i + 1, r);
        }
    }

    /**
     * 必选检查
     *
     * @param choosedList
     * @return
     */
    private boolean checkMust(List<AddItemBean.PackageDatasEntity> choosedList) {
        boolean flag = true;
        List<String> mustList = new ArrayList<String>();// 必选包名
        for (int i = 0; i < ServiceOrderData.allAddItems.size(); i++) {
            if (ServiceOrderData.allAddItems.get(i).isMust()) {
                mustList.add(ServiceOrderData.allAddItems.get(i).getPackageName());
            }
        }

        List<String> choosed = new ArrayList<String>();
        for (int i = 0; i < choosedList.size(); i++) {//已选包名
            choosed.add(choosedList.get(i).getPackageName());
        }
        for (int i = 0; i < choosed.size(); i++) {//去除重复
            for (int j = choosed.size() - 1; j > i; j--) {
                if (choosed.get(i).equals(choosed.get(j))) {
                    choosed.remove(j);
                }
            }
        }

        for (int i = 0; i < mustList.size(); i++) {
            if (!choosed.contains(mustList.get(i))) {
                dialog = new CommonDialog(context, 1, "重新选择体检项", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                }, null, null, "必选体检项包[" + mustList.get(i) + "]中至少选择一个体检项");
                dialog.show();
                flag = false;
                break;
            }
        }

        return flag;
    }

    /**
     * 重复检查
     *
     * @param choosedList
     * @return
     */
    public boolean checkRepeat(List<AddItemBean.PackageDatasEntity> choosedList) {
        List<String> choosed = new ArrayList<String>();
        for (int i = 0; i < choosedList.size(); i++) {//已选包名
            choosed.add(choosedList.get(i).getProductName());
        }
        for (int i = 0; i < choosed.size(); i++) {//去除重复
            for (int j = choosed.size() - 1; j > i; j--) {
                if (choosed.get(i).equals(choosed.get(j))) {
                    dialog = new CommonDialog(context, 1, "重新选择体检项", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    }, null, null, choosed.get(j) + "：项目已经存在，不能重复添加");
                    dialog.show();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 改变购物车按钮的角标数量
     */
    public void setAdd_items_choosecount() {
        add_items_choosecount.setText("0");
        if (ServiceOrderData.checkedChildren != null)
            add_items_choosecount.setText("" + ServiceOrderData.checkedChildren.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (resetChoose) {
            resetSelectedItem();
        }
        if (ServiceOrderData.selectedAddItems != null)
            resetChoose = true;
    }

    //是否需要将本次选中的体检项替换为上次选中的体检项
    private boolean resetChoose = false;

    /**
     * 取消本次对选中体检项的修改
     * Ps:预约流程之间切换时本次修改无效
     * 进入已选体检项和筛选页面本次修改有效
     */
    private void resetSelectedItem() {
        if (ServiceOrderData.selectedAddItems != null
                && ServiceOrderData.selectedAddItems.size() > 0) {
            ServiceOrderData.checkedParents.clear();
            List<AddItemBean.PackageDatasEntity> packageDatas = new ArrayList<AddItemBean.PackageDatasEntity>();
            packageDatas.addAll(ServiceOrderData.selectedAddItems);
            for (int i = 0; i < packageDatas.size(); i++) {
                if (packageDatas.get(i).getType().equals("A")) {
                    List<AddItemBean.PackageDatasEntity> packageTemp = new ArrayList<AddItemBean.PackageDatasEntity>();
                    packageTemp.add(packageDatas.get(i));
                    for (int j = i + 1; j < packageDatas.size(); j++) {
                        String packageId1 = packageDatas.get(i).getPackageId();
                        String packageId2 = packageDatas.get(j).getPackageId();
                        if (packageId1.equals(packageId2)) {
                            packageTemp.add(packageDatas.get(j));
                            packageDatas.remove(j);
                            j--;
                        }
                    }
                    AddItemBean bean = new AddItemBean();
                    bean.setPackageName(packageDatas.get(i).getPackageName());
                    bean.setPriceMarket(packageDatas.get(i).getPriceMarket());
                    bean.setPriceSell(packageDatas.get(i).getPriceSell());
                    bean.setAccountType(packageDatas.get(i).getAccountType());
                    bean.setMust(packageDatas.get(i).getMust());
                    bean.setType(packageDatas.get(i).getType());
                    bean.setPackageDatas(packageTemp);
                    ServiceOrderData.checkedParents.add(bean);
                }
            }
            ServiceOrderData.checkedChildren.clear();
            ServiceOrderData.checkedChildren.addAll(ServiceOrderData.selectedAddItems);
        } else {
            ServiceOrderData.checkedChildren.clear();
            ServiceOrderData.checkedParents.clear();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //ExpandableListView全部展开
        if (ServiceOrderData.allAddItems != null && ServiceOrderData.allAddItems.size() > 0) {
            int groupCount = ServiceOrderData.allAddItems.size();
            for (int i = 0; i < groupCount; i++) {
                //                eListView.expandGroup(i);
                eListView.collapseGroup(i);
            }
        }
    }
}