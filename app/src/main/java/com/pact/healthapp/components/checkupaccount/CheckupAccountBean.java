package com.pact.healthapp.components.checkupaccount;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangdong on 2016/1/7.
 */
public class CheckupAccountBean implements Serializable {

    /**
     * cardNo : yuyuetijianren1
     * accountName : Re秦营
     * customerId : 1642503
     * cardTypeName : 新华体检卡
     * data : [{"PayableAmount":0,"nci_customer_Id":"0682f2a693a344f79e3999d9d6215556","paymentData":[{"paymentAmount":"","paymentType":"","paymentStatus":"","paymentObject":""}],"statusName":"已使用","voucherNo":"10131907360100057","card_status":"0","sexLimit":"M","paymentStatusName":"未支付","serviceData":[{"serviceEndDate":"2017-01-31","serviceId":"30C0FAEEBFF2446EA22B66CBD868DDFE","serviceBeginDate":"2016-01-18","serviceCount":0,"packageCategoryId":"023D4DC6CC7B4CE397ABA9858B8F1621","packageCategoryName":"体检类套餐"}],"isAble":"2","customerLimit":"1","PaidAmount":0,"statusValue":"1","paymentStatusValue":"0","voucherId":"8DC411186C2E43B39E0397B8D54AE1E8","voucherDesc":"预约测试"}]
     * checkupAccountId : 2408e52a-b48c-46c7-a1a2-646cecbca87d
     * cardType : 3
     */

    private String cardNo;
    private String accountName;
    private String customerId;
    private String cardTypeName;
    private String checkupAccountId;
    private String cardType;
    private List<DataEntity> data;

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public void setCheckupAccountId(String checkupAccountId) {
        this.checkupAccountId = checkupAccountId;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public String getCheckupAccountId() {
        return checkupAccountId;
    }

    public String getCardType() {
        return cardType;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * PayableAmount : 0
         * serviceTypeCount : 1
         * paymentData : [{"paymentAmount":0,"paidAmount":0,"unPayAmount":0,"paymentObject":""}]
         * statusName : 未使用
         * voucherNo :
         * customerId : 06C72810AAD742D691DE52EA905F6EFA
         * sexLimit : F
         * paymentStatusName : 已支付
         * isAble : 1
         * customerLimit : 1
         * PaidAmount : 0
         * statusValue : 0
         * cardStatusId : 0
         * paymentStatusValue : 1
         * packageCategoryName : 体检类套餐
         * voucherId : 49EF394D717D4FB2B9AFE62F66947B00
         * voucherDesc : 预约测试
         */
        private String PayableAmount;
        private String serviceTypeCount;
        private String statusName;
        private String voucherNo;
        private String customerId;
        private String sexLimit;
        private String paymentStatusName;
        private String isAble;
        private String customerLimit;
        private String PaidAmount;
        private String statusValue;
        private String cardStatusId;
        private String paymentStatusValue;
        private String packageCategoryName;
        private String voucherId;
        private String voucherDesc;
        private List<PaymentDataEntity> paymentData;

        public String getPayableAmount() {
            return PayableAmount;
        }

        public void setPayableAmount(String PayableAmount) {
            this.PayableAmount = PayableAmount;
        }

        public String getServiceTypeCount() {
            return serviceTypeCount;
        }

        public void setServiceTypeCount(String serviceTypeCount) {
            this.serviceTypeCount = serviceTypeCount;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getVoucherNo() {
            return voucherNo;
        }

        public void setVoucherNo(String voucherNo) {
            this.voucherNo = voucherNo;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getSexLimit() {
            return sexLimit;
        }

        public void setSexLimit(String sexLimit) {
            this.sexLimit = sexLimit;
        }

        public String getPaymentStatusName() {
            return paymentStatusName;
        }

        public void setPaymentStatusName(String paymentStatusName) {
            this.paymentStatusName = paymentStatusName;
        }

        public String getIsAble() {
            return isAble;
        }

        public void setIsAble(String isAble) {
            this.isAble = isAble;
        }

        public String getCustomerLimit() {
            return customerLimit;
        }

        public void setCustomerLimit(String customerLimit) {
            this.customerLimit = customerLimit;
        }

        public String getPaidAmount() {
            return PaidAmount;
        }

        public void setPaidAmount(String PaidAmount) {
            this.PaidAmount = PaidAmount;
        }

        public String getStatusValue() {
            return statusValue;
        }

        public void setStatusValue(String statusValue) {
            this.statusValue = statusValue;
        }

        public String getCardStatusId() {
            return cardStatusId;
        }

        public void setCardStatusId(String cardStatusId) {
            this.cardStatusId = cardStatusId;
        }

        public String getPaymentStatusValue() {
            return paymentStatusValue;
        }

        public void setPaymentStatusValue(String paymentStatusValue) {
            this.paymentStatusValue = paymentStatusValue;
        }

        public String getPackageCategoryName() {
            return packageCategoryName;
        }

        public void setPackageCategoryName(String packageCategoryName) {
            this.packageCategoryName = packageCategoryName;
        }

        public String getVoucherId() {
            return voucherId;
        }

        public void setVoucherId(String voucherId) {
            this.voucherId = voucherId;
        }

        public String getVoucherDesc() {
            return voucherDesc;
        }

        public void setVoucherDesc(String voucherDesc) {
            this.voucherDesc = voucherDesc;
        }

        public List<PaymentDataEntity> getPaymentData() {
            return paymentData;
        }

        public void setPaymentData(List<PaymentDataEntity> paymentData) {
            this.paymentData = paymentData;
        }

        /**
         * paymentAmount : 0
         * freeAmount: 0
         * companyPaidAmount: 0
         * nciPaidAmount: 0
         * paidAmount : 0
         * unPayAmount : 0
         * paymentObject :
         */
        public static class PaymentDataEntity {
            private String paymentAmount;
            private String freeAmount;
            private String companyPaidAmount;
            private String nciPaidAmount;
            private String paidAmount;
            private String unPayAmount;
            private String paymentObject;

            public String getPaymentAmount() {
                return paymentAmount;
            }

            public void setPaymentAmount(String paymentAmount) {
                this.paymentAmount = paymentAmount;
            }

            public String getFreeAmount() {
                return freeAmount;
            }

            public void setFreeAmount(String freeAmount) {
                this.freeAmount = freeAmount;
            }

            public String getCompanyPaidAmount() {
                return companyPaidAmount;
            }

            public void setCompanyPaidAmount(String companyPaidAmount) {
                this.companyPaidAmount = companyPaidAmount;
            }

            public String getNciPaidAmount() {
                return nciPaidAmount;
            }

            public void setNciPaidAmount(String nciPaidAmount) {
                this.nciPaidAmount = nciPaidAmount;
            }

            public String getPaidAmount() {
                return paidAmount;
            }

            public void setPaidAmount(String paidAmount) {
                this.paidAmount = paidAmount;
            }

            public String getUnPayAmount() {
                return unPayAmount;
            }

            public void setUnPayAmount(String unPayAmount) {
                this.unPayAmount = unPayAmount;
            }

            public String getPaymentObject() {
                return paymentObject;
            }

            public void setPaymentObject(String paymentObject) {
                this.paymentObject = paymentObject;
            }
        }
    }
}
