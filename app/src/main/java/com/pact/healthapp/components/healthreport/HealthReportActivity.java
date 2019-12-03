package com.pact.healthapp.components.healthreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;

/**
 * 体检报告、健康报告
 *
 * @author Administrator
 */
public class HealthReportActivity extends BaseFragmentActivity {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private String TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContenierView(1);
        myApplication.addActivity(this);
        btn_right.setVisibility(View.GONE);
        TYPE = getIntent().getStringExtra("TYPE");
        if (TYPE.equals("0")) {
            setTitle("体检报告");
        } else if (TYPE.equals("1")) {
            setTitle("评估报告");
        }
        setBackType(0);
        if (!sp.getLoginState(context)) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
            return;
        }
        mFragmentManager
                .beginTransaction()
                .replace(R.id.login_fl_continer,
                        new HealthReportListFragment(TYPE))
                .addToBackStack("HealthReportListFragment").commit();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == ServiceEngin.REQUEST_LOGIN &&arg1 == Activity.RESULT_CANCELED) {
            finish();
        } else {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.login_fl_continer,
                            new HealthReportListFragment(TYPE))
                    .addToBackStack("HealthReportListFragment").commit();
        }

    }
}