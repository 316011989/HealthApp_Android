package com.pact.healthapp.components.appointment;

import java.io.Serializable;

/**
 * Created by wangdong on 2015/12/18.
 */
public class OrgListBean implements Serializable {

    /**
     * orgId : 机构编号
     * orgName : 机构名称
     * orgType : 健管中心
     * address : 机构地址
     * regionid : 省编号
     * regionName : 省名称
     * cityId : 城市编号
     * cityName : 城市名称
     * orgCode : 机构代码
     * Longitude : 经度
     * Latitude : 纬度
     * orgTel : 机构联系电话
     * openTime : 营业时间
     */

    private String orgId;
    private String orgName;
    private String orgType;
    private String address;
    private String regionid;
    private String regionName;
    private String cityId;
    private String cityName;
    private String orgCode;
    private String Longitude;
    private String Latitude;
    private String orgTel;
    private String openTime;

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public String getAddress() {
        return address;
    }

    public String getRegionid() {
        return regionid;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getOrgTel() {
        return orgTel;
    }

    public String getOpenTime() {
        return openTime;
    }
}
