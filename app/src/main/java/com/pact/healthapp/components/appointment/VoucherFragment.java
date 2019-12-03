/**
 *
 */
package com.pact.healthapp.components.appointment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;

import java.util.ArrayList;

/**
 * 凭证
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class VoucherFragment extends BaseFragment implements ChildOfItemClickCallBack {
    @ViewInject(R.id.appointment_voucher_list)
    private ListView appointment_voucher_list;
    private VoucherAdapter adapter;
    private ArrayList<VoucherBean> voucherlist;

    @ViewInject(R.id.btn_next)
    private Button btn_next;

    public VoucherBean voucherBean;//当前页面选中的凭证

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_voucher_layout, null);
        ViewUtils.inject(this, view);
        getActivity().setTitle("凭证");
        voucherlist = (ArrayList<VoucherBean>) ServiceOrderData.vouchers;
        adapter = new VoucherAdapter(context, voucherlist, this);
        appointment_voucher_list.setAdapter(adapter);
        if (ServiceOrderData.vb == null) {
            if (voucherBean != null) {
                adapter.setChoicedVoucherBean(voucherBean);
                adapter.notifyDataSetChanged();
                btn_next.setEnabled(true);
                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            } else {
                btn_next.setEnabled(false);
                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.not_click_btn_shape));
            }
        } else {
            voucherBean = ServiceOrderData.vb;
            btn_next.setEnabled(true);
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
            adapter.setChoicedVoucherBean(voucherBean);
            adapter.notifyDataSetChanged();
        }
        appointment_voucher_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                voucherBean = voucherlist.get(position);
                adapter.setChoicedVoucherBean(voucherBean);
                btn_next.setEnabled(true);
                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                adapter.notifyDataSetChanged();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voucherBean != null) {
                    if (!voucherBean.equals(ServiceOrderData.vb))
                        ServiceOrderData.setVb(voucherBean);
                    ((AppointmentActivity) getActivity()).toStep3();
                    voucherBean = null;
                } else {
                    if (ServiceOrderData.vb!= null) {
                        ((AppointmentActivity) getActivity()).toStep3();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void mCallback(int position, View v) {
        switch (v.getId()) {
            case R.id.voucher_item_btn:
                //没选择过凭证或上次选择与本次不同
                if (voucherBean == null || !voucherlist.get(position).equals(voucherBean)) {
                    voucherBean = voucherlist.get(position);
                    adapter.setChoicedVoucherBean(voucherBean);
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button_selector));
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
