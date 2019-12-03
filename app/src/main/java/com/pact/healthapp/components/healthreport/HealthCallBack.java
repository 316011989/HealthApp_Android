package com.pact.healthapp.components.healthreport;

/**
 * 报告列表adapter的回调，实现重新下载按钮的点击事件
 * 
 * @author Administrator
 * 
 */
public interface HealthCallBack {
	void callBack(int position, int type, boolean isReLoad);
}
