package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.serviceorder.ServiceOrderDetailBean;

import java.util.List;

/**
 * Created by wangdong on 2016/3/3.
 */
public class Packageitem2Adapter extends BaseAdapter {
    private List<ServiceOrderDetailBean.PackageDatasEntity> list;
    private Context context;

    public Packageitem2Adapter(Context context, List<ServiceOrderDetailBean.PackageDatasEntity> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
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
            convertView = LinearLayout.inflate(context,
                    R.layout.appointment_additem_listview_children_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.addItem_name = (TextView) convertView
                    .findViewById(R.id.addItem_name);
            viewHolder.addItem_cost = (TextView) convertView
                    .findViewById(R.id.addItem_price_market);
            viewHolder.addItem_description = (TextView) convertView
                    .findViewById(R.id.addItem_description);
            viewHolder.addItem_checked = (ImageView) convertView
                    .findViewById(R.id.addItem_checked);
            viewHolder.addItem_prices = (LinearLayout) convertView
                    .findViewById(R.id.addItem_prices);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        viewHolder.addItem_name.setText(list.get(position).getProductName());
        if (list.get(position).getProductDesc() == null
                || list.get(position).getProductDesc().length() == 0) {
            viewHolder.addItem_description.setVisibility(View.GONE);
        } else {
            viewHolder.addItem_description.setVisibility(View.VISIBLE);
            viewHolder.addItem_description.setText(list.get(position).getProductDesc());
        }
        viewHolder.addItem_cost.setVisibility(View.GONE);
        viewHolder.addItem_checked.setVisibility(View.GONE);
        viewHolder.addItem_prices.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHolder {
        TextView addItem_name;
        TextView addItem_cost;
        TextView addItem_description;
        ImageView addItem_checked;
        LinearLayout addItem_prices;
    }
}
