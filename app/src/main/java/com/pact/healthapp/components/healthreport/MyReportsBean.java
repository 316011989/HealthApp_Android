package com.pact.healthapp.components.healthreport;

import java.io.Serializable;

/**
 * Created by wangdong on 2015/10/10.
 */
public class MyReportsBean implements Serializable {

	/**
	 * customerId : 509224 dateregister : 2015-05-23 healthCenterName :
	 * 新华健康重庆健康管理中心 healthCheckNumber : 8650010115053038_28781 id : 237208
	 * isConvertedToPicutre : 1 isDownLoad : 1 pictureNumbers : 14
	 */

	private int customerId;
	private String dateregister;
	private String healthCenterName;
	private String healthCheckNumber;
	private int id;
	private int isConvertedToPicutre;
	private String isDownLoad;
	private int pictureNumbers;

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setDateregister(String dateregister) {
		this.dateregister = dateregister;
	}

	public void setHealthCenterName(String healthCenterName) {
		this.healthCenterName = healthCenterName;
	}

	public void setHealthCheckNumber(String healthCheckNumber) {
		this.healthCheckNumber = healthCheckNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIsConvertedToPicutre(int isConvertedToPicutre) {
		this.isConvertedToPicutre = isConvertedToPicutre;
	}

	public void setIsDownLoad(String isDownLoad) {
		this.isDownLoad = isDownLoad;
	}

	public void setPictureNumbers(int pictureNumbers) {
		this.pictureNumbers = pictureNumbers;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getDateregister() {
		return dateregister;
	}

	public String getHealthCenterName() {
		return healthCenterName;
	}

	public String getHealthCheckNumber() {
		return healthCheckNumber;
	}

	public int getId() {
		return id;
	}

	public int getIsConvertedToPicutre() {
		return isConvertedToPicutre;
	}

	public String getIsDownLoad() {
		return isDownLoad;
	}

	public int getPictureNumbers() {
		return pictureNumbers;
	}
}
