package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pact.healthapp.R;

import java.util.List;

/**
 * Created by wangdong on 2015/12/18.
 */
public class OrgListAdapter4ListView extends BaseAdapter {
    private List<OrgListBean> list;
    private OrgListBean orgListBean;
    private Context context;
    private ChildOfItemClickCallBack mCallback;

    public OrgListAdapter4ListView(List<OrgListBean> list, Context context, ChildOfItemClickCallBack mCallback) {
        this.list = list;
        this.context = context;
        this.mCallback = mCallback;
    }

    @Override
    public int getCount() {
        return list.isEmpty() ? 0 : list.size();
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
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.appointment_choiceorg_listview_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.organization_center_name);
            viewHolder.organization_center = (LinearLayout) convertView
                    .findViewById(R.id.organization_center);
            viewHolder.organization_center_choice = (RadioButton) convertView
                    .findViewById(R.id.organization_center_choice);
            viewHolder.address = (TextView) convertView
                    .findViewById(R.id.organization_center_address);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.organization_center_time);
            viewHolder.phone = (TextView) convertView
                    .findViewById(R.id.organization_center_phone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //健管中心已选中过并进入预约流程,显示选中
        if (orgListBean != null
                && orgListBean.getOrgId().equals(list.get(position).getOrgId())) {
            viewHolder.organization_center_choice.setChecked(true);
        } else {
            viewHolder.organization_center_choice.setChecked(false);
        }
        viewHolder.name.setText(list.get(position).getOrgName());
        //选择机构
        viewHolder.organization_center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCallback.mCallback(position, v);
            }
        });
        //点击RadioButton和点击整行效果一样
        viewHolder.organization_center_choice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCallback.mCallback(position, v);
            }
        });

        viewHolder.address.setText(list.get(position).getAddress());
        if (list.get(position).getAddress() != null && !list.get(position).getAddress().equals("")) {

            //调用百度地图
            viewHolder.address.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mCallback.mCallback(position, v);
                }
            });
        }
        viewHolder.time.setText(list.get(position).getOpenTime());
        viewHolder.phone.setText(list.get(position).getOrgTel());
        if (list.get(position).getOrgTel() != null && !list.get(position).getOrgTel().equals("")) {
            //拨打电话
            viewHolder.phone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mCallback.mCallback(position, v);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView name; // 健管中心名字
        TextView address;// 健管中心地址
        TextView time;// 健管中心营业时间
        TextView phone;// 健管中心电话
        LinearLayout organization_center;//键管中心整行,点击使用
        RadioButton organization_center_choice;//健管中心是否选中
    }

    public void setOrgListBean(OrgListBean orgListBean) {
        this.orgListBean = orgListBean;
    }
}
