package com.pact.healthapp.components.serviceorder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangdong on 2016/2/18.
 */
public class ServiceOrderListBean implements Serializable {

    /**
     * examinationNo : 8633010116260004                           体检编号
     * actualServiceTime :                                        到检时间
     * packageName : 体检男不限                                    套餐名称
     * packageId : C4F46F84DB1A424FBB40355A66114E30               套餐编号
     * paymentMethodName : 前台支付                                支付方式
     * unPayAmount : 0                                            个人未付
     * orgId : 9326B58C50304E61B26F9136CBA667A9                   机构编号
     * customerId : 0cf6e20e198741e88d76f7b2d9f4ad25
     * refundAmount : 0                                           退款金额
     * isExamination : Y                                          是否是体检类    Y是N否
     * paymentMethodId : 2                                        支付方式编号
     * appointmentServiceDate :                                   预约日期
     * serviceOrderNo: 1043971                                    服务单号
     * customerName : AA阿轩
     * serviceEndDate : 2017-01-31 00:00:00.0                     结束日期
     * PayableAmount : 714671.36                                  个人应付
     * appointmentEndMinute :                                     预约结束分钟
     * serviceOrderStatusName : 已取消                             服务单状态
     * appointmentStartMinute :                                    预约开始分钟
     * paymentOrderNum ：                                          支付编号
     * paymentStatus : 1                                           支付状态   1 待支付、2 全额支付、3 部分支付
     * serviceOrderId : 61673ba9e12b4ff38dc2c8af8c1124bf           服务单编码
     * attchObjList : ["经颅多普勒","经颅多普勒","易感全套检测"]      体检项列表 前三项
     * orgName: 新华呼和浩特健康管理中心                             机构名称
     * appointmentEndHour ：                                       预约结束小时
     * cardStatus : 0
     * serviceOrderStatusId : 4                                    服务单状态(编码)
     * address : 杭州市上城区春江花月流云苑3-1906                    机构地址
     * serviceBeginDate : 2016-01-18 00:00:00.0                    开始日期
     * PaidAmount : 0                                              个人已付
     * appointmentStartHour :                                      预约开始小时
     */

    private String examinationNo;
    private String actualServiceTime;
    private String packageName;
    private String packageId;
    private String paymentMethodName;
    private String unPayAmount;
    private String orgId;
    private String customerId;
    private String refundAmount;
    private String isExamination;
    private String paymentMethodId;
    private String appointmentServiceDate;
    private String serviceOrderNo;
    private String customerName;
    private String serviceEndDate;
    private String PayableAmount;
    private String appointmentEndMinute;
    private String serviceOrderStatusName;
    private String appointmentStartMinute;
    private String paymentOrderNum;
    private String paymentStatus;
    private String serviceOrderId;
    private String orgName;

    public String getAppointmentEndMinute() {
        return appointmentEndMinute;
    }

    public void setAppointmentEndMinute(String appointmentEndMinute) {
        this.appointmentEndMinute = appointmentEndMinute;
    }

    public String getPaymentOrderNum() {
        return paymentOrderNum;
    }

    public void setPaymentOrderNum(String paymentOrderNum) {
        this.paymentOrderNum = paymentOrderNum;
    }

    public String getAppointmentEndHour() {
        return appointmentEndHour;
    }

    public void setAppointmentEndHour(String appointmentEndHour) {
        this.appointmentEndHour = appointmentEndHour;
    }

    private String appointmentEndHour;
    private String cardStatus;
    private String serviceOrderStatusId;
    private String address;
    private String serviceBeginDate;
    private String PaidAmount;
    private String appointmentStartHour;
    private List<String> attchObjList;

    public String getUnPayAmount() {
        return unPayAmount;
    }

    public void setUnPayAmount(String unPayAmount) {
        this.unPayAmount = unPayAmount;
    }

    public void setExaminationNo(String examinationNo) {
        this.examinationNo = examinationNo;
    }

    public void setActualServiceTime(String actualServiceTime) {
        this.actualServiceTime = actualServiceTime;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setIsExamination(String isExamination) {
        this.isExamination = isExamination;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }

    public void setAppointmentServiceDate(String appointmentServiceDate) {
        this.appointmentServiceDate = appointmentServiceDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public void setPayableAmount(String PayableAmount) {
        this.PayableAmount = PayableAmount;
    }

    public void setServiceOrderStatusName(String serviceOrderStatusName) {
        this.serviceOrderStatusName = serviceOrderStatusName;
    }

    public void setAppointmentStartMinute(String appointmentStartMinute) {
        this.appointmentStartMinute = appointmentStartMinute;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public void setServiceOrderStatusId(String serviceOrderStatusId) {
        this.serviceOrderStatusId = serviceOrderStatusId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setServiceBeginDate(String serviceBeginDate) {
        this.serviceBeginDate = serviceBeginDate;
    }

    public void setPaidAmount(String PaidAmount) {
        this.PaidAmount = PaidAmount;
    }

    public void setAppointmentStartHour(String appointmentStartHour) {
        this.appointmentStartHour = appointmentStartHour;
    }

    public void setAttchObjList(List<String> attchObjList) {
        this.attchObjList = attchObjList;
    }

    public String getExaminationNo() {
        return examinationNo;
    }

    public String getActualServiceTime() {
        return actualServiceTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public String getIsExamination() {
        return isExamination;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getAppointmentServiceDate() {
        return appointmentServiceDate;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getServiceEndDate() {
        return serviceEndDate;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public String getServiceOrderStatusName() {
        return serviceOrderStatusName;
    }

    public String getAppointmentStartMinute() {
        return appointmentStartMinute;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public String getServiceOrderStatusId() {
        return serviceOrderStatusId;
    }

    public String getAddress() {
        return address;
    }

    public String getServiceBeginDate() {
        return serviceBeginDate;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public String getAppointmentStartHour() {
        return appointmentStartHour;
    }

    public List<String> getAttchObjList() {
        return attchObjList;
    }
}
