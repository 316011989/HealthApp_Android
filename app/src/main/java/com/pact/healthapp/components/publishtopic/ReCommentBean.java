package com.pact.healthapp.components.publishtopic;

import com.pact.healthapp.components.community.ComTopicCustomerBean;

/**
 * 评论的回复的模型
 * 
 * @author zhangyl
 * 
 */
public class ReCommentBean {
	String content;
	String id;
	String likeCount;
	String releaseTime;
	ComTopicCustomerBean comTopicCustomerBean;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ComTopicCustomerBean getComTopicCustomerBean() {
		return comTopicCustomerBean;
	}

	public void setComTopicCustomerBean(
			ComTopicCustomerBean comTopicCustomerBean) {
		this.comTopicCustomerBean = comTopicCustomerBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

}
