package com.pact.healthapp.universal;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;

/**
 * 和Framework表字段相同,用于和服务器同步管理项目中模块,功能等
 * @author  zhangyl
 *
 */
public class Framework implements Serializable {

	// 主键 模块的 id号
	@Id
	public String moduleId;
	// 是否需要登录
	public String isLogin;
	// 父id, 上一级的主键
	public String parentId;
	// 是否显示
	public String isVisible;
	// 首页主菜单排序字段 由后台维护
	public String isVisibleOrder;
	// 是否为首页主菜单固定菜单-后台维护
	public String fixedPage;
	// 第三方应用包名
	public String packageName;
	// 模块类型 1为列表,有下一级菜单 2为详情 3为 webview 4为打开另一个程序
	public String moduleType;
	// 图标名称
	public String iconName;
	// 小图标名称
	public String thumbnailName;
	// 模块名称
	public String moduleName;
	// 添加列表点击后是不是显示下一级 0-显示 1-不显示
	public String isMenuItem;
	// 是否为添加列表显示的模块 0-否 1-是
	public String isAddMenuItem;
	// 模块后台规定的顺序
	public String moduleOrderby;
	// webview点击链接
	public String clickUrl;
	// 更新时间
	public String updateDate;
	//模块状态 u:update ,c:create,d:delete
	public String opeFlag;

	public String getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(String opeFlag) {
		this.opeFlag = opeFlag;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getIsMenuItem() {
		return isMenuItem;
	}

	public void setIsMenuItem(String isMenuItem) {
		this.isMenuItem = isMenuItem;
	}

	public String getIsAddMenuItem() {
		return isAddMenuItem;
	}

	public void setIsAddMenuItem(String isAddMenuItem) {
		this.isAddMenuItem = isAddMenuItem;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getThumbnailName() {
		return thumbnailName;
	}

	public void setThumbnailName(String thumbnailName) {
		this.thumbnailName = thumbnailName;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public String getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}

	public String getModuleOrderby() {
		return moduleOrderby;
	}

	public void setModuleOrderby(String moduleOrderby) {
		this.moduleOrderby = moduleOrderby;
	}

	public String getIsVisibleOrder() {
		return isVisibleOrder;
	}

	public void setIsVisibleOrder(String isVisibleOrder) {
		this.isVisibleOrder = isVisibleOrder;
	}

	public String getFixedPage() {
		return fixedPage;
	}

	public void setFixedPage(String fixedPage) {
		this.fixedPage = fixedPage;
	}

	public String getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

}
