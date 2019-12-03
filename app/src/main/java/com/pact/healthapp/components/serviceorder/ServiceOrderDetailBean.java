package com.pact.healthapp.components.serviceorder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangdong on 2016/2/26.
 */
public class ServiceOrderDetailBean implements Serializable {

    /**
     * certNumber : 330511196606204025                         证件号
     * actualServiceTime :                                     到检时间
     * reportSendMethodName : 统一                             递送方式
     * packageName : 家属女                                    套餐名称
     * remark :
     * salesChannelName : 直销渠道                             销售渠道
     * examTypeId :                                           体检类型(编码) 1:健康体检、3:单项体检、4:新华寿险入职体检、5:新华健康入职体检
     * statusName : 已取消                                     使用状态
     * creditLimit :                                          信用额度
     * packageId : 47C712FB6ADC43378E51459B7E74895A                 套餐编号
     * appointmentEndhour : 8                                       结束小时
     * department2 : 湖州本级151112                                  部门2
     * appointmentStarthour : 7                                     开始小时
     * unpaidAmount : 0                                             应付金额（整单 应付金额 不是个人的 ）
     * refundAmount : 0                                             退款金额
     * postalCode :                                                 邮编
     * phoneNumber :                                                电话号码
     * created : 2015-11-12 16:14:40                                创建时间
     * sexId : F                                                    性别编码
     * isForbid : N                                                 是否禁用  N 否  Y是
     * payObjectName : 企业付费                                      付费对象
     * partnerCode :                                                商业伙伴(编号)
     * packageDatas : [{}]                                          套餐原子项
     * reportPriorityLevelId : 1                                    报告优先级(编码)
     * appointmentServiceDate : 2015-11-13                          预约日期
     * paymentMethodId : 2                                          支付方式编号
     * serviceOrderNo : 5021690                                     服务单号
     * creditPayObjectId :                                          付费对象(编码)
     * allowExchange : N                                            是否允许退换项   Y 允许；N 不允许；
     * payPercentageCompany : 0                                     企业付费比例
     * reportSendMethodId : TUANQU                                  递送方式(编码)
     * PayableAmount : 0                                            个人应付
     * expirationDate : 2017-10-11                                  服务失效日期
     * appPaymentStatus :                                           支付状态
     * underOrgAddress :                                            体检机构地址
     * sexName : 女                                                 性别
     * paymentStatus : 1                                            支付状态     1 待支付、2 全额支付、3 部分支付
     * examinationTypeId : a                                        体检者类型(编码)  VIP:VIP、a:普通、VVIP:VVIP
     * creditPayObjectName :                                        付费对象
     * serviceOrderId : 8f8c49f0087440ca89fe8e661aaf8722            服务单编号
     * examineeName : 沈阿平                                         体检者名称
     * additemDiscountRate :                                        体检项折扣率
     * department1 :                                                部门1
     * electronicReport : Y                                         是否电子报告 Y 是；N 否；
     * email :                                                      邮箱
     * marryId : 2                                                  婚否
     * underOrgName : 新华杭州健康管理中心                            承接机构
     * underOrgCode : 新华杭州健康管理中心                            承接机构(编号)
     * projectName : 新华寿险湖州中支癌筛                             项目名称
     * examinationNo : 8633010115110836                             体检号
     * birthday : 1966-06-20                                        出生日期
     * serviceObjectLimitId : 1                                     服务对象限制(编码)
     * projectCode : XM151000570                                    项目号
     * payPercentageNci : 0                                         新华付费比例
     * marryName : 已婚                                              婚姻
     * examTypeName :                                               体检类型
     * reportSendAddress :                                          递送地址
     * payObjectId : 1                                              付费对象(编码)
     * issameperson : Y                                             是否本人
     * certTypeId : 1                                               证件类型
     * partnerId : 8CEDCA681CD945D2AEC62A4C2C720589                 商业伙伴编码
     * PackageDesc :                                                套餐描述信息
     * examinationTypeName : 普通                                   体检者类型
     * paymentMethodName : 前台支付                                 支付方式名称
     * projectId : A91AB7BC287E45459E0EF93FA75A5E2D                销售项目(编码)
     * unPayAmount :                                               未付金额
     * serviceObjectLimitName : 不限本人使用                        服务对象限制
     * additemSet : N                                              是否体检项折扣
     * packageSellPrice : 0                                        套餐销售价格
     * statusId : 3                                                服务单状态(编码)   1  未预约、2 已预约、3  已到检、4 已取消
     * packageProductNum : 2                                       套餐服务个数
     * isExamination : Y                                           是否是体检类
     * reportPriorityLevelName : 一般                              报告优先级
     * packageMarketPrice : 0                                      套餐市场价格
     * salesChannelId : A                                          销售渠道(编码)  A:直销渠道、C:交叉渠道、B:中介渠道
     * appointmentStartminute : 30                                 开始分钟
     * appointmentEndminute : 0                                    结束分钟
     * partnerName : 新华人寿保险股份有限公司湖州中心支公司           商业伙伴名称
     * payPercentagePersonal : 100                                个人付费比例
     * examineeId : 088fe19138654d65bd8c878b424fcd5d              体检者编码
     * orderSource : 1                                            订单来源    1 CRM系统；2 前台体检系统；3 网上商城；
     * enterpriseName : 新华人寿保险股份有限公司湖州中心支公司       企业名称
     * paymentOrderNum :                                         支付单号
     * attchObjList : ["酪氨酸(L-Tyrosine)"]                      所有体检项名字
     * isGroupReport : N                                         是否出具团检报告
     * nationality :                                             民族
     * orgAdress :                                               机构地址
     * certTypeName : 身份证                                     证件类型
     * isCreditLimit : N                                        是否设置信用额度
     * PaidAmount : 0                                           个人已付
     * mobileNumber : 13058921102                               手机号
     * payRange :                                               支付范围
     * attachData : [{}]                                        体检项 散项
     * underOrgId : 9326B58C50304E61B26F9136CBA667A9            承接机构(编码)
     */

    private String certNumber;
    private String actualServiceTime;
    private String reportSendMethodName;
    private String packageName;
    private String remark;
    private String salesChannelName;
    private String examTypeId;
    private String statusName;
    private String creditLimit;
    private String packageId;
    private String appointmentEndhour;
    private String department2;
    private String appointmentStarthour;
    private String unpaidAmount;
    private String refundAmount;
    private String postalCode;
    private String phoneNumber;
    private String created;
    private String sexId;
    private String isForbid;
    private String payObjectName;
    private String partnerCode;
    private String reportPriorityLevelId;
    private String appointmentServiceDate;
    private String paymentMethodId;
    private String serviceOrderNo;
    private String creditPayObjectId;
    private String allowExchange;
    private String payPercentageCompany;
    private String reportSendMethodId;
    private String PayableAmount;
    private String expirationDate;
    private String appPaymentStatus;
    private String underOrgAddress;
    private String sexName;
    private String paymentStatus;
    private String examinationTypeId;
    private String creditPayObjectName;
    private String serviceOrderId;
    private String examineeName;
    private String additemDiscountRate;
    private String department1;
    private String electronicReport;
    private String email;
    private String marryId;
    private String underOrgName;
    private String underOrgCode;
    private String projectName;
    private String examinationNo;
    private String birthday;
    private String serviceObjectLimitId;
    private String projectCode;
    private String payPercentageNci;
    private String marryName;
    private String examTypeName;
    private String reportSendAddress;
    private String payObjectId;
    private String issameperson;
    private String certTypeId;
    private String partnerId;
    private String PackageDesc;
    private String examinationTypeName;
    private String paymentMethodName;
    private String projectId;
    private String unPayAmount;
    private String serviceObjectLimitName;
    private String additemSet;
    private String packageSellPrice;
    private String statusId;
    private String packageProductNum;
    private String isExamination;
    private String reportPriorityLevelName;
    private String packageMarketPrice;
    private String salesChannelId;
    private String appointmentStartminute;
    private String appointmentEndminute;
    private String partnerName;
    private String payPercentagePersonal;
    private String examineeId;
    private String orderSource;
    private String enterpriseName;
    private String paymentOrderNum;
    private String isGroupReport;
    private String nationality;
    private String orgAdress;
    private String certTypeName;
    private String isCreditLimit;
    private String paidAmount;
    private String mobileNumber;
    private String payRange;
    private String underOrgId;
    private String underOrgPackageSellPrice;
    private String serviceBeginDate;
    private String serviceEndDate;
    private String nciPaidAmount;
    private String companyPaidAmount;
    private String freeAmount;
    private List<PackageDatasEntity> packageDatas;
    private List<AttachPackageDataEntity> attachPackageData;
    private List<AttachDataEntity> attachData;

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

    public String getUnderOrgPackageSellPrice() {
        return underOrgPackageSellPrice;
    }

    public void setUnderOrgPackageSellPrice(String underOrgPackageSellPrice) {
        this.underOrgPackageSellPrice = underOrgPackageSellPrice;
    }

    public String getServiceBeginDate() {
        return serviceBeginDate;
    }

    public void setServiceBeginDate(String serviceBeginDate) {
        this.serviceBeginDate = serviceBeginDate;
    }

    public String getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public String getUnderOrgAddress() {
        return underOrgAddress;
    }

    public void setUnderOrgAddress(String underOrgAddress) {
        this.underOrgAddress = underOrgAddress;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public void setActualServiceTime(String actualServiceTime) {
        this.actualServiceTime = actualServiceTime;
    }

    public void setReportSendMethodName(String reportSendMethodName) {
        this.reportSendMethodName = reportSendMethodName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSalesChannelName(String salesChannelName) {
        this.salesChannelName = salesChannelName;
    }

    public void setExamTypeId(String examTypeId) {
        this.examTypeId = examTypeId;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setAppointmentEndhour(String appointmentEndhour) {
        this.appointmentEndhour = appointmentEndhour;
    }

    public void setDepartment2(String department2) {
        this.department2 = department2;
    }

    public void setAppointmentStarthour(String appointmentStarthour) {
        this.appointmentStarthour = appointmentStarthour;
    }

    public void setUnpaidAmount(String unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSexId(String sexId) {
        this.sexId = sexId;
    }

    public void setIsForbid(String isForbid) {
        this.isForbid = isForbid;
    }

    public void setPayObjectName(String payObjectName) {
        this.payObjectName = payObjectName;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public void setReportPriorityLevelId(String reportPriorityLevelId) {
        this.reportPriorityLevelId = reportPriorityLevelId;
    }

    public void setAppointmentServiceDate(String appointmentServiceDate) {
        this.appointmentServiceDate = appointmentServiceDate;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }

    public void setCreditPayObjectId(String creditPayObjectId) {
        this.creditPayObjectId = creditPayObjectId;
    }

    public void setAllowExchange(String allowExchange) {
        this.allowExchange = allowExchange;
    }

    public void setPayPercentageCompany(String payPercentageCompany) {
        this.payPercentageCompany = payPercentageCompany;
    }

    public void setReportSendMethodId(String reportSendMethodId) {
        this.reportSendMethodId = reportSendMethodId;
    }

    public void setPayableAmount(String PayableAmount) {
        this.PayableAmount = PayableAmount;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setAppPaymentStatus(String appPaymentStatus) {
        this.appPaymentStatus = appPaymentStatus;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setExaminationTypeId(String examinationTypeId) {
        this.examinationTypeId = examinationTypeId;
    }

    public void setCreditPayObjectName(String creditPayObjectName) {
        this.creditPayObjectName = creditPayObjectName;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public void setExamineeName(String examineeName) {
        this.examineeName = examineeName;
    }

    public void setAdditemDiscountRate(String additemDiscountRate) {
        this.additemDiscountRate = additemDiscountRate;
    }

    public void setDepartment1(String department1) {
        this.department1 = department1;
    }

    public void setElectronicReport(String electronicReport) {
        this.electronicReport = electronicReport;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMarryId(String marryId) {
        this.marryId = marryId;
    }

    public void setUnderOrgName(String underOrgName) {
        this.underOrgName = underOrgName;
    }

    public void setUnderOrgCode(String underOrgCode) {
        this.underOrgCode = underOrgCode;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setExaminationNo(String examinationNo) {
        this.examinationNo = examinationNo;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setServiceObjectLimitId(String serviceObjectLimitId) {
        this.serviceObjectLimitId = serviceObjectLimitId;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public void setPayPercentageNci(String payPercentageNci) {
        this.payPercentageNci = payPercentageNci;
    }

    public void setMarryName(String marryName) {
        this.marryName = marryName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }

    public void setReportSendAddress(String reportSendAddress) {
        this.reportSendAddress = reportSendAddress;
    }

    public void setPayObjectId(String payObjectId) {
        this.payObjectId = payObjectId;
    }

    public void setIssameperson(String issameperson) {
        this.issameperson = issameperson;
    }

    public void setCertTypeId(String certTypeId) {
        this.certTypeId = certTypeId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setPackageDesc(String PackageDesc) {
        this.PackageDesc = PackageDesc;
    }

    public void setExaminationTypeName(String examinationTypeName) {
        this.examinationTypeName = examinationTypeName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setUnPayAmount(String unPayAmount) {
        this.unPayAmount = unPayAmount;
    }

    public void setServiceObjectLimitName(String serviceObjectLimitName) {
        this.serviceObjectLimitName = serviceObjectLimitName;
    }

    public void setAdditemSet(String additemSet) {
        this.additemSet = additemSet;
    }

    public void setPackageSellPrice(String packageSellPrice) {
        this.packageSellPrice = packageSellPrice;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public void setPackageProductNum(String packageProductNum) {
        this.packageProductNum = packageProductNum;
    }

    public void setIsExamination(String isExamination) {
        this.isExamination = isExamination;
    }

    public void setReportPriorityLevelName(String reportPriorityLevelName) {
        this.reportPriorityLevelName = reportPriorityLevelName;
    }

    public void setPackageMarketPrice(String packageMarketPrice) {
        this.packageMarketPrice = packageMarketPrice;
    }

    public void setSalesChannelId(String salesChannelId) {
        this.salesChannelId = salesChannelId;
    }

    public void setAppointmentStartminute(String appointmentStartminute) {
        this.appointmentStartminute = appointmentStartminute;
    }

    public void setAppointmentEndminute(String appointmentEndminute) {
        this.appointmentEndminute = appointmentEndminute;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void setPayPercentagePersonal(String payPercentagePersonal) {
        this.payPercentagePersonal = payPercentagePersonal;
    }

    public void setExamineeId(String examineeId) {
        this.examineeId = examineeId;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public void setPaymentOrderNum(String paymentOrderNum) {
        this.paymentOrderNum = paymentOrderNum;
    }

    public void setIsGroupReport(String isGroupReport) {
        this.isGroupReport = isGroupReport;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setOrgAdress(String orgAdress) {
        this.orgAdress = orgAdress;
    }

    public void setCertTypeName(String certTypeName) {
        this.certTypeName = certTypeName;
    }

    public void setIsCreditLimit(String isCreditLimit) {
        this.isCreditLimit = isCreditLimit;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setPayRange(String payRange) {
        this.payRange = payRange;
    }

    public void setUnderOrgId(String underOrgId) {
        this.underOrgId = underOrgId;
    }

    public void setPackageDatas(List<PackageDatasEntity> packageDatas) {
        this.packageDatas = packageDatas;
    }

    public void setAttachPackageData(List<AttachPackageDataEntity> attachPackageData) {
        this.attachPackageData = attachPackageData;
    }

    public void setAttachData(List<AttachDataEntity> attachData) {
        this.attachData = attachData;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public String getActualServiceTime() {
        return actualServiceTime;
    }

    public String getReportSendMethodName() {
        return reportSendMethodName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getRemark() {
        return remark;
    }

    public String getSalesChannelName() {
        return salesChannelName;
    }

    public String getExamTypeId() {
        return examTypeId;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getAppointmentEndhour() {
        return appointmentEndhour;
    }

    public String getDepartment2() {
        return department2;
    }

    public String getAppointmentStarthour() {
        return appointmentStarthour;
    }

    public String getUnpaidAmount() {
        return unpaidAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreated() {
        return created;
    }

    public String getSexId() {
        return sexId;
    }

    public String getIsForbid() {
        return isForbid;
    }

    public String getPayObjectName() {
        return payObjectName;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public String getReportPriorityLevelId() {
        return reportPriorityLevelId;
    }

    public String getAppointmentServiceDate() {
        return appointmentServiceDate;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public String getCreditPayObjectId() {
        return creditPayObjectId;
    }

    public String getAllowExchange() {
        return allowExchange;
    }

    public String getPayPercentageCompany() {
        return payPercentageCompany;
    }

    public String getReportSendMethodId() {
        return reportSendMethodId;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getAppPaymentStatus() {
        return appPaymentStatus;
    }

    public String getSexName() {
        return sexName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getExaminationTypeId() {
        return examinationTypeId;
    }

    public String getCreditPayObjectName() {
        return creditPayObjectName;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public String getExamineeName() {
        return examineeName;
    }

    public String getAdditemDiscountRate() {
        return additemDiscountRate;
    }

    public String getDepartment1() {
        return department1;
    }

    public String getElectronicReport() {
        return electronicReport;
    }

    public String getEmail() {
        return email;
    }

    public String getMarryId() {
        return marryId;
    }

    public String getUnderOrgName() {
        return underOrgName;
    }

    public String getUnderOrgCode() {
        return underOrgCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getExaminationNo() {
        return examinationNo;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getServiceObjectLimitId() {
        return serviceObjectLimitId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public String getPayPercentageNci() {
        return payPercentageNci;
    }

    public String getMarryName() {
        return marryName;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public String getReportSendAddress() {
        return reportSendAddress;
    }

    public String getPayObjectId() {
        return payObjectId;
    }

    public String getIssameperson() {
        return issameperson;
    }

    public String getCertTypeId() {
        return certTypeId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPackageDesc() {
        return PackageDesc;
    }

    public String getExaminationTypeName() {
        return examinationTypeName;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getUnPayAmount() {
        return unPayAmount;
    }

    public String getServiceObjectLimitName() {
        return serviceObjectLimitName;
    }

    public String getAdditemSet() {
        return additemSet;
    }

    public String getPackageSellPrice() {
        return packageSellPrice;
    }

    public String getStatusId() {
        return statusId;
    }

    public String getPackageProductNum() {
        return packageProductNum;
    }

    public String getIsExamination() {
        return isExamination;
    }

    public String getReportPriorityLevelName() {
        return reportPriorityLevelName;
    }

    public String getPackageMarketPrice() {
        return packageMarketPrice;
    }

    public String getSalesChannelId() {
        return salesChannelId;
    }

    public String getAppointmentStartminute() {
        return appointmentStartminute;
    }

    public String getAppointmentEndminute() {
        return appointmentEndminute;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPayPercentagePersonal() {
        return payPercentagePersonal;
    }

    public String getExamineeId() {
        return examineeId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getPaymentOrderNum() {
        return paymentOrderNum;
    }

    public String getIsGroupReport() {
        return isGroupReport;
    }

    public String getNationality() {
        return nationality;
    }

    public String getOrgAdress() {
        return orgAdress;
    }

    public String getCertTypeName() {
        return certTypeName;
    }

    public String getIsCreditLimit() {
        return isCreditLimit;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPayRange() {
        return payRange;
    }

    public String getUnderOrgId() {
        return underOrgId;
    }

    public List<PackageDatasEntity> getPackageDatas() {
        return packageDatas;
    }

    public List<AttachPackageDataEntity> getAttachPackageData() {
        return attachPackageData;
    }

    public List<AttachDataEntity> getAttachData() {
        return attachData;
    }

    public static class PackageDatasEntity {
        /**
         * sellPrice : 60                                                        销售价
         * serviceOrderDetailId : 7803f3fddfc642ee98f0e8d720ae5599               明细编码
         * itemTypeName : 项目内套餐                                              项目类型
         * packageName : 家属女                                                   套餐名称
         * packageId : 5FFC779359074092834802C0B5001986                          套餐编号
         * settleCost : 60                                                       应收费用
         * itemTypeId : 1                                                        项目类型(编码)
         * sexId : F                                                             性别(编码)
         * useStatusName : 未检                                                  使用状态
         * accountMethod : 1                                                     计费方式       整包计价0累计计价1
         * marketPrice : 100                                                     市场价
         * settleStatusName : 未计算                                             计算状态
         * groupPackId : F669437BEA00419CBF318890BA87606E                        分组套餐编号
         * productCode : SFXM0189                                                产品编号
         * itemFlagId : O                                                        项目标识(编码)      A 体检项；D 退项；O 原有
         * sexName : 女                                                          性别
         * settleStatusId : 0                                                    计算状态(编码)      0 未计算； 1已计算；
         * operateUserCode : 001                                                 登记人(员工号)
         * productId : 03AACEBDAD9745E4AC13B1ECA70EAA7C                          产品编号
         * accountStatusName : 未结算                                             结算状态
         * productDesc :                                                         原子项描述
         * workCode : 001                                                        收费人(员工号)
         * operateUserTime :                                                     登记时间
         * useStatusId : 0                                                       使用状态(编码)      0 未使用； 1 已使用；2弃检
         * operateUserName : Openbravo                                           登记人
         * payObject: 2                                                          付费对象 1 企业付费  2 个人付费  3  新华付费
         * productName : 子宫、附件彩超                                           产品名称
         * itemFlagName : 原有                                                   项目标识
         * accountStatusId : 0                                                   结算状态(编码)      0 未结算； 1已结算；
         */

        private String sellPrice;
        private String serviceOrderDetailId;
        private String itemTypeName;
        private String packageName;
        private String packageId;
        private String settleCost;
        private String itemTypeId;
        private String sexId;
        private String useStatusName;
        private String accountMethod;
        private String marketPrice;
        private String settleStatusName;
        private String groupPackId;
        private String productCode;
        private String itemFlagId;
        private String sexName;
        private String settleStatusId;
        private String operateUserCode;
        private String productId;
        private String accountStatusName;
        private String productDesc;
        private String workCode;
        private String operateUserTime;
        private String useStatusId;
        private String operateUserName;
        private String payObject;
        private String productName;
        private String itemFlagName;
        private String accountStatusId;

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public void setServiceOrderDetailId(String serviceOrderDetailId) {
            this.serviceOrderDetailId = serviceOrderDetailId;
        }

        public void setItemTypeName(String itemTypeName) {
            this.itemTypeName = itemTypeName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public void setSettleCost(String settleCost) {
            this.settleCost = settleCost;
        }

        public void setItemTypeId(String itemTypeId) {
            this.itemTypeId = itemTypeId;
        }

        public void setSexId(String sexId) {
            this.sexId = sexId;
        }

        public void setUseStatusName(String useStatusName) {
            this.useStatusName = useStatusName;
        }

        public void setAccountMethod(String accountMethod) {
            this.accountMethod = accountMethod;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public void setSettleStatusName(String settleStatusName) {
            this.settleStatusName = settleStatusName;
        }

        public void setGroupPackId(String groupPackId) {
            this.groupPackId = groupPackId;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public void setItemFlagId(String itemFlagId) {
            this.itemFlagId = itemFlagId;
        }

        public void setSexName(String sexName) {
            this.sexName = sexName;
        }

        public void setSettleStatusId(String settleStatusId) {
            this.settleStatusId = settleStatusId;
        }

        public void setOperateUserCode(String operateUserCode) {
            this.operateUserCode = operateUserCode;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setAccountStatusName(String accountStatusName) {
            this.accountStatusName = accountStatusName;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public void setWorkCode(String workCode) {
            this.workCode = workCode;
        }

        public void setOperateUserTime(String operateUserTime) {
            this.operateUserTime = operateUserTime;
        }

        public void setUseStatusId(String useStatusId) {
            this.useStatusId = useStatusId;
        }

        public void setOperateUserName(String operateUserName) {
            this.operateUserName = operateUserName;
        }

        public void setPayObject(String payObject) {
            this.payObject = payObject;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setItemFlagName(String itemFlagName) {
            this.itemFlagName = itemFlagName;
        }

        public void setAccountStatusId(String accountStatusId) {
            this.accountStatusId = accountStatusId;
        }

        public String getSellPrice() {
            return sellPrice;
        }

        public String getServiceOrderDetailId() {
            return serviceOrderDetailId;
        }

        public String getItemTypeName() {
            return itemTypeName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getPackageId() {
            return packageId;
        }

        public String getSettleCost() {
            return settleCost;
        }

        public String getItemTypeId() {
            return itemTypeId;
        }

        public String getSexId() {
            return sexId;
        }

        public String getUseStatusName() {
            return useStatusName;
        }

        public String getAccountMethod() {
            return accountMethod;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public String getSettleStatusName() {
            return settleStatusName;
        }

        public String getGroupPackId() {
            return groupPackId;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getItemFlagId() {
            return itemFlagId;
        }

        public String getSexName() {
            return sexName;
        }

        public String getSettleStatusId() {
            return settleStatusId;
        }

        public String getOperateUserCode() {
            return operateUserCode;
        }

        public String getProductId() {
            return productId;
        }

        public String getAccountStatusName() {
            return accountStatusName;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public String getWorkCode() {
            return workCode;
        }

        public String getOperateUserTime() {
            return operateUserTime;
        }

        public String getUseStatusId() {
            return useStatusId;
        }

        public String getOperateUserName() {
            return operateUserName;
        }

        public String getPayObject() {
            return payObject;
        }

        public String getProductName() {
            return productName;
        }

        public String getItemFlagName() {
            return itemFlagName;
        }

        public String getAccountStatusId() {
            return accountStatusId;
        }
    }

    public static class AttachPackageDataEntity {


        /**
         * sellPrice : 414
         * accountMethod : 1
         * marketPrice : 460
         * packageName : test123体检项包
         * packageDatas : [{"sellPrice":414,"serviceOrderDetailId":"589ce5ab79a8451892103248f42fd6e5","itemTypeName":"其他项目体检项包","packageName":"test123体检项包","packageId":"68137AC1E6A340F2827F01CE25240656","settleCost":90,"itemTypeId":"5","sexId":"U","useStatusName":"未检","accountMethod":"1","marketPrice":460,"settleStatusName":"未计算","groupPackId":"","productCode":"SFXM0054","itemFlagId":"A","sexName":"不限","settleStatusId":"0","operateUserCode":"001","productId":"AC60FC8CDDF04E20936F3770CBA47D98","accountStatusName":"未结算","productDesc":"","workCode":"001","operateUserTime":"","useStatusId":"0","operateUserName":"Openbravo","productName":"颈椎正侧位","itemFlagName":"体检项","accountStatusId":"0"}]
         * packageId : 68137AC1E6A340F2827F01CE25240656
         */

        private String sellPrice;
        private String accountMethod;
        private String marketPrice;
        private String packageName;
        private String packageId;
        private List<PackageDatasEntity> packageDatas;

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public void setAccountMethod(String accountMethod) {
            this.accountMethod = accountMethod;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public void setPackageDatas(List<PackageDatasEntity> packageDatas) {
            this.packageDatas = packageDatas;
        }

        public String getSellPrice() {
            return sellPrice;
        }

        public String getAccountMethod() {
            return accountMethod;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getPackageId() {
            return packageId;
        }

        public List<PackageDatasEntity> getPackageDatas() {
            return packageDatas;
        }

        public static class PackageDatasEntity {
            /**
             * sellPrice : 414
             * serviceOrderDetailId : 589ce5ab79a8451892103248f42fd6e5
             * itemTypeName : 其他项目体检项包
             * packageName : test123体检项包
             * packageId : 68137AC1E6A340F2827F01CE25240656
             * settleCost : 90
             * itemTypeId : 5
             * sexId : U
             * useStatusName : 未检
             * accountMethod : 1
             * marketPrice : 460
             * settleStatusName : 未计算
             * groupPackId :
             * productCode : SFXM0054
             * itemFlagId : A
             * sexName : 不限
             * settleStatusId : 0
             * operateUserCode : 001
             * productId : AC60FC8CDDF04E20936F3770CBA47D98
             * accountStatusName : 未结算
             * productDesc :
             * workCode : 001
             * operateUserTime :
             * useStatusId : 0
             * operateUserName : Openbravo
             * payObject : 2
             * productName : 颈椎正侧位
             * itemFlagName : 体检项
             * accountStatusId : 0
             */

            private String sellPrice;
            private String serviceOrderDetailId;
            private String itemTypeName;
            private String packageName;
            private String packageId;
            private String settleCost;
            private String itemTypeId;
            private String sexId;
            private String useStatusName;
            private String accountMethod;
            private String marketPrice;
            private String settleStatusName;
            private String groupPackId;
            private String productCode;
            private String itemFlagId;
            private String sexName;
            private String settleStatusId;
            private String operateUserCode;
            private String productId;
            private String accountStatusName;
            private String productDesc;
            private String workCode;
            private String operateUserTime;
            private String useStatusId;
            private String operateUserName;
            private String payObject;
            private String productName;
            private String itemFlagName;
            private String accountStatusId;

            public void setSellPrice(String sellPrice) {
                this.sellPrice = sellPrice;
            }

            public void setServiceOrderDetailId(String serviceOrderDetailId) {
                this.serviceOrderDetailId = serviceOrderDetailId;
            }

            public void setItemTypeName(String itemTypeName) {
                this.itemTypeName = itemTypeName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public void setPackageId(String packageId) {
                this.packageId = packageId;
            }

            public void setSettleCost(String settleCost) {
                this.settleCost = settleCost;
            }

            public void setItemTypeId(String itemTypeId) {
                this.itemTypeId = itemTypeId;
            }

            public void setSexId(String sexId) {
                this.sexId = sexId;
            }

            public void setUseStatusName(String useStatusName) {
                this.useStatusName = useStatusName;
            }

            public void setAccountMethod(String accountMethod) {
                this.accountMethod = accountMethod;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public void setSettleStatusName(String settleStatusName) {
                this.settleStatusName = settleStatusName;
            }

            public void setGroupPackId(String groupPackId) {
                this.groupPackId = groupPackId;
            }

            public void setProductCode(String productCode) {
                this.productCode = productCode;
            }

            public void setItemFlagId(String itemFlagId) {
                this.itemFlagId = itemFlagId;
            }

            public void setSexName(String sexName) {
                this.sexName = sexName;
            }

            public void setSettleStatusId(String settleStatusId) {
                this.settleStatusId = settleStatusId;
            }

            public void setOperateUserCode(String operateUserCode) {
                this.operateUserCode = operateUserCode;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public void setAccountStatusName(String accountStatusName) {
                this.accountStatusName = accountStatusName;
            }

            public void setProductDesc(String productDesc) {
                this.productDesc = productDesc;
            }

            public void setWorkCode(String workCode) {
                this.workCode = workCode;
            }

            public void setOperateUserTime(String operateUserTime) {
                this.operateUserTime = operateUserTime;
            }

            public void setUseStatusId(String useStatusId) {
                this.useStatusId = useStatusId;
            }

            public void setOperateUserName(String operateUserName) {
                this.operateUserName = operateUserName;
            }

            public void setPayObject(String payObject) {
                this.payObject = payObject;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public void setItemFlagName(String itemFlagName) {
                this.itemFlagName = itemFlagName;
            }

            public void setAccountStatusId(String accountStatusId) {
                this.accountStatusId = accountStatusId;
            }

            public String getSellPrice() {
                return sellPrice;
            }

            public String getServiceOrderDetailId() {
                return serviceOrderDetailId;
            }

            public String getItemTypeName() {
                return itemTypeName;
            }

            public String getPackageName() {
                return packageName;
            }

            public String getPackageId() {
                return packageId;
            }

            public String getSettleCost() {
                return settleCost;
            }

            public String getItemTypeId() {
                return itemTypeId;
            }

            public String getSexId() {
                return sexId;
            }

            public String getUseStatusName() {
                return useStatusName;
            }

            public String getAccountMethod() {
                return accountMethod;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public String getSettleStatusName() {
                return settleStatusName;
            }

            public String getGroupPackId() {
                return groupPackId;
            }

            public String getProductCode() {
                return productCode;
            }

            public String getItemFlagId() {
                return itemFlagId;
            }

            public String getSexName() {
                return sexName;
            }

            public String getSettleStatusId() {
                return settleStatusId;
            }

            public String getOperateUserCode() {
                return operateUserCode;
            }

            public String getProductId() {
                return productId;
            }

            public String getAccountStatusName() {
                return accountStatusName;
            }

            public String getProductDesc() {
                return productDesc;
            }

            public String getWorkCode() {
                return workCode;
            }

            public String getOperateUserTime() {
                return operateUserTime;
            }

            public String getUseStatusId() {
                return useStatusId;
            }

            public String getOperateUserName() {
                return operateUserName;
            }

            public String getPayObject() {
                return payObject;
            }

            public String getProductName() {
                return productName;
            }

            public String getItemFlagName() {
                return itemFlagName;
            }

            public String getAccountStatusId() {
                return accountStatusId;
            }
        }
    }

    public static class AttachDataEntity {
        /**
         * sellPrice : 624
         * serviceOrderDetailId : 4fc84c45b4854b01bf8584b199f4e804
         * itemTypeName : 非体检项包体检项
         * packageName : 功能医学
         * packageId :
         * settleCost : 624
         * itemTypeId : 3
         * sexId : U
         * useStatusName : 未检
         * accountMethod : 1
         * marketPrice : 780
         * settleStatusName : 未计算
         * groupPackId :
         * productCode : GN1555205S
         * itemFlagId : A
         * sexName : 不限
         * settleStatusId : 0
         * operateUserCode : 001
         * productId : F7856C32045F484AB33C62649A3060EE
         * accountStatusName : 未结算
         * productDesc :
         * workCode : 001
         * operateUserTime :
         * useStatusId : 0
         * operateUserName : Openbravo
         * payObject : 2
         * productName : 酪氨酸(L-Tyrosine)
         * itemFlagName : 体检项
         * accountStatusId : 0
         */

        private String sellPrice;
        private String serviceOrderDetailId;
        private String itemTypeName;
        private String packageName;
        private String packageId;
        private String settleCost;
        private String itemTypeId;
        private String sexId;
        private String useStatusName;
        private String accountMethod;
        private String marketPrice;
        private String settleStatusName;
        private String groupPackId;
        private String productCode;
        private String itemFlagId;
        private String sexName;
        private String settleStatusId;
        private String operateUserCode;
        private String productId;
        private String accountStatusName;
        private String productDesc;
        private String workCode;
        private String operateUserTime;
        private String useStatusId;
        private String operateUserName;
        private String payObject;
        private String productName;
        private String itemFlagName;
        private String accountStatusId;

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public void setServiceOrderDetailId(String serviceOrderDetailId) {
            this.serviceOrderDetailId = serviceOrderDetailId;
        }

        public void setItemTypeName(String itemTypeName) {
            this.itemTypeName = itemTypeName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public void setSettleCost(String settleCost) {
            this.settleCost = settleCost;
        }

        public void setItemTypeId(String itemTypeId) {
            this.itemTypeId = itemTypeId;
        }

        public void setSexId(String sexId) {
            this.sexId = sexId;
        }

        public void setUseStatusName(String useStatusName) {
            this.useStatusName = useStatusName;
        }

        public void setAccountMethod(String accountMethod) {
            this.accountMethod = accountMethod;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public void setSettleStatusName(String settleStatusName) {
            this.settleStatusName = settleStatusName;
        }

        public void setGroupPackId(String groupPackId) {
            this.groupPackId = groupPackId;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public void setItemFlagId(String itemFlagId) {
            this.itemFlagId = itemFlagId;
        }

        public void setSexName(String sexName) {
            this.sexName = sexName;
        }

        public void setSettleStatusId(String settleStatusId) {
            this.settleStatusId = settleStatusId;
        }

        public void setOperateUserCode(String operateUserCode) {
            this.operateUserCode = operateUserCode;
        }

        public void setPayObject(String payObject) {
            this.payObject = payObject;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setAccountStatusName(String accountStatusName) {
            this.accountStatusName = accountStatusName;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public void setWorkCode(String workCode) {
            this.workCode = workCode;
        }

        public void setOperateUserTime(String operateUserTime) {
            this.operateUserTime = operateUserTime;
        }

        public void setUseStatusId(String useStatusId) {
            this.useStatusId = useStatusId;
        }

        public void setOperateUserName(String operateUserName) {
            this.operateUserName = operateUserName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setItemFlagName(String itemFlagName) {
            this.itemFlagName = itemFlagName;
        }

        public void setAccountStatusId(String accountStatusId) {
            this.accountStatusId = accountStatusId;
        }

        public String getSellPrice() {
            return sellPrice;
        }

        public String getServiceOrderDetailId() {
            return serviceOrderDetailId;
        }

        public String getItemTypeName() {
            return itemTypeName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getPackageId() {
            return packageId;
        }

        public String getSettleCost() {
            return settleCost;
        }

        public String getItemTypeId() {
            return itemTypeId;
        }

        public String getSexId() {
            return sexId;
        }

        public String getUseStatusName() {
            return useStatusName;
        }

        public String getAccountMethod() {
            return accountMethod;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public String getSettleStatusName() {
            return settleStatusName;
        }

        public String getGroupPackId() {
            return groupPackId;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getItemFlagId() {
            return itemFlagId;
        }

        public String getSexName() {
            return sexName;
        }

        public String getSettleStatusId() {
            return settleStatusId;
        }

        public String getOperateUserCode() {
            return operateUserCode;
        }

        public String getProductId() {
            return productId;
        }

        public String getAccountStatusName() {
            return accountStatusName;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public String getWorkCode() {
            return workCode;
        }

        public String getOperateUserTime() {
            return operateUserTime;
        }

        public String getUseStatusId() {
            return useStatusId;
        }

        public String getOperateUserName() {
            return operateUserName;
        }

        public String getPayObject() {
            return payObject;
        }

        public String getProductName() {
            return productName;
        }

        public String getItemFlagName() {
            return itemFlagName;
        }

        public String getAccountStatusId() {
            return accountStatusId;
        }
    }
}
