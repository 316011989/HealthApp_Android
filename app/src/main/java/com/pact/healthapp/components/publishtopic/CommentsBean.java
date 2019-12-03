package com.pact.healthapp.components.publishtopic;

import com.pact.healthapp.components.community.ComTopicCustomerBean;

/**
 * 评论模型
 * @author  zhangyl
 *
 */
public class CommentsBean {
	String content;
	ComTopicCustomerBean comTopicCustomerBean;
	String id;
	String likeCount;
	String releaseTime;
	String pic[];
	ReCommentBean commentBean;
	
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ComTopicCustomerBean getComTopicCustomerBean() {
		return comTopicCustomerBean;
	}
	public void setComTopicCustomerBean(ComTopicCustomerBean comTopicCustomerBean) {
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
	public String[] getPic() {
		return pic;
	}
	public void setPic(String[] pic) {
		this.pic = pic;
	}
	public ReCommentBean getCommentBean() {
		return commentBean;
	}
	public void setCommentBean(ReCommentBean commentBean) {
		this.commentBean = commentBean;
	}
	
}
