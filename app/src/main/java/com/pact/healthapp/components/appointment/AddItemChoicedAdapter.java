package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.utils.CommonUtil;

import java.util.List;

/**
 * Created by zhangyl on 2016/2/23.
 */
public class AddItemChoicedAdapter implements ExpandableListAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private Context context;
    private List<AddItemBean> list;
    private LayoutInflater inflater;

    public AddItemChoicedAdapter(Context context, List<AddItemBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public int getGroupCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getPackageDatas().isEmpty() ? 0 : list.get(groupPosition).getPackageDatas().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getPackageDatas().get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.appointment_additem_listview_parent_layout, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.addItems_rl = (LinearLayout) convertView.findViewById(R.id.addItems_rl);
            groupViewHolder.addItems_name = (TextView) convertView.findViewById(R.id.addItems_name);
            groupViewHolder.ismust = (TextView) convertView.findViewById(R.id.ismust);
            groupViewHolder.addItems_checked = (ImageView) convertView.findViewById(R.id.addItems_checked);
            groupViewHolder.isExpand = (ImageView) convertView.findViewById(R.id.isExpand);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupViewHolder.isExpand.setBackgroundResource(R.mipmap.up_img);
        } else {
            groupViewHolder.isExpand.setBackgroundResource(R.mipmap.down_img);
        }
        groupViewHolder.addItems_checked.setBackgroundResource(R.mipmap.additems_delete);
        AddItemBean addItemBean = list.get(groupPosition);
        groupViewHolder.addItems_checked.setOnClickListener(new GroupCBOnCkickListener(addItemBean));
        String packageName = addItemBean.getPackageName();
        groupViewHolder.addItems_name.setText(packageName);
        if (list.get(groupPosition).getPackageDatas().get(0).getMust()) {//必选
            groupViewHolder.ismust.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.ismust.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.appointment_additem_listview_children_layout, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.addItem_name = (TextView) convertView.findViewById(R.id.addItem_name);
            childViewHolder.addItem_description = (TextView) convertView.findViewById(R.id.addItem_description);
            childViewHolder.addItem_checked = (ImageView) convertView.findViewById(R.id.addItem_checked);
            childViewHolder.addItem_prices = (LinearLayout) convertView.findViewById(R.id.addItem_prices);
            childViewHolder.addItem_price_market = (TextView) convertView.findViewById(R.id.addItem_price_market);
            childViewHolder.addItem_price_sell = (TextView) convertView.findViewById(R.id.addItem_price_sell);
            childViewHolder.addItem_pay_object = (TextView) convertView.findViewById(R.id.addItem_pay_object);
            childViewHolder.addItem_childdivider = convertView.findViewById(R.id.addItem_childdivider);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.addItem_checked.setBackgroundResource(R.mipmap.additems_delete);
        childViewHolder.addItem_description.setVisibility(View.GONE);
        AddItemBean.PackageDatasEntity additem = list.get(groupPosition).getPackageDatas().get(childPosition);
        childViewHolder.addItem_name.setText(additem.getProductName());

        /**
         * 整包计价最后一项显示价格(代表包价格),累计计价和散项全部显示体检项价格
         * 0：整包计价  1：累计计价
         */
        if (additem.getAccountType().equals("0") && list.get(groupPosition).getPackageDatas().size() - 1 != childPosition) {
            //整包计价包非最后一项
            childViewHolder.addItem_prices.setVisibility(View.GONE);
            childViewHolder.addItem_childdivider.setVisibility(View.VISIBLE);
        } else if (additem.getAccountType().equals("0") && list.get(groupPosition).getPackageDatas().size() - 1 == childPosition) {
            //整包计价包最后一项
            childViewHolder.addItem_childdivider.setVisibility(View.GONE);
            childViewHolder.addItem_prices.setVisibility(View.VISIBLE);
            //付费对象
            if (additem.getPayObject().contains("2")) {
                childViewHolder.addItem_price_market.setVisibility(View.VISIBLE);
                childViewHolder.addItem_price_sell.setVisibility(View.VISIBLE);
                childViewHolder.addItem_pay_object.setVisibility(View.GONE);
                childViewHolder.addItem_price_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                childViewHolder.addItem_price_market.setText("¥" + CommonUtil.toPrice(additem.getPriceMarket()));
                childViewHolder.addItem_price_sell.setText("¥" + CommonUtil.toPrice(additem.getPriceSell()));
            } else {
                childViewHolder.addItem_price_market.setVisibility(View.GONE);
                childViewHolder.addItem_price_sell.setVisibility(View.GONE);
                childViewHolder.addItem_pay_object.setVisibility(View.VISIBLE);
                if (additem.getPayObject().equals("1")) {
                    childViewHolder.addItem_pay_object.setText("企业付费");
                } else if (additem.getPayObject().equals("3")) {
                    childViewHolder.addItem_pay_object.setText("新华付费");
                } else {
                    childViewHolder.addItem_pay_object.setText("企业付费+新华付费");
                }
            }
        } else if (additem.getAccountType().equals("1") && list.get(groupPosition).getPackageDatas().size() - 1 != childPosition) {
            //累计计价包非最后一项
            childViewHolder.addItem_childdivider.setVisibility(View.VISIBLE);
            childViewHolder.addItem_prices.setVisibility(View.VISIBLE);
            //付费对象
            if (additem.getPayObject().contains("2")) {
                childViewHolder.addItem_price_market.setVisibility(View.VISIBLE);
                childViewHolder.addItem_price_sell.setVisibility(View.VISIBLE);
                childViewHolder.addItem_pay_object.setVisibility(View.GONE);
                childViewHolder.addItem_price_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                childViewHolder.addItem_price_market.setText("¥" + CommonUtil.toPrice(additem.getPriceMarket()));
                childViewHolder.addItem_price_sell.setText("¥" + CommonUtil.toPrice(additem.getPriceSell()));
            } else {
                childViewHolder.addItem_price_market.setVisibility(View.GONE);
                childViewHolder.addItem_price_sell.setVisibility(View.GONE);
                childViewHolder.addItem_pay_object.setVisibility(View.VISIBLE);
                if (additem.getPayObject().equals("1")) {
                    childViewHolder.addItem_pay_object.setText("企业付费");
                } else if (additem.getPayObject().equals("3")) {
                    childViewHolder.addItem_pay_object.setText("新华付费");
                } else {
                    childViewHolder.addItem_pay_object.setText("企业付费+新华付费");
                }
            }
        } else if (additem.getAccountType().equals("1") && list.get(groupPosition).getPackageDatas().size() - 1 == childPosition) {
            //累计计价包最后一项
            childViewHolder.addItem_childdivider.setVisibility(View.GONE);
            childViewHolder.addItem_prices.setVisibility(View.VISIBLE);
            //付费对象
            if (additem.getPayObject().contains("2")) {
                childViewHolder.addItem_price_market.setVisibility(View.VISIBLE);
                childViewHolder.addItem_price_sell.setVisibility(View.VISIBLE);
                childViewHolder.addItem_pay_object.setVisibility(View.GONE);
                childViewHolder.addItem_price_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                childViewHolder.addItem_price_market.setText("¥" + CommonUtil.toPrice(additem.getPriceMarket()));
                childViewHolder.addItem_price_sell.setText("¥" + CommonUtil.toPrice(additem.getPriceSell()));
            } else {
                childViewHolder.addItem_price_market.setVisibility(View.GONE);
                childViewHolder.addItem_price_sell.setVisibility(View.GONE);
                childViewHolder.addItem_pay_object.setVisibility(View.VISIBLE);
                if (additem.getPayObject().equals("1")) {
                    childViewHolder.addItem_pay_object.setText("企业付费");
                } else if (additem.getPayObject().equals("3")) {
                    childViewHolder.addItem_pay_object.setText("新华付费");
                } else {
                    childViewHolder.addItem_pay_object.setText("企业付费+新华付费");
                }
            }
        } else {
            //散项
            convertView.setBackgroundResource(R.color.white);
            childViewHolder.addItem_childdivider.setBackgroundResource(R.drawable.line_dash);

            childViewHolder.addItem_prices.setVisibility(View.VISIBLE);
            //付费对象
            if (additem.getPayObject().contains("2")) {
                childViewHolder.addItem_price_market.setVisibility(View.VISIBLE);
                childViewHolder.addItem_price_sell.setVisibility(View.VISIBLE);
                childViewHolder.addItem_pay_object.setVisibility(View.GONE);
                childViewHolder.addItem_price_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                childViewHolder.addItem_price_market.setText("¥" + CommonUtil.toPrice(additem.getPriceMarket()));
                childViewHolder.addItem_price_sell.setText("¥" + CommonUtil.toPrice(additem.getPriceSell()));
            } else {
                childViewHolder.addItem_price_market.setVisibility(View.GONE);
                childViewHolder.addItem_price_sell.setVisibility(View.GONE);
                childViewHolder.addItem_pay_object.setVisibility(View.VISIBLE);
                if (additem.getPayObject().equals("1")) {
                    childViewHolder.addItem_pay_object.setText("企业付费");
                } else if (additem.getPayObject().equals("3")) {
                    childViewHolder.addItem_pay_object.setText("新华付费");
                } else {
                    childViewHolder.addItem_pay_object.setText("企业付费+新华付费");
                }
            }
        }

        //选中状态修改时判断是否该原子项所属的包中所有原子项都选中,进而改变体检项包的选中状态
        childViewHolder.addItem_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ServiceOrderData.checkedChildren.contains(list.get(groupPosition).getPackageDatas().get(childPosition))) {
                    ServiceOrderData.checkedChildren.remove(list.get(groupPosition).getPackageDatas().get(childPosition));//直接移除原子项
                    list.get(groupPosition).getPackageDatas().remove(list.get(groupPosition).getPackageDatas().get(childPosition));//移除包中原子项
                    for (int i = 0; i < ServiceOrderData.checkedParents.size(); i++) {
                        if (ServiceOrderData.checkedParents.get(i).getPackageName().equals(list.get(groupPosition).getPackageName())) {
                            ServiceOrderData.checkedParents.remove(i);
                        }
                    }
                }
                setGroupItemCheckedState(list.get(groupPosition));
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    /**
     * 根据childview选中状态改变groupview的选中状态
     *
     * @param groupItem
     */
    private void setGroupItemCheckedState(AddItemBean groupItem) {
        if (groupItem.getPackageDatas() == null || groupItem.getPackageDatas().size() == 0) {
            list.remove(groupItem);
        }
        notifyDataSetChanged();
    }


    private static class GroupViewHolder {
        LinearLayout addItems_rl;
        TextView addItems_name;
        TextView ismust;
        ImageView addItems_checked;
        ImageView isExpand;
    }

    private static class ChildViewHolder {
        TextView addItem_name;
        TextView addItem_description;
        ImageView addItem_checked;
        LinearLayout addItem_prices;
        TextView addItem_price_market;
        TextView addItem_price_sell;
        TextView addItem_pay_object;
        View addItem_childdivider;
    }

    public class GroupCBOnCkickListener implements View.OnClickListener {

        private AddItemBean groupItem;

        public GroupCBOnCkickListener(AddItemBean groupItem) {
            this.groupItem = groupItem;
        }

        @Override
        public void onClick(View v) {
            if (list.contains(groupItem)) {
                list.remove(groupItem);
                for (AddItemBean.PackageDatasEntity childrenItem : groupItem.getPackageDatas()) {
                    if (ServiceOrderData.checkedChildren.contains(childrenItem)) {
                        ServiceOrderData.checkedChildren.remove(childrenItem);
                    }
                }
            }
            if (ServiceOrderData.checkedParents.contains(groupItem)) {
                ServiceOrderData.checkedParents.remove(groupItem);
                for (AddItemBean.PackageDatasEntity childrenItem : groupItem.getPackageDatas()) {
                    if (ServiceOrderData.checkedChildren.contains(childrenItem)) {
                        ServiceOrderData.checkedChildren.remove(childrenItem);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
