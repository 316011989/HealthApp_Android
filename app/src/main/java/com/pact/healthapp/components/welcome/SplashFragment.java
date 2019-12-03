package com.pact.healthapp.components.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;

public class SplashFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.login_logo_layout, null);
		return view;
	}



}
