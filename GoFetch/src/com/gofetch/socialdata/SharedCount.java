package com.gofetch.socialdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents the interface of the sharedcount API.
 * @author alandonohoe
 *
 */
public class SharedCount {

	private int Delicious;
	private int GooglePlusOne;
	private int Diggs;
	private int Pinterest;
	private int LinkedIn;
	private int Twitter;

	//facebook //
	private int total_count;
	private int like_count;
	private int comments_box_count;
	private int share_count;
	private int click_count;
	private int comment_count;
	
	public int getDelicious() {
		return Delicious;
	}
	public void setDelicious(int delicious) {
		Delicious = delicious;
	}
	public int getGooglePlusOne() {
		return GooglePlusOne;
	}
	public void setGooglePlusOne(int googlePlusOne) {
		GooglePlusOne = googlePlusOne;
	}
	public int getPinterest() {
		return Pinterest;
	}
	public void setPinterest(int pinterest) {
		Pinterest = pinterest;
	}
	public int getLinkedIn() {
		return LinkedIn;
	}
	public void setLinkedIn(int linkedIn) {
		LinkedIn = linkedIn;
	}
	public int getTwitter() {
		return Twitter;
	}
	public void setTwitter(int twitter) {
		Twitter = twitter;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getLike_count() {
		return like_count;
	}
	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}
	public int getCommentsbox_count() {
		return comments_box_count;
	}
	public void setCommentsbox_count(int commentsbox_count) {
		this.comments_box_count = commentsbox_count;
	}
	public int getShare_count() {
		return share_count;
	}
	public void setShare_count(int share_count) {
		this.share_count = share_count;
	}
	public int getClick_count() {
		return click_count;
	}
	public void setClick_count(int click_count) {
		this.click_count = click_count;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	
}
