package com.gofetch.socialdata;

/**
 * 
 * @author alandonohoe
 *
 * Class maintains the facebook data regarding a given URL on a particular date.
 */

public class FaceBookURLData {

	private java.sql.Date date;
	private int totalCount;
	private int likeCount;
	private int commentCount;
	private int shareCount;
	private int clickCount;
	
	public FaceBookURLData(){
		
		java.util.Date dateJava = new java.util.Date();
		java.sql.Date dateSQL = new java.sql.Date(dateJava.getTime());
		
		date = dateSQL;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public java.sql.Date getDate() {
		return date;
	}
	
	
}
