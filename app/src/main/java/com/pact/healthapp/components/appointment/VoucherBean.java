package com.pact.healthapp.components.appointment;

import java.util.List;

/**
 * Created by zhangyl on 2015/12/24.
 */
public class VoucherBean {
    /**
     * voucherId : 凭证编码
     * vouvherNo : 凭证号
     * statusValue : 凭证状态(编码)
     * statusName : 使用状态
     * paymentStatusValue : 付款状态(编码)
     * paymentStatusName : 付款状态
     * voucherDesc : 凭证描述
     * card_status : 凭证状态编码
     * nci_customer_Id : 客人编码
     * PayableAmount : 个人应付
     * PaidAmount : 个人已付
     * customerLimit : 是否本人限制
     * sexLimit : 性别限制
     * endDate : 结束日期
     * beginDate : 开始日期
     * serviceData : [{"serviceEndDate":"2016-01-31","serviceId":"6BD2146107474FFEA71AA526ECDA0F83","serviceBeginDate":"2015-12-23","serviceCount":3,"packageCategoryId":"023D4DC6CC7B4CE397ABA9858B8F1621","packageCategoryName":"体检类套餐"}]
     * 凭证包含的服务
     */

    private String PayableAmount;
    private String nci_customer_Id;
    private String statusName;
    private String endDate;
    private String beginDate;
    private String voucherNo;
    private String card_statusId;
    private String sexLimit;
    private String paymentStatusName;
    private String customerLimit;
    private String PaidAmount;
    private String statusValue;
    private String paymentStatusValue;
    private String voucherId;
    private String voucherDesc;
    private String serviceTypeCount;
    private String paymentObject;
    private String nciPaidAmount;
    private String companyPaidAmount;
    private String freeAmount;
    private List<ServiceDataEntity> serviceData;

    public String getNciPaidAmount() {
        return nciPaidAmount;
    }

    public void setNciPaidAmount(String nciPaidAmount) {
        this.nciPaidAmount = nciPaidAmount;
    }

    public String getCompanyPaidAmount() {
        return companyPaidAmount;
    }

    public void setCompanyPaidAmount(String companyPaidAmount) {
        this.companyPaidAmount = companyPaidAmount;
    }

    public String getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(String freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getPaymentObject() {
        return paymentObject;
    }

    public void setPaymentObject(String paymentObject) {
        this.paymentObject = paymentObject;
    }

    public String getCard_statusId() {
        return card_statusId;
    }

    public void setCard_statusId(String card_statusId) {
        this.card_statusId = card_statusId;
    }

    public String getServiceTypeCount() {
        return serviceTypeCount;
    }

    public void setServiceTypeCount(String serviceTypeCount) {
        this.serviceTypeCount = serviceTypeCount;
    }

    public void setPayableAmount(String PayableAmount) {
        this.PayableAmount = PayableAmount;
    }

    public void setNci_customer_Id(String nci_customer_Id) {
        this.nci_customer_Id = nci_customer_Id;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }


    public void setSexLimit(String sexLimit) {
        this.sexLimit = sexLimit;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    public void setCustomerLimit(String customerLimit) {
        this.customerLimit = customerLimit;
    }

    public void setPaidAmount(String PaidAmount) {
        this.PaidAmount = PaidAmount;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public void setPaymentStatusValue(String paymentStatusValue) {
        this.paymentStatusValue = paymentStatusValue;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public void setVoucherDesc(String voucherDesc) {
        this.voucherDesc = voucherDesc;
    }

    public void setServiceData(List<ServiceDataEntity> serviceData) {
        this.serviceData = serviceData;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public String getNci_customer_Id() {
        return nci_customer_Id;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getVoucherNo() {
        return voucherNo;
    }


    public String getSexLimit() {
        return sexLimit;
    }

    public String getPaymentStatusName() {
        return paymentStatusName;
    }

    public String getCustomerLimit() {
        return customerLimit;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public String getPaymentStatusValue() {
        return paymentStatusValue;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public String getVoucherDesc() {
        return voucherDesc;
    }

    public List<ServiceDataEntity> getServiceData() {
        return serviceData;
    }

    public static class ServiceDataEntity {
        /**
         * serviceEndDate : 服务结束日期
         * serviceId : 服务id
         * serviceBeginDate : 服务开始日期
         * serviceCount : 服务次数
         * packageCategoryId : 服务包类型对应id
         * packageCategoryName : 服务包类型
         */

        private String serviceEndDate;
        private String serviceId;
        private String serviceBeginDate;
        private int serviceCount;
        private String packageCategoryId;
        private String packageCategoryName;

        public void setServiceEndDate(String serviceEndDate) {
            this.serviceEndDate = serviceEndDate;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public void setServiceBeginDate(String serviceBeginDate) {
            this.serviceBeginDate = serviceBeginDate;
        }

        public void setServiceCount(int serviceCount) {
            this.serviceCount = serviceCount;
        }

        public void setPackageCategoryId(String packageCategoryId) {
            this.packageCategoryId = packageCategoryId;
        }

        public void setPackageCategoryName(String packageCategoryName) {
            this.packageCategoryName = packageCategoryName;
        }

        public String getServiceEndDate() {
            return serviceEndDate;
        }

        public String getServiceId() {
            return serviceId;
        }

        public String getServiceBeginDate() {
            return serviceBeginDate;
        }

        public int getServiceCount() {
            return serviceCount;
        }

        public String getPackageCategoryId() {
            return packageCategoryId;
        }

        public String getPackageCategoryName() {
            return packageCategoryName;
        }
    }


}
