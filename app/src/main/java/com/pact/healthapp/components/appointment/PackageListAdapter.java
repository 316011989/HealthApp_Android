package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.MyApplication;
import com.pact.healthapp.utils.CommonUtil;

import java.util.ArrayList;

/**
 * Created by 张一龙 on 2015/12/24.
 */
public class PackageListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PackageBean> list;
    private PackageBean packageBean;
    private ChildOfItemClickCallBack listener1;

    public PackageListAdapter(Context context, ArrayList<PackageBean> list, ChildOfItemClickCallBack listener1) {
        this.context = context;
        this.list = list;
        this.listener1 = listener1;
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
                    R.layout.appointment_checkupset_item, null);
            viewHolder = new ViewHolder();
            viewHolder.checkupset_item_image = (ImageView) convertView
                    .findViewById(R.id.checkupset_item_image);
            viewHolder.checkupset_item_packagename = (TextView) convertView
                    .findViewById(R.id.checkupset_item_packagename);
            viewHolder.checkupset_item_tv3 = (TextView) convertView
                    .findViewById(R.id.checkupset_item_tv3);
            viewHolder.checkupset_item_payable = (TextView) convertView
                    .findViewById(R.id.checkupset_item_payable);
            viewHolder.checkupset_item_payed = (TextView) convertView
                    .findViewById(R.id.checkupset_item_payed);
            viewHolder.checkupset_item_btn1 = (RadioButton) convertView
                    .findViewById(R.id.checkupset_item_btn1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).getMediaUrl() == null
                || list.get(position).getMediaUrl().length() == 0) {
            viewHolder.checkupset_item_image.setVisibility(View.GONE);
        } else {
            viewHolder.checkupset_item_image.setVisibility(View.VISIBLE);
            MyApplication.bitmapUtils.display(viewHolder.checkupset_item_image,
                    list.get(position).getMediaUrl());
        }

        viewHolder.checkupset_item_packagename.setText(list.get(position).getPackageName());
        if (list.get(position).getPayObject().equals("1")) {
            viewHolder.checkupset_item_payable.setText("企业付费");
            viewHolder.checkupset_item_payed.setVisibility(View.INVISIBLE);
        } else if (list.get(position).getPayObject().equals("3")) {
            viewHolder.checkupset_item_payable.setText("新华付费");
            viewHolder.checkupset_item_payed.setVisibility(View.INVISIBLE);
        } else if (list.get(position).getPayObject().equals("2")) {
            viewHolder.checkupset_item_payable.setText("个人付费");
            if (Double.parseDouble(list.get(position).getMarketPrice()) == 0) {
                viewHolder.checkupset_item_payed.setVisibility(View.VISIBLE);
                viewHolder.checkupset_item_payed.setText("¥ " + 0);
            } else {
                viewHolder.checkupset_item_payed.setVisibility(View.VISIBLE);
                viewHolder.checkupset_item_payed.setText("¥ " + CommonUtil.toPrice(list.get(position).getMarketPrice()));
            }
        }

        if (packageBean != null
                && packageBean.getPackageId().equals(list.get(position).getPackageId())) {
            viewHolder.checkupset_item_btn1.setChecked(true);
        } else {
            viewHolder.checkupset_item_btn1.setChecked(false);
        }

        viewHolder.checkupset_item_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.mCallback(position, v);
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView checkupset_item_image;
        TextView checkupset_item_packagename;
        TextView checkupset_item_tv3;
        TextView checkupset_item_payable;
        TextView checkupset_item_payed;
        RadioButton checkupset_item_btn1;
    }

    public void setPackageBean(PackageBean packageBean) {
        this.packageBean = packageBean;
    }

}
