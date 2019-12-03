package com.pact.healthapp.components.serviceorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.ChildOfItemClickCallBack;
import com.pact.healthapp.utils.CommonUtil;

import java.util.List;

/**
 * Created by wangdong on 2016/1/11.
 */
public class ServiceOrderAdapter extends BaseAdapter {
    private List<ServiceOrderListBean> list;
    private Context context;
    private boolean allFlag;
    private ChildOfItemClickCallBack callBack;
    private LayoutInflater inflater;

    public ServiceOrderAdapter(Context context, List<ServiceOrderListBean> list, ChildOfItemClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.serviceorder_listview_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.serviceorder_num.setText(list.get(position).getServiceOrderNo());
        viewHolder.serviceorder_checkupuser.setText(list.get(position).getCustomerName());
        viewHolder.serviceorder_package.setText(list.get(position).getPackageName());

        //体检项最多显示三个，若超出一行，只显示一行数据
        if (list.get(position).getAttchObjList().size() == 0) {
            viewHolder.serviceorder_additem1.setText("");
            viewHolder.serviceorder_additem2.setText("");
            viewHolder.serviceorder_additem3.setText("");
        } else if (list.get(position).getAttchObjList().size() == 1) {
            viewHolder.serviceorder_additem1.setText(list.get(position).getAttchObjList().get(0));
            viewHolder.serviceorder_additem2.setText("");
            viewHolder.serviceorder_additem3.setText("");
        } else if (list.get(position).getAttchObjList().size() == 2) {
            viewHolder.serviceorder_additem1.setText(list.get(position).getAttchObjList().get(0));
            viewHolder.serviceorder_additem2.setText(list.get(position).getAttchObjList().get(1));
            viewHolder.serviceorder_additem3.setText("");
        } else if (list.get(position).getAttchObjList().size() == 3) {
            viewHolder.serviceorder_additem1.setText(list.get(position).getAttchObjList().get(0));
            viewHolder.serviceorder_additem2.setText(list.get(position).getAttchObjList().get(1));
            viewHolder.serviceorder_additem3.setText(list.get(position).getAttchObjList().get(2));
        }

        if (list.get(position).getServiceOrderStatusId().equals("1")) {//未预约 不显示时间
            viewHolder.ll4.setVisibility(View.GONE);
        } else if (list.get(position).getServiceOrderStatusId().equals("2")) {//已预约 显示预约时间 yyyy-MM-dd HH:mm
            viewHolder.ll4.setVisibility(View.VISIBLE);
            String time = CommonUtil.convertTime1(list.get(position).getAppointmentServiceDate(),
                    list.get(position).getAppointmentStartHour(), list.get(position).getAppointmentStartMinute(),
                    list.get(position).getAppointmentEndHour(), list.get(position).getAppointmentEndMinute());
            viewHolder.serviceorder_date.setText(time);
        } else if (list.get(position).getServiceOrderStatusId().equals("3")) {//已到检 显示到检时间
            viewHolder.ll4.setVisibility(View.VISIBLE);
            viewHolder.serviceorder_date.setText(list.get(position).getActualServiceTime().substring(0, 16));
        }

        viewHolder.serviceorder_organization.setText(list.get(position).getOrgName());
        viewHolder.serviceorder_address.setText(list.get(position).getAddress());
        if (list.get(position).getPaymentMethodId().equals("1")) {
            viewHolder.serviceorder_paymentMethod.setText("在线支付");
        } else if (list.get(position).getPaymentMethodId().equals("2")) {
            viewHolder.serviceorder_paymentMethod.setText("前台支付");
        }

        viewHolder.serviceorder_payable.setText("¥" + CommonUtil.toPrice(list.get(position).getPayableAmount()));
        viewHolder.serviceorder_paid.setText("¥" + CommonUtil.toPrice(list.get(position).getPaidAmount()));
        viewHolder.serviceorder_refund.setText("¥" + CommonUtil.toPrice(list.get(position).getRefundAmount()));
        if (list.get(position).getUnPayAmount() != null && Double.parseDouble(list.get(position).getUnPayAmount()) > 0) {
            viewHolder.serviceorder_unpaid.setText("¥" + CommonUtil.toPrice(list.get(position).getUnPayAmount()));
//            viewHolder.serviceorder_paymentMethod_ll.setVisibility(View.VISIBLE);
        } else {
            viewHolder.serviceorder_unpaid.setText("¥0.00");
//            viewHolder.serviceorder_paymentMethod_ll.setVisibility(View.GONE);
        }


        //服务单明细
        viewHolder.serviceorder_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.mCallback(position, v);
            }
        });

        if (list.get(position).getServiceEndDate() != null
                && (CommonUtil.getStampTime(list.get(position).getServiceEndDate())
                < System.currentTimeMillis())) {//服务单已过期
            viewHolder.out_of_time_tag.setVisibility(View.VISIBLE);
            viewHolder.serviceorder_appointment.setVisibility(View.GONE);
            viewHolder.serviceorder_cancel_appointment.setVisibility(View.GONE);
            viewHolder.serviceorder_changedate.setVisibility(View.GONE);
            viewHolder.serviceorder_pay.setVisibility(View.GONE);
        } else {//服务单未过期
            viewHolder.out_of_time_tag.setVisibility(View.INVISIBLE);
            String statusId = list.get(position).getServiceOrderStatusId();//服务单状态
            String paymentStatus = list.get(position).getPaymentStatus();//支付状态
            String paymentMethodId = list.get(position).getPaymentMethodId();//支付方式
            if (statusId.equals("1")) {//未预约
                viewHolder.serviceorder_appointment.setVisibility(View.VISIBLE);
                viewHolder.serviceorder_cancel_appointment.setVisibility(View.GONE);
                viewHolder.serviceorder_changedate.setVisibility(View.GONE);
                if (paymentStatus.equals("1") || paymentStatus.equals("3")) {//未支付
                    if (paymentMethodId.equals("1")) {//在线支付
                        viewHolder.serviceorder_pay.setVisibility(View.VISIBLE);
                    } else if (paymentMethodId.equals("2")) {//前台支付
                        viewHolder.serviceorder_pay.setVisibility(View.GONE);
                    }
                } else if (paymentStatus.equals("2")) {//已支付
                    viewHolder.serviceorder_pay.setVisibility(View.GONE);
                }
            } else if (statusId.equals("2")) {//已预约
                viewHolder.serviceorder_appointment.setVisibility(View.GONE);
                viewHolder.serviceorder_changedate.setVisibility(View.VISIBLE);
                viewHolder.serviceorder_cancel_appointment.setVisibility(View.VISIBLE);
                if (paymentStatus.equals("1") || paymentStatus.equals("3")) {//未支付
                    if (paymentMethodId.equals("1")) {//在线支付
                        viewHolder.serviceorder_pay.setVisibility(View.VISIBLE);
                    } else if (paymentMethodId.equals("2")) {//前台支付
                        viewHolder.serviceorder_pay.setVisibility(View.GONE);
                    }
                } else if (paymentStatus.equals("2")) {//已支付
                    viewHolder.serviceorder_pay.setVisibility(View.GONE);
                }
            } else if (statusId.equals("3")) {//已到检
                viewHolder.serviceorder_appointment.setVisibility(View.GONE);
                viewHolder.serviceorder_cancel_appointment.setVisibility(View.GONE);
                viewHolder.serviceorder_changedate.setVisibility(View.GONE);
                if (paymentStatus.equals("1") || paymentStatus.equals("3")) {//未支付
                    if (paymentMethodId.equals("1")) {//在线支付
                        viewHolder.serviceorder_pay.setVisibility(View.VISIBLE);
                    } else if (paymentMethodId.equals("2")) {//前台支付
                        viewHolder.serviceorder_pay.setVisibility(View.GONE);
                    }
                } else if (paymentStatus.equals("2")) {//已支付
                    viewHolder.serviceorder_pay.setVisibility(View.GONE);
                }
            } else if (statusId.equals("4")) {//已取消
                viewHolder.serviceorder_appointment.setVisibility(View.GONE);
                viewHolder.serviceorder_cancel_appointment.setVisibility(View.GONE);
                viewHolder.serviceorder_pay.setVisibility(View.GONE);
                viewHolder.serviceorder_changedate.setVisibility(View.GONE);
            }
        }

        //服务单状态
        if (allFlag) {
            viewHolder.serviceorder_status_tag1.setText(list.get(position).getServiceOrderStatusName());
            viewHolder.serviceorder_status_tag1.setVisibility(View.VISIBLE);
        } else {
            viewHolder.serviceorder_status_tag1.setVisibility(View.GONE);
        }

        //支付状态
        if (list.get(position).getPaymentStatus().equals("1")
                || list.get(position).getPaymentStatus().equals("3")) {//未支付 部分支付
            viewHolder.serviceorder_status_tag2.setText("未支付");
            viewHolder.serviceorder_status_tag2.setTextColor(context.getResources().getColor(R.color.green_common));
        } else if (list.get(position).getPaymentStatus().equals("2")) {//已支付
            viewHolder.serviceorder_status_tag2.setText("已支付");
            viewHolder.serviceorder_status_tag2.setTextColor(context.getResources().getColor(R.color.orange_light));
        }

        //预约
        viewHolder.serviceorder_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.mCallback(position, v);
            }
        });
        //取消预约
        viewHolder.serviceorder_cancel_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.mCallback(position, v);
            }
        });
        //去支付
        viewHolder.serviceorder_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.mCallback(position, v);
            }
        });
        //改期
        viewHolder.serviceorder_changedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.mCallback(position, v);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView out_of_time_tag;//是否过期
        TextView serviceorder_num;//服务单号
        TextView serviceorder_status_tag1;//服务单状态
        TextView serviceorder_status_tag2;//支付状态
        RelativeLayout serviceorder_detail;//服务单明细
        TextView serviceorder_checkupuser;//体检人
        TextView serviceorder_package;//体检套餐
        LinearLayout serviceorder_additem;//已选体检项
        TextView serviceorder_additem1;
        TextView serviceorder_additem2;
        TextView serviceorder_additem3;
        LinearLayout ll4;
        TextView serviceorder_date;//体检时间
        TextView serviceorder_organization;//体检机构
        TextView serviceorder_address;//体检地点
        LinearLayout serviceorder_paymentMethod_ll;
        TextView serviceorder_paymentMethod;//支付方式
        LinearLayout serviceorder_ll1;
        TextView tv1;
        TextView serviceorder_payable;//个人应付
        TextView tv2;
        TextView serviceorder_paid;//个人已付
        LinearLayout serviceorder_ll2;
        TextView tv3;
        TextView serviceorder_refund;//退款
        TextView tv4;
        TextView serviceorder_unpaid;//个人未付
        TextView serviceorder_appointment;//预约
        TextView serviceorder_cancel_appointment;//取消预约
        TextView serviceorder_pay;//去支付
        TextView serviceorder_changedate;//改期

        public ViewHolder(View view) {
            out_of_time_tag = (ImageView) view.findViewById(R.id.out_of_time_tag);
            serviceorder_num = (TextView) view.findViewById(R.id.serviceorder_num);
            serviceorder_status_tag1 = (TextView) view.findViewById(R.id.serviceorder_status_tag1);
            serviceorder_status_tag2 = (TextView) view.findViewById(R.id.serviceorder_status_tag2);
            serviceorder_detail = (RelativeLayout) view.findViewById(R.id.serviceorder_detail);
            serviceorder_checkupuser = (TextView) view.findViewById(R.id.serviceorder_checkupuser);
            serviceorder_package = (TextView) view.findViewById(R.id.serviceorder_package);
            serviceorder_additem = (LinearLayout) view.findViewById(R.id.serviceorder_additem);
            serviceorder_additem1 = (TextView) view.findViewById(R.id.serviceorder_additem1);
            serviceorder_additem2 = (TextView) view.findViewById(R.id.serviceorder_additem2);
            serviceorder_additem3 = (TextView) view.findViewById(R.id.serviceorder_additem3);
            ll4 = (LinearLayout) view.findViewById(R.id.ll4);
            serviceorder_date = (TextView) view.findViewById(R.id.serviceorder_date);
            serviceorder_organization = (TextView) view.findViewById(R.id.serviceorder_organization);
            serviceorder_address = (TextView) view.findViewById(R.id.serviceorder_address);
            serviceorder_paymentMethod_ll = (LinearLayout) view.findViewById(R.id.serviceorder_paymentMethod_ll);
            serviceorder_paymentMethod = (TextView) view.findViewById(R.id.serviceorder_paymentMethod);
            serviceorder_ll1 = (LinearLayout) view.findViewById(R.id.serviceorder_ll1);
            tv1 = (TextView) view.findViewById(R.id.tv1);
            serviceorder_payable = (TextView) view.findViewById(R.id.serviceorder_payable);
            tv2 = (TextView) view.findViewById(R.id.tv2);
            serviceorder_paid = (TextView) view.findViewById(R.id.serviceorder_paid);
            serviceorder_ll2 = (LinearLayout) view.findViewById(R.id.serviceorder_ll2);
            tv3 = (TextView) view.findViewById(R.id.tv3);
            serviceorder_refund = (TextView) view.findViewById(R.id.serviceorder_refund);
            tv4 = (TextView) view.findViewById(R.id.tv4);
            serviceorder_unpaid = (TextView) view.findViewById(R.id.serviceorder_unpaid);
            serviceorder_appointment = (TextView) view.findViewById(R.id.serviceorder_appointment);
            serviceorder_cancel_appointment = (TextView) view.findViewById(R.id.serviceorder_cancel_appointment);
            serviceorder_pay = (TextView) view.findViewById(R.id.serviceorder_pay);
            serviceorder_changedate = (TextView) view.findViewById(R.id.serviceorder_changedate);
        }
    }

    public void setAllFlag(boolean allFlag) {
        this.allFlag = allFlag;
    }
}
