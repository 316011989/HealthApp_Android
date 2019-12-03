package com.pact.healthapp.components.community;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 帖子实体,用于接口(发布帖子,huoq)
 * @author  caotong
 *
 */
public class ComTopicBean implements Serializable{
	ComTopicBoardBean board;
	String clickCount;
	String commentsCount;
	String content;
	ComTopicCustomerBean customer;
	boolean favorited;
	String id;
	private ArrayList<String> pics;
	String releaseTime;
	String title;
	boolean havePic;
	String lastCommentTime;
	
	

	public String getLastCommentTime() {
		return lastCommentTime;
	}

	public void setLastCommentTime(String lastCommentTime) {
		this.lastCommentTime = lastCommentTime;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public boolean isHavePic() {
		return havePic;
	}

	public void setHavePic(boolean havePic) {
		this.havePic = havePic;
	}

	public ComTopicBoardBean getBoard() {
		return board;
	}

	public void setBoard(ComTopicBoardBean board) {
		this.board = board;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public String getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(String commentsCount) {
		this.commentsCount = commentsCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ComTopicCustomerBean getCustomer() {
		return customer;
	}

	public void setCustomer(ComTopicCustomerBean customer) {
		this.customer = customer;
	}

	public boolean getFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public ArrayList<String> getPics() {
		return pics;
	}

	public void setPics(ArrayList<String> pics) {
		this.pics = pics;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
