package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.utils.CommonUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangyl on 2016/2/14.
 */
public class ChoiceDateDaysAdapter extends BaseAdapter {
    private Context context;
    private Map<String, List<ChoiceDateBean>> map;
    private Object[] sets;
    private int year, month;//当前显示的年月
    private int dayofweek;//当前月第一天是星期几
    private int counts;//当前月有多少天
    private String choicedDay;


    public ChoiceDateDaysAdapter(Context context, Map<String, List<ChoiceDateBean>> map, int year, int month) {
        this.context = context;
        this.map = map;
        this.year = year;
        this.month = month;
        Set<String> set = map.keySet();
        sets = set.toArray();
        if (month < 10)
            dayofweek = spareNum("0" + month + "/01/" + year);
        else
            dayofweek = spareNum(month + "/01/" + year);

        Calendar cal = new GregorianCalendar(year, month - 1, 1);
        counts = dayofweek + cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    @Override
    public int getCount() {
        return counts;
    }

    @Override
    public Object getItem(int position) {
        String monthString = "";
        if (month < 10)
            monthString = "0" + month;
        else
            monthString = month + "";
        String dayString = "";
        if ((position - dayofweek + 1) < 10)
            dayString = "0" + (position - dayofweek + 1);
        else
            dayString = (position - dayofweek + 1) + "";

        String day = year + "-" + monthString + "-" + dayString;
        return day;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.appointment_choicedate_days_gridview_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.chocie_date_ll);
            viewHolder.day = (TextView) convertView.findViewById(R.id.chocie_date_day);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //gridview从1号开始显示,前边的隐藏
        if (position - dayofweek + 1 > 0) {
            viewHolder.day.setVisibility(View.VISIBLE);
            viewHolder.day.setText((position - dayofweek + 1) + "");
        } else {
            viewHolder.day.setVisibility(View.INVISIBLE);
        }

        //排期数据是否包含今日,设置可选不可选和颜色
        String monthString = "";
        if (month < 10)
            monthString = "0" + month;
        else
            monthString = month + "";
        String dayString = "";
        if (position - dayofweek + 1 < 10)
            dayString = "0" + (position - dayofweek + 1);
        else
            dayString = "" + (position - dayofweek + 1);
        if (choicedDay == null || !choicedDay.equals(year + "-" + monthString + "-" + dayString)) {
            if (map.get(year + "-" + monthString + "-" + (position - dayofweek + 1)) != null) {
                viewHolder.day.setTextColor(context.getResources().getColor(R.color.titleColor));
                convertView.setClickable(false);
            } else {
                viewHolder.day.setTextColor(context.getResources().getColor(R.color.gray));
                convertView.setClickable(true);
            }
            viewHolder.ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.ll.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_button_normal));
            viewHolder.day.setTextColor(context.getResources().getColor(R.color.white));
        }

        return convertView;

    }

    class ViewHolder {
        LinearLayout ll;
        TextView day;
    }

    /**
     * 判断每个月一号是周几
     * 用于显示时间的gridview前几项item不显示
     *
     * @return
     */
    private int spareNum(String pTime) {
        String week = CommonUtil.getWeek(pTime);
        if (week.equals("星期日")) {
            return 0;
        } else if (week.equals("星期一")) {
            return 1;
        } else if (week.equals("星期二")) {
            return 2;
        } else if (week.equals("星期三")) {
            return 3;
        } else if (week.equals("星期四")) {
            return 4;
        } else if (week.equals("星期五")) {
            return 5;
        } else if (week.equals("星期六")) {
            return 6;
        }
        return 0;
    }

    public void setchoicedDay(String choicedDay) {
        this.choicedDay = choicedDay;
    }
}
