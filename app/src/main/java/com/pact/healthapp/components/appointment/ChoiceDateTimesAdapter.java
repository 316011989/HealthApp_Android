package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.util.List;

/**
 * Created by wangdong on 2016/2/14.
 */
public class ChoiceDateTimesAdapter extends BaseAdapter {
    private List<ChoiceDateBean> list;
    private Context context;
    private ChoiceDateBean choicedTime;

    public ChoiceDateTimesAdapter(Context context, List<ChoiceDateBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (list == null || list.isEmpty()) ? 0 : list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.appointment_choicedate_times_gridview_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.chocie_date_ll);
            viewHolder.day = (TextView) convertView.findViewById(R.id.chocie_date_time);
            viewHolder.num = (TextView) convertView.findViewById(R.id.chocie_date_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String startDate = list.get(position).getStartDate().substring(11);
        String endDate = list.get(position).getEndDate().substring(11);
        if (list.get(position).getPeopleAvailableNo() == 0) {//可预约人数为0 显示已排满
            viewHolder.num.setText("已排满");
            viewHolder.day.setText(startDate + "-" + endDate);
            viewHolder.num.setTextColor(context.getResources().getColor(R.color.gray));
            viewHolder.day.setTextColor(context.getResources().getColor(R.color.gray));
            viewHolder.ll.setBackgroundResource(R.drawable.grey_border_shape);
            convertView.setClickable(true);
            convertView.setEnabled(true);
        } else {
            viewHolder.num.setText("可约" + list.get(position).getPeopleAvailableNo() + "人");
            viewHolder.day.setText(startDate + "-" + endDate);
            viewHolder.num.setTextColor(context.getResources().getColor(R.color.gray_dark));
            viewHolder.day.setTextColor(context.getResources().getColor(R.color.gray_dark));
            viewHolder.ll.setBackgroundResource(R.drawable.green_border_shape);
            convertView.setClickable(false);
            convertView.setEnabled(false);
        }

        //判断是否选中过
        if (choicedTime != null
                && choicedTime.getDaytimeId().equals(list.get(position).getDaytimeId())) {
            viewHolder.ll.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_button_normal));
            viewHolder.day.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.num.setTextColor(context.getResources().getColor(R.color.white));
        }


        return convertView;
    }

    class ViewHolder {
        LinearLayout ll;
        TextView day;
        TextView num;
    }


    public void setChoicedTime(ChoiceDateBean choicedTime) {
        this.choicedTime = choicedTime;
    }
}
