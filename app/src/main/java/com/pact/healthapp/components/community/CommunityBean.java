package com.pact.healthapp.components.community;

import java.io.Serializable;

/**
 * 版块模型
 * 
 * @author zhangyl
 * 
 */
public class CommunityBean implements Serializable {
	/**
	 * id : 123 subBoards :
	 * [{"id":"456","favoriteCount":1,"topicsCount":6,"favorite"
	 * :false,"boardTitle"
	 * :"健康人生"},{"id":"a8507bf7-cc51-4c78-91ad-440ff023ecfc","favoriteCount"
	 * :1,"topicsCount":11,"favorite":true,"boardTitle":"心理健康"},{"id":
	 * "52a702b0-b738-4977-8217-8037219b7095"
	 * ,"favoriteCount":2,"topicsCount":6,"favorite":true,"boardTitle":"健康故事"}]
	 * favoriteCount : 9 topicsCount : 107 favorite : false boardTitle : 健康管理
	 */

	private String id;
	public int favoriteCount;
	private int topicsCount;
	public boolean favorite;
	private String boardTitle;
	private String subBoards;

	public void setId(String id) {
		this.id = id;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public void setTopicsCount(int topicsCount) {
		this.topicsCount = topicsCount;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	public void setSubBoards(String subBoards) {
		this.subBoards = subBoards;
	}

	public String getId() {
		return id;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public int getTopicsCount() {
		return topicsCount;
	}

	public boolean getFavorite() {
		return favorite;
	}

	public String getBoardTitle() {
		return boardTitle;
	}

	public String getSubBoards() {
		return subBoards;
	}

}
