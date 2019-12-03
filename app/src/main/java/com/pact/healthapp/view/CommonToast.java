package com.pact.healthapp.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pact.healthapp.R;

public class CommonToast extends Toast {

    private static CommonToast toast;
    private static TextView toast_tv;

    public static CommonToast getInstance() {
        return toast;
    }

    public CommonToast(Context context) {
        super(context);
    }

    public CommonToast(Context context, String message) {
        super(context);
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_commontoast, null);
        toast_tv = (TextView) view.findViewById(R.id.toast_tv);
        toast_tv.setText(message);
        if (context == null) {
            return;
        }
        setGravity(Gravity.CENTER, 0, 0);
        setDuration(LENGTH_LONG);// 设置长显示
        setView(view);
    }

    public static void makeText(Context context, String message) {
        if (toast == null)
            toast = new CommonToast(context, message);
        else
            toast_tv.setText(message);
        toast.show();
    }


}
