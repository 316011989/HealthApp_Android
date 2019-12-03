package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.utils.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhangyl on 2015/12/24.
 */
public class ChoiceUserAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CheckupUserBean> list;

    public ChoiceUserAdapter(Context context, ArrayList<CheckupUserBean> list) {
        this.context = context;
        this.list = list;
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
                    R.layout.appointment_choiceuser_item, null);
            viewHolder = new ViewHolder();
            viewHolder.choiceuser_item_company = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_company);
            viewHolder.choiceuser_item_username = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_username);
            viewHolder.choiceuser_item_usersex = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_usersex);
            viewHolder.choiceuser_item_userage = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_userage);
            viewHolder.choiceuser_item_isselected = (ImageView) convertView
                    .findViewById(R.id.choiceuser_item_isselected);
            viewHolder.choiceuser_item_userphone = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_userphone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).getEnterpriseName() != null && !list.get(position).getEnterpriseName().equals("")) {
            viewHolder.choiceuser_item_company.setText(list.get(position).getEnterpriseName());
            viewHolder.choiceuser_item_company.setVisibility(View.VISIBLE);
        } else {
            viewHolder.choiceuser_item_company.setVisibility(View.GONE);
        }

//        if (list.get(position).getCustomerName().length() > 2) {
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append(list.get(position).getCustomerName().substring(0, 1));
//            for (int i = 2; i < list.get(position).getCustomerName().length(); i++) {
//                stringBuffer.append("*");
//            }
//            stringBuffer.append(list.get(position).getCustomerName().substring(list.get(position).getCustomerName().length() - 1));
//            viewHolder.choiceuser_item_username.setText(stringBuffer);
//        } else if (list.get(position).getCustomerName().length() == 2) {
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append("*");
//            stringBuffer.append(list.get(position).getCustomerName().substring(1));
//            viewHolder.choiceuser_item_username.setText(stringBuffer);
//        } else {
        viewHolder.choiceuser_item_username.setText(list.get(position).getCustomerName());
//        }
        viewHolder.choiceuser_item_usersex.setText(list.get(position).getSexName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(list.get(position).getBirthday());
            viewHolder.choiceuser_item_userage.setText(CommonUtil.getAgeByBirthday(date) + "岁");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.size() > 1 && !list.get(position).getMobileNumber().equals("")) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("手机号：");
            stringBuffer.append(list.get(position).getMobileNumber().substring(0, 3));
            stringBuffer.append("****");
            stringBuffer.append(list.get(position).getMobileNumber().substring(7, list.get(position).getMobileNumber().length()));
            viewHolder.choiceuser_item_userphone.setText(stringBuffer);
            viewHolder.choiceuser_item_userphone.setVisibility(View.VISIBLE);
        } else if (list.get(position).getMobileNumber() != null && !list.get(position).getMobileNumber().equals("")) {
            viewHolder.choiceuser_item_userphone.setText(list.get(position).getMobileNumber());
            viewHolder.choiceuser_item_userphone.setVisibility(View.VISIBLE);
        } else {
            viewHolder.choiceuser_item_userphone.setVisibility(View.GONE);
        }

//        //预约流程已选用户显示对勾
//        if (ServiceOrderData.cb != null &&
//                ServiceOrderData.cb.getCustomerId().equals(list.get(position).getCustomerId())) {
//            viewHolder.choiceuser_item_isselected.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.choiceuser_item_isselected.setVisibility(View.INVISIBLE);
//        }
        //不可选用户置空
        if (list.get(position).getIsAble().equals("1")) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }
        return convertView;
    }

    class ViewHolder {
        TextView choiceuser_item_company;
        TextView choiceuser_item_username;
        TextView choiceuser_item_usersex;
        TextView choiceuser_item_userage;
        ImageView choiceuser_item_isselected;
        TextView choiceuser_item_userphone;
    }
}
