package com.pact.healthapp.components.appointment;

import java.io.Serializable;

/**
 * Created by zhangyl on 2015/12/29.
 */
public class CheckupUserBean implements Serializable {

    /**
     * certNumber : 2015111201
     * birthday : 1987-11-01
     * industryName :
     * socialClass : a
     * marryName : 不限
     * nCIDictionaryIncomeName :
     * certTypeId : 4
     * department2 :
     * vocation :
     * postalCode :
     * customerId : D2CFDD4384A94AD9BCFE9F58E15567D5
     * phoneNumber :
     * sexId : M
     * homeAddress :
     * customerName : 测试5035
     * nCIDictionaryIncomeId :
     * sexName : 男
     * enterpriseName : 中国电信（2015）
     * socialClassName : 普通
     * educationLevelId :
     * department1 :
     * nationality :
     * email :
     * certTypeName : 员工号
     * marryId : 6
     * postalAddress :
     * educationLevelName :
     * mobileNumber :
     * industryId :
     * isAble
     * isEdit  是否修改过所有属性,初始为false,修改过为true,在保存个人信息时用于判断
     */

    private String certNumber = "";
    private String birthday = "";
    private String industryName = "";
    private String socialClass = "";
    private String marryName = "";
    private String nCIDictionaryIncomeName = "";
    private String certTypeId = "";
    private String department2 = "";
    private String vocation = "";
    private String postalCode = "";
    private String customerId = "";
    private String phoneNumber = "";
    private String sexId = "";
    private String homeAddress = "";
    private String customerName = "";
    private String nCIDictionaryIncomeId = "";
    private String sexName = "";
    private String enterpriseName = "";
    private String socialClassName = "";
    private String educationLevelId = "";
    private String department1 = "";
    private String nationality = "";
    private String email = "";
    private String certTypeName = "";
    private String marryId = "";
    private String postalAddress = "";
    private String educationLevelName = "";
    private String mobileNumber = "";
    private String industryId = "";
    private boolean isSelected;//当一个证卡号对应多个体检人时,让用户点击选择,为ui添加此属性,不做请求参数
    private String isAble = "1";
    private boolean isEdit = false;

    public String getIsAble() {
        return isAble;
    }

    public void setIsAble(String isAble) {
        this.isAble = isAble;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
        this.isEdit = true;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public void setSocialClass(String socialClass) {
        this.socialClass = socialClass;
    }

    public void setMarryName(String marryName) {
        this.marryName = marryName;
        this.isEdit = true;
    }

    public void setNCIDictionaryIncomeName(String nCIDictionaryIncomeName) {
        this.nCIDictionaryIncomeName = nCIDictionaryIncomeName;
    }

    public void setCertTypeId(String certTypeId) {
        this.certTypeId = certTypeId;
        this.isEdit = true;
    }

    public void setDepartment2(String department2) {
        this.department2 = department2;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        this.isEdit = true;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.isEdit = true;
    }

    public void setSexId(String sexId) {
        this.sexId = sexId;
        this.isEdit = true;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        this.isEdit = true;
    }

    public void setNCIDictionaryIncomeId(String nCIDictionaryIncomeId) {
        this.nCIDictionaryIncomeId = nCIDictionaryIncomeId;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
        this.isEdit = true;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public void setSocialClassName(String socialClassName) {
        this.socialClassName = socialClassName;
    }

    public void setEducationLevelId(String educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public void setDepartment1(String department1) {
        this.department1 = department1;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setEmail(String email) {
        this.email = email;
        this.isEdit = true;
    }

    public void setCertTypeName(String certTypeName) {
        this.certTypeName = certTypeName;
        this.isEdit = true;
    }

    public void setMarryId(String marryId) {
        this.marryId = marryId;
        this.isEdit = true;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public void setEducationLevelName(String educationLevelName) {
        this.educationLevelName = educationLevelName;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        this.isEdit = true;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getIndustryName() {
        return industryName;
    }

    public String getSocialClass() {
        return socialClass;
    }

    public String getMarryName() {
        return marryName;
    }

    public String getNCIDictionaryIncomeName() {
        return nCIDictionaryIncomeName;
    }

    public String getCertTypeId() {
        return certTypeId;
    }

    public String getDepartment2() {
        return department2;
    }

    public String getVocation() {
        return vocation;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSexId() {
        return sexId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getNCIDictionaryIncomeId() {
        return nCIDictionaryIncomeId;
    }

    public String getSexName() {
        return sexName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getSocialClassName() {
        return socialClassName;
    }

    public String getEducationLevelId() {
        return educationLevelId;
    }

    public String getDepartment1() {
        return department1;
    }

    public String getNationality() {
        return nationality;
    }

    public String getEmail() {
        return email;
    }

    public String getCertTypeName() {
        return certTypeName;
    }

    public String getMarryId() {
        return marryId;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public String getEducationLevelName() {
        return educationLevelName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getIndustryId() {
        return industryId;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
