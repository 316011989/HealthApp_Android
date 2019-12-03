package com.pact.healthapp.components.healthreport;

import java.io.Serializable;

/**
 * Created by wangdong on 2015/10/20.
 */
public class MyReportsPgBean implements Serializable {

    /**
     * date : 2015-05-26
     * fileUrl : healthManagerReport/2015/05/26/343339120150526.pdf
     * healthCenterName : 健康管理报告
     * picType : jpg
     * pictureNumbers : 6
     * picurl : healthManagerReport/2015/05/26/343339120150526
     * reportname : 健康管理报告
     */

    private String date;
    private String fileUrl;
    private String healthCenterName;
    private String picType;
    private String pictureNumbers;
    private String picurl;
    private String reportname;

    public void setDate(String date) {
        this.date = date;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setHealthCenterName(String healthCenterName) {
        this.healthCenterName = healthCenterName;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public void setPictureNumbers(String pictureNumbers) {
        this.pictureNumbers = pictureNumbers;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getDate() {
        return date;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getHealthCenterName() {
        return healthCenterName;
    }

    public String getPicType() {
        return picType;
    }

    public String getPictureNumbers() {
        return pictureNumbers;
    }

    public String getPicurl() {
        return picurl;
    }

    public String getReportname() {
        return reportname;
    }
}
