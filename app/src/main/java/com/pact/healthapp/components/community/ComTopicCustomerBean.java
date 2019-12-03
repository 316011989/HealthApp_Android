package com.pact.healthapp.components.community;

public class ComTopicCustomerBean {
	String headImg;
	String nickName;
	String  id;
	String favoriteCount;
	String topicsCount;
	String boardTitle;
	
	public String getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(String favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public String getTopicsCount() {
		return topicsCount;
	}
	public void setTopicsCount(String topicsCount) {
		this.topicsCount = topicsCount;
	}
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
