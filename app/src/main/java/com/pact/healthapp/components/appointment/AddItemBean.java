package com.pact.healthapp.components.appointment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangdong on 2015/12/29.
 */
public class AddItemBean implements Serializable {

    /**
     * packageName : test123体检项包(整包计价)
     * priceMarket": "460"
     * priceSell": "414"
     * packageDatas : [{"productIncode":"JLDPL","deptName":"经颅多普勒室","packageName":"test123体检项包(整包计价)","itemType":"3","Must":false,"packageId":"68137AC1E6A340F2827F01CE25240656","inCode":"JLDPL","type":"A","costCount":"1","productId":"2DF07E3BB8FA4BE6A7F2515E6355155C","selected":false,"sexValue":"不限","accountType":"0","priceMarket":460,"deptCode":"KS0310","payObject":"2","zjm":"SFXM0197","productName":"经颅多普勒","priceSell":414,"allowExchange":"N"},{"productIncode":"XBZW","deptName":"放射科","packageName":"test123体检项包(整包计价)","itemType":"3","Must":false,"packageId":"68137AC1E6A340F2827F01CE25240656","inCode":"XBZW","type":"A","costCount":"1","productId":"50D1D69E7B8B4EC58546B488ECA8116B","selected":false,"sexValue":"不限","accountType":"0","priceMarket":460,"deptCode":"KS0110","payObject":"2","zjm":"SFXM0046","productName":"胸部正位","priceSell":414,"allowExchange":"N"},{"productIncode":"YZCW","deptName":"放射科","packageName":"test123体检项包(整包计价)","itemType":"3","Must":false,"packageId":"68137AC1E6A340F2827F01CE25240656","inCode":"YZCW","type":"A","costCount":"1","productId":"568DEAEA0FE04F68AE237FD23C11DC33","selected":false,"sexValue":"不限","accountType":"0","priceMarket":460,"deptCode":"KS0110","payObject":"2","zjm":"SFXM0048","productName":"腰椎侧位","priceSell":414,"allowExchange":"N"},{"productIncode":"SSZW","deptName":"放射科","packageName":"test123体检项包(整包计价)","itemType":"3","Must":false,"packageId":"68137AC1E6A340F2827F01CE25240656","inCode":"SSZW","type":"A","costCount":"1","productId":"5ED99D7BF5174FEF9172747339369F72","selected":false,"sexValue":"不限","accountType":"0","priceMarket":460,"deptCode":"KS0110","payObject":"2","zjm":"SFXM0034","productName":"双手正位","priceSell":414,"allowExchange":"N"},{"productIncode":"JZZCW","deptName":"放射科","packageName":"test123体检项包(整包计价)","itemType":"3","Must":false,"packageId":"68137AC1E6A340F2827F01CE25240656","inCode":"JZZCW","type":"A","costCount":"1","productId":"AC60FC8CDDF04E20936F3770CBA47D98","selected":false,"sexValue":"不限","accountType":"0","priceMarket":460,"deptCode":"KS0110","payObject":"2","zjm":"SFXM0054","productName":"颈椎正侧位","priceSell":414,"allowExchange":"N"}]
     */

    private String packageName;
    private String priceMarket;
    private String priceSell;
    private String accountType;//计费方式(整包,累计)
    private boolean Must;//是否必选
    private String type;//散项还是包(A包,P散项)
    private List<PackageDatasEntity> packageDatas;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isMust() {
        return Must;
    }

    public void setMust(boolean must) {
        Must = must;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setPriceMarket(String priceMarket) {
        this.priceMarket = priceMarket;
    }

    public void setPriceSell(String priceSell) {
        this.priceSell = priceSell;
    }

    public void setPackageDatas(List<PackageDatasEntity> packageDatas) {
        this.packageDatas = packageDatas;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPriceMarket() {
        return priceMarket;
    }

    public String getPriceSell() {
        return priceSell;
    }

    public List<PackageDatasEntity> getPackageDatas() {
        return packageDatas;
    }

    public static class PackageDatasEntity {

        /**
         * productIncode : JZCW
         * deptName : 放射科
         * packageName : 医技检查
         * itemType : 3
         * Must : false
         * packageId :
         * inCode : JZCW
         * type : P
         * costCount : 1
         * productId : 0DF34B35DF6243EE810B9E5D9B2AF1D5
         * productDesc :
         * selected : false
         * sexId : U
         * sexValue : 不限
         * accountType : 1
         * marryId : 6
         * priceMarket : 50
         * deptCode : KS0110
         * payObject : 2
         * zjm : SFXM0053
         * productName : 颈椎侧位
         * priceSell : 40
         * allowExchange : N
         * marryValue : 不限
         */

        private String productIncode;
        private String deptName;
        private String packageName;
        private String itemType;
        private boolean Must;
        private String packageId;
        private String inCode;
        private String type;
        private String costCount;
        private String productId;
        private String productDesc;
        private boolean selected;
        private String sexId;
        private String sexValue;
        private String accountType;
        private String marryId;
        private String priceMarket;
        private String deptCode;
        private String payObject;
        private String zjm;
        private String productName;
        private String priceSell;
        private String allowExchange;
        private String marryValue;

        public void setProductIncode(String productIncode) {
            this.productIncode = productIncode;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public void setMust(boolean Must) {
            this.Must = Must;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public void setInCode(String inCode) {
            this.inCode = inCode;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCostCount(String costCount) {
            this.costCount = costCount;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setSexId(String sexId) {
            this.sexId = sexId;
        }

        public void setSexValue(String sexValue) {
            this.sexValue = sexValue;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public void setMarryId(String marryId) {
            this.marryId = marryId;
        }

        public void setPriceMarket(String priceMarket) {
            this.priceMarket = priceMarket;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public void setPayObject(String payObject) {
            this.payObject = payObject;
        }

        public void setZjm(String zjm) {
            this.zjm = zjm;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setPriceSell(String priceSell) {
            this.priceSell = priceSell;
        }

        public void setAllowExchange(String allowExchange) {
            this.allowExchange = allowExchange;
        }

        public void setMarryValue(String marryValue) {
            this.marryValue = marryValue;
        }

        public String getProductIncode() {
            return productIncode;
        }

        public String getDeptName() {
            return deptName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getItemType() {
            return itemType;
        }

        public boolean getMust() {
            return Must;
        }

        public String getPackageId() {
            return packageId;
        }

        public String getInCode() {
            return inCode;
        }

        public String getType() {
            return type;
        }

        public String getCostCount() {
            return costCount;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public boolean getSelected() {
            return selected;
        }

        public String getSexId() {
            return sexId;
        }

        public String getSexValue() {
            return sexValue;
        }

        public String getAccountType() {
            return accountType;
        }

        public String getMarryId() {
            return marryId;
        }

        public String getPriceMarket() {
            return priceMarket;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public String getPayObject() {
            return payObject;
        }

        public String getZjm() {
            return zjm;
        }

        public String getProductName() {
            return productName;
        }

        public String getPriceSell() {
            return priceSell;
        }

        public String getAllowExchange() {
            return allowExchange;
        }

        public String getMarryValue() {
            return marryValue;
        }

        public boolean equals(Object obj) {
            if (obj instanceof PackageDatasEntity) {
                PackageDatasEntity packageDatasEntity = (PackageDatasEntity) obj;
                return this.productId.equals(packageDatasEntity.productId)
                        && this.packageId.equals(packageDatasEntity.packageId);
            }
            return super.equals(obj);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof AddItemBean) {
            AddItemBean addItemBean = (AddItemBean) obj;
            return this.packageName.equals(addItemBean.packageName);
        }
        return super.equals(obj);
    }

}
