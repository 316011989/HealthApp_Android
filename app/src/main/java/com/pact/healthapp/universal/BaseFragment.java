package com.pact.healthapp.universal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.pact.healthapp.view.CommonToast;

/**
 * 基类Fragment,可自动继承各变量
 *
 * @author zhangyl
 */
public class BaseFragment extends Fragment {
    protected FragmentTransaction mFragmentTransaction;
    protected MyApplication myApplication;
    protected Context context;
    protected SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        mFragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        myApplication = MyApplication.myApplication;
        db = myApplication.db;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (CommonToast.getInstance() != null)
            CommonToast.getInstance().cancel();

    }
}
