/**
 * 
 */
package com.pact.healthapp.components.news;

/**
 * @author zhangyl
 * 
 */
public class NewsBean {
	private String title;
	private String newsId;
	private String image;
	private String releaseTime;
	private String isBanner;

	public String getIsBanner() {
		return isBanner;
	}

	public void setIsBanner(String isBanner) {
		this.isBanner = isBanner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

}
