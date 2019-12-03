package com.pact.healthapp.components.checkupaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;

/**
 * 亲情账户
 * Created by wangdong on 2016/1/7.
 */
public class CheckupAccountActivity extends BaseFragmentActivity {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContenierView(1);
        myApplication.addActivity(this);
        setTitle("亲情账户");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.GONE);
        if (!sp.getLoginState(context)) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
            return;
        }
        mFragmentManager
                .beginTransaction()
                .replace(R.id.login_fl_continer, new MyCheckupAccountFragment())
                .addToBackStack("MyCheckupAccountFragment").commit();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == ServiceEngin.REQUEST_LOGIN && arg1 == RESULT_CANCELED) {
            finish();
        } else if (arg1 == RESULT_OK) {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.login_fl_continer, new MyCheckupAccountFragment())
                    .addToBackStack("MyCheckupAccountFragment").commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountData.clearData();
    }
}
