package com.pact.healthapp.components.checkupaccount;

import com.pact.healthapp.components.appointment.CheckupUserBean;

import java.util.List;

/**
 * Created by wangdong on 2016/1/27.
 */
public class AccountData {
    public static String certTypeId, certTypeName, certNumber;
    public static CheckupUserBean cb;//需要保存的用户对象
    public static List<CheckupUserBean> users;//用户列表
    public static List<CheckupAccountBean.DataEntity> voucherlist;
    public static String accountName;

    public static void setCb(CheckupUserBean cb) {
        AccountData.cb = cb;
    }

    /**
     * 清空所有数据
     */
    public static void clearData() {
        certTypeId = null;
        certTypeName = null;
        certNumber = null;
        cb = null;
        users = null;
        accountName = null;
    }
}
