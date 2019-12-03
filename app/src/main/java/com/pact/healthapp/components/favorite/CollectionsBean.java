package com.pact.healthapp.components.favorite;

import com.pact.healthapp.components.community.ComTopicCustomerBean;
import com.pact.healthapp.components.event.EventBean;

import java.io.Serializable;

/**
 * Created by wangdong on 2015/8/14.
 */
public class CollectionsBean implements Serializable {

	private String collection;
	private String id;
	private String title;
	private EventBean event;
	private String target;
	private String favoriteCount;
	private String clickCount;
	private String releaseTime;
	private ComTopicCustomerBean customer;
	private String boardTitle;
	private String topicsCount;
	private String subBoards;
	private String commentsCount;
	private boolean havePic;

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EventBean getEvent() {
		return event;
	}

	public void setEvent(EventBean event) {
		this.event = event;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(String favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public ComTopicCustomerBean getCustomer() {
		return customer;
	}

	public void setCustomer(ComTopicCustomerBean customer) {
		this.customer = customer;
	}

	public String getBoardTitle() {
		return boardTitle;
	}

	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	public String getTopicsCount() {
		return topicsCount;
	}

	public void setTopicsCount(String topicsCount) {
		this.topicsCount = topicsCount;
	}

	public String getSubBoards() {
		return subBoards;
	}

	public void setSubBoards(String subBoards) {
		this.subBoards = subBoards;
	}

	public String getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(String commentsCount) {
		this.commentsCount = commentsCount;
	}

	public boolean isHavePic() {
		return havePic;
	}

	public void setHavePic(boolean havePic) {
		this.havePic = havePic;
	}
}
