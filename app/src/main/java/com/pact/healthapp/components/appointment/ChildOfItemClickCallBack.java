package com.pact.healthapp.components.appointment;

import android.view.View;

/**
 * 自定义接口，用于回调按钮点击事件到Fragment
 * Created by wangdong on 2015/12/23.
 */
public interface ChildOfItemClickCallBack {
    void mCallback(int position, View v);
}
