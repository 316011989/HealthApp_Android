package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.util.ArrayList;

/**
 * Created by zhangyl on 2015/12/24.
 */
public class VoucherAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VoucherBean> list;
    private VoucherBean choicedVoucherBean;
    private ChildOfItemClickCallBack callback;

    public VoucherAdapter(Context context, ArrayList<VoucherBean> list, ChildOfItemClickCallBack callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LinearLayout.inflate(context,
                    R.layout.appointment_voucher_item, null);
            viewHolder = new ViewHolder();
            viewHolder.voucher_item_vouchernum = (TextView) convertView
                    .findViewById(R.id.voucher_item_vouchernum);
            viewHolder.voucher_item_servetime = (TextView) convertView
                    .findViewById(R.id.voucher_item_servetime);
            viewHolder.voucher_item_payObject = (TextView) convertView
                    .findViewById(R.id.voucher_item_payObject);
            viewHolder.voucher_item_effecttime = (TextView) convertView
                    .findViewById(R.id.voucher_item_effecttime);
            viewHolder.voucher_item_expiretime = (TextView) convertView
                    .findViewById(R.id.voucher_item_expiretime);
            viewHolder.voucher_item_tv7 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv7);
            viewHolder.voucher_item_tv8 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv8);
            viewHolder.voucher_item_tv10 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv10);
            viewHolder.voucher_item_tv11 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv11);
            viewHolder.voucher_item_tv12 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv12);
            viewHolder.voucher_item_tv13 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv13);
            viewHolder.voucher_item_xinhuapay = (TextView) convertView
                    .findViewById(R.id.voucher_item_xinhuapay);
            viewHolder.voucher_item_qiyepay = (TextView) convertView
                    .findViewById(R.id.voucher_item_qiyepay);
            viewHolder.voucher_item_freepay = (TextView) convertView
                    .findViewById(R.id.voucher_item_freepay);
            viewHolder.voucher_item_line2 = (LinearLayout) convertView
                    .findViewById(R.id.voucher_item_line2);
            viewHolder.voucher_item_line3 = (LinearLayout) convertView
                    .findViewById(R.id.voucher_item_line3);
            viewHolder.voucher_item_line4 = (LinearLayout) convertView
                    .findViewById(R.id.voucher_item_line4);
            viewHolder.voucher_item_btn = (RadioButton) convertView
                    .findViewById(R.id.voucher_item_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VoucherBean vb = list.get(position);
        //凭证号
        viewHolder.voucher_item_vouchernum.setText(vb.getVoucherNo());
        //服务类型名称
        if (vb.getServiceData() != null && vb.getServiceData().size() > 0) {
            StringBuffer packageCategoryName = new StringBuffer();
            for (int i = 0; i < vb.getServiceData().size(); i++) {
                if (i > 1)
                    packageCategoryName.append(",");

                if (!packageCategoryName.toString().contains(vb.getServiceData().get(i).getPackageCategoryName()))
                    packageCategoryName.append(vb.getServiceData().get(i).getPackageCategoryName());

            }
            viewHolder.voucher_item_servetime.setText(packageCategoryName);
        }

        //没有结算信息
        if (vb.getPaymentObject().equals("") || vb.getPaymentObject().equals("--")) {
            viewHolder.voucher_item_tv7.setVisibility(View.GONE);
            viewHolder.voucher_item_payObject.setVisibility(View.GONE);
            viewHolder.voucher_item_tv8.setVisibility(View.GONE);
            viewHolder.voucher_item_effecttime.setVisibility(View.GONE);
            viewHolder.voucher_item_tv10.setVisibility(View.GONE);
            viewHolder.voucher_item_expiretime.setVisibility(View.GONE);
            viewHolder.voucher_item_tv11.setVisibility(View.GONE);
            viewHolder.voucher_item_xinhuapay.setVisibility(View.GONE);
            viewHolder.voucher_item_tv12.setVisibility(View.GONE);
            viewHolder.voucher_item_qiyepay.setVisibility(View.GONE);
            viewHolder.voucher_item_tv13.setVisibility(View.GONE);
            viewHolder.voucher_item_freepay.setVisibility(View.GONE);
            viewHolder.voucher_item_line2.setVisibility(View.GONE);
            viewHolder.voucher_item_line3.setVisibility(View.GONE);
            viewHolder.voucher_item_line4.setVisibility(View.GONE);
        } else {//有结算信息
            //付费方式显示
            viewHolder.voucher_item_tv7.setVisibility(View.VISIBLE);
            viewHolder.voucher_item_payObject.setVisibility(View.VISIBLE);
            viewHolder.voucher_item_payObject.setText(vb.getPaymentObject());
//            //个人付费
//            if (vb.getPaymentObject().contains("个人付费")) {
//                viewHolder.voucher_item_line2.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_line3.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_line4.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv8.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_effecttime.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv10.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_expiretime.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv11.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_xinhuapay.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv12.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_qiyepay.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv13.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_freepay.setVisibility(View.VISIBLE);
//                //个人应付
//                viewHolder.voucher_item_effecttime.setText("¥ " + CommonUtil.toPrice(list.get(position).getPayableAmount()));
//                //个人已付
//                viewHolder.voucher_item_expiretime.setText("¥ " + CommonUtil.toPrice(list.get(position).getPaidAmount()));
//                //新华付费
//                viewHolder.voucher_item_xinhuapay.setText("¥ " + CommonUtil.toPrice(list.get(position).getNciPaidAmount()));
//                //企业付费
//                viewHolder.voucher_item_qiyepay.setText("¥ " + CommonUtil.toPrice(list.get(position).getCompanyPaidAmount()));
//                //免费
//                viewHolder.voucher_item_freepay.setText("¥ " + CommonUtil.toPrice(list.get(position).getFreeAmount()));
//            } else { //企业付费或新华付费或新华+企业付费
                viewHolder.voucher_item_line2.setVisibility(View.GONE);
                viewHolder.voucher_item_line3.setVisibility(View.GONE);
                viewHolder.voucher_item_line4.setVisibility(View.GONE);
                viewHolder.voucher_item_tv8.setVisibility(View.GONE);
                viewHolder.voucher_item_effecttime.setVisibility(View.GONE);
                viewHolder.voucher_item_tv10.setVisibility(View.GONE);
                viewHolder.voucher_item_expiretime.setVisibility(View.GONE);
                viewHolder.voucher_item_tv11.setVisibility(View.GONE);
                viewHolder.voucher_item_xinhuapay.setVisibility(View.GONE);
                viewHolder.voucher_item_tv12.setVisibility(View.GONE);
                viewHolder.voucher_item_qiyepay.setVisibility(View.GONE);
                viewHolder.voucher_item_tv13.setVisibility(View.GONE);
                viewHolder.voucher_item_freepay.setVisibility(View.GONE);

//            }
        }


        viewHolder.voucher_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.mCallback(position, v);
            }
        });
        if (list.size() == 1) {
            viewHolder.voucher_item_btn.setChecked(true);
        } else {
            if (choicedVoucherBean != null && choicedVoucherBean.getVoucherId().equals(list.get(position).getVoucherId())) {
                viewHolder.voucher_item_btn.setChecked(true);
            } else {
                viewHolder.voucher_item_btn.setChecked(false);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView voucher_item_vouchernum;
        TextView voucher_item_servetime;
        TextView voucher_item_payObject;
        TextView voucher_item_effecttime;
        TextView voucher_item_expiretime;
        TextView voucher_item_tv7;
        TextView voucher_item_tv8;
        TextView voucher_item_tv10;
        TextView voucher_item_tv11;
        TextView voucher_item_tv12;
        TextView voucher_item_tv13;
        TextView voucher_item_freepay;
        TextView voucher_item_xinhuapay;
        TextView voucher_item_qiyepay;
        LinearLayout voucher_item_line2;
        LinearLayout voucher_item_line3;
        LinearLayout voucher_item_line4;

        RadioButton voucher_item_btn;
    }

    public void setChoicedVoucherBean(VoucherBean choicedVoucherBean) {
        this.choicedVoucherBean = choicedVoucherBean;
    }
}
