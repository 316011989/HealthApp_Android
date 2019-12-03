package com.pact.healthapp.components.checkupaccount;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.CheckupUserBean;
import com.pact.healthapp.utils.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by wangdong on 2016/01/26.
 */
public class ChoiceAccountAdapter4ListView extends BaseAdapter {
    private Context context;
    private ArrayList<CheckupUserBean> list;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();

    public ChoiceAccountAdapter4ListView(Context context, ArrayList<CheckupUserBean> list) {
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
                    R.layout.checkupaccount_add_listview_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.chocie_account_ll = (LinearLayout) convertView
                    .findViewById(R.id.chocie_account_ll);
            viewHolder.choiceuser_item_company = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_company);
            viewHolder.choiceuser_item_username = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_username);
            viewHolder.choiceuser_item_usersex = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_usersex);
            viewHolder.choiceuser_item_userage = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_userage);
            viewHolder.choiceuser_item_isselected = (RadioButton) convertView
                    .findViewById(R.id.choiceuser_item_isselected);
            viewHolder.choiceuser_item_userphone = (TextView) convertView
                    .findViewById(R.id.choiceuser_item_userphone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getEnterpriseName() != null
                && list.get(position).getEnterpriseName().length() > 0) {
            viewHolder.choiceuser_item_company.setText(list.get(position).getEnterpriseName());
            viewHolder.choiceuser_item_company.setVisibility(View.VISIBLE);
        } else {
            viewHolder.choiceuser_item_company.setVisibility(View.GONE);
        }

        viewHolder.choiceuser_item_username.setText(list.get(position).getCustomerName());
        viewHolder.choiceuser_item_usersex.setText(list.get(position).getSexName());
        if (list.get(position).getBirthday() != null && list.get(position).getBirthday().length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(list.get(position).getBirthday());
                viewHolder.choiceuser_item_userage.setText(CommonUtil.getAgeByBirthday(date) + "岁");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            viewHolder.choiceuser_item_userage.setText("");
        }

        if (list.get(position).getMobileNumber().length() == 11) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(list.get(position).getMobileNumber().substring(0, 3));
            stringBuffer.append("****");
            stringBuffer.append(list.get(position).getMobileNumber().substring(7, 11));
            viewHolder.choiceuser_item_userphone.setText(stringBuffer);
            viewHolder.choiceuser_item_userphone.setVisibility(View.VISIBLE);
        } else {
            viewHolder.choiceuser_item_userphone.setVisibility(View.GONE);
        }

        convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        viewHolder.choiceuser_item_isselected.setClickable(false);
        viewHolder.choiceuser_item_isselected.setEnabled(false);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.chocie_account_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.choiceuser_item_isselected.setChecked(true);
                AccountData.setCb(list.get(position));
                // 重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), true);
                ChoiceAccountAdapter4ListView.this.notifyDataSetChanged();
            }
        });
        if (states.get(String.valueOf(position)) != null) {
            viewHolder.choiceuser_item_isselected.setChecked(states.get(String.valueOf(position)));
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout chocie_account_ll;
        TextView choiceuser_item_company;
        TextView choiceuser_item_username;
        TextView choiceuser_item_usersex;
        TextView choiceuser_item_userage;
        RadioButton choiceuser_item_isselected;
        TextView choiceuser_item_userphone;
    }
}
