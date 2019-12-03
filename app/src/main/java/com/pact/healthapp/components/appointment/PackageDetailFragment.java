package com.pact.healthapp.components.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.serviceorder.OrderData;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;


/**
 * 套餐详情
 * Created by zhangyl on 2016/1/13.
 */
public class PackageDetailFragment extends BaseFragment {
    @ViewInject(R.id.appointment_package_itemlist)
    private ListView listView;
    private PackageitemAdapter adapter;
    private Packageitem2Adapter adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_packagedetail_layout, null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("套餐详情");

        if (ServiceOrderData.pbdetail != null && ServiceOrderData.packageitems != null
                && !ServiceOrderData.isPriceSell) {//预约流程中套餐列表进套餐详情
            addHeader(ServiceOrderData.pbdetail.getPackageName(),
                    ServiceOrderData.pbdetail.getPayObject(),
                    ServiceOrderData.pbdetail.getMarketPrice());
            adapter = new PackageitemAdapter(context, ServiceOrderData.packageitems);
            listView.setAdapter(adapter);
        } else if (ServiceOrderData.pbdetail != null && ServiceOrderData.packageitems != null
                && ServiceOrderData.isPriceSell) {//预约流程中预览进套餐详情
            addHeader(ServiceOrderData.pbdetail.getPackageName(),
                    ServiceOrderData.pbdetail.getPayObject(),
                    ServiceOrderData.packagePriceSell + "");
            if (ServiceOrderData.underProductList != null) {
                adapter = new PackageitemAdapter(context, ServiceOrderData.underProductList);
            } else {
                adapter = new PackageitemAdapter(context, ServiceOrderData.packageitems);
            }
            listView.setAdapter(adapter);
        } else if (ServiceOrderData.orderDetail != null) {//预约服务单改期后 进套餐详情
            addHeader(ServiceOrderData.orderDetail.getPackageName(),
                    ServiceOrderData.orderDetail.getPayObjectId(),
                    ServiceOrderData.orderDetail.getPackageSellPrice());
            adapter2 = new Packageitem2Adapter(context, ServiceOrderData.orderDetail.getPackageDatas());
            listView.setAdapter(adapter2);
        } else if (OrderData.detailBean != null) {//预约服务单中 进套餐详情
            addHeader(OrderData.detailBean.getPackageName(),
                    OrderData.detailBean.getPayObjectId(),
                    OrderData.detailBean.getPackageSellPrice());
            adapter2 = new Packageitem2Adapter(context, OrderData.detailBean.getPackageDatas());
            listView.setAdapter(adapter2);
        }

        return view;
    }

    private void addHeader(String packageName, String payObject, String price) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_packagedetail_head_layout, null);
        TextView packagename = (TextView) view.findViewById(R.id.appointment_packageitem_packagename);
        TextView payobject = (TextView) view.findViewById(R.id.appointment_packageitem_payObject);
        TextView tv2 = (TextView) view.findViewById(R.id.appointment_packageitem_tv2);
        TextView sellprice = (TextView) view.findViewById(R.id.appointment_packageitem_sellprice);

        packagename.setText(packageName);

        if (payObject.equals("1")) {
            payobject.setText("企业付费");
            tv2.setVisibility(View.GONE);
            sellprice.setVisibility(View.GONE);
        } else if (payObject.equals("3")) {
            payobject.setText("新华付费");
            tv2.setVisibility(View.GONE);
            sellprice.setVisibility(View.GONE);
        } else if (payObject.equals("2")) {
            payobject.setText("个人付费");
            if (ServiceOrderData.isPriceSell) {
                tv2.setText(getResources().getString(R.string.sell_price));
            } else {
                tv2.setText(getResources().getString(R.string.market_price));
            }
            if (Double.parseDouble(CommonUtil.toPrice(price)) == 0) {
                tv2.setVisibility(View.GONE);
                sellprice.setVisibility(View.GONE);
            } else {
                sellprice.setText("¥" + CommonUtil.toPrice(price));
            }
        }

        listView.addHeaderView(view);
    }


}
