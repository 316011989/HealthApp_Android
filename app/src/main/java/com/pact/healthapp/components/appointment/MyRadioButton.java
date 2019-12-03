package com.pact.healthapp.components.appointment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by zhangyl on 2016/1/5.
 */
public class MyRadioButton extends RadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((double) width * 1);
        setMeasuredDimension(width, height);
//        super.onMeasure(width, height);
    }
}
