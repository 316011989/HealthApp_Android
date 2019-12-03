package com.pact.healthapp.components.appointment;

/**
 * 套餐原子项
 * Created by zhangyl on 2016/1/13.
 */
public class PackageitemBean {

    /**
     * productDesc : 女性肿标八项筛查
     * accountType : 1
     * usedCount : 0
     * packageId : 47C712FB6ADC43378E51459B7E74895A
     * payObject : 1
     * isExam : true
     * costCount : 1
     * leftCount : 1
     * productName : 女性肿标八项筛查
     * allowExchange : N
     * choosed : true
     * canUseCount : 1
     * productId : 1B2A09B9358547F0942F426AC889FE74
     */

    private String productDesc;
    private String accountType;
    private int usedCount;
    private String packageId;
    private String payObject;
    private boolean isExam;
    private String costCount;
    private int leftCount;
    private String productName;
    private String allowExchange;
    private boolean choosed;
    private int canUseCount;
    private String productId;

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setPayObject(String payObject) {
        this.payObject = payObject;
    }

    public void setIsExam(boolean isExam) {
        this.isExam = isExam;
    }

    public void setCostCount(String costCount) {
        this.costCount = costCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAllowExchange(String allowExchange) {
        this.allowExchange = allowExchange;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public void setCanUseCount(int canUseCount) {
        this.canUseCount = canUseCount;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getAccountType() {
        return accountType;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getPayObject() {
        return payObject;
    }

    public boolean getIsExam() {
        return isExam;
    }

    public String getCostCount() {
        return costCount;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public String getProductName() {
        return productName;
    }

    public String getAllowExchange() {
        return allowExchange;
    }

    public boolean getChoosed() {
        return choosed;
    }

    public int getCanUseCount() {
        return canUseCount;
    }

    public String getProductId() {
        return productId;
    }
}
