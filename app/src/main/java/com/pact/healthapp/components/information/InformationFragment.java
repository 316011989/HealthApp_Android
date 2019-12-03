package com.pact.healthapp.components.information;

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
import java.util.Random;

public class InformationFragment extends BaseFragment {
	@ViewInject(R.id.information_listView)
	ListView information_listView;
	InformationAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.information_information_layout,
				null);
		ViewUtils.inject(this, view);
		ArrayList<InformationBean> list = getList();
		adapter = new InformationAdapter(getActivity(), list);
		information_listView.setAdapter(adapter);
		return view;
	}

	// **生成数据
	private ArrayList<InformationBean> getList() {
		ArrayList<InformationBean> list = new ArrayList<InformationBean>();
		Random random = new Random();
		int num = 3 + random.nextInt(6);
		for (int i = 0; i < num; i++) {
			InformationBean informationBean = new InformationBean();
			informationBean.setImageId(R.mipmap.defalt_pic);
			informationBean.setNum((1 + random.nextInt(98)) + "");
			informationBean.setTitle("消息" + i);
			if (i % 2 == 0)
				informationBean.setContent("养娃问题多");
			else
				informationBean.setContent("爸妈有话说");
			list.add(informationBean);
		}
		return list;
	}
}
