package com.pact.healthapp.components.serviceorder;

import java.util.List;

/**
 * Created by wangdong on 2016/1/29.
 */
public class OrderData {
    public static List<ServiceOrderListBean> serviceOrderBeanList;
    public static ServiceOrderDetailBean detailBean;
    public static String operation;

    public static ServiceOrderDetailBean getDetailBean() {
        return detailBean;
    }

    public static void setDetailBean(ServiceOrderDetailBean detailBean) {
        OrderData.detailBean = detailBean;
    }

    public static List<ServiceOrderListBean> getServiceOrderBeanList() {
        return serviceOrderBeanList;
    }

    public static void setServiceOrderBeanList(List<ServiceOrderListBean> serviceOrderBeanList) {
        OrderData.serviceOrderBeanList = serviceOrderBeanList;
    }

    /**
     * 清空所有数据
     */
    public static void clearData() {
        serviceOrderBeanList = null;
        detailBean = null;
//        operation = null;
    }
}
