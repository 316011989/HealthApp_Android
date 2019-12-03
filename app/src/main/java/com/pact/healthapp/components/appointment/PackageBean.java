package com.pact.healthapp.components.appointment;

import java.io.Serializable;

/**
 * 套餐
 * Created by 张一龙 on 2016/1/5.
 */
public class PackageBean implements Serializable {
    /**
     * isStandardId : N
     * marketPrice : 525
     * mediaUrl :
     * groupPackId : FC79C5B8231D4E8FBBDF7A4729097F57
     * packageName : 体检女不限
     * marryName : 不限
     * sexName : 女
     * isStandardName : 否
     * packageId : A82DFF1CFF04493CA476591C5AD59133
     * PackageDesc :
     * sexIdLimit : F
     * packageCode : TCBM90432903
     * groupId : 9CE489FB61D04D9BA08DD3A7A82DBCDA
     * marrayIdLimit : 6
     * payObject : 1
     * packageProductNumbers : 20
     */

    private String isStandardId;//是否是标准
    private String marketPrice;//市场价格
    private String mediaUrl;
    private String groupPackId;//分组套餐编号
    private String packageName;//套餐名称
    private String marryName;//婚否限制名称
    private String sexName;//性别限制名称
    private String isStandardName;//是否是标准名称
    private String packageId;//套餐编号
    private String PackageDesc;//套餐描述
    private String sexIdLimit;//性别限制（字典值）
    private String packageCode;//套餐编码
    private String groupId;//分组编号
    private String marrayIdLimit;//婚否限制（字典值）
    private String payObject;//付费对象
    private String packageProductNumbers;//产品/服务个数

    public void setIsStandardId(String isStandardId) {
        this.isStandardId = isStandardId;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setGroupPackId(String groupPackId) {
        this.groupPackId = groupPackId;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setMarryName(String marryName) {
        this.marryName = marryName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public void setIsStandardName(String isStandardName) {
        this.isStandardName = isStandardName;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setPackageDesc(String PackageDesc) {
        this.PackageDesc = PackageDesc;
    }

    public void setSexIdLimit(String sexIdLimit) {
        this.sexIdLimit = sexIdLimit;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setMarrayIdLimit(String marrayIdLimit) {
        this.marrayIdLimit = marrayIdLimit;
    }

    public void setPayObject(String payObject) {
        this.payObject = payObject;
    }

    public void setPackageProductNumbers(String packageProductNumbers) {
        this.packageProductNumbers = packageProductNumbers;
    }

    public String getIsStandardId() {
        return isStandardId;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getGroupPackId() {
        return groupPackId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getMarryName() {
        return marryName;
    }

    public String getSexName() {
        return sexName;
    }

    public String getIsStandardName() {
        return isStandardName;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getPackageDesc() {
        return PackageDesc;
    }

    public String getSexIdLimit() {
        return sexIdLimit;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMarrayIdLimit() {
        return marrayIdLimit;
    }

    public String getPayObject() {
        return payObject;
    }

    public String getPackageProductNumbers() {
        return packageProductNumbers;
    }

}
