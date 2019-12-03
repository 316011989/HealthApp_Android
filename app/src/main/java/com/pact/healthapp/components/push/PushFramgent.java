package com.pact.healthapp.components.push;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;

import java.util.ArrayList;

public class PushFramgent extends BaseFragment {
	public static ArrayList<PushBean> list = new ArrayList<PushBean>();
	PushAdapter adapter;
	@ViewInject(R.id.push_listView)
	private ListView pushListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.push_layout, null);
		ViewUtils.inject(this, view);
		adapter = new PushAdapter(getActivity(), list);
		pushListView.setAdapter(adapter);
		return view;
	}

	public void notifyListView(PushBean pushBean) {
		list.add(0, pushBean);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}
}
