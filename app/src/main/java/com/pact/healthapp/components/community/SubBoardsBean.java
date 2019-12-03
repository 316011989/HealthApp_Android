package com.pact.healthapp.components.community;

import java.io.Serializable;

public class SubBoardsBean implements Serializable {
	/**
	 * id : 456 favoriteCount : 1 topicsCount : 6 favorite : false boardTitle :
	 * 健康人生
	 */

	private String id;
	private int favoriteCount;
	private int topicsCount;
	private boolean favorite;
	private String boardTitle;

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
}
