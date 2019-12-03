package com.pact.healthapp.components.homepage;

import java.io.Serializable;

/**
 * Created by wangdong on 2015/8/27.
 */
public class HomePageBannerBean implements Serializable{
    private String categoryId;
    private String consultXrefId;
    private String imgPath;
    private String isSetTop;
    private String releaseTime;
    private String showAppHome;
    private String title;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getConsultXrefId() {
        return consultXrefId;
    }

    public void setConsultXrefId(String consultXrefId) {
        this.consultXrefId = consultXrefId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getIsSetTop() {
        return isSetTop;
    }

    public void setIsSetTop(String isSetTop) {
        this.isSetTop = isSetTop;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getShowAppHome() {
        return showAppHome;
    }

    public void setShowAppHome(String showAppHome) {
        this.showAppHome = showAppHome;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
