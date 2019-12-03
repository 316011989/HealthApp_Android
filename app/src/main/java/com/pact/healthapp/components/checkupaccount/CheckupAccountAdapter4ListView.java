package com.pact.healthapp.components.checkupaccount;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.ChildOfItemClickCallBack;

import java.util.List;

/**
 * Created by wangdong on 2016/1/27.
 */
public class CheckupAccountAdapter4ListView extends BaseExpandableListAdapter {

    private Context context;
    private List<CheckupAccountBean> list;
    private ChildOfItemClickCallBack callBack;
    private AccountCallBack aCallBack;
    private GroupCallBack gCallBack;
    private LayoutInflater inflater;

    public CheckupAccountAdapter4ListView(Context context, List<CheckupAccountBean> list, ChildOfItemClickCallBack callBack, AccountCallBack aCallBack, GroupCallBack gCallBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
        this.aCallBack = aCallBack;
        this.gCallBack = gCallBack;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return (list == null || list.isEmpty()) ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (list.get(groupPosition).getData() == null || list.get(groupPosition).getData().isEmpty())
                ? 0 : list.get(groupPosition).getData().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getData().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mycheckupaccount_listview_item_layout, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.account_ll = (LinearLayout) convertView
                    .findViewById(R.id.account_ll);
            groupViewHolder.account_name = (EditText) convertView
                    .findViewById(R.id.account_name);
            groupViewHolder.account_tv1 = (TextView) convertView
                    .findViewById(R.id.account_tv1);
            groupViewHolder.account_idcardnum = (TextView) convertView
                    .findViewById(R.id.account_idcardnum);
            groupViewHolder.account_idcardtype = (TextView) convertView
                    .findViewById(R.id.account_idcardtype);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final GroupViewHolder finalGroupViewHolder = groupViewHolder;
        //长按删除体检账户
        groupViewHolder.account_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finalGroupViewHolder.account_name.clearFocus();
                finalGroupViewHolder.account_ll.requestFocus();
                gCallBack.gCallBack(groupPosition, true);
                return true;
            }
        });
        //点击展开或则折叠
        groupViewHolder.account_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalGroupViewHolder.account_name.clearFocus();
                finalGroupViewHolder.account_ll.requestFocus();
                gCallBack.gCallBack(groupPosition, false);
            }
        });
        groupViewHolder.account_name.setText(list.get(groupPosition).getAccountName());

        //迁移焦点，修改账户名；清空账户名，显示原来的账户名
        groupViewHolder.account_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AccountData.accountName = finalGroupViewHolder
                            .account_name.getText().toString();
                    callBack.mCallback(groupPosition, v);
                }
            }
        });

        //点击软键盘“完成”，修改账户名；清空账户名，显示原来的账户名
        groupViewHolder.account_name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    finalGroupViewHolder
                            .account_name.clearFocus();
                    return true;
                }
                return false;
            }
        });


        if (list.get(groupPosition).getCardType().equals("0")) {
            groupViewHolder.account_tv1.setText("凭证号");
        } else {
            groupViewHolder.account_tv1.setText("证件号码");
        }
        groupViewHolder.account_idcardnum.setText(list.get(groupPosition).getCardNo());
        groupViewHolder.account_idcardtype.setText(list.get(groupPosition).getCardTypeName());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LinearLayout.inflate(context,
                    R.layout.mycheckupaccount_listview_voucher_item_layout, null);
            viewHolder = new ChildViewHolder();
            viewHolder.voucher_item_ll = (RelativeLayout) convertView
                    .findViewById(R.id.voucher_item_ll);
            viewHolder.voucher_item_vouchernum = (TextView) convertView
                    .findViewById(R.id.voucher_item_vouchernum);
            viewHolder.voucher_item_servetime = (TextView) convertView
                    .findViewById(R.id.voucher_item_servetime);
            viewHolder.voucher_item_img = (ImageView) convertView
                    .findViewById(R.id.voucher_item_img);
            viewHolder.voucher_item_effecttime = (TextView) convertView
                    .findViewById(R.id.voucher_item_effecttime);
            viewHolder.voucher_item_expiretime = (TextView) convertView
                    .findViewById(R.id.voucher_item_expiretime);
            viewHolder.voucher_item_tv1 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv1);
            viewHolder.voucher_item_tv2 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv2);
            viewHolder.voucher_item_payobject = (TextView) convertView
                    .findViewById(R.id.voucher_item_payobject);
            viewHolder.voucher_item_tv4 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv4);
            viewHolder.voucher_item_tv6 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv6);
            viewHolder.voucher_item_tv7 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv7);
            viewHolder.voucher_item_tv8 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv8);
            viewHolder.voucher_item_tv9 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv9);
            viewHolder.voucher_item_tv10 = (TextView) convertView
                    .findViewById(R.id.voucher_item_tv10);
            viewHolder.voucher_item_xinhuapay = (TextView) convertView
                    .findViewById(R.id.voucher_item_xinhuapay);
            viewHolder.voucher_item_qiyepay = (TextView) convertView
                    .findViewById(R.id.voucher_item_qiyepay);
            viewHolder.voucher_item_freepay = (TextView) convertView
                    .findViewById(R.id.voucher_item_freepay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        //凭证对象
        CheckupAccountBean cab = list.get(groupPosition);
        //服务类型
        if (list.get(groupPosition).getData().get(childPosition).getPackageCategoryName() != null) {
            viewHolder.voucher_item_servetime.setText(cab.getData().get(childPosition).getPackageCategoryName());
        }
        //付费对象
        String payobject = cab.getData().get(childPosition).getPaymentData().get(0).getPaymentObject();
        //凭证号
        viewHolder.voucher_item_vouchernum.setText(cab.getData().get(childPosition).getVoucherNo());
        //没有结算信息时，不显示付费方式，个人应付，个人已付。
        if (payobject.equals("") || payobject.equals("--")) {
            viewHolder.voucher_item_tv6.setVisibility(View.GONE);
            viewHolder.voucher_item_payobject.setVisibility(View.GONE);
            viewHolder.voucher_item_tv1.setVisibility(View.GONE);
            viewHolder.voucher_item_effecttime.setVisibility(View.GONE);
            viewHolder.voucher_item_tv4.setVisibility(View.GONE);
            viewHolder.voucher_item_tv10.setVisibility(View.GONE);
            viewHolder.voucher_item_tv2.setVisibility(View.GONE);
            viewHolder.voucher_item_expiretime.setVisibility(View.GONE);
            viewHolder.voucher_item_tv8.setVisibility(View.GONE);
            viewHolder.voucher_item_xinhuapay.setVisibility(View.GONE);
            viewHolder.voucher_item_tv7.setVisibility(View.GONE);
            viewHolder.voucher_item_qiyepay.setVisibility(View.GONE);
            viewHolder.voucher_item_tv9.setVisibility(View.GONE);
            viewHolder.voucher_item_freepay.setVisibility(View.GONE);
        } else {//有结算信息
            viewHolder.voucher_item_tv6.setVisibility(View.VISIBLE);
            viewHolder.voucher_item_payobject.setVisibility(View.VISIBLE);
            viewHolder.voucher_item_payobject.setText(payobject);
//            if (payobject.contains("个人付费")) {//个人显示所有价格
//                viewHolder.voucher_item_tv1.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_effecttime.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv4.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv10.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv2.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_expiretime.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv8.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_xinhuapay.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv7.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_qiyepay.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_tv9.setVisibility(View.VISIBLE);
//                viewHolder.voucher_item_freepay.setVisibility(View.VISIBLE);
//                //个人应付
//                viewHolder.voucher_item_effecttime.setText("¥" + CommonUtil.toPrice(cab.getData().get(childPosition).getPayableAmount()));
//                //个人已付
//                viewHolder.voucher_item_expiretime.setText("¥" + CommonUtil.toPrice(cab.getData().get(childPosition).getPaidAmount()));
//                //新华付费
//                viewHolder.voucher_item_xinhuapay.setText("¥" + CommonUtil.toPrice(cab.getData().get(childPosition).getPaymentData().get(0).getNciPaidAmount()));
//                //企业付费
//                viewHolder.voucher_item_qiyepay.setText("¥" + CommonUtil.toPrice(cab.getData().get(childPosition).getPaymentData().get(0).getCompanyPaidAmount()));
//                //免费
//                viewHolder.voucher_item_freepay.setText("¥" + CommonUtil.toPrice(cab.getData().get(childPosition).getPaymentData().get(0).getFreeAmount()));
//            } else {//企业付费时隐藏个人应付,个人已付,新华付费,企业付费,免费
            viewHolder.voucher_item_tv1.setVisibility(View.GONE);
            viewHolder.voucher_item_effecttime.setVisibility(View.GONE);
            viewHolder.voucher_item_tv4.setVisibility(View.GONE);
            viewHolder.voucher_item_tv10.setVisibility(View.GONE);
            viewHolder.voucher_item_tv2.setVisibility(View.GONE);
            viewHolder.voucher_item_expiretime.setVisibility(View.GONE);
            viewHolder.voucher_item_tv8.setVisibility(View.GONE);
            viewHolder.voucher_item_xinhuapay.setVisibility(View.GONE);
            viewHolder.voucher_item_tv7.setVisibility(View.GONE);
            viewHolder.voucher_item_qiyepay.setVisibility(View.GONE);
            viewHolder.voucher_item_tv9.setVisibility(View.GONE);
            viewHolder.voucher_item_freepay.setVisibility(View.GONE);
//            }
        }

        final ChildViewHolder finalViewHolder = viewHolder;
        if (cab.getData().get(childPosition).getIsAble().equals("1")) {//凭证号可用
            viewHolder.voucher_item_ll.setBackgroundResource(R.color.white);
            viewHolder.voucher_item_ll.setClickable(true);
            viewHolder.voucher_item_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//选择凭证进行预约
                    finalViewHolder.voucher_item_ll.requestFocus();
                    aCallBack.callBack(groupPosition, childPosition);
                }
            });
            viewHolder.voucher_item_img.setVisibility(View.INVISIBLE);
        } else if (cab.getData().get(childPosition).getIsAble().equals("2")) {//凭证号不可用
            viewHolder.voucher_item_ll.setBackgroundResource(R.color.gray_backgroud);
            viewHolder.voucher_item_ll.setClickable(false);
            viewHolder.voucher_item_img.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        LinearLayout account_ll;
        EditText account_name;
        TextView account_tv1;
        TextView account_idcardnum;
        TextView account_idcardtype;
    }

    class ChildViewHolder {
        RelativeLayout voucher_item_ll;
        TextView voucher_item_vouchernum;
        TextView voucher_item_servetime;
        ImageView voucher_item_img;
        TextView voucher_item_effecttime;
        TextView voucher_item_expiretime;
        TextView voucher_item_tv1;
        TextView voucher_item_tv2;
        TextView voucher_item_payobject;
        TextView voucher_item_tv4;
        TextView voucher_item_tv9;
        TextView voucher_item_tv10;
        TextView voucher_item_tv6;
        TextView voucher_item_xinhuapay;
        TextView voucher_item_tv7;
        TextView voucher_item_qiyepay;
        TextView voucher_item_tv8;
        TextView voucher_item_freepay;
    }

    //选择凭证进行预约
    public interface AccountCallBack {
        void callBack(int groupPosition, int childPosition);
    }

    /**
     * flag为true时，长按group栏删除体检账户
     * flag为false时，点击group栏进行child栏展开和折叠
     */
    public interface GroupCallBack {
        void gCallBack(int position, boolean flag);
    }

//    //输入框文字超长时间，转换字符内容
//    public String get12Sub(String value) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < value.length(); i++) {
//            // 获取一个字符
//            String temp = value.substring(i, i + 1);
//            sb.append(temp);
//            Paint paint = new Paint();
//            Rect rect = new Rect();
//            paint.getTextBounds(sb.toString(), 0, sb.toString().length(), rect);
//            if (rect.width() >= 118) {
//                sb.append("…");
//                Log.i("aaa", sb.toString());
//                return sb.toString();
//            }
//        }
//        return value;
//    }
}
